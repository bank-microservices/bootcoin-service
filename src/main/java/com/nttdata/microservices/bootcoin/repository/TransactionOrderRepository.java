package com.nttdata.microservices.bootcoin.repository;

import com.nttdata.microservices.bootcoin.entity.TransactionOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionOrderRepository
    extends ReactiveMongoRepository<TransactionOrder, String> {
}
