package com.scaler.assignment.walletservices.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "balances", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Wallet wallet;

    @Column(name = "currency_code") private String currencyCode;

    @Column(name = "amount") private BigDecimal amount;

    @Column(name = "version") private Long version;
}
