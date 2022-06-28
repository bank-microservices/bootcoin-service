package com.nttdata.microservices.bootcoin.entity.account;

import com.nttdata.microservices.bootcoin.entity.client.Client;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

  @NotBlank(message = "is required")
  private String id;
  @NotBlank(message = "is required")
  private String accountNumber;

  @ReadOnlyProperty
  private String cci;
  @ReadOnlyProperty
  private Double amount;
  @ReadOnlyProperty
  private Double maintenanceFee;
  @ReadOnlyProperty
  private Double transactionFee;
  @ReadOnlyProperty
  private Integer maxLimitMonthlyMovements;
  @ReadOnlyProperty
  private AccountType accountType;
  @ReadOnlyProperty
  private Client client;

}
