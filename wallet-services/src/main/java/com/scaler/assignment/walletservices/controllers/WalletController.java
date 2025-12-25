package com.scaler.assignment.walletservices.controllers;

import com.scaler.assignment.walletservices.dtos.WalletDetailsDto;
import com.scaler.assignment.walletservices.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{userId}")
    public WalletDetailsDto getWalletInfo(@PathVariable UUID userId) {
        return walletService.getWalletInfo(userId);
    }
}
