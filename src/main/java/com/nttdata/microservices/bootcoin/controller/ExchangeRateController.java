package com.nttdata.microservices.bootcoin.controller;

import com.nttdata.microservices.bootcoin.service.ExchangeRateService;
import com.nttdata.microservices.bootcoin.service.dto.ExchangeRateDto;
import com.nttdata.microservices.bootcoin.util.ResponseUtil;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(path = "/api/v1/bootcoin/exchange")
public class ExchangeRateController {

  private final ExchangeRateService exchangeRateService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  private Flux<ExchangeRateDto> findAll() {
    return exchangeRateService.findAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<ExchangeRateDto>> findById(@PathVariable("id") String id) {
    return ResponseUtil.wrapOrNotFound(exchangeRateService.findById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<ResponseEntity<ExchangeRateDto>> create(
      @Valid @RequestBody ExchangeRateDto exchangeRateDto) {
    return ResponseUtil.wrapOrNotFound(exchangeRateService.create(exchangeRateDto));
  }

}
