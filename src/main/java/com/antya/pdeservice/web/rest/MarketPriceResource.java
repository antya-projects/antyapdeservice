package com.antya.pdeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.antya.pdeservice.service.MarketPriceService;
import com.antya.pdeservice.web.rest.errors.BadRequestAlertException;
import com.antya.pdeservice.web.rest.util.HeaderUtil;
import com.antya.pdeservice.web.rest.util.PaginationUtil;
import com.antya.pdeservice.service.dto.MarketPriceDTO;
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
 * REST controller for managing MarketPrice.
 */
@RestController
@RequestMapping("/api")
public class MarketPriceResource {

    private final Logger log = LoggerFactory.getLogger(MarketPriceResource.class);

    private static final String ENTITY_NAME = "marketPrice";

    private final MarketPriceService marketPriceService;

    public MarketPriceResource(MarketPriceService marketPriceService) {
        this.marketPriceService = marketPriceService;
    }

    /**
     * POST  /market-prices : Create a new marketPrice.
     *
     * @param marketPriceDTO the marketPriceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marketPriceDTO, or with status 400 (Bad Request) if the marketPrice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/market-prices")
    @Timed
    public ResponseEntity<MarketPriceDTO> createMarketPrice(@RequestBody MarketPriceDTO marketPriceDTO) throws URISyntaxException {
        log.debug("REST request to save MarketPrice : {}", marketPriceDTO);
        if (marketPriceDTO.getId() != null) {
            throw new BadRequestAlertException("A new marketPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarketPriceDTO result = marketPriceService.save(marketPriceDTO);
        return ResponseEntity.created(new URI("/api/market-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /market-prices : Updates an existing marketPrice.
     *
     * @param marketPriceDTO the marketPriceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marketPriceDTO,
     * or with status 400 (Bad Request) if the marketPriceDTO is not valid,
     * or with status 500 (Internal Server Error) if the marketPriceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/market-prices")
    @Timed
    public ResponseEntity<MarketPriceDTO> updateMarketPrice(@RequestBody MarketPriceDTO marketPriceDTO) throws URISyntaxException {
        log.debug("REST request to update MarketPrice : {}", marketPriceDTO);
        if (marketPriceDTO.getId() == null) {
            return createMarketPrice(marketPriceDTO);
        }
        MarketPriceDTO result = marketPriceService.save(marketPriceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, marketPriceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /market-prices : get all the marketPrices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of marketPrices in body
     */
    @GetMapping("/market-prices")
    @Timed
    public ResponseEntity<List<MarketPriceDTO>> getAllMarketPrices(Pageable pageable) {
        log.debug("REST request to get a page of MarketPrices");
        Page<MarketPriceDTO> page = marketPriceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/market-prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /market-prices/:id : get the "id" marketPrice.
     *
     * @param id the id of the marketPriceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marketPriceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/market-prices/{id}")
    @Timed
    public ResponseEntity<MarketPriceDTO> getMarketPrice(@PathVariable Long id) {
        log.debug("REST request to get MarketPrice : {}", id);
        MarketPriceDTO marketPriceDTO = marketPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(marketPriceDTO));
    }

    /**
     * DELETE  /market-prices/:id : delete the "id" marketPrice.
     *
     * @param id the id of the marketPriceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/market-prices/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarketPrice(@PathVariable Long id) {
        log.debug("REST request to delete MarketPrice : {}", id);
        marketPriceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
