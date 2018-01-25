package com.antya.pdeservice.web.rest;

import com.antya.pdeservice.AntyapdeserviceApp;

import com.antya.pdeservice.domain.Coin;
import com.antya.pdeservice.repository.CoinRepository;
import com.antya.pdeservice.service.CoinService;
import com.antya.pdeservice.service.dto.CoinDTO;
import com.antya.pdeservice.service.mapper.CoinMapper;
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
 * Test class for the CoinResource REST controller.
 *
 * @see CoinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AntyapdeserviceApp.class)
public class CoinResourceIntTest {

    private static final String DEFAULT_COIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COIN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COIN_INFO_URL = "AAAAAAAAAA";
    private static final String UPDATED_COIN_INFO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COIN_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_COIN_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COIN_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_ACTIVE = 1;
    private static final Integer UPDATED_IS_ACTIVE = 2;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private CoinService coinService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoinMockMvc;

    private Coin coin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoinResource coinResource = new CoinResource(coinService);
        this.restCoinMockMvc = MockMvcBuilders.standaloneSetup(coinResource)
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
    public static Coin createEntity(EntityManager em) {
        Coin coin = new Coin()
            .coinCode(DEFAULT_COIN_CODE)
            .coinInfoUrl(DEFAULT_COIN_INFO_URL)
            .coinImageUrl(DEFAULT_COIN_IMAGE_URL)
            .coinName(DEFAULT_COIN_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
        return coin;
    }

    @Before
    public void initTest() {
        coin = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoin() throws Exception {
        int databaseSizeBeforeCreate = coinRepository.findAll().size();

        // Create the Coin
        CoinDTO coinDTO = coinMapper.toDto(coin);
        restCoinMockMvc.perform(post("/api/coins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinDTO)))
            .andExpect(status().isCreated());

        // Validate the Coin in the database
        List<Coin> coinList = coinRepository.findAll();
        assertThat(coinList).hasSize(databaseSizeBeforeCreate + 1);
        Coin testCoin = coinList.get(coinList.size() - 1);
        assertThat(testCoin.getCoinCode()).isEqualTo(DEFAULT_COIN_CODE);
        assertThat(testCoin.getCoinInfoUrl()).isEqualTo(DEFAULT_COIN_INFO_URL);
        assertThat(testCoin.getCoinImageUrl()).isEqualTo(DEFAULT_COIN_IMAGE_URL);
        assertThat(testCoin.getCoinName()).isEqualTo(DEFAULT_COIN_NAME);
        assertThat(testCoin.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createCoinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coinRepository.findAll().size();

        // Create the Coin with an existing ID
        coin.setId(1L);
        CoinDTO coinDTO = coinMapper.toDto(coin);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoinMockMvc.perform(post("/api/coins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coin in the database
        List<Coin> coinList = coinRepository.findAll();
        assertThat(coinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoins() throws Exception {
        // Initialize the database
        coinRepository.saveAndFlush(coin);

        // Get all the coinList
        restCoinMockMvc.perform(get("/api/coins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coin.getId().intValue())))
            .andExpect(jsonPath("$.[*].coinCode").value(hasItem(DEFAULT_COIN_CODE.toString())))
            .andExpect(jsonPath("$.[*].coinInfoUrl").value(hasItem(DEFAULT_COIN_INFO_URL.toString())))
            .andExpect(jsonPath("$.[*].coinImageUrl").value(hasItem(DEFAULT_COIN_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].coinName").value(hasItem(DEFAULT_COIN_NAME.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    public void getCoin() throws Exception {
        // Initialize the database
        coinRepository.saveAndFlush(coin);

        // Get the coin
        restCoinMockMvc.perform(get("/api/coins/{id}", coin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coin.getId().intValue()))
            .andExpect(jsonPath("$.coinCode").value(DEFAULT_COIN_CODE.toString()))
            .andExpect(jsonPath("$.coinInfoUrl").value(DEFAULT_COIN_INFO_URL.toString()))
            .andExpect(jsonPath("$.coinImageUrl").value(DEFAULT_COIN_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.coinName").value(DEFAULT_COIN_NAME.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    public void getNonExistingCoin() throws Exception {
        // Get the coin
        restCoinMockMvc.perform(get("/api/coins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoin() throws Exception {
        // Initialize the database
        coinRepository.saveAndFlush(coin);
        int databaseSizeBeforeUpdate = coinRepository.findAll().size();

        // Update the coin
        Coin updatedCoin = coinRepository.findOne(coin.getId());
        // Disconnect from session so that the updates on updatedCoin are not directly saved in db
        em.detach(updatedCoin);
        updatedCoin
            .coinCode(UPDATED_COIN_CODE)
            .coinInfoUrl(UPDATED_COIN_INFO_URL)
            .coinImageUrl(UPDATED_COIN_IMAGE_URL)
            .coinName(UPDATED_COIN_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        CoinDTO coinDTO = coinMapper.toDto(updatedCoin);

        restCoinMockMvc.perform(put("/api/coins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinDTO)))
            .andExpect(status().isOk());

        // Validate the Coin in the database
        List<Coin> coinList = coinRepository.findAll();
        assertThat(coinList).hasSize(databaseSizeBeforeUpdate);
        Coin testCoin = coinList.get(coinList.size() - 1);
        assertThat(testCoin.getCoinCode()).isEqualTo(UPDATED_COIN_CODE);
        assertThat(testCoin.getCoinInfoUrl()).isEqualTo(UPDATED_COIN_INFO_URL);
        assertThat(testCoin.getCoinImageUrl()).isEqualTo(UPDATED_COIN_IMAGE_URL);
        assertThat(testCoin.getCoinName()).isEqualTo(UPDATED_COIN_NAME);
        assertThat(testCoin.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingCoin() throws Exception {
        int databaseSizeBeforeUpdate = coinRepository.findAll().size();

        // Create the Coin
        CoinDTO coinDTO = coinMapper.toDto(coin);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoinMockMvc.perform(put("/api/coins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coinDTO)))
            .andExpect(status().isCreated());

        // Validate the Coin in the database
        List<Coin> coinList = coinRepository.findAll();
        assertThat(coinList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoin() throws Exception {
        // Initialize the database
        coinRepository.saveAndFlush(coin);
        int databaseSizeBeforeDelete = coinRepository.findAll().size();

        // Get the coin
        restCoinMockMvc.perform(delete("/api/coins/{id}", coin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Coin> coinList = coinRepository.findAll();
        assertThat(coinList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coin.class);
        Coin coin1 = new Coin();
        coin1.setId(1L);
        Coin coin2 = new Coin();
        coin2.setId(coin1.getId());
        assertThat(coin1).isEqualTo(coin2);
        coin2.setId(2L);
        assertThat(coin1).isNotEqualTo(coin2);
        coin1.setId(null);
        assertThat(coin1).isNotEqualTo(coin2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoinDTO.class);
        CoinDTO coinDTO1 = new CoinDTO();
        coinDTO1.setId(1L);
        CoinDTO coinDTO2 = new CoinDTO();
        assertThat(coinDTO1).isNotEqualTo(coinDTO2);
        coinDTO2.setId(coinDTO1.getId());
        assertThat(coinDTO1).isEqualTo(coinDTO2);
        coinDTO2.setId(2L);
        assertThat(coinDTO1).isNotEqualTo(coinDTO2);
        coinDTO1.setId(null);
        assertThat(coinDTO1).isNotEqualTo(coinDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(coinMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(coinMapper.fromId(null)).isNull();
    }
}
