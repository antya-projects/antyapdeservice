package com.antya.pdeservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.antya.pdeservice.service.ExchangeService;
import com.antya.pdeservice.web.rest.errors.BadRequestAlertException;
import com.antya.pdeservice.web.rest.util.HeaderUtil;
import com.antya.pdeservice.web.rest.util.PaginationUtil;
import com.antya.pdeservice.service.dto.ExchangeDTO;
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
 * REST controller for managing Exchange.
 */
@RestController
@RequestMapping("/api")
public class ExchangeResource {

    private final Logger log = LoggerFactory.getLogger(ExchangeResource.class);

    private static final String ENTITY_NAME = "exchange";

    private final ExchangeService exchangeService;

    public ExchangeResource(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    /**
     * POST  /exchanges : Create a new exchange.
     *
     * @param exchangeDTO the exchangeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new exchangeDTO, or with status 400 (Bad Request) if the exchange has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exchanges")
    @Timed
    public ResponseEntity<ExchangeDTO> createExchange(@RequestBody ExchangeDTO exchangeDTO) throws URISyntaxException {
        log.debug("REST request to save Exchange : {}", exchangeDTO);
        if (exchangeDTO.getId() != null) {
            throw new BadRequestAlertException("A new exchange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExchangeDTO result = exchangeService.save(exchangeDTO);
        return ResponseEntity.created(new URI("/api/exchanges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exchanges : Updates an existing exchange.
     *
     * @param exchangeDTO the exchangeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated exchangeDTO,
     * or with status 400 (Bad Request) if the exchangeDTO is not valid,
     * or with status 500 (Internal Server Error) if the exchangeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exchanges")
    @Timed
    public ResponseEntity<ExchangeDTO> updateExchange(@RequestBody ExchangeDTO exchangeDTO) throws URISyntaxException {
        log.debug("REST request to update Exchange : {}", exchangeDTO);
        if (exchangeDTO.getId() == null) {
            return createExchange(exchangeDTO);
        }
        ExchangeDTO result = exchangeService.save(exchangeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, exchangeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exchanges : get all the exchanges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of exchanges in body
     */
    @GetMapping("/exchanges")
    @Timed
    public ResponseEntity<List<ExchangeDTO>> getAllExchanges(Pageable pageable) {
        log.debug("REST request to get a page of Exchanges");
        Page<ExchangeDTO> page = exchangeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exchanges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exchanges/:id : get the "id" exchange.
     *
     * @param id the id of the exchangeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the exchangeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/exchanges/{id}")
    @Timed
    public ResponseEntity<ExchangeDTO> getExchange(@PathVariable Long id) {
        log.debug("REST request to get Exchange : {}", id);
        ExchangeDTO exchangeDTO = exchangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(exchangeDTO));
    }

    /**
     * DELETE  /exchanges/:id : delete the "id" exchange.
     *
     * @param id the id of the exchangeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exchanges/{id}")
    @Timed
    public ResponseEntity<Void> deleteExchange(@PathVariable Long id) {
        log.debug("REST request to delete Exchange : {}", id);
        exchangeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
