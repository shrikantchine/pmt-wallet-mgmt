package com.scaler.assignment.walletservices.services;

import com.scaler.assignment.walletservices.models.UserCreateEventData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserCreateEventListenerService {

    private final WalletService walletService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user_events", groupId = "wallet-group")
    public void handleUserCreateEvents(String data) {
        UserCreateEventData eventData = objectMapper.readValue(data, UserCreateEventData.class);
        walletService.createWallet(eventData.id());
    }

}
