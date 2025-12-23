package com.scaler.assignment.usermgmtservice.services;

import com.scaler.assignment.usermgmtservice.dtos.UserRegistrationRequestDto;
import com.scaler.assignment.usermgmtservice.dtos.UserRegistrationResponseDto;
import com.scaler.assignment.usermgmtservice.models.User;
import com.scaler.assignment.usermgmtservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationResponseDto create(UserRegistrationRequestDto dto) {
        User user = new User(dto.email(), passwordEncoder.encode(dto.password()));
        User savedUser = this.userRepository.save(user);
        return new UserRegistrationResponseDto(
                savedUser.getId(), savedUser.getEmail(), savedUser.getCreatedAt()
        );
    }
}
