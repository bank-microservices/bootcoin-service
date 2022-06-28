package com.nttdata.microservices.bootcoin.repository.converter;


import com.nttdata.microservices.bootcoin.entity.Wallet;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.mapping.DocumentPointer;

@WritingConverter
public enum PurchaseOrderWriteConverter implements Converter<DocumentPointer<Object>, Wallet> {
  INSTANCE;

  @Override
  public Wallet convert(DocumentPointer<Object> source) {

    Object pointer = source.getPointer();
    Wallet accountType = new Wallet();
    if (pointer instanceof ObjectId) {
      ObjectId id = (ObjectId) pointer;
      accountType.setId(id.toHexString());
    } else {
      Document document = (Document) pointer;
      BsonDocument bsonDocument = document.toBsonDocument();
      accountType.setId(bsonDocument.getObjectId("_id").getValue().toString());
      accountType.setDocumentNumber(bsonDocument.getString("documentNumber").getValue());
      accountType.setFullName(bsonDocument.getString("fullName").getValue());
      accountType.setPhone(bsonDocument.getString("phone").getValue());
      accountType.setAmount(bsonDocument.getDouble("amount").getValue());
    }
    return accountType;
  }
}