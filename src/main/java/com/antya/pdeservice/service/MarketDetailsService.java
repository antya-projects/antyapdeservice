package com.antya.pdeservice.service;

import com.antya.pdeservice.service.dto.MarketDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MarketDetails.
 */
public interface MarketDetailsService {

    /**
     * Save a marketDetails.
     *
     * @param marketDetailsDTO the entity to save
     * @return the persisted entity
     */
    MarketDetailsDTO save(MarketDetailsDTO marketDetailsDTO);

    /**
     * Get all the marketDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MarketDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" marketDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MarketDetailsDTO findOne(Long id);

    /**
     * Delete the "id" marketDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
