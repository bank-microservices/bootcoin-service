package com.nttdata.microservices.bootcoin.service.mapper;

import com.nttdata.microservices.bootcoin.entity.ExchangeRate;
import com.nttdata.microservices.bootcoin.entity.Wallet;
import com.nttdata.microservices.bootcoin.service.dto.ExchangeRateDto;
import com.nttdata.microservices.bootcoin.service.dto.WalletDto;
import com.nttdata.microservices.bootcoin.service.mapper.base.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper extends EntityMapper<ExchangeRateDto, ExchangeRate> {

}
