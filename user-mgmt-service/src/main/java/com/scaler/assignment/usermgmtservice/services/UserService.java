package com.scaler.assignment.usermgmtservice.services;

import com.scaler.assignment.usermgmtservice.dtos.UserRegistrationRequestDto;
import com.scaler.assignment.usermgmtservice.dtos.UserRegistrationResponseDto;
import com.scaler.assignment.usermgmtservice.models.EventOutbox;
import com.scaler.assignment.usermgmtservice.models.User;
import com.scaler.assignment.usermgmtservice.repositories.EventOutboxRepository;
import com.scaler.assignment.usermgmtservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventOutboxRepository eventOutboxRepository;
    private final ObjectMapper objectMapper;

    public UserRegistrationResponseDto create(UserRegistrationRequestDto dto) {
        User user = new User(dto.email(), passwordEncoder.encode(dto.password()));
        User savedUser = this.userRepository.save(user);

        String payload = objectMapper.writeValueAsString(Map.of(
                "id", user.getId(),
                "email", user.getEmail()
        ));
        EventOutbox eventOutbox = EventOutbox.builder()
                .type("USER_CREATED")
                .aggregateType("USER")
                .aggregateId(savedUser.getId())
                .payload(payload)
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build();
        this.eventOutboxRepository.save(eventOutbox);

        return new UserRegistrationResponseDto(
                savedUser.getId(), savedUser.getEmail(), savedUser.getCreatedAt()
        );
    }
}
