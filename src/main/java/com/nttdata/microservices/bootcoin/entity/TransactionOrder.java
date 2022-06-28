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
@Document("purchase-order")
public class TransactionOrder extends AbstractAuditingEntity {

  @Id
  private String id;
  private String transactionCode;
  private Wallet walletBuyer;
  private Wallet walletSeller;
  private Double amount;
  private ExchangeRate exchangeRate;
  private PaymentMethod paymentMethod;
  private LocalDateTime registerDate;

}
