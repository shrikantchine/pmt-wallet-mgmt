package com.scaler.assignment.walletservices.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions", schema = "public")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "idempotency_key")
    private UUID idempotencyKey;

    @ManyToOne
    private Wallet sourceWallet;

    @ManyToOne
    private Wallet targetWallet;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
