# Transaction Routine Service

This is a Spring Boot based REST API that allows:

- Account creation and retrieval
- Transaction creation associated with an account
- Idempotent transaction processing
- Support for multiple operation types (Purchase, Withdrawal, Installment, Credit Voucher)

Transactions are stored with signed amounts based on the operation type.

---

## Containers Spun Up

Running the application starts the following Docker containers:

| Container        | Purpose                          |
|------------------|----------------------------------|
| PostgreSQL       | Primary relational database      |
| Spring Boot App  | Transaction Routine Service      |
| pgAdmin (optional) | DB UI for inspection           |

The service is exposed on:

http://localhost:8085

---

## Database Structure

### account

| Column       | Type   |
|-------------|--------|
| account_id  | BIGINT (PK) |
| document_id | VARCHAR     |

---

### operation_type (Lookup Table)

| Column             | Type   |
|--------------------|--------|
| operation_type_id  | INT (PK) |
| operation_type     | VARCHAR  |

Preloaded values:

| ID | Type           |
|----|----------------|
| 1  | PURCHASE       |
| 2  | WITHDRAWAL     |
| 3  | INSTALLMENT    |
| 4  | CREDIT_VOUCHER |

---

### transaction

| Column           | Type    |
|------------------|---------|
| transaction_id   | BIGINT (PK) |
| idempotency_key  | UUID (UNIQUE) |
| account_id       | BIGINT (FK) |
| operation_id     | INT (FK) |
| amount           | NUMERIC |
| created_at       | TIMESTAMP |

---

## How To Run

Ensure Docker is installed.

Run the following:

chmod +x start.sh
./start.sh

## Sample API Requests

### 1. Create Account

curl -X POST http://localhost:8085/api/v1/accounts \
-H "Content-Type: application/json" \
-d '{
  "document_number": "12345678900"
}'

### 2. get account by account id
curl -X GET http://localhost:8085/api/v1/accounts/1

### 3. create transaction with idempotency 

curl -X POST http://localhost:8085/api/v1/transactions \
-H "Content-Type: application/json" \
-d '{
"account_id": 1,
"operation_type": "PURCHASE",
"amount": 100.00,
"idempotency_key": "11111111-1111-1111-1111-111111111111"
}'