package com.antya.pdeservice.web.rest;

import com.antya.pdeservice.AntyapdeserviceApp;

import com.antya.pdeservice.domain.Exchange;
import com.antya.pdeservice.repository.ExchangeRepository;
import com.antya.pdeservice.service.ExchangeService;
import com.antya.pdeservice.service.dto.ExchangeDTO;
import com.antya.pdeservice.service.mapper.ExchangeMapper;
import com.antya.pdeservice.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.antya.pdeservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExchangeResource REST controller.
 *
 * @see ExchangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AntyapdeserviceApp.class)
public class ExchangeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_ACTIVE = 1;
    private static final Integer UPDATED_IS_ACTIVE = 2;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExchangeMockMvc;

    private Exchange exchange;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExchangeResource exchangeResource = new ExchangeResource(exchangeService);
        this.restExchangeMockMvc = MockMvcBuilders.standaloneSetup(exchangeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exchange createEntity(EntityManager em) {
        Exchange exchange = new Exchange()
            .name(DEFAULT_NAME)
            .country(DEFAULT_COUNTRY)
            .isActive(DEFAULT_IS_ACTIVE);
        return exchange;
    }

    @Before
    public void initTest() {
        exchange = createEntity(em);
    }

    @Test
    @Transactional
    public void createExchange() throws Exception {
        int databaseSizeBeforeCreate = exchangeRepository.findAll().size();

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);
        restExchangeMockMvc.perform(post("/api/exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exchangeDTO)))
            .andExpect(status().isCreated());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeCreate + 1);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExchange.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testExchange.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createExchangeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exchangeRepository.findAll().size();

        // Create the Exchange with an existing ID
        exchange.setId(1L);
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExchangeMockMvc.perform(post("/api/exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exchangeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExchanges() throws Exception {
        // Initialize the database
        exchangeRepository.saveAndFlush(exchange);

        // Get all the exchangeList
        restExchangeMockMvc.perform(get("/api/exchanges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exchange.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    public void getExchange() throws Exception {
        // Initialize the database
        exchangeRepository.saveAndFlush(exchange);

        // Get the exchange
        restExchangeMockMvc.perform(get("/api/exchanges/{id}", exchange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exchange.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    public void getNonExistingExchange() throws Exception {
        // Get the exchange
        restExchangeMockMvc.perform(get("/api/exchanges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExchange() throws Exception {
        // Initialize the database
        exchangeRepository.saveAndFlush(exchange);
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();

        // Update the exchange
        Exchange updatedExchange = exchangeRepository.findOne(exchange.getId());
        // Disconnect from session so that the updates on updatedExchange are not directly saved in db
        em.detach(updatedExchange);
        updatedExchange
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .isActive(UPDATED_IS_ACTIVE);
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(updatedExchange);

        restExchangeMockMvc.perform(put("/api/exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exchangeDTO)))
            .andExpect(status().isOk());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExchange.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testExchange.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExchangeMockMvc.perform(put("/api/exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exchangeDTO)))
            .andExpect(status().isCreated());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExchange() throws Exception {
        // Initialize the database
        exchangeRepository.saveAndFlush(exchange);
        int databaseSizeBeforeDelete = exchangeRepository.findAll().size();

        // Get the exchange
        restExchangeMockMvc.perform(delete("/api/exchanges/{id}", exchange.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exchange.class);
        Exchange exchange1 = new Exchange();
        exchange1.setId(1L);
        Exchange exchange2 = new Exchange();
        exchange2.setId(exchange1.getId());
        assertThat(exchange1).isEqualTo(exchange2);
        exchange2.setId(2L);
        assertThat(exchange1).isNotEqualTo(exchange2);
        exchange1.setId(null);
        assertThat(exchange1).isNotEqualTo(exchange2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExchangeDTO.class);
        ExchangeDTO exchangeDTO1 = new ExchangeDTO();
        exchangeDTO1.setId(1L);
        ExchangeDTO exchangeDTO2 = new ExchangeDTO();
        assertThat(exchangeDTO1).isNotEqualTo(exchangeDTO2);
        exchangeDTO2.setId(exchangeDTO1.getId());
        assertThat(exchangeDTO1).isEqualTo(exchangeDTO2);
        exchangeDTO2.setId(2L);
        assertThat(exchangeDTO1).isNotEqualTo(exchangeDTO2);
        exchangeDTO1.setId(null);
        assertThat(exchangeDTO1).isNotEqualTo(exchangeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(exchangeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(exchangeMapper.fromId(null)).isNull();
    }
}
