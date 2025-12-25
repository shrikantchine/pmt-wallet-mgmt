package com.scaler.assignment.walletservices.dtos;

import java.util.List;
import java.util.UUID;

public record WalletDetailsDto(UUID walletId, List<BalanceDto> balances) {
}
