package com.nttdata.microservices.bootcoin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  private DocumentType documentType;
  private String documentNumber;
  private String fullName;
  private String phone;
  private String email;
  private Double amount;
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime registerDate;

}
