package com.scaler.assignment.usermgmtservice.models;

public record TokenData(
        String jwt,
        Long exp,
        String type
) {
}
