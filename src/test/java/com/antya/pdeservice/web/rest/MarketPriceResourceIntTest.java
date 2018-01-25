package com.antya.pdeservice.web.rest;

import com.antya.pdeservice.AntyapdeserviceApp;

import com.antya.pdeservice.domain.MarketPrice;
import com.antya.pdeservice.repository.MarketPriceRepository;
import com.antya.pdeservice.service.MarketPriceService;
import com.antya.pdeservice.service.dto.MarketPriceDTO;
import com.antya.pdeservice.service.mapper.MarketPriceMapper;
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
 * Test class for the MarketPriceResource REST controller.
 *
 * @see MarketPriceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AntyapdeserviceApp.class)
public class MarketPriceResourceIntTest {

    private static final BigDecimal DEFAULT_CURRENT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_PRICE = new BigDecimal(2);

    private static final Instant DEFAULT_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Autowired
    private MarketPriceMapper marketPriceMapper;

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMarketPriceMockMvc;

    private MarketPrice marketPrice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarketPriceResource marketPriceResource = new MarketPriceResource(marketPriceService);
        this.restMarketPriceMockMvc = MockMvcBuilders.standaloneSetup(marketPriceResource)
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
    public static MarketPrice createEntity(EntityManager em) {
        MarketPrice marketPrice = new MarketPrice()
            .currentPrice(DEFAULT_CURRENT_PRICE)
            .timeStamp(DEFAULT_TIME_STAMP);
        return marketPrice;
    }

    @Before
    public void initTest() {
        marketPrice = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarketPrice() throws Exception {
        int databaseSizeBeforeCreate = marketPriceRepository.findAll().size();

        // Create the MarketPrice
        MarketPriceDTO marketPriceDTO = marketPriceMapper.toDto(marketPrice);
        restMarketPriceMockMvc.perform(post("/api/market-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketPriceDTO)))
            .andExpect(status().isCreated());

        // Validate the MarketPrice in the database
        List<MarketPrice> marketPriceList = marketPriceRepository.findAll();
        assertThat(marketPriceList).hasSize(databaseSizeBeforeCreate + 1);
        MarketPrice testMarketPrice = marketPriceList.get(marketPriceList.size() - 1);
        assertThat(testMarketPrice.getCurrentPrice()).isEqualTo(DEFAULT_CURRENT_PRICE);
        assertThat(testMarketPrice.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
    }

    @Test
    @Transactional
    public void createMarketPriceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = marketPriceRepository.findAll().size();

        // Create the MarketPrice with an existing ID
        marketPrice.setId(1L);
        MarketPriceDTO marketPriceDTO = marketPriceMapper.toDto(marketPrice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketPriceMockMvc.perform(post("/api/market-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketPriceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MarketPrice in the database
        List<MarketPrice> marketPriceList = marketPriceRepository.findAll();
        assertThat(marketPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMarketPrices() throws Exception {
        // Initialize the database
        marketPriceRepository.saveAndFlush(marketPrice);

        // Get all the marketPriceList
        restMarketPriceMockMvc.perform(get("/api/market-prices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].currentPrice").value(hasItem(DEFAULT_CURRENT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(DEFAULT_TIME_STAMP.toString())));
    }

    @Test
    @Transactional
    public void getMarketPrice() throws Exception {
        // Initialize the database
        marketPriceRepository.saveAndFlush(marketPrice);

        // Get the marketPrice
        restMarketPriceMockMvc.perform(get("/api/market-prices/{id}", marketPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marketPrice.getId().intValue()))
            .andExpect(jsonPath("$.currentPrice").value(DEFAULT_CURRENT_PRICE.intValue()))
            .andExpect(jsonPath("$.timeStamp").value(DEFAULT_TIME_STAMP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMarketPrice() throws Exception {
        // Get the marketPrice
        restMarketPriceMockMvc.perform(get("/api/market-prices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarketPrice() throws Exception {
        // Initialize the database
        marketPriceRepository.saveAndFlush(marketPrice);
        int databaseSizeBeforeUpdate = marketPriceRepository.findAll().size();

        // Update the marketPrice
        MarketPrice updatedMarketPrice = marketPriceRepository.findOne(marketPrice.getId());
        // Disconnect from session so that the updates on updatedMarketPrice are not directly saved in db
        em.detach(updatedMarketPrice);
        updatedMarketPrice
            .currentPrice(UPDATED_CURRENT_PRICE)
            .timeStamp(UPDATED_TIME_STAMP);
        MarketPriceDTO marketPriceDTO = marketPriceMapper.toDto(updatedMarketPrice);

        restMarketPriceMockMvc.perform(put("/api/market-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketPriceDTO)))
            .andExpect(status().isOk());

        // Validate the MarketPrice in the database
        List<MarketPrice> marketPriceList = marketPriceRepository.findAll();
        assertThat(marketPriceList).hasSize(databaseSizeBeforeUpdate);
        MarketPrice testMarketPrice = marketPriceList.get(marketPriceList.size() - 1);
        assertThat(testMarketPrice.getCurrentPrice()).isEqualTo(UPDATED_CURRENT_PRICE);
        assertThat(testMarketPrice.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingMarketPrice() throws Exception {
        int databaseSizeBeforeUpdate = marketPriceRepository.findAll().size();

        // Create the MarketPrice
        MarketPriceDTO marketPriceDTO = marketPriceMapper.toDto(marketPrice);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMarketPriceMockMvc.perform(put("/api/market-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketPriceDTO)))
            .andExpect(status().isCreated());

        // Validate the MarketPrice in the database
        List<MarketPrice> marketPriceList = marketPriceRepository.findAll();
        assertThat(marketPriceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMarketPrice() throws Exception {
        // Initialize the database
        marketPriceRepository.saveAndFlush(marketPrice);
        int databaseSizeBeforeDelete = marketPriceRepository.findAll().size();

        // Get the marketPrice
        restMarketPriceMockMvc.perform(delete("/api/market-prices/{id}", marketPrice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MarketPrice> marketPriceList = marketPriceRepository.findAll();
        assertThat(marketPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketPrice.class);
        MarketPrice marketPrice1 = new MarketPrice();
        marketPrice1.setId(1L);
        MarketPrice marketPrice2 = new MarketPrice();
        marketPrice2.setId(marketPrice1.getId());
        assertThat(marketPrice1).isEqualTo(marketPrice2);
        marketPrice2.setId(2L);
        assertThat(marketPrice1).isNotEqualTo(marketPrice2);
        marketPrice1.setId(null);
        assertThat(marketPrice1).isNotEqualTo(marketPrice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketPriceDTO.class);
        MarketPriceDTO marketPriceDTO1 = new MarketPriceDTO();
        marketPriceDTO1.setId(1L);
        MarketPriceDTO marketPriceDTO2 = new MarketPriceDTO();
        assertThat(marketPriceDTO1).isNotEqualTo(marketPriceDTO2);
        marketPriceDTO2.setId(marketPriceDTO1.getId());
        assertThat(marketPriceDTO1).isEqualTo(marketPriceDTO2);
        marketPriceDTO2.setId(2L);
        assertThat(marketPriceDTO1).isNotEqualTo(marketPriceDTO2);
        marketPriceDTO1.setId(null);
        assertThat(marketPriceDTO1).isNotEqualTo(marketPriceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(marketPriceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(marketPriceMapper.fromId(null)).isNull();
    }
}
