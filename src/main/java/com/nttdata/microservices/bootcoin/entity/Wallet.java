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
@Document("wallet")
public class Wallet extends AbstractAuditingEntity {

  @Id
  private String id;
  private String documentNumber;
  private String fullName;
  private String phone;
  private String email;
  private Double amount;
  private LocalDateTime registerDate;

}
