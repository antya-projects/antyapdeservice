package com.antya.pdeservice.service;

import com.antya.pdeservice.service.dto.CoinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Coin.
 */
public interface CoinService {

    /**
     * Save a coin.
     *
     * @param coinDTO the entity to save
     * @return the persisted entity
     */
    CoinDTO save(CoinDTO coinDTO);

    /**
     * Get all the coins.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CoinDTO> findAll(Pageable pageable);

    /**
     * Get the "id" coin.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CoinDTO findOne(Long id);

    /**
     * Delete the "id" coin.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
