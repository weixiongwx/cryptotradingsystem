package com.system.cryptotrading.entity;

import javax.persistence.*;

@Entity
@Table(name = "crypto_wallets")
public class CryptoWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String cryptoSymbol;
    private double cryptoBalance;
    private double usdtBalance;

}
