package com.nttdata.microservices.bootcoin.service.impl;

import static com.nttdata.microservices.bootcoin.util.MessageUtils.getMsg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.microservices.bootcoin.entity.ExchangeRate;
import com.nttdata.microservices.bootcoin.entity.PaymentMethod;
import com.nttdata.microservices.bootcoin.entity.PurchaseOrderStatus;
import com.nttdata.microservices.bootcoin.entity.TransactionOrder;
import com.nttdata.microservices.bootcoin.entity.account.Account;
import com.nttdata.microservices.bootcoin.exception.BadRequestException;
import com.nttdata.microservices.bootcoin.exception.TransactionException;
import com.nttdata.microservices.bootcoin.kafka.KafkaProducerService;
import com.nttdata.microservices.bootcoin.repository.ExchangeRateRepository;
import com.nttdata.microservices.bootcoin.repository.PurchaseOrderRepository;
import com.nttdata.microservices.bootcoin.repository.TransactionOrderRepository;
import com.nttdata.microservices.bootcoin.service.PurchaseOrderService;
import com.nttdata.microservices.bootcoin.service.WalletService;
import com.nttdata.microservices.bootcoin.service.dto.ProcessOrderRequestDto;
import com.nttdata.microservices.bootcoin.service.dto.PurchaseOrderDto;
import com.nttdata.microservices.bootcoin.service.mapper.PurchaseOrderMapper;
import com.nttdata.microservices.bootcoin.service.mapper.WalletMapper;
import com.nttdata.microservices.bootcoin.util.NumberUtil;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

  private final PurchaseOrderRepository orderRepository;
  private final ExchangeRateRepository exchangeRateRepository;
  private final TransactionOrderRepository transactionOrderRepository;

  private final WalletService walletService;

  private final PurchaseOrderMapper orderMapper;
  private final WalletMapper walletMapper;

  private final KafkaProducerService<String, String> kafkaProducerService;

  private final ObjectMapper objectMapper;

  private final Sinks.One<PurchaseOrderDto> sseEventSender = Sinks.one();

  @Value("${kafka.topic.account-request}")
  private String topicAccountRequest;

  @Value("${kafka.topic.yanki-request}")
  private String topicYankiRequest;

  @Override
  public Flux<PurchaseOrderDto> findAll() {
    return orderRepository.findAll()
        .map(orderMapper::toDto);
  }

  @Override
  public Mono<PurchaseOrderDto> findById(String id) {
    return orderRepository.findById(id)
        .map(orderMapper::toDto);
  }

  @Override
  public Flux<PurchaseOrderDto> findByDocumentNumberSeller(String documentNumber) {
    return orderRepository.findByWalletSellerDocumentNumber(documentNumber)
        .map(orderMapper::toDto);
  }

  @Override
  public Mono<PurchaseOrderDto> create(PurchaseOrderDto orderDto) {
    return Mono.just(orderDto)
        .flatMap(this::existWalletBuyer)
        .flatMap(this::existWalletSeller)
        .flatMap(this::validatePaymentMethod)
        .map(orderMapper::toEntity)
        .map(dto -> {
          dto.setRegisterDate(LocalDateTime.now());
          dto.setStatus(PurchaseOrderStatus.PENDING);
          dto.setOrderCode(NumberUtil.generateRandomNumber(6));
          return dto;
        })
        .flatMap(orderRepository::insert)
        .map(orderMapper::toDto)
        .subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<PurchaseOrderDto> existWalletBuyer(PurchaseOrderDto purchaseOrderDto) {
    return this.walletService.findByDocumentNumber(purchaseOrderDto.getDocumentNumberBuyer())
        .switchIfEmpty(Mono.error(new BadRequestException(getMsg("wallet.buyer.not.found"))))
        .map(walletMapper::toEntity)
        .doOnNext(purchaseOrderDto::setWalletBuyer)
        .thenReturn(purchaseOrderDto);
  }

  private Mono<PurchaseOrderDto> existWalletSeller(PurchaseOrderDto purchaseOrderDto) {
    return this.walletService.findByDocumentNumber(purchaseOrderDto.getDocumentNumberSeller())
        .switchIfEmpty(Mono.error(new BadRequestException(getMsg("wallet.seller.not.found"))))
        .map(walletMapper::toEntity)
        .doOnNext(purchaseOrderDto::setWalletSeller)
        .thenReturn(purchaseOrderDto);
  }

  private Mono<PurchaseOrderDto> validatePaymentMethod(PurchaseOrderDto purchaseOrderDto) {
    return Mono.just(purchaseOrderDto)
        .handle((dto, sink) -> {
          if (PaymentMethod.TRANSFER.name().equals(dto.getPaymentMethod())
              && StringUtils.isBlank(dto.getAccountNumber())) {
            sink.error(new BadRequestException("account.number.required"));
          } else if (PaymentMethod.YANKI.name().equals(dto.getPaymentMethod())
              && StringUtils.isBlank(dto.getPhoneNumber())) {
            sink.error(new BadRequestException("yanki.number.required"));
          } else {
            sink.next(dto);
          }
        });
  }

  @Override
  public Mono<PurchaseOrderDto> process(ProcessOrderRequestDto processOrderRequestDto) {
    return orderRepository.findByOrderCode(processOrderRequestDto.getOrderCode())
        .map(orderMapper::toDto)
        .flatMap(this::validateStatus);
  }

  private Mono<PurchaseOrderDto> updateStatus(PurchaseOrderDto purchaseOrderDto,
                                              PurchaseOrderStatus status) {
    return Mono.just(purchaseOrderDto)
        .map(dto -> {
          dto.setStatus(status);
          dto.setProcessedDate(LocalDateTime.now());
          return dto;
        })
        .map(orderMapper::toEntity)
        .flatMap(orderRepository::save)
        .map(orderMapper::toDto);
  }


  private Mono<PurchaseOrderDto> validateStatus(PurchaseOrderDto purchaseOrderDto) {
    return Mono.just(purchaseOrderDto)
        .<PurchaseOrderDto>handle((dto, sink) -> {
          if (PurchaseOrderStatus.REJECTED.equals(purchaseOrderDto.getStatus())) {
            sink.error(new TransactionException(getMsg("transaction.reject.order")));
          } else {
            sink.next(dto);
          }
        })
        .onErrorResume(ex -> this.updateStatus(purchaseOrderDto, PurchaseOrderStatus.REJECTED)
        )
        .flatMap(dto -> {
          if (PaymentMethod.TRANSFER.name().equals(dto.getPaymentMethod())) {
            return validateAccountBuyer(dto);
          } else {
            // validar con MS YANKI
            return Mono.just(dto);
          }
        });
  }

  private Mono<PurchaseOrderDto> validateAccountBuyer(PurchaseOrderDto purchaseOrderDto) {

    CompletableFuture<SendResult<String, String>> completable =
        kafkaProducerService.send(topicAccountRequest,
            purchaseOrderDto.getAccountNumber(),
            purchaseOrderDto.getWalletBuyer().getDocumentNumber(),
            new ListenableFutureCallback<>() {
              @Override
              public void onFailure(Throwable ex) {
                Mono.error(new BadRequestException("")).subscribe();
                log.error("Success {}", ex.getMessage(), ex);
              }

              @Override
              public void onSuccess(SendResult<String, String> result) {
                sseEventSender.tryEmitValue(purchaseOrderDto);
                log.debug("Success {}", result);
              }
            }).completable();


    return Mono.fromFuture(completable)
        .doFinally((signalType) -> {
          if (signalType == SignalType.CANCEL) {
            completable.cancel(true);
          }
        })
        .thenReturn(purchaseOrderDto);
  }

  private Mono<PurchaseOrderDto> getExchangeRate(PurchaseOrderDto purchaseOrderDto) {
    return exchangeRateRepository.findTopByOrderByRegisterDateDesc()
        .doOnNext(purchaseOrderDto::setExchangeRate)
        .thenReturn(purchaseOrderDto);
  }

  private Mono<TransactionOrder> buildTransactionOrder(PurchaseOrderDto purchaseOrderDto) {

    Double numberBootCoins = purchaseOrderDto.getAmount();
    ExchangeRate exchange = purchaseOrderDto.getExchangeRate();
    Double purchaseAmount = exchange.getPurchase() * numberBootCoins;
    String paymentMethod = purchaseOrderDto.getPaymentMethod();

    TransactionOrder transactionOrder = new TransactionOrder();
    transactionOrder.setTransactionCode(NumberUtil.generateRandomNumber(8));
    transactionOrder.setAmount(purchaseAmount);
    transactionOrder.setWalletBuyer(purchaseOrderDto.getWalletBuyer());
    transactionOrder.setWalletSeller(purchaseOrderDto.getWalletSeller());
    transactionOrder.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
    transactionOrder.setExchangeRate(purchaseOrderDto.getExchangeRate());
    transactionOrder.setRegisterDate(LocalDateTime.now());
    return Mono.just(transactionOrder);
  }

  @SneakyThrows
  @KafkaListener(topics = "#{'${kafka.topic.account-response}'}",
      clientIdPrefix = "string", containerFactory = "kafkaListenerStringContainerFactory")
  public void OnMessage(ConsumerRecord<String, Object> rc) {
    Mono<PurchaseOrderDto> purchaseOrderDtoMono = sseEventSender.asMono();

    log.info("Consumer record: {}", rc);
    if (HttpStatus.NOT_FOUND.name().equals(rc.key())) {
      Mono.error(new TransactionException(rc.value().toString(), HttpStatus.NOT_FOUND.value()))
          .subscribe();
    } else {
      Account account = objectMapper.readValue(rc.value().toString(), Account.class);
      purchaseOrderDtoMono.flatMap(this::getExchangeRate)
          .flatMap(this::buildTransactionOrder)
          .<TransactionOrder>handle((tx, sk) -> {
            if (account.getAmount() < tx.getAmount()) {
              sk.error(new BadRequestException(getMsg("account.balance.not.available")));
            } else {
              sk.next(tx);
            }
          })
          .flatMap(transactionOrderRepository::insert)
          .subscribe();

      log.debug("Account convert: {}", account);
    }

  }


}
