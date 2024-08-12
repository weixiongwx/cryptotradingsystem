package com.system.cryptotrading.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aggregated_prices")
public class AggregatedPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cryptoSymbol;
    private double bestBid;
    private double bestAsk;
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

}
