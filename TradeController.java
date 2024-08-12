package com.system.cryptotrading.controller;

import com.system.cryptotrading.service.TradeRequest;
import com.system.cryptotrading.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/execute")
    public ResponseEntity<String> executeTrade(@RequestBody TradeRequest request) {
        try {
            tradeService.executeTrade(request);
            return ResponseEntity.ok("Trade executed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while executing the trade");
        }
    }
}
