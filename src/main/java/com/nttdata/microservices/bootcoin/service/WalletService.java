package com.nttdata.microservices.bootcoin.service;

import com.nttdata.microservices.bootcoin.service.dto.WalletDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {

  Flux<WalletDto> findAll();

  Mono<WalletDto> findById(String id);

  Mono<WalletDto> findByDocumentNumber(String documentNumber);

  Mono<WalletDto> create(WalletDto wallet);

  Mono<WalletDto> update(String id, WalletDto walletDto);

  Mono<Void> delete(String id);
}
