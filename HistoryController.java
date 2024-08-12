package com.system.cryptotrading.controller;

import com.system.cryptotrading.entity.CryptoTransaction;
import com.system.cryptotrading.repository.CryptoTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private CryptoTransactionRepository transactionRepository;

    @GetMapping("/transactions")
    public ResponseEntity<List<CryptoTransaction>> getTradingHistory(@RequestParam Long userId) {
        List<CryptoTransaction> transactions = transactionRepository.findByUserId(userId);
        if (transactions.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(transactions);
    }
}
