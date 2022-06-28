package com.nttdata.microservices.bootcoin.repository;

import com.nttdata.microservices.bootcoin.entity.ExchangeRate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRateRepository extends ReactiveMongoRepository<ExchangeRate, String> {

  Mono<ExchangeRate> findTopByOrOrderByRegisterDateDesc();
}
