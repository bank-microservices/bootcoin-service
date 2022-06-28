package com.nttdata.microservices.bootcoin.repository;

import com.nttdata.microservices.bootcoin.entity.PurchaseOrder;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PurchaseOrderRepository extends ReactiveMongoRepository<PurchaseOrder, String> {

  Flux<PurchaseOrder> findByWalletSellerDocumentNumber(String documentNumber);

  @Aggregation(pipeline = {
      "{$lookup: { from: 'wallet', localField: 'walletBuyer', foreignField: '_id', as: 'walletBuyer'}}",
      "{$lookup: { from: 'wallet', localField: 'walletSeller', foreignField: '_id', as: 'walletSeller'}}",
      "{$unwind: {path: '$walletBuyer', preserveNullAndEmptyArrays: false }}",
      "{$unwind: {path: '$walletSeller', preserveNullAndEmptyArrays: false }}"
  })
  Mono<PurchaseOrder> findByOrderCode(String orderCode);

}
