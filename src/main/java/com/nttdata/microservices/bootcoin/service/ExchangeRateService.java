package com.nttdata.microservices.bootcoin.service;

import com.nttdata.microservices.bootcoin.service.dto.ExchangeRateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {

  Flux<ExchangeRateDto> findAll();

  Mono<ExchangeRateDto> findById(String id);

  Mono<ExchangeRateDto> create(ExchangeRateDto wallet);

  Mono<ExchangeRateDto> update(String id, ExchangeRateDto walletDto);

  Mono<Void> delete(String id);
}
