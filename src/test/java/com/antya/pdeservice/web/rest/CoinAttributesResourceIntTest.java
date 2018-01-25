package com.antya.pdeservice.web.rest;

import com.antya.pdeservice.AntyapdeserviceApp;

import com.antya.pdeservice.domain.CoinAttributes;
import com.antya.pdeservice.repository.CoinAttributesRepository;
import com.antya.pdeservice.service.CoinAttributesService;
import com.antya.pdeservice.service.dto.CoinAttributesDTO;
import com.antya.pdeservice.service.mapper.CoinAttributesMapper;
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
import java.math.BigDecimal;
import java.util.List;

import static com.antya.pdeservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoinAttributesResource REST controller.
 *
 * @see CoinAttributesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AntyapdeserviceApp.class)
public class CoinAttributesResourceIntTest {

    private static final Integer DEFAULT_MIN_CONFIRMATION = 1;
    private static final Integer UPDATED_MIN_CONFIRMATION = 2;

    private static final BigDecimal DEFAULT_TXN_FEES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TXN_FEES = new BigDecimal(2);

    @Autowired
    private CoinAttributesRepository coinAttributesRepository;

    @Autowired
    private CoinAttributesMapper coinAttributesMapper;

    @Autowired
    private CoinAttributesService coinAttributesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoinAttributesMockMvc;

    private CoinAttributes coinAttributes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoinAttributesResource coinAttributesResource = new CoinAttributesResource(coinAttributesService);
        this.restCoinAttributesMockMvc = MockMvcBuilders.standaloneSetup(coinAttributesResource)
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
    public static CoinAttributes createEntity(EntityManager em) {
        CoinAttributes coinAttributes = new CoinAttributes()
            .minConfirmation(DEFAULT_MIN_CONFIRMATION)
            .txnFees(DEFAULT_TXN_FEES);
        return coinAttributes;
    }

    @Before
    public void initTest() {
        coinAttributes = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoinAttributes() throws Exception {
        int databaseSizeBeforeCreate = coinAttributesRepository.findAll().size();

        // Create the CoinAttributes
        CoinAttributesDTO coinAttributesDTO = coinAttributesMapper.toDto(coinAttributes);
        restCoinAttributesMockMvc.perform(post("/api/coin-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinAttributesDTO)))
            .andExpect(status().isCreated());

        // Validate the CoinAttributes in the database
        List<CoinAttributes> coinAttributesList = coinAttributesRepository.findAll();
        assertThat(coinAttributesList).hasSize(databaseSizeBeforeCreate + 1);
        CoinAttributes testCoinAttributes = coinAttributesList.get(coinAttributesList.size() - 1);
        assertThat(testCoinAttributes.getMinConfirmation()).isEqualTo(DEFAULT_MIN_CONFIRMATION);
        assertThat(testCoinAttributes.getTxnFees()).isEqualTo(DEFAULT_TXN_FEES);
    }

    @Test
    @Transactional
    public void createCoinAttributesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coinAttributesRepository.findAll().size();

        // Create the CoinAttributes with an existing ID
        coinAttributes.setId(1L);
        CoinAttributesDTO coinAttributesDTO = coinAttributesMapper.toDto(coinAttributes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoinAttributesMockMvc.perform(post("/api/coin-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinAttributesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CoinAttributes in the database
        List<CoinAttributes> coinAttributesList = coinAttributesRepository.findAll();
        assertThat(coinAttributesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoinAttributes() throws Exception {
        // Initialize the database
        coinAttributesRepository.saveAndFlush(coinAttributes);

        // Get all the coinAttributesList
        restCoinAttributesMockMvc.perform(get("/api/coin-attributes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coinAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].minConfirmation").value(hasItem(DEFAULT_MIN_CONFIRMATION)))
            .andExpect(jsonPath("$.[*].txnFees").value(hasItem(DEFAULT_TXN_FEES.intValue())));
    }

    @Test
    @Transactional
    public void getCoinAttributes() throws Exception {
        // Initialize the database
        coinAttributesRepository.saveAndFlush(coinAttributes);

        // Get the coinAttributes
        restCoinAttributesMockMvc.perform(get("/api/coin-attributes/{id}", coinAttributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coinAttributes.getId().intValue()))
            .andExpect(jsonPath("$.minConfirmation").value(DEFAULT_MIN_CONFIRMATION))
            .andExpect(jsonPath("$.txnFees").value(DEFAULT_TXN_FEES.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCoinAttributes() throws Exception {
        // Get the coinAttributes
        restCoinAttributesMockMvc.perform(get("/api/coin-attributes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoinAttributes() throws Exception {
        // Initialize the database
        coinAttributesRepository.saveAndFlush(coinAttributes);
        int databaseSizeBeforeUpdate = coinAttributesRepository.findAll().size();

        // Update the coinAttributes
        CoinAttributes updatedCoinAttributes = coinAttributesRepository.findOne(coinAttributes.getId());
        // Disconnect from session so that the updates on updatedCoinAttributes are not directly saved in db
        em.detach(updatedCoinAttributes);
        updatedCoinAttributes
            .minConfirmation(UPDATED_MIN_CONFIRMATION)
            .txnFees(UPDATED_TXN_FEES);
        CoinAttributesDTO coinAttributesDTO = coinAttributesMapper.toDto(updatedCoinAttributes);

        restCoinAttributesMockMvc.perform(put("/api/coin-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinAttributesDTO)))
            .andExpect(status().isOk());

        // Validate the CoinAttributes in the database
        List<CoinAttributes> coinAttributesList = coinAttributesRepository.findAll();
        assertThat(coinAttributesList).hasSize(databaseSizeBeforeUpdate);
        CoinAttributes testCoinAttributes = coinAttributesList.get(coinAttributesList.size() - 1);
        assertThat(testCoinAttributes.getMinConfirmation()).isEqualTo(UPDATED_MIN_CONFIRMATION);
        assertThat(testCoinAttributes.getTxnFees()).isEqualTo(UPDATED_TXN_FEES);
    }

    @Test
    @Transactional
    public void updateNonExistingCoinAttributes() throws Exception {
        int databaseSizeBeforeUpdate = coinAttributesRepository.findAll().size();

        // Create the CoinAttributes
        CoinAttributesDTO coinAttributesDTO = coinAttributesMapper.toDto(coinAttributes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoinAttributesMockMvc.perform(put("/api/coin-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinAttributesDTO)))
            .andExpect(status().isCreated());

        // Validate the CoinAttributes in the database
        List<CoinAttributes> coinAttributesList = coinAttributesRepository.findAll();
        assertThat(coinAttributesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoinAttributes() throws Exception {
        // Initialize the database
        coinAttributesRepository.saveAndFlush(coinAttributes);
        int databaseSizeBeforeDelete = coinAttributesRepository.findAll().size();

        // Get the coinAttributes
        restCoinAttributesMockMvc.perform(delete("/api/coin-attributes/{id}", coinAttributes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CoinAttributes> coinAttributesList = coinAttributesRepository.findAll();
        assertThat(coinAttributesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoinAttributes.class);
        CoinAttributes coinAttributes1 = new CoinAttributes();
        coinAttributes1.setId(1L);
        CoinAttributes coinAttributes2 = new CoinAttributes();
        coinAttributes2.setId(coinAttributes1.getId());
        assertThat(coinAttributes1).isEqualTo(coinAttributes2);
        coinAttributes2.setId(2L);
        assertThat(coinAttributes1).isNotEqualTo(coinAttributes2);
        coinAttributes1.setId(null);
        assertThat(coinAttributes1).isNotEqualTo(coinAttributes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoinAttributesDTO.class);
        CoinAttributesDTO coinAttributesDTO1 = new CoinAttributesDTO();
        coinAttributesDTO1.setId(1L);
        CoinAttributesDTO coinAttributesDTO2 = new CoinAttributesDTO();
        assertThat(coinAttributesDTO1).isNotEqualTo(coinAttributesDTO2);
        coinAttributesDTO2.setId(coinAttributesDTO1.getId());
        assertThat(coinAttributesDTO1).isEqualTo(coinAttributesDTO2);
        coinAttributesDTO2.setId(2L);
        assertThat(coinAttributesDTO1).isNotEqualTo(coinAttributesDTO2);
        coinAttributesDTO1.setId(null);
        assertThat(coinAttributesDTO1).isNotEqualTo(coinAttributesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(coinAttributesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(coinAttributesMapper.fromId(null)).isNull();
    }
}
