package com.nttdata.microservices.bootcoin.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public TransactionException(String message, Integer statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public TransactionException(String message) {
    super(message);
  }

  public TransactionException(String message, Throwable cause) {
    super(message, cause);
  }
}
