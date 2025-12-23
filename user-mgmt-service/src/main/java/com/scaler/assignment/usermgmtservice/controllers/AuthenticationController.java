package com.scaler.assignment.usermgmtservice.controllers;

import com.scaler.assignment.usermgmtservice.dtos.LoginRequestDto;
import com.scaler.assignment.usermgmtservice.dtos.LoginResponseDto;
import com.scaler.assignment.usermgmtservice.models.TokenData;
import com.scaler.assignment.usermgmtservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", version = "v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;

    @PostMapping
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) throws IllegalAccessException {
        TokenData tokenData = jwtService.login(loginRequestDto.email(), loginRequestDto.password());
        return new LoginResponseDto(tokenData.jwt(), tokenData.exp(), tokenData.type());
    }

    @PostMapping(value = "/introspect", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> verify(@RequestParam("token")  String token) {
        boolean isVerified = jwtService.verify(token);
        return isVerified
                ? ResponseEntity.accepted().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
