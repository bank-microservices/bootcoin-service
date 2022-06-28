package com.nttdata.microservices.bootcoin.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nttdata.microservices.bootcoin.entity.ExchangeRate;
import com.nttdata.microservices.bootcoin.entity.PaymentMethod;
import com.nttdata.microservices.bootcoin.entity.PurchaseOrderStatus;
import com.nttdata.microservices.bootcoin.entity.Wallet;
import com.nttdata.microservices.bootcoin.util.validation.ValueOfEnum;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseOrderDto {

  @JsonProperty(access = WRITE_ONLY)
  @NotBlank(message = "documentNumberBuyer is required")
  @Pattern(regexp = "\\d+$", message = "documentNumberBuyer is not valid")
  private String documentNumberBuyer;

  @JsonProperty(access = WRITE_ONLY)
  @NotBlank(message = "documentNumberSeller is required")
  @Pattern(regexp = "\\d+$", message = "documentNumberSeller is not valid")
  private String documentNumberSeller;

  @NotNull(message = "amount is required")
  @Positive(message = "amount must be greater than zero (0)")
  private Double amount;

  @NotNull(message = "paymentMethod is required")
  @ValueOfEnum(enumClass = PaymentMethod.class, message = "paymentMethod is invalid value")
  private String paymentMethod;

  @Pattern(regexp = "\\d+$", message = "accountNumber is not valid")
  private String accountNumber;

  @Pattern(regexp = "\\d+$", message = "phoneNumber is not valid")
  private String phoneNumber;

  @JsonProperty(access = READ_ONLY)
  private String orderCode;

  @JsonProperty(access = READ_ONLY)
  private PurchaseOrderStatus status;

  @JsonProperty(access = READ_ONLY)
  private LocalDateTime registerDate;

  @JsonProperty(access = READ_ONLY)
  private LocalDateTime processedDate;

  @JsonProperty(access = READ_ONLY)
  private String id;

  @JsonProperty(access = READ_ONLY)
  private Wallet walletBuyer;

  @JsonProperty(access = READ_ONLY)
  private Wallet walletSeller;

  @JsonProperty(access = READ_ONLY)
  private ExchangeRate exchangeRate;

}
