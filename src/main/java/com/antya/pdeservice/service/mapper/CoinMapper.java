package com.antya.pdeservice.service.mapper;

import com.antya.pdeservice.domain.*;
import com.antya.pdeservice.service.dto.CoinDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Coin and its DTO CoinDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CoinMapper extends EntityMapper<CoinDTO, Coin> {


    @Mapping(target = "coinAttributes", ignore = true)
    Coin toEntity(CoinDTO coinDTO);

    default Coin fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coin coin = new Coin();
        coin.setId(id);
        return coin;
    }
}
