package com.antya.pdeservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Coin.
 */
@Entity
@Table(name = "coin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Coin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coin_code")
    private String coinCode;

    @Column(name = "coin_info_url")
    private String coinInfoUrl;

    @Column(name = "coin_image_url")
    private String coinImageUrl;

    @Column(name = "coin_name")
    private String coinName;

    @Column(name = "is_active")
    private Integer isActive;

    @OneToMany(mappedBy = "coin")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CoinAttributes> coinAttributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public Coin coinCode(String coinCode) {
        this.coinCode = coinCode;
        return this;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinInfoUrl() {
        return coinInfoUrl;
    }

    public Coin coinInfoUrl(String coinInfoUrl) {
        this.coinInfoUrl = coinInfoUrl;
        return this;
    }

    public void setCoinInfoUrl(String coinInfoUrl) {
        this.coinInfoUrl = coinInfoUrl;
    }

    public String getCoinImageUrl() {
        return coinImageUrl;
    }

    public Coin coinImageUrl(String coinImageUrl) {
        this.coinImageUrl = coinImageUrl;
        return this;
    }

    public void setCoinImageUrl(String coinImageUrl) {
        this.coinImageUrl = coinImageUrl;
    }

    public String getCoinName() {
        return coinName;
    }

    public Coin coinName(String coinName) {
        this.coinName = coinName;
        return this;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public Coin isActive(Integer isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Set<CoinAttributes> getCoinAttributes() {
        return coinAttributes;
    }

    public Coin coinAttributes(Set<CoinAttributes> coinAttributes) {
        this.coinAttributes = coinAttributes;
        return this;
    }

    public Coin addCoinAttributes(CoinAttributes coinAttributes) {
        this.coinAttributes.add(coinAttributes);
        coinAttributes.setCoin(this);
        return this;
    }

    public Coin removeCoinAttributes(CoinAttributes coinAttributes) {
        this.coinAttributes.remove(coinAttributes);
        coinAttributes.setCoin(null);
        return this;
    }

    public void setCoinAttributes(Set<CoinAttributes> coinAttributes) {
        this.coinAttributes = coinAttributes;
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
        Coin coin = (Coin) o;
        if (coin.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coin.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Coin{" +
            "id=" + getId() +
            ", coinCode='" + getCoinCode() + "'" +
            ", coinInfoUrl='" + getCoinInfoUrl() + "'" +
            ", coinImageUrl='" + getCoinImageUrl() + "'" +
            ", coinName='" + getCoinName() + "'" +
            ", isActive=" + getIsActive() +
            "}";
    }
}
