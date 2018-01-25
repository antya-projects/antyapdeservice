package com.antya.pdeservice.service;

import com.antya.pdeservice.service.dto.ExchangeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Exchange.
 */
public interface ExchangeService {

    /**
     * Save a exchange.
     *
     * @param exchangeDTO the entity to save
     * @return the persisted entity
     */
    ExchangeDTO save(ExchangeDTO exchangeDTO);

    /**
     * Get all the exchanges.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExchangeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exchange.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ExchangeDTO findOne(Long id);

    /**
     * Delete the "id" exchange.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
