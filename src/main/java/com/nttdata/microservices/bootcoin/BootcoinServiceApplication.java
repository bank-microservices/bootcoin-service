package com.nttdata.microservices.bootcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@EnableReactiveMongoAuditing
@SpringBootApplication
public class BootcoinServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(BootcoinServiceApplication.class, args);
  }

}
