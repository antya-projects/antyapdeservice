package com.antya.pdeservice.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Exchange entity.
 */
public class ExchangeDTO implements Serializable {

    private Long id;

    private String name;

    private String country;

    private Integer isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

        ExchangeDTO exchangeDTO = (ExchangeDTO) o;
        if(exchangeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exchangeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExchangeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            ", isActive=" + getIsActive() +
            "}";
    }
}
