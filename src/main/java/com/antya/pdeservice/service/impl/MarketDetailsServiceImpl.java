package com.antya.pdeservice.service.impl;

import com.antya.pdeservice.service.MarketDetailsService;
import com.antya.pdeservice.domain.MarketDetails;
import com.antya.pdeservice.repository.MarketDetailsRepository;
import com.antya.pdeservice.service.dto.MarketDetailsDTO;
import com.antya.pdeservice.service.mapper.MarketDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MarketDetails.
 */
@Service
@Transactional
public class MarketDetailsServiceImpl implements MarketDetailsService {

    private final Logger log = LoggerFactory.getLogger(MarketDetailsServiceImpl.class);

    private final MarketDetailsRepository marketDetailsRepository;

    private final MarketDetailsMapper marketDetailsMapper;

    public MarketDetailsServiceImpl(MarketDetailsRepository marketDetailsRepository, MarketDetailsMapper marketDetailsMapper) {
        this.marketDetailsRepository = marketDetailsRepository;
        this.marketDetailsMapper = marketDetailsMapper;
    }

    /**
     * Save a marketDetails.
     *
     * @param marketDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MarketDetailsDTO save(MarketDetailsDTO marketDetailsDTO) {
        log.debug("Request to save MarketDetails : {}", marketDetailsDTO);
        MarketDetails marketDetails = marketDetailsMapper.toEntity(marketDetailsDTO);
        marketDetails = marketDetailsRepository.save(marketDetails);
        return marketDetailsMapper.toDto(marketDetails);
    }

    /**
     * Get all the marketDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MarketDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MarketDetails");
        return marketDetailsRepository.findAll(pageable)
            .map(marketDetailsMapper::toDto);
    }

    /**
     * Get one marketDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MarketDetailsDTO findOne(Long id) {
        log.debug("Request to get MarketDetails : {}", id);
        MarketDetails marketDetails = marketDetailsRepository.findOne(id);
        return marketDetailsMapper.toDto(marketDetails);
    }

    /**
     * Delete the marketDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MarketDetails : {}", id);
        marketDetailsRepository.delete(id);
    }
}
