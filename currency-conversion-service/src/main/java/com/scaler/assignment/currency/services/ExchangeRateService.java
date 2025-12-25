package com.scaler.assignment.currency.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RedisService redisService;

    public Double getExchangeRate(final String fromCurrency, final String toCurrency) {
        var key = fromCurrency + ":" + toCurrency;
        Optional<Double> conversionRate = redisService.get(key, Double.class);
        return conversionRate.orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }
}
