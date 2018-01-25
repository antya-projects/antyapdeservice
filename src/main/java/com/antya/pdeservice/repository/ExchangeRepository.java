package com.antya.pdeservice.repository;

import com.antya.pdeservice.domain.Exchange;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Exchange entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

}
