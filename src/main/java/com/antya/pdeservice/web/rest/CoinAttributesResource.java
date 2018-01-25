package com.antya.pdeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.antya.pdeservice.service.CoinAttributesService;
import com.antya.pdeservice.web.rest.errors.BadRequestAlertException;
import com.antya.pdeservice.web.rest.util.HeaderUtil;
import com.antya.pdeservice.web.rest.util.PaginationUtil;
import com.antya.pdeservice.service.dto.CoinAttributesDTO;
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
 * REST controller for managing CoinAttributes.
 */
@RestController
@RequestMapping("/api")
public class CoinAttributesResource {

    private final Logger log = LoggerFactory.getLogger(CoinAttributesResource.class);

    private static final String ENTITY_NAME = "coinAttributes";

    private final CoinAttributesService coinAttributesService;

    public CoinAttributesResource(CoinAttributesService coinAttributesService) {
        this.coinAttributesService = coinAttributesService;
    }

    /**
     * POST  /coin-attributes : Create a new coinAttributes.
     *
     * @param coinAttributesDTO the coinAttributesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coinAttributesDTO, or with status 400 (Bad Request) if the coinAttributes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coin-attributes")
    @Timed
    public ResponseEntity<CoinAttributesDTO> createCoinAttributes(@RequestBody CoinAttributesDTO coinAttributesDTO) throws URISyntaxException {
        log.debug("REST request to save CoinAttributes : {}", coinAttributesDTO);
        if (coinAttributesDTO.getId() != null) {
            throw new BadRequestAlertException("A new coinAttributes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoinAttributesDTO result = coinAttributesService.save(coinAttributesDTO);
        return ResponseEntity.created(new URI("/api/coin-attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coin-attributes : Updates an existing coinAttributes.
     *
     * @param coinAttributesDTO the coinAttributesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coinAttributesDTO,
     * or with status 400 (Bad Request) if the coinAttributesDTO is not valid,
     * or with status 500 (Internal Server Error) if the coinAttributesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coin-attributes")
    @Timed
    public ResponseEntity<CoinAttributesDTO> updateCoinAttributes(@RequestBody CoinAttributesDTO coinAttributesDTO) throws URISyntaxException {
        log.debug("REST request to update CoinAttributes : {}", coinAttributesDTO);
        if (coinAttributesDTO.getId() == null) {
            return createCoinAttributes(coinAttributesDTO);
        }
        CoinAttributesDTO result = coinAttributesService.save(coinAttributesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coinAttributesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coin-attributes : get all the coinAttributes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coinAttributes in body
     */
    @GetMapping("/coin-attributes")
    @Timed
    public ResponseEntity<List<CoinAttributesDTO>> getAllCoinAttributes(Pageable pageable) {
        log.debug("REST request to get a page of CoinAttributes");
        Page<CoinAttributesDTO> page = coinAttributesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coin-attributes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coin-attributes/:id : get the "id" coinAttributes.
     *
     * @param id the id of the coinAttributesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coinAttributesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coin-attributes/{id}")
    @Timed
    public ResponseEntity<CoinAttributesDTO> getCoinAttributes(@PathVariable Long id) {
        log.debug("REST request to get CoinAttributes : {}", id);
        CoinAttributesDTO coinAttributesDTO = coinAttributesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coinAttributesDTO));
    }

    /**
     * DELETE  /coin-attributes/:id : delete the "id" coinAttributes.
     *
     * @param id the id of the coinAttributesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coin-attributes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoinAttributes(@PathVariable Long id) {
        log.debug("REST request to delete CoinAttributes : {}", id);
        coinAttributesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
