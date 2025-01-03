CREATE TABLE IF NOT EXISTS customer_accounts
(
    id             BIGSERIAL PRIMARY KEY,
    customer_id    VARCHAR(50) NOT NULL UNIQUE,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    description    TEXT,
    version        BIGINT      NOT NULL DEFAULT 0,
    created_at     TIMESTAMP   NOT NULL,
    updated_at     TIMESTAMP   NOT NULL
);

CREATE INDEX idx_customer_id ON customer_accounts(customer_id);
CREATE INDEX idx_account_number ON customer_accounts(account_number);
