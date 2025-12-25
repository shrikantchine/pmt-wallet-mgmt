package com.scaler.assignment.walletservices.models;

import java.util.UUID;

public record UserCreateEventData(UUID id, String email) {
}
