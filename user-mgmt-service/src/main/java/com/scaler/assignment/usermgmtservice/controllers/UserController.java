package com.scaler.assignment.usermgmtservice.controllers;

import com.scaler.assignment.usermgmtservice.dtos.UserRegistrationRequestDto;
import com.scaler.assignment.usermgmtservice.dtos.UserRegistrationResponseDto;
import com.scaler.assignment.usermgmtservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", version = "v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDto create(@RequestBody UserRegistrationRequestDto dto) {
        return userService.create(dto);
    }

}
