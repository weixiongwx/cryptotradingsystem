package com.system.cryptotrading.service;

import com.system.cryptotrading.entity.AggregatedPrice;
import com.system.cryptotrading.repository.AggregatedPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PriceAggregationService {

    @Autowired
    private AggregatedPriceRepository priceRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 10000)
    public void fetchAndStorePrices() {
        AggregatedPrice binancePrice = fetchFromBinance();
        AggregatedPrice huobiPrice = fetchFromHuobi();

        AggregatedPrice bestPrice = aggregatePrices(binancePrice, huobiPrice);

        priceRepository.save(bestPrice);
    }

    private AggregatedPrice fetchFromBinance() {
        String url = "https://api.binance.com/api/v3/ticker/bookTicker?symbol=BTCUSDT";
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);

        double bidPrice = Double.parseDouble((String) response.get("bidPrice"));
        double askPrice = Double.parseDouble((String) response.get("askPrice"));

        AggregatedPrice binancePrice = new AggregatedPrice();
        binancePrice.setCryptoSymbol("BTCUSDT");
        binancePrice.setBestBid(bidPrice);
        binancePrice.setBestAsk(askPrice);

        return binancePrice;
    }

    private AggregatedPrice fetchFromHuobi() {
        String url = "https://api.huobi.pro/market/tickers";
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        Map<String, Object> btcusdtData = data.stream()
            .filter(ticker -> "btcusdt".equals(ticker.get("symbol")))
            .findFirst()
            .orElseThrow();

        double bidPrice = (Double) btcusdtData.get("bid");
        double askPrice = (Double) btcusdtData.get("ask");

        AggregatedPrice huobiPrice = new AggregatedPrice();
        huobiPrice.setCryptoSymbol("BTCUSDT");
        huobiPrice.setBestBid(bidPrice);
        huobiPrice.setBestAsk(askPrice);

        return huobiPrice;
    }

    private AggregatedPrice aggregatePrices(AggregatedPrice binancePrice, AggregatedPrice huobiPrice) {
        double bestBid = Math.max(binancePrice.getBestBid(), huobiPrice.getBestBid());
        double bestAsk = Math.min(binancePrice.getBestAsk(), huobiPrice.getBestAsk());

        AggregatedPrice bestPrice = new AggregatedPrice();
        bestPrice.setCryptoSymbol("BTCUSDT");
        bestPrice.setBestBid(bestBid);
        bestPrice.setBestAsk(bestAsk);
        return bestPrice;
    }
}
