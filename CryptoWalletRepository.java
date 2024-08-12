package com.system.cryptotrading.repository;

import com.system.cryptotrading.entity.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {
    List<CryptoWallet> findByUserId(Long userId);
}
