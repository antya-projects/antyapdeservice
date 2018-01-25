package com.antya.pdeservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A MarketPrice.
 */
@Entity
@Table(name = "market_price")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MarketPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_price", precision=10, scale=2)
    private BigDecimal currentPrice;

    @Column(name = "time_stamp")
    private Instant timeStamp;

    @ManyToOne
    private Exchange exchange;

    @ManyToOne
    private Market market;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public MarketPrice currentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        return this;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public MarketPrice timeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public MarketPrice exchange(Exchange exchange) {
        this.exchange = exchange;
        return this;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Market getMarket() {
        return market;
    }

    public MarketPrice market(Market market) {
        this.market = market;
        return this;
    }

    public void setMarket(Market market) {
        this.market = market;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarketPrice marketPrice = (MarketPrice) o;
        if (marketPrice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), marketPrice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MarketPrice{" +
            "id=" + getId() +
            ", currentPrice=" + getCurrentPrice() +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
