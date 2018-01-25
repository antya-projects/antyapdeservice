package com.antya.pdeservice.service.impl;

import com.antya.pdeservice.service.CoinAttributesService;
import com.antya.pdeservice.domain.CoinAttributes;
import com.antya.pdeservice.repository.CoinAttributesRepository;
import com.antya.pdeservice.service.dto.CoinAttributesDTO;
import com.antya.pdeservice.service.mapper.CoinAttributesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing CoinAttributes.
 */
@Service
@Transactional
public class CoinAttributesServiceImpl implements CoinAttributesService {

    private final Logger log = LoggerFactory.getLogger(CoinAttributesServiceImpl.class);

    private final CoinAttributesRepository coinAttributesRepository;

    private final CoinAttributesMapper coinAttributesMapper;

    public CoinAttributesServiceImpl(CoinAttributesRepository coinAttributesRepository, CoinAttributesMapper coinAttributesMapper) {
        this.coinAttributesRepository = coinAttributesRepository;
        this.coinAttributesMapper = coinAttributesMapper;
    }

    /**
     * Save a coinAttributes.
     *
     * @param coinAttributesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CoinAttributesDTO save(CoinAttributesDTO coinAttributesDTO) {
        log.debug("Request to save CoinAttributes : {}", coinAttributesDTO);
        CoinAttributes coinAttributes = coinAttributesMapper.toEntity(coinAttributesDTO);
        coinAttributes = coinAttributesRepository.save(coinAttributes);
        return coinAttributesMapper.toDto(coinAttributes);
    }

    /**
     * Get all the coinAttributes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoinAttributesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CoinAttributes");
        return coinAttributesRepository.findAll(pageable)
            .map(coinAttributesMapper::toDto);
    }

    /**
     * Get one coinAttributes by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CoinAttributesDTO findOne(Long id) {
        log.debug("Request to get CoinAttributes : {}", id);
        CoinAttributes coinAttributes = coinAttributesRepository.findOne(id);
        return coinAttributesMapper.toDto(coinAttributes);
    }

    /**
     * Delete the coinAttributes by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoinAttributes : {}", id);
        coinAttributesRepository.delete(id);
    }
}
