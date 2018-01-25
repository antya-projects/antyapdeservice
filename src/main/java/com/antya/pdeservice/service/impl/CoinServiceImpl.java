package com.antya.pdeservice.service.impl;

import com.antya.pdeservice.service.CoinService;
import com.antya.pdeservice.domain.Coin;
import com.antya.pdeservice.repository.CoinRepository;
import com.antya.pdeservice.service.dto.CoinDTO;
import com.antya.pdeservice.service.mapper.CoinMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Coin.
 */
@Service
@Transactional
public class CoinServiceImpl implements CoinService {

    private final Logger log = LoggerFactory.getLogger(CoinServiceImpl.class);

    private final CoinRepository coinRepository;

    private final CoinMapper coinMapper;

    public CoinServiceImpl(CoinRepository coinRepository, CoinMapper coinMapper) {
        this.coinRepository = coinRepository;
        this.coinMapper = coinMapper;
    }

    /**
     * Save a coin.
     *
     * @param coinDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CoinDTO save(CoinDTO coinDTO) {
        log.debug("Request to save Coin : {}", coinDTO);
        Coin coin = coinMapper.toEntity(coinDTO);
        coin = coinRepository.save(coin);
        return coinMapper.toDto(coin);
    }

    /**
     * Get all the coins.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Coins");
        return coinRepository.findAll(pageable)
            .map(coinMapper::toDto);
    }

    /**
     * Get one coin by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CoinDTO findOne(Long id) {
        log.debug("Request to get Coin : {}", id);
        Coin coin = coinRepository.findOne(id);
        return coinMapper.toDto(coin);
    }

    /**
     * Delete the coin by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coin : {}", id);
        coinRepository.delete(id);
    }
}
