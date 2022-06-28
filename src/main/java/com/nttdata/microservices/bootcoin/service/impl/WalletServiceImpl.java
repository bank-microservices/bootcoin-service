package com.nttdata.microservices.bootcoin.service.impl;

import static com.nttdata.microservices.bootcoin.util.MessageUtils.getMsg;

import com.nttdata.microservices.bootcoin.exception.BadRequestException;
import com.nttdata.microservices.bootcoin.repository.WalletRepository;
import com.nttdata.microservices.bootcoin.service.WalletService;
import com.nttdata.microservices.bootcoin.service.dto.WalletDto;
import com.nttdata.microservices.bootcoin.service.mapper.WalletMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cache.enabled", havingValue = "false", matchIfMissing = true)
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  protected final WalletMapper walletMapper;

  @Override
  public Flux<WalletDto> findAll() {
    log.debug("list all Wallets");
    return walletRepository.findAll()
        .map(walletMapper::toDto);
  }

  @Override
  public Mono<WalletDto> findById(String id) {
    return walletRepository.findById(id)
        .map(walletMapper::toDto);
  }

  @Override
  public Mono<WalletDto> findByDocumentNumber(String documentNumber) {
    return walletRepository.findByDocumentNumber(documentNumber)
        .map(walletMapper::toDto);
  }

  @Override
  public Mono<WalletDto> create(WalletDto wallet) {
    return Mono.just(wallet)
        .flatMap(this::existWallet)
        .map(walletMapper::toEntity)
        .map(entity -> {
          entity.setRegisterDate(LocalDateTime.now());
          return entity;
        })
        .flatMap(walletRepository::insert)
        .map(walletMapper::toDto)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<WalletDto> update(String id, WalletDto walletDto) {
    return walletRepository.findById(id)
        .flatMap(p -> Mono.just(walletDto)
            .map(walletMapper::toEntity)
            .doOnNext(e -> e.setId(id)))
        .flatMap(this.walletRepository::save)
        .map(walletMapper::toDto);
  }

  @Override
  public Mono<Void> delete(String id) {
    return walletRepository.deleteById(id);
  }


  private Mono<WalletDto> existWallet(WalletDto walletDto) {
    log.debug("find Wallet by documentNumber: {}", walletDto.getDocumentNumber());

    return findByDocumentNumber(walletDto.getDocumentNumber())
        .flatMap(r -> Mono.error(new BadRequestException(getMsg("debit.card.already",
            walletDto.getDocumentNumber()))))
        .thenReturn(walletDto);
  }

}
