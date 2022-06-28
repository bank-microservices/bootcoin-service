package com.nttdata.microservices.bootcoin.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {

  @NotBlank(message = "documentNumber is required")
  @Pattern(regexp = "\\d+$", message = "documentNumber is not valid")
  private String documentNumber;

  @NotBlank(message = "fullName is required")
  private String fullName;

  @NotBlank(message = "phoneNumber is required")
  private String phone;

  @NotBlank(message = "email is required")
  @Email(message = "email is not valid")
  private String email;

  @NotNull(message = "amount is required")
  @Positive(message = "amount must be greater than zero (0)")
  private Double amount;

  private String id;

}
