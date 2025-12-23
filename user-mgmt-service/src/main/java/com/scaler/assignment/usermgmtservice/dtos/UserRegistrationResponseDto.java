package com.scaler.assignment.usermgmtservice.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegistrationResponseDto(UUID id, String email, LocalDateTime createdAt) {
}
