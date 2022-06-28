package com.nttdata.microservices.bootcoin.service.impl;

import com.nttdata.microservices.bootcoin.repository.WalletRepository;
import com.nttdata.microservices.bootcoin.service.dto.WalletDto;
import com.nttdata.microservices.bootcoin.service.mapper.WalletMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class WalletCacheServiceImpl extends WalletServiceImpl{

  private final ReactiveHashOperations<String, String, WalletDto> redisOperations;

  private static final String KEY_CACHE = "wallets";

  public WalletCacheServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper,
                                ReactiveHashOperations<String, String, WalletDto> redisOperations) {
    super(walletRepository, walletMapper);
    this.redisOperations = redisOperations;
  }

  @Override
  public Flux<WalletDto> findAll() {
    return redisOperations.values(KEY_CACHE)
        .switchIfEmpty(getAllFromDatabase());
  }

  @Override
  public Mono<WalletDto> findById(String id) {
    return redisOperations.get(KEY_CACHE, id)
        .switchIfEmpty(this.getFromDatabase(id));
  }

  @Override
  public Mono<WalletDto> findByDocumentNumber(String documentNumber) {
    return redisOperations.values(KEY_CACHE)
        .filter(walletDto -> walletDto.getDocumentNumber().equals(documentNumber))
        .singleOrEmpty()
        .switchIfEmpty(super.findByDocumentNumber(documentNumber));
  }

//  @Override
//  public Mono<Void> findByDocumentNumberForKafka(String documentNumber) {
//    log.debug("kafka client find by documentNumber: {}", documentNumber);
//    return this.findByDocumentNumber(documentNumber)
//        .map(walletMapper::toEntity)
//        .doOnNext(dto -> {
//          log.debug("kafka client find result: {}", dto);
//          kafkaProducerService.send(topic, dto);
//        })
//        .then();
//  }

  @Override
  public Mono<WalletDto> create(WalletDto wallet) {
    return super.create(wallet)
        .flatMap(this::saveCacheRedis);
  }

  @Override
  public Mono<WalletDto> update(String id, WalletDto walletDto) {
    return this.redisOperations.remove(KEY_CACHE, id)
        .then(super.update(id, walletDto))
        .flatMap(this::saveCacheRedis);
  }

  private Mono<WalletDto> saveCacheRedis(WalletDto walletDto) {
    log.info("put redis cache WalletDto: {}", walletDto);
    return this.redisOperations.put(KEY_CACHE,
            walletDto.getId(),
            walletDto)
        .thenReturn(walletDto);
  }

  private Mono<WalletDto> getFromDatabase(String id) {
    return super.findById(id)
        .flatMap(dto -> this.redisOperations.put(KEY_CACHE, id, dto)
            .thenReturn(dto));
  }

  private Flux<WalletDto> getAllFromDatabase() {
    return super.findAll()
        .flatMap(dto -> this.redisOperations.put(KEY_CACHE, dto.getId(), dto)
            .thenReturn(dto));
  }


}
