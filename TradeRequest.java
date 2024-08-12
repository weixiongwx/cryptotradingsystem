package com.system.cryptotrading.service;

import lombok.Data;

@Data
public class TradeRequest {
    private Long userId;
    private String cryptoSymbol;
    private String transactionType;
    private double quantity;
}
