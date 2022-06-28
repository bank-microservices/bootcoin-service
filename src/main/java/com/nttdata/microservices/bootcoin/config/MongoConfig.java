package com.nttdata.microservices.bootcoin.config;

import com.nttdata.microservices.bootcoin.repository.converter.PurchaseOrderReadConverter;
import com.nttdata.microservices.bootcoin.repository.converter.PurchaseOrderWriteConverter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {

  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(List.of(
        PurchaseOrderReadConverter.INSTANCE,
        PurchaseOrderWriteConverter.INSTANCE
    ));
  }

}
