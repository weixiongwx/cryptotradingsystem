package com.system.cryptotrading.repository;

import com.system.cryptotrading.entity.AggregatedPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregatedPriceRepository extends JpaRepository<AggregatedPrice, Long> {
    AggregatedPrice findTopByOrderByIdDesc();
}
