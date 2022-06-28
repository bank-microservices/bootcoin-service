package com.nttdata.microservices.bootcoin.entity.client;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.ReadOnlyProperty;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Client {

  @NotBlank(message = "is required")
  private String id;

  @NotBlank(message = "is required")
  private String documentNumber;

  private ClientType clientType;

  @ReadOnlyProperty
  private String firstNameBusiness;
  @ReadOnlyProperty
  private String surnames;
  @ReadOnlyProperty
  private ClientProfile clientProfile;
}
