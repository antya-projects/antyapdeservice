package com.antya.pdeservice.service.impl;

import com.antya.pdeservice.service.MarketService;
import com.antya.pdeservice.domain.Market;
import com.antya.pdeservice.repository.MarketRepository;
import com.antya.pdeservice.service.dto.MarketDTO;
import com.antya.pdeservice.service.mapper.MarketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Market.
 */
@Service
@Transactional
public class MarketServiceImpl implements MarketService {

    private final Logger log = LoggerFactory.getLogger(MarketServiceImpl.class);

    private final MarketRepository marketRepository;

    private final MarketMapper marketMapper;

    public MarketServiceImpl(MarketRepository marketRepository, MarketMapper marketMapper) {
        this.marketRepository = marketRepository;
        this.marketMapper = marketMapper;
    }

    /**
     * Save a market.
     *
     * @param marketDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MarketDTO save(MarketDTO marketDTO) {
        log.debug("Request to save Market : {}", marketDTO);
        Market market = marketMapper.toEntity(marketDTO);
        market = marketRepository.save(market);
        return marketMapper.toDto(market);
    }

    /**
     * Get all the markets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MarketDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Markets");
        return marketRepository.findAll(pageable)
            .map(marketMapper::toDto);
    }

    /**
     * Get one market by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MarketDTO findOne(Long id) {
        log.debug("Request to get Market : {}", id);
        Market market = marketRepository.findOne(id);
        return marketMapper.toDto(market);
    }

    /**
     * Delete the market by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Market : {}", id);
        marketRepository.delete(id);
    }
}
