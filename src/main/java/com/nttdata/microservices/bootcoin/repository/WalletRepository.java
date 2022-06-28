package com.nttdata.microservices.bootcoin.repository;

import com.nttdata.microservices.bootcoin.entity.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveMongoRepository<Wallet, String> {

  Mono<Wallet> findByDocumentNumber(String documentNumber);

}
