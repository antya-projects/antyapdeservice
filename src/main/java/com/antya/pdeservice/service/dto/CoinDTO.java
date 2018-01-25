package com.antya.pdeservice.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Coin entity.
 */
public class CoinDTO implements Serializable {

    private Long id;

    private String coinCode;

    private String coinInfoUrl;

    private String coinImageUrl;

    private String coinName;

    private Integer isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinInfoUrl() {
        return coinInfoUrl;
    }

    public void setCoinInfoUrl(String coinInfoUrl) {
        this.coinInfoUrl = coinInfoUrl;
    }

    public String getCoinImageUrl() {
        return coinImageUrl;
    }

    public void setCoinImageUrl(String coinImageUrl) {
        this.coinImageUrl = coinImageUrl;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoinDTO coinDTO = (CoinDTO) o;
        if(coinDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coinDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CoinDTO{" +
            "id=" + getId() +
            ", coinCode='" + getCoinCode() + "'" +
            ", coinInfoUrl='" + getCoinInfoUrl() + "'" +
            ", coinImageUrl='" + getCoinImageUrl() + "'" +
            ", coinName='" + getCoinName() + "'" +
            ", isActive=" + getIsActive() +
            "}";
    }
}
