package com.scaler.assignment.currency.dtos;

public record ExchangeRateResponseDto(
        String fromCurrency,
        String toCurrency,
        Double rate
) {
}
