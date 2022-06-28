package com.nttdata.microservices.bootcoin.service.impl;

import com.nttdata.microservices.bootcoin.repository.ExchangeRateRepository;
import com.nttdata.microservices.bootcoin.service.ExchangeRateService;
import com.nttdata.microservices.bootcoin.service.dto.ExchangeRateDto;
import com.nttdata.microservices.bootcoin.service.mapper.ExchangeRateMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "cache.enabled", havingValue = "false", matchIfMissing = true)
public class ExchangeRateServiceImpl implements ExchangeRateService {

  private final ExchangeRateRepository exchangeRateRepository;
  protected final ExchangeRateMapper exchangeRateMapper;

  @Override
  public Flux<ExchangeRateDto> findAll() {
    return exchangeRateRepository.findAll()
        .map(exchangeRateMapper::toDto);
  }

  @Override
  public Mono<ExchangeRateDto> findById(String id) {
    return exchangeRateRepository.findById(id)
        .map(exchangeRateMapper::toDto);
  }

  @Override
  public Mono<ExchangeRateDto> create(ExchangeRateDto wallet) {
    return Mono.just(wallet)
        .map(exchangeRateMapper::toEntity)
        .map(entity -> {
          entity.setRegisterDate(LocalDateTime.now());
          return entity;
        })
        .flatMap(exchangeRateRepository::insert)
        .map(exchangeRateMapper::toDto);
  }

}
