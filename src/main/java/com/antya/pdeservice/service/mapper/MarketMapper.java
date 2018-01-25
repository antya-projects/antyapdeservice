package com.antya.pdeservice.service.mapper;

import com.antya.pdeservice.domain.*;
import com.antya.pdeservice.service.dto.MarketDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Market and its DTO MarketDTO.
 */
@Mapper(componentModel = "spring", uses = {ExchangeMapper.class})
public interface MarketMapper extends EntityMapper<MarketDTO, Market> {

    @Mapping(source = "exchange.id", target = "exchangeId")
    MarketDTO toDto(Market market);

    @Mapping(source = "exchangeId", target = "exchange")
    Market toEntity(MarketDTO marketDTO);

    default Market fromId(Long id) {
        if (id == null) {
            return null;
        }
        Market market = new Market();
        market.setId(id);
        return market;
    }
}
