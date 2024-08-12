package com.system.cryptotrading.controller;

import com.system.cryptotrading.entity.CryptoWallet;
import com.system.cryptotrading.repository.CryptoWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private CryptoWalletRepository walletRepository;

    @GetMapping("/balance")
    public ResponseEntity<List<CryptoWallet>> getWalletBalance(@RequestParam Long userId) {
        List<CryptoWallet> walletBalances = walletRepository.findByUserId(userId);
        if (walletBalances.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(walletBalances);
    }
}
