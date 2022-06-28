package com.nttdata.microservices.bootcoin.controller;

import com.nttdata.microservices.bootcoin.service.WalletService;
import com.nttdata.microservices.bootcoin.service.dto.WalletDto;
import com.nttdata.microservices.bootcoin.util.ResponseUtil;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/bootcoin/wallet")
public class WalletController {

  private final WalletService walletService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  private Flux<WalletDto> findAll() {
    return walletService.findAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<WalletDto>> findById(@PathVariable("id") String id) {
    return ResponseUtil.wrapOrNotFound(walletService.findById(id));
  }

  @GetMapping(value = "/document/{document-number}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<WalletDto>> findByCardNumber(
      @PathVariable("document-number") String documentNumber) {
    return ResponseUtil.wrapOrNotFound(walletService.findByDocumentNumber(documentNumber));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<ResponseEntity<WalletDto>> create(@Valid @RequestBody WalletDto walletDto) {
    return ResponseUtil.wrapOrNotFound(walletService.create(walletDto));
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<WalletDto>> update(@PathVariable("id") String id,
                                                 @Valid @RequestBody WalletDto walletDto) {
    return ResponseUtil.wrapOrNotFound(walletService.update(id, walletDto));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
    return ResponseUtil.wrapOrNotFound(walletService.delete(id));
  }

}
