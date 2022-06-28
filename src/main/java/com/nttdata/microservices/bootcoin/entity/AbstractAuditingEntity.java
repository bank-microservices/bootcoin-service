package com.nttdata.microservices.bootcoin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class AbstractAuditingEntity implements Serializable {

  @JsonIgnore
  @Field("created_by")
  private String createdBy;

  @JsonIgnore
  @Field("created_date")
  @CreatedDate
  private LocalDateTime createdDate;

  @JsonIgnore
  @Field("last_modified_by")
  private String lastModifiedBy;

  @JsonIgnore
  @Field("last_modified_date")
  @LastModifiedDate
  private LocalDateTime lastModifiedDate;

}
