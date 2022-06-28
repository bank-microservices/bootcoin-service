package com.nttdata.microservices.bootcoin.service.dto;

import com.nttdata.microservices.bootcoin.entity.PurchaseOrderStatus;
import com.nttdata.microservices.bootcoin.util.validation.ValueOfEnum;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcessOrderResponseDto {

  @NotBlank(message = "orderCode is required")
  private String orderCode;

  @NotBlank(message = "status is required")
  @ValueOfEnum(enumClass = PurchaseOrderStatus.class, message = "status is invalid value")
  private PurchaseOrderStatus status;

}
