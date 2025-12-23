package com.scaler.assignment.usermgmtservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
@Data
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column
    private String email;

    @Column
    private String passwordHash;

    @Column
    private LocalDateTime createdAt;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    public User() {
    }
}
