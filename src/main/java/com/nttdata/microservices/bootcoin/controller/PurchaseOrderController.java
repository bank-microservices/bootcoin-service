package com.nttdata.microservices.bootcoin.controller;

import com.nttdata.microservices.bootcoin.service.PurchaseOrderService;
import com.nttdata.microservices.bootcoin.service.dto.ProcessOrderRequestDto;
import com.nttdata.microservices.bootcoin.service.dto.PurchaseOrderDto;
import com.nttdata.microservices.bootcoin.util.ResponseUtil;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/bootcoin/purchase")
public class PurchaseOrderController {

  private final PurchaseOrderService orderService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  private Flux<PurchaseOrderDto> findAll() {
    return orderService.findAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<PurchaseOrderDto>> findById(@PathVariable("id") String id) {
    return ResponseUtil.wrapOrNotFound(orderService.findById(id));
  }

//  @GetMapping(value = "/document/{document-number}")
//  @ResponseStatus(HttpStatus.OK)
//  private Mono<ResponseEntity<PurchaseOrderDto>> findByCardNumber(
//      @PathVariable("document-number") String documentNumber) {
//    return ResponseUtil.wrapOrNotFound(orderService.findByDocumentNumber(documentNumber));
//  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<ResponseEntity<PurchaseOrderDto>> create(
      @Valid @RequestBody PurchaseOrderDto orderDto) {
    return ResponseUtil.wrapOrNotFound(orderService.create(orderDto));
  }

  @PostMapping("/process")
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<ResponseEntity<PurchaseOrderDto>> process(
      @Valid @RequestBody ProcessOrderRequestDto requestDto) {
    return ResponseUtil.wrapOrNotFound(orderService.process(requestDto));
  }

}
