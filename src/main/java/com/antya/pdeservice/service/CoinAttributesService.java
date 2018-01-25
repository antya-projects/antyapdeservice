package com.antya.pdeservice.service;

import com.antya.pdeservice.service.dto.CoinAttributesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CoinAttributes.
 */
public interface CoinAttributesService {

    /**
     * Save a coinAttributes.
     *
     * @param coinAttributesDTO the entity to save
     * @return the persisted entity
     */
    CoinAttributesDTO save(CoinAttributesDTO coinAttributesDTO);

    /**
     * Get all the coinAttributes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CoinAttributesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" coinAttributes.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CoinAttributesDTO findOne(Long id);

    /**
     * Delete the "id" coinAttributes.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
