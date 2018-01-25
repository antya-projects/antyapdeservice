package com.antya.pdeservice.service;

import com.antya.pdeservice.service.dto.MarketPriceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MarketPrice.
 */
public interface MarketPriceService {

    /**
     * Save a marketPrice.
     *
     * @param marketPriceDTO the entity to save
     * @return the persisted entity
     */
    MarketPriceDTO save(MarketPriceDTO marketPriceDTO);

    /**
     * Get all the marketPrices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MarketPriceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" marketPrice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MarketPriceDTO findOne(Long id);

    /**
     * Delete the "id" marketPrice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
