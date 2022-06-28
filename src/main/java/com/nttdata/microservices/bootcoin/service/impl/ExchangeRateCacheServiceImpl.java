package com.nttdata.microservices.bootcoin.service.impl;

import com.nttdata.microservices.bootcoin.repository.ExchangeRateRepository;
import com.nttdata.microservices.bootcoin.service.dto.ExchangeRateDto;
import com.nttdata.microservices.bootcoin.service.mapper.ExchangeRateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class ExchangeRateCacheServiceImpl extends ExchangeRateServiceImpl {

  private final ReactiveHashOperations<String, String, ExchangeRateDto> redisOperations;

  private static final String KEY_CACHE = "exchange-rate";

  public ExchangeRateCacheServiceImpl(ExchangeRateRepository exchangeRateRepository,
                                      ExchangeRateMapper exchangeRateMapper,
                                      ReactiveHashOperations<String, String, ExchangeRateDto> redisOperations) {
    super(exchangeRateRepository, exchangeRateMapper);
    this.redisOperations = redisOperations;
  }

  @Override
  public Flux<ExchangeRateDto> findAll() {
    return redisOperations.values(KEY_CACHE)
        .switchIfEmpty(getAllFromDatabase());
  }

  @Override
  public Mono<ExchangeRateDto> findById(String id) {
    return redisOperations.get(KEY_CACHE, id)
        .switchIfEmpty(this.getFromDatabase(id));
  }

  @Override
  public Mono<ExchangeRateDto> create(ExchangeRateDto wallet) {
    return super.create(wallet)
        .flatMap(this::saveCacheRedis);
  }

  private Mono<ExchangeRateDto> saveCacheRedis(ExchangeRateDto walletDto) {
    log.info("put redis cache WalletDto: {}", walletDto);
    return this.redisOperations.put(KEY_CACHE,
            walletDto.getId(),
            walletDto)
        .thenReturn(walletDto);
  }

  private Mono<ExchangeRateDto> getFromDatabase(String id) {
    return super.findById(id)
        .flatMap(dto -> this.redisOperations.put(KEY_CACHE, id, dto)
            .thenReturn(dto));
  }

  private Flux<ExchangeRateDto> getAllFromDatabase() {
    return super.findAll()
        .flatMap(dto -> this.redisOperations.put(KEY_CACHE, dto.getId(), dto)
            .thenReturn(dto));
  }

}
