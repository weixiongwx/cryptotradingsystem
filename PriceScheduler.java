package com.system.cryptotrading.scheduler;

import com.system.cryptotrading.entity.AggregatedPrice;
import com.system.cryptotrading.repository.AggregatedPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class PriceScheduler {

    @Autowired
    private AggregatedPriceRepository priceRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static final String HUOBI_URL = "https://api.huobi.pro/market/tickers";

    @Scheduled(fixedRate = 10000)
    public void updatePrices() {
        try {

            Map<String, Double> binancePrices = fetchBinancePrices();

            Map<String, Double> huobiPrices = fetchHuobiPrices();

            for (Map.Entry<String, Double> entry : binancePrices.entrySet()) {
                String cryptoSymbol = entry.getKey();
                double binancePrice = entry.getValue();
                double huobiPrice = huobiPrices.getOrDefault(cryptoSymbol, binancePrice);

                AggregatedPrice aggregatedPrice = new AggregatedPrice();
                aggregatedPrice.setCryptoSymbol(cryptoSymbol);
                aggregatedPrice.setBestBid(Math.max(binancePrice, huobiPrice));
                aggregatedPrice.setBestAsk(Math.min(binancePrice, huobiPrice));

                priceRepository.save(aggregatedPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Double> fetchBinancePrices() {
        Map<String, Double> prices = new HashMap<>();
        String response = restTemplate.getForObject(BINANCE_URL, String.class);
        return prices;
    }

    private Map<String, Double> fetchHuobiPrices() {
        Map<String, Double> prices = new HashMap<>();
        String response = restTemplate.getForObject(HUOBI_URL, String.class);
        return prices;
    }
}
