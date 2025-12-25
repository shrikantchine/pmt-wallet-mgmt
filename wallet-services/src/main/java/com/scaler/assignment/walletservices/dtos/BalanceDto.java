package com.scaler.assignment.walletservices.dtos;

import java.math.BigDecimal;

public record BalanceDto(
        String currencyCode,
        BigDecimal amount
) {
}
