package com.nttdata.microservices.bootcoin.exception;

public class WalletException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public WalletException(String message, Integer statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public WalletException(String message) {
    super(message);
  }

  public WalletException(String message, Throwable cause) {
    super(message, cause);
  }
}