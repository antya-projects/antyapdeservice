package com.antya.pdeservice.service.mapper;

import com.antya.pdeservice.domain.*;
import com.antya.pdeservice.service.dto.CoinAttributesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CoinAttributes and its DTO CoinAttributesDTO.
 */
@Mapper(componentModel = "spring", uses = {CoinMapper.class})
public interface CoinAttributesMapper extends EntityMapper<CoinAttributesDTO, CoinAttributes> {

    @Mapping(source = "coin.id", target = "coinId")
    CoinAttributesDTO toDto(CoinAttributes coinAttributes);

    @Mapping(source = "coinId", target = "coin")
    CoinAttributes toEntity(CoinAttributesDTO coinAttributesDTO);

    default CoinAttributes fromId(Long id) {
        if (id == null) {
            return null;
        }
        CoinAttributes coinAttributes = new CoinAttributes();
        coinAttributes.setId(id);
        return coinAttributes;
    }
}
