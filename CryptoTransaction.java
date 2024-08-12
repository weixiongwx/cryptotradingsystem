package com.system.cryptotrading.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "crypto_transactions")
public class CryptoTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String cryptoSymbol;
    private String transactionType;
    private double price;
    private double quantity;
    private double totalAmount;
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

}
