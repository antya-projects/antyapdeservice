package com.antya.pdeservice.service.impl;

import com.antya.pdeservice.service.ExchangeService;
import com.antya.pdeservice.domain.Exchange;
import com.antya.pdeservice.repository.ExchangeRepository;
import com.antya.pdeservice.service.dto.ExchangeDTO;
import com.antya.pdeservice.service.mapper.ExchangeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Exchange.
 */
@Service
@Transactional
public class ExchangeServiceImpl implements ExchangeService {

    private final Logger log = LoggerFactory.getLogger(ExchangeServiceImpl.class);

    private final ExchangeRepository exchangeRepository;

    private final ExchangeMapper exchangeMapper;

    public ExchangeServiceImpl(ExchangeRepository exchangeRepository, ExchangeMapper exchangeMapper) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeMapper = exchangeMapper;
    }

    /**
     * Save a exchange.
     *
     * @param exchangeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExchangeDTO save(ExchangeDTO exchangeDTO) {
        log.debug("Request to save Exchange : {}", exchangeDTO);
        Exchange exchange = exchangeMapper.toEntity(exchangeDTO);
        exchange = exchangeRepository.save(exchange);
        return exchangeMapper.toDto(exchange);
    }

    /**
     * Get all the exchanges.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExchangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exchanges");
        return exchangeRepository.findAll(pageable)
            .map(exchangeMapper::toDto);
    }

    /**
     * Get one exchange by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExchangeDTO findOne(Long id) {
        log.debug("Request to get Exchange : {}", id);
        Exchange exchange = exchangeRepository.findOne(id);
        return exchangeMapper.toDto(exchange);
    }

    /**
     * Delete the exchange by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exchange : {}", id);
        exchangeRepository.delete(id);
    }
}
