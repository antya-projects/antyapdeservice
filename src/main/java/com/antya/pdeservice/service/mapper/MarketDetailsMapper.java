package com.antya.pdeservice.service.mapper;

import com.antya.pdeservice.domain.*;
import com.antya.pdeservice.service.dto.MarketDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MarketDetails and its DTO MarketDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {ExchangeMapper.class, MarketMapper.class})
public interface MarketDetailsMapper extends EntityMapper<MarketDetailsDTO, MarketDetails> {

    @Mapping(source = "exchange.id", target = "exchangeId")
    @Mapping(source = "market.id", target = "marketId")
    MarketDetailsDTO toDto(MarketDetails marketDetails);

    @Mapping(source = "exchangeId", target = "exchange")
    @Mapping(source = "marketId", target = "market")
    MarketDetails toEntity(MarketDetailsDTO marketDetailsDTO);

    default MarketDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        MarketDetails marketDetails = new MarketDetails();
        marketDetails.setId(id);
        return marketDetails;
    }
}
