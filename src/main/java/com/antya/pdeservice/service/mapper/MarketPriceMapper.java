package com.antya.pdeservice.service.mapper;

import com.antya.pdeservice.domain.*;
import com.antya.pdeservice.service.dto.MarketPriceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MarketPrice and its DTO MarketPriceDTO.
 */
@Mapper(componentModel = "spring", uses = {ExchangeMapper.class, MarketMapper.class})
public interface MarketPriceMapper extends EntityMapper<MarketPriceDTO, MarketPrice> {

    @Mapping(source = "exchange.id", target = "exchangeId")
    @Mapping(source = "market.id", target = "marketId")
    MarketPriceDTO toDto(MarketPrice marketPrice);

    @Mapping(source = "exchangeId", target = "exchange")
    @Mapping(source = "marketId", target = "market")
    MarketPrice toEntity(MarketPriceDTO marketPriceDTO);

    default MarketPrice fromId(Long id) {
        if (id == null) {
            return null;
        }
        MarketPrice marketPrice = new MarketPrice();
        marketPrice.setId(id);
        return marketPrice;
    }
}
