package com.antya.pdeservice.repository;

import com.antya.pdeservice.domain.MarketPrice;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MarketPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {

}
