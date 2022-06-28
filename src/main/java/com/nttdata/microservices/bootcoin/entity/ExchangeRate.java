package com.nttdata.microservices.bootcoin.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Document(collection = "exchange-rate")
public class ExchangeRate extends AbstractAuditingEntity {
  @Id
  private String id;
  private Double purchase;
  private Double sale;
  private LocalDateTime registerDate;

}
