package com.nttdata.microservices.bootcoin.service;

import com.nttdata.microservices.bootcoin.service.dto.ProcessOrderRequestDto;
import com.nttdata.microservices.bootcoin.service.dto.PurchaseOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PurchaseOrderService {

  Flux<PurchaseOrderDto> findAll();

  Mono<PurchaseOrderDto> findById(String id);

  Flux<PurchaseOrderDto> findByDocumentNumberSeller(String documentNumber);

  Mono<PurchaseOrderDto> create(PurchaseOrderDto orderDto);

  Mono<PurchaseOrderDto> process(ProcessOrderRequestDto processOrderRequestDto);

}
