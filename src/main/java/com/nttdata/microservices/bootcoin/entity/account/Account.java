package com.nttdata.microservices.bootcoin.entity.account;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
public class Account {

  @NotBlank(message = "is required")
  private String id;
  @NotBlank(message = "is required")
  private String accountNumber;
  private AccountType accountType;

  @ReadOnlyProperty
  private String cci;
  @ReadOnlyProperty
  private Double creditLimit;
  @ReadOnlyProperty
  private Double amount;
  @ReadOnlyProperty
  private boolean status;
  @ReadOnlyProperty
  private Double maintenanceFee;
  @ReadOnlyProperty
  private Double transactionFee;
  @ReadOnlyProperty
  private Integer maxLimitMonthlyMovements;

}
