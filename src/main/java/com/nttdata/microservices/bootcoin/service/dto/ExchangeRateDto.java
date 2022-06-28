package com.nttdata.microservices.bootcoin.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ExchangeRateDto {

  @NotNull(message = "purchase is required")
  @Positive(message = "purchase must be greater than zero (0)")
  private Double purchase;

  @NotNull(message = "sale is required")
  @Positive(message = "sale must be greater than zero (0)")
  private Double sale;

  @JsonProperty(access = READ_ONLY)
  private String id;
}
