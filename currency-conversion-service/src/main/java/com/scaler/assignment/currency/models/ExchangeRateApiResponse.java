package com.scaler.assignment.currency.models;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateApiResponse(
        String base_code,
        Map<String, BigDecimal> conversion_rates,
        long time_last_update_unix
) {}
