package com.nttdata.microservices.bootcoin.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumerListener {

  @KafkaListener(topics = "#{'${kafka.topic.wallet-request}'}")
  public void OnMessage(ConsumerRecord<String, String> rc) {
    log.info("Consumer record: {}", rc);
//    walletService.findByDocumentNumberForKafka(rc.value()).subscribe();
  }

}
