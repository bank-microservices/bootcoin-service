package com.nttdata.microservices.bootcoin.repository.converter;

import com.nttdata.microservices.bootcoin.entity.Wallet;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.mapping.DocumentPointer;

@ReadingConverter
public enum PurchaseOrderReadConverter implements Converter<Wallet, DocumentPointer<ObjectId>> {
  INSTANCE;

  public DocumentPointer<ObjectId> convert(Wallet source) {
    return () -> new ObjectId(source.getId());
  }
}