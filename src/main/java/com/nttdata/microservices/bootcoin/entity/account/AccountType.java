package com.nttdata.microservices.bootcoin.entity.account;

// 1: saving | 2: current | 3: fixed term

import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
public class AccountType {
  private String id;
  private String code;

  @ReadOnlyProperty
  private String description;
  @ReadOnlyProperty
  private Double commission;

}
