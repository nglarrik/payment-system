CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    owner_user VARCHAR(255) NOT NULL,
    balance NUMERIC(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);