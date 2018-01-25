package com.antya.pdeservice.service.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CoinAttributes entity.
 */
public class CoinAttributesDTO implements Serializable {

    private Long id;

    private Integer minConfirmation;

    private BigDecimal txnFees;

    private Long coinId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinConfirmation() {
        return minConfirmation;
    }

    public void setMinConfirmation(Integer minConfirmation) {
        this.minConfirmation = minConfirmation;
    }

    public BigDecimal getTxnFees() {
        return txnFees;
    }

    public void setTxnFees(BigDecimal txnFees) {
        this.txnFees = txnFees;
    }

    public Long getCoinId() {
        return coinId;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoinAttributesDTO coinAttributesDTO = (CoinAttributesDTO) o;
        if(coinAttributesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coinAttributesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CoinAttributesDTO{" +
            "id=" + getId() +
            ", minConfirmation=" + getMinConfirmation() +
            ", txnFees=" + getTxnFees() +
            "}";
    }
}
