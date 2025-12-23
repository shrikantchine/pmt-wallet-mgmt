package com.scaler.assignment.usermgmtservice.dtos;

public record LoginResponseDto(
        String token,
        Long expiresAt,
        String type
) {
}
