package com.system.cryptotrading.service;

import com.system.cryptotrading.entity.CryptoTransaction;
import com.system.cryptotrading.entity.CryptoWallet;
import com.system.cryptotrading.entity.AggregatedPrice;
import com.system.cryptotrading.repository.CryptoTransactionRepository;
import com.system.cryptotrading.repository.CryptoWalletRepository;
import com.system.cryptotrading.repository.AggregatedPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private CryptoTransactionRepository transactionRepository;

    @Autowired
    private CryptoWalletRepository walletRepository;

    @Autowired
    private AggregatedPriceRepository priceRepository;

    public void executeTrade(TradeRequest request) {
        Optional<CryptoWallet> walletOpt = walletRepository.findById(request.getUserId());
        if (!walletOpt.isPresent()) {
            throw new IllegalArgumentException("User wallet not found");
        }
        CryptoWallet wallet = walletOpt.get();

        AggregatedPrice latestPrice = priceRepository.findTopByOrderByIdDesc();
        if (latestPrice == null) {
            throw new IllegalStateException("No price data available");
        }

        double transactionAmount;
        if (request.getTransactionType().equalsIgnoreCase("BUY")) {
            transactionAmount = latestPrice.getBestAsk() * request.getQuantity();
            if (wallet.getUsdtBalance() < transactionAmount) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() - transactionAmount);
            wallet.setCryptoBalance(wallet.getCryptoBalance() + request.getQuantity());
        } else if (request.getTransactionType().equalsIgnoreCase("SELL")) {
            if (wallet.getCryptoBalance() < request.getQuantity()) {
                throw new IllegalArgumentException("Insufficient crypto balance");
            }
            transactionAmount = latestPrice.getBestBid() * request.getQuantity();
            wallet.setCryptoBalance(wallet.getCryptoBalance() - request.getQuantity());
            wallet.setUsdtBalance(wallet.getUsdtBalance() + transactionAmount);
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        walletRepository.save(wallet);

        CryptoTransaction transaction = new CryptoTransaction();
        transaction.setUserId(request.getUserId());
        transaction.setCryptoSymbol(request.getCryptoSymbol());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setPrice(latestPrice.getBestBid());
        transaction.setQuantity(request.getQuantity());
        transaction.setTotalAmount(transactionAmount);
        transactionRepository.save(transaction);
    }
}
