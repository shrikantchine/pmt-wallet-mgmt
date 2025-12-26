#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER user_manager WITH PASSWORD 'password';
    CREATE DATABASE user_db;
    GRANT ALL PRIVILEGES ON DATABASE user_db TO user_manager;

    CREATE USER wallet_manager WITH PASSWORD 'password';
    CREATE DATABASE wallet_db;
    GRANT ALL PRIVILEGES ON DATABASE wallet_db TO wallet_manager;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "user_db" <<-EOSQL
    GRANT ALL ON SCHEMA public TO user_manager;

    drop table if exists users;
    drop table if exists outbox;

    CREATE TABLE users (
        id UUID PRIMARY KEY,
        email VARCHAR(255) UNIQUE NOT NULL,
        password_hash TEXT NOT NULL,
        created_at TIMESTAMPTZ DEFAULT NOW()
    );

    CREATE TABLE outbox (
        id UUID PRIMARY KEY,
        aggregate_type VARCHAR(50) NOT NULL,
        aggregate_id UUID NOT NULL,
        type VARCHAR(50) NOT NULL,
        payload JSONB NOT NULL,
        processed BOOLEAN NOT NULL default false,
        created_at TIMESTAMPTZ DEFAULT NOW()
    );
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "wallet_db" <<-EOSQL
    GRANT ALL ON SCHEMA public TO wallet_manager;

    -- 1. Main Wallet table (Links a user to their financial account)
    CREATE TABLE wallets (
        id UUID PRIMARY KEY,
        user_id UUID NOT NULL UNIQUE, -- The ID from your User Service
        status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, FROZEN, CLOSED
        created_at TIMESTAMPTZ DEFAULT NOW(),
        updated_at TIMESTAMPTZ DEFAULT NOW()
    );

    -- 2. Balances table (Supports multiple currencies per wallet)
    CREATE TABLE balances (
        id UUID PRIMARY KEY,
        wallet_id UUID NOT NULL REFERENCES wallets(id),
        currency_code VARCHAR(3) NOT NULL, -- e.g., 'USD', 'EUR', 'GBP'
        amount DECIMAL(19, 4) NOT NULL DEFAULT 0.0000,
        version BIGINT NOT NULL DEFAULT 0, -- Crucial for Optimistic Locking
        UNIQUE(wallet_id, currency_code)
    );

    -- 3. Transactions table (The immutable ledger)
    CREATE TABLE transactions (
        id UUID PRIMARY KEY,
        idempotency_key UUID UNIQUE, -- Prevents duplicate processing
        source_wallet_id UUID REFERENCES wallets(id),
        target_wallet_id UUID REFERENCES wallets(id),
        amount DECIMAL(19, 4) NOT NULL,
        currency_code VARCHAR(3) NOT NULL,
        transaction_type VARCHAR(20) NOT NULL, -- TRANSFER, DEPOSIT, WITHDRAWAL, CONVERSION
        status VARCHAR(20) NOT NULL,           -- PENDING, COMPLETED, FAILED
        description TEXT,
        created_at TIMESTAMPTZ DEFAULT NOW()
    );

    -- 4. Create indexes for performance
    CREATE INDEX idx_wallets_user_id ON wallets(user_id);
    CREATE INDEX idx_balances_wallet_id ON balances(wallet_id);
    CREATE INDEX idx_transactions_source_wallet ON transactions(source_wallet_id);
    CREATE INDEX idx_transactions_target_wallet ON transactions(target_wallet_id);
EOSQL
