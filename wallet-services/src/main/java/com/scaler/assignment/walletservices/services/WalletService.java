package com.scaler.assignment.walletservices.services;

import com.scaler.assignment.walletservices.dtos.BalanceDto;
import com.scaler.assignment.walletservices.dtos.WalletDetailsDto;
import com.scaler.assignment.walletservices.models.Balance;
import com.scaler.assignment.walletservices.models.Wallet;
import com.scaler.assignment.walletservices.models.WalletStatus;
import com.scaler.assignment.walletservices.repositories.BalanceRepository;
import com.scaler.assignment.walletservices.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final BalanceRepository balanceRepository;

    @Transactional
    public void createWallet(UUID userId) {
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .status(WalletStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
        Balance balance = Balance.builder()
                .amount(new BigDecimal("0.0"))
                .currencyCode("INR")
                .version(0L)
                .wallet(wallet)
                .build();

        walletRepository.save(wallet);
        balanceRepository.save(balance);
    }

    public WalletDetailsDto getWalletInfo(UUID userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("wallet not found"));

        return new WalletDetailsDto(
                wallet.getId(),
                wallet.getBalances().stream().map(balance -> new BalanceDto(balance.getCurrencyCode(), balance.getAmount())).toList()
        );
    }
}
