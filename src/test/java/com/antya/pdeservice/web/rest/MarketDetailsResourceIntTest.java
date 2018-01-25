package com.antya.pdeservice.web.rest;

import com.antya.pdeservice.AntyapdeserviceApp;

import com.antya.pdeservice.domain.MarketDetails;
import com.antya.pdeservice.repository.MarketDetailsRepository;
import com.antya.pdeservice.service.MarketDetailsService;
import com.antya.pdeservice.service.dto.MarketDetailsDTO;
import com.antya.pdeservice.service.mapper.MarketDetailsMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.antya.pdeservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MarketDetailsResource REST controller.
 *
 * @see MarketDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AntyapdeserviceApp.class)
public class MarketDetailsResourceIntTest {

    private static final BigDecimal DEFAULT_HIGH_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_HIGH_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LOW_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LOW_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LAST_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LAST_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ASK_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASK_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BID_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BID_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VOLUME_24_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_VOLUME_24_HOURS = new BigDecimal(2);

    private static final Instant DEFAULT_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_IS_ACTIVE = 1;
    private static final Integer UPDATED_IS_ACTIVE = 2;

    @Autowired
    private MarketDetailsRepository marketDetailsRepository;

    @Autowired
    private MarketDetailsMapper marketDetailsMapper;

    @Autowired
    private MarketDetailsService marketDetailsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMarketDetailsMockMvc;

    private MarketDetails marketDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarketDetailsResource marketDetailsResource = new MarketDetailsResource(marketDetailsService);
        this.restMarketDetailsMockMvc = MockMvcBuilders.standaloneSetup(marketDetailsResource)
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
    public static MarketDetails createEntity(EntityManager em) {
        MarketDetails marketDetails = new MarketDetails()
            .highPrice(DEFAULT_HIGH_PRICE)
            .lowPrice(DEFAULT_LOW_PRICE)
            .lastPrice(DEFAULT_LAST_PRICE)
            .askPrice(DEFAULT_ASK_PRICE)
            .bidPrice(DEFAULT_BID_PRICE)
            .volume24hours(DEFAULT_VOLUME_24_HOURS)
            .timeStamp(DEFAULT_TIME_STAMP)
            .isActive(DEFAULT_IS_ACTIVE);
        return marketDetails;
    }

    @Before
    public void initTest() {
        marketDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarketDetails() throws Exception {
        int databaseSizeBeforeCreate = marketDetailsRepository.findAll().size();

        // Create the MarketDetails
        MarketDetailsDTO marketDetailsDTO = marketDetailsMapper.toDto(marketDetails);
        restMarketDetailsMockMvc.perform(post("/api/market-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the MarketDetails in the database
        List<MarketDetails> marketDetailsList = marketDetailsRepository.findAll();
        assertThat(marketDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        MarketDetails testMarketDetails = marketDetailsList.get(marketDetailsList.size() - 1);
        assertThat(testMarketDetails.getHighPrice()).isEqualTo(DEFAULT_HIGH_PRICE);
        assertThat(testMarketDetails.getLowPrice()).isEqualTo(DEFAULT_LOW_PRICE);
        assertThat(testMarketDetails.getLastPrice()).isEqualTo(DEFAULT_LAST_PRICE);
        assertThat(testMarketDetails.getAskPrice()).isEqualTo(DEFAULT_ASK_PRICE);
        assertThat(testMarketDetails.getBidPrice()).isEqualTo(DEFAULT_BID_PRICE);
        assertThat(testMarketDetails.getVolume24hours()).isEqualTo(DEFAULT_VOLUME_24_HOURS);
        assertThat(testMarketDetails.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
        assertThat(testMarketDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createMarketDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = marketDetailsRepository.findAll().size();

        // Create the MarketDetails with an existing ID
        marketDetails.setId(1L);
        MarketDetailsDTO marketDetailsDTO = marketDetailsMapper.toDto(marketDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketDetailsMockMvc.perform(post("/api/market-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MarketDetails in the database
        List<MarketDetails> marketDetailsList = marketDetailsRepository.findAll();
        assertThat(marketDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMarketDetails() throws Exception {
        // Initialize the database
        marketDetailsRepository.saveAndFlush(marketDetails);

        // Get all the marketDetailsList
        restMarketDetailsMockMvc.perform(get("/api/market-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].highPrice").value(hasItem(DEFAULT_HIGH_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].lowPrice").value(hasItem(DEFAULT_LOW_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].lastPrice").value(hasItem(DEFAULT_LAST_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].askPrice").value(hasItem(DEFAULT_ASK_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(DEFAULT_BID_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].volume24hours").value(hasItem(DEFAULT_VOLUME_24_HOURS.intValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(DEFAULT_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    public void getMarketDetails() throws Exception {
        // Initialize the database
        marketDetailsRepository.saveAndFlush(marketDetails);

        // Get the marketDetails
        restMarketDetailsMockMvc.perform(get("/api/market-details/{id}", marketDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marketDetails.getId().intValue()))
            .andExpect(jsonPath("$.highPrice").value(DEFAULT_HIGH_PRICE.intValue()))
            .andExpect(jsonPath("$.lowPrice").value(DEFAULT_LOW_PRICE.intValue()))
            .andExpect(jsonPath("$.lastPrice").value(DEFAULT_LAST_PRICE.intValue()))
            .andExpect(jsonPath("$.askPrice").value(DEFAULT_ASK_PRICE.intValue()))
            .andExpect(jsonPath("$.bidPrice").value(DEFAULT_BID_PRICE.intValue()))
            .andExpect(jsonPath("$.volume24hours").value(DEFAULT_VOLUME_24_HOURS.intValue()))
            .andExpect(jsonPath("$.timeStamp").value(DEFAULT_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    public void getNonExistingMarketDetails() throws Exception {
        // Get the marketDetails
        restMarketDetailsMockMvc.perform(get("/api/market-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarketDetails() throws Exception {
        // Initialize the database
        marketDetailsRepository.saveAndFlush(marketDetails);
        int databaseSizeBeforeUpdate = marketDetailsRepository.findAll().size();

        // Update the marketDetails
        MarketDetails updatedMarketDetails = marketDetailsRepository.findOne(marketDetails.getId());
        // Disconnect from session so that the updates on updatedMarketDetails are not directly saved in db
        em.detach(updatedMarketDetails);
        updatedMarketDetails
            .highPrice(UPDATED_HIGH_PRICE)
            .lowPrice(UPDATED_LOW_PRICE)
            .lastPrice(UPDATED_LAST_PRICE)
            .askPrice(UPDATED_ASK_PRICE)
            .bidPrice(UPDATED_BID_PRICE)
            .volume24hours(UPDATED_VOLUME_24_HOURS)
            .timeStamp(UPDATED_TIME_STAMP)
            .isActive(UPDATED_IS_ACTIVE);
        MarketDetailsDTO marketDetailsDTO = marketDetailsMapper.toDto(updatedMarketDetails);

        restMarketDetailsMockMvc.perform(put("/api/market-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the MarketDetails in the database
        List<MarketDetails> marketDetailsList = marketDetailsRepository.findAll();
        assertThat(marketDetailsList).hasSize(databaseSizeBeforeUpdate);
        MarketDetails testMarketDetails = marketDetailsList.get(marketDetailsList.size() - 1);
        assertThat(testMarketDetails.getHighPrice()).isEqualTo(UPDATED_HIGH_PRICE);
        assertThat(testMarketDetails.getLowPrice()).isEqualTo(UPDATED_LOW_PRICE);
        assertThat(testMarketDetails.getLastPrice()).isEqualTo(UPDATED_LAST_PRICE);
        assertThat(testMarketDetails.getAskPrice()).isEqualTo(UPDATED_ASK_PRICE);
        assertThat(testMarketDetails.getBidPrice()).isEqualTo(UPDATED_BID_PRICE);
        assertThat(testMarketDetails.getVolume24hours()).isEqualTo(UPDATED_VOLUME_24_HOURS);
        assertThat(testMarketDetails.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
        assertThat(testMarketDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingMarketDetails() throws Exception {
        int databaseSizeBeforeUpdate = marketDetailsRepository.findAll().size();

        // Create the MarketDetails
        MarketDetailsDTO marketDetailsDTO = marketDetailsMapper.toDto(marketDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMarketDetailsMockMvc.perform(put("/api/market-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the MarketDetails in the database
        List<MarketDetails> marketDetailsList = marketDetailsRepository.findAll();
        assertThat(marketDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMarketDetails() throws Exception {
        // Initialize the database
        marketDetailsRepository.saveAndFlush(marketDetails);
        int databaseSizeBeforeDelete = marketDetailsRepository.findAll().size();

        // Get the marketDetails
        restMarketDetailsMockMvc.perform(delete("/api/market-details/{id}", marketDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MarketDetails> marketDetailsList = marketDetailsRepository.findAll();
        assertThat(marketDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketDetails.class);
        MarketDetails marketDetails1 = new MarketDetails();
        marketDetails1.setId(1L);
        MarketDetails marketDetails2 = new MarketDetails();
        marketDetails2.setId(marketDetails1.getId());
        assertThat(marketDetails1).isEqualTo(marketDetails2);
        marketDetails2.setId(2L);
        assertThat(marketDetails1).isNotEqualTo(marketDetails2);
        marketDetails1.setId(null);
        assertThat(marketDetails1).isNotEqualTo(marketDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketDetailsDTO.class);
        MarketDetailsDTO marketDetailsDTO1 = new MarketDetailsDTO();
        marketDetailsDTO1.setId(1L);
        MarketDetailsDTO marketDetailsDTO2 = new MarketDetailsDTO();
        assertThat(marketDetailsDTO1).isNotEqualTo(marketDetailsDTO2);
        marketDetailsDTO2.setId(marketDetailsDTO1.getId());
        assertThat(marketDetailsDTO1).isEqualTo(marketDetailsDTO2);
        marketDetailsDTO2.setId(2L);
        assertThat(marketDetailsDTO1).isNotEqualTo(marketDetailsDTO2);
        marketDetailsDTO1.setId(null);
        assertThat(marketDetailsDTO1).isNotEqualTo(marketDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(marketDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(marketDetailsMapper.fromId(null)).isNull();
    }
}
