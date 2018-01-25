package com.antya.pdeservice.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the MarketPrice entity.
 */
public class MarketPriceDTO implements Serializable {

    private Long id;

    private BigDecimal currentPrice;

    private Instant timeStamp;

    private Long exchangeId;

    private Long marketId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MarketPriceDTO marketPriceDTO = (MarketPriceDTO) o;
        if(marketPriceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), marketPriceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MarketPriceDTO{" +
            "id=" + getId() +
            ", currentPrice=" + getCurrentPrice() +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
