package com.nttdata.microservices.bootcoin.service.mapper;

import com.nttdata.microservices.bootcoin.entity.PurchaseOrder;
import com.nttdata.microservices.bootcoin.service.dto.PurchaseOrderDto;
import com.nttdata.microservices.bootcoin.service.mapper.base.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper extends EntityMapper<PurchaseOrderDto, PurchaseOrder> {
}
