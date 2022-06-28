package com.nttdata.microservices.bootcoin.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Document("purchase-order")
public class PurchaseOrder extends AbstractAuditingEntity {

  @Id
  private String id;
  private String orderCode;
  @DocumentReference
  private Wallet walletBuyer;
  @DocumentReference
  private Wallet walletSeller;
  private Double amount;
  private PaymentMethod paymentMethod;
  private String accountNumber;
  private String phoneNumber;
  private PurchaseOrderStatus status;
  private LocalDateTime registerDate;
  private LocalDateTime processedDate;

}
