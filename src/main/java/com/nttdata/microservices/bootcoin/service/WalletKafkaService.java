package com.nttdata.microservices.bootcoin.service;

import reactor.core.publisher.Mono;

public interface WalletKafkaService {

  Mono<Void> findByDocumentNumberForKafka(String documentNumber);

}
