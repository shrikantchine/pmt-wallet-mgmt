package com.scaler.assignment.currency.config;

import com.scaler.assignment.currency.models.ExchangeRateApiResponse;
import com.scaler.assignment.currency.services.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerConfig {

    private final RestClient restClient;
    private final RedisService redisService;

    @Value("${rates.api}")
    private String ratesApiUrl;

    @Value("#{'${rates.supportedCurrencies}'.split(', ')}")
    private List<String> supportedCurrencies;

    /*@PostConstruct
    public void onStartup() {
        log.info("Application started. Warming up Exchange Rate Cache...");
        refreshHotRates();
    }*/

    @Scheduled(cron = "0 0 * * * *")
    public void refreshHotRates() {
        log.info("Scheduling currency exchange rates retrieval");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            supportedCurrencies.forEach(fromCurrency -> {
                executorService.execute(() -> {
                    log.info("Retrieving currency exchange rates from currency {}", fromCurrency);
                    ExchangeRateApiResponse response = restClient
                            .get()
                            .uri(String.format(ratesApiUrl, fromCurrency))
                            .retrieve()
                            .body(ExchangeRateApiResponse.class);

                    assert response != null;
                    response.conversion_rates().forEach((toCurrency, conversionRate) -> {
                        var key = String.format("%s:%s", fromCurrency, toCurrency);
                        redisService.set(key, conversionRate);
                    });
                });
            });
        }

        stopWatch.stop();
        log.info("Total execution time for Cache Refresh: {}s", stopWatch.getTotalTimeSeconds());
    }
}
