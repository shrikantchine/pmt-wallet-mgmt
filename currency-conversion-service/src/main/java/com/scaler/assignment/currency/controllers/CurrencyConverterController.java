package com.scaler.assignment.currency.controllers;

import com.scaler.assignment.currency.dtos.ExchangeRateResponseDto;
import com.scaler.assignment.currency.services.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CurrencyConverterController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/rate/{fromCurrency}/{toCurrency}")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRateResponseDto rate(@PathVariable final String fromCurrency,
                                        @PathVariable final String toCurrency) {
        Double rate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        return new ExchangeRateResponseDto(fromCurrency, toCurrency, rate);
    }
}
