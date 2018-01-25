package com.antya.pdeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.antya.pdeservice.service.CoinService;
import com.antya.pdeservice.web.rest.errors.BadRequestAlertException;
import com.antya.pdeservice.web.rest.util.HeaderUtil;
import com.antya.pdeservice.web.rest.util.PaginationUtil;
import com.antya.pdeservice.service.dto.CoinDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Coin.
 */
@RestController
@RequestMapping("/api")
public class CoinResource {

    private final Logger log = LoggerFactory.getLogger(CoinResource.class);

    private static final String ENTITY_NAME = "coin";

    private final CoinService coinService;

    public CoinResource(CoinService coinService) {
        this.coinService = coinService;
    }

    /**
     * POST  /coins : Create a new coin.
     *
     * @param coinDTO the coinDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coinDTO, or with status 400 (Bad Request) if the coin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coins")
    @Timed
    public ResponseEntity<CoinDTO> createCoin(@RequestBody CoinDTO coinDTO) throws URISyntaxException {
        log.debug("REST request to save Coin : {}", coinDTO);
        if (coinDTO.getId() != null) {
            throw new BadRequestAlertException("A new coin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoinDTO result = coinService.save(coinDTO);
        return ResponseEntity.created(new URI("/api/coins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coins : Updates an existing coin.
     *
     * @param coinDTO the coinDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coinDTO,
     * or with status 400 (Bad Request) if the coinDTO is not valid,
     * or with status 500 (Internal Server Error) if the coinDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coins")
    @Timed
    public ResponseEntity<CoinDTO> updateCoin(@RequestBody CoinDTO coinDTO) throws URISyntaxException {
        log.debug("REST request to update Coin : {}", coinDTO);
        if (coinDTO.getId() == null) {
            return createCoin(coinDTO);
        }
        CoinDTO result = coinService.save(coinDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coinDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coins : get all the coins.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coins in body
     */
    @GetMapping("/coins")
    @Timed
    public ResponseEntity<List<CoinDTO>> getAllCoins(Pageable pageable) {
        log.debug("REST request to get a page of Coins");
        Page<CoinDTO> page = coinService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coins/:id : get the "id" coin.
     *
     * @param id the id of the coinDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coinDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coins/{id}")
    @Timed
    public ResponseEntity<CoinDTO> getCoin(@PathVariable Long id) {
        log.debug("REST request to get Coin : {}", id);
        CoinDTO coinDTO = coinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coinDTO));
    }

    /**
     * DELETE  /coins/:id : delete the "id" coin.
     *
     * @param id the id of the coinDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coins/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoin(@PathVariable Long id) {
        log.debug("REST request to delete Coin : {}", id);
        coinService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
