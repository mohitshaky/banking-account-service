# Banking Account Service

![Java 17](https://img.shields.io/badge/Java-17-blue?style=flat-square&logo=java)
![Spring Boot 3.2](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?style=flat-square&logo=spring)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-Event--Driven-black?style=flat-square&logo=apachekafka)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green?style=flat-square&logo=mongodb)
![Gradle](https://img.shields.io/badge/Gradle-8-blue?style=flat-square&logo=gradle)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

Event-driven banking account service — manages accounts and transactions with Kafka-based audit logging and MongoDB persistence.

---

## Architecture

```
REST Client
    │
    ▼
AccountController / TransactionController
    │
    ▼
AccountService / TransactionService
    │
    ├── MongoDB (accounts) — reads/writes account state
    │
    └── AccountEventProducer / TransactionEventProducer
            │  publishes to Kafka
            ▼
      ┌─────────────────────────────────────┐
      │  Kafka Topics                       │
      │  BA_ACCOUNT_CREATED                 │
      │  BA_TRANSACTION_EVENT               │
      │  BA_AUDIT_LOG                       │
      └─────────────────────────────────────┘
            │
            ▼
      AccountEventListener / TransactionEventListener
            │  consumes & persists
            ▼
      MongoDB (transaction_audit)
```

---

## Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.2.5 |
| Messaging | Apache Kafka | — |
| Database | MongoDB | — |
| Build | Gradle | 8 |
| API Docs | SpringDoc OpenAPI | — |

---

## Prerequisites

| Software | Version | Notes |
|----------|---------|-------|
| Java | 17+ | `java -version` |
| Gradle | 8+ | or use `./gradlew` wrapper |
| Docker | 20.10+ | for Docker Compose option |
| Docker Compose | 2.x+ | `docker compose version` |
| MongoDB | 6+ | only needed for local option |
| Apache Kafka | 3.x+ | only needed for local option |

---

## Quick Start

### Option A: Docker Compose (Recommended)

Starts MongoDB, Zookeeper, Kafka, and the app in one command.

```bash
git clone https://github.com/mohitshaky/banking-account-service.git
cd banking-account-service
docker-compose up --build
```

App starts on **http://localhost:8081** after all containers are healthy.

> Kafka is available at `localhost:29092` from your host machine.  
> MongoDB is available at `localhost:27017`.

---

### Option B: Local (Manual)

**1. Start MongoDB and Kafka manually** (or point to existing instances).

**2. Export environment variables:**

```bash
export MONGO_URI=mongodb://localhost:27017/bankingdb
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export SERVER_PORT=8081
export KAFKA_TOPIC_ACCOUNT_CREATED=BA_ACCOUNT_CREATED
export KAFKA_TOPIC_TRANSACTION=BA_TRANSACTION_EVENT
export KAFKA_TOPIC_AUDIT=BA_AUDIT_LOG
export KAFKA_GROUP_ACCOUNT=BA_ACCOUNT_GRP
export KAFKA_GROUP_TRANSACTION=BA_TXN_GRP
export KAFKA_CONCURRENCY=3
```

**3. Build and run:**

```bash
./gradlew bootRun
```

Or build a JAR and run it:

```bash
./gradlew build
java -jar build/libs/banking-account-service-*.jar
```

---

## Environment Variables

All variables have defaults in `application.yml`. Override as needed.

| Variable | Default | Description |
|----------|---------|-------------|
| `MONGO_URI` | `mongodb://localhost:27017/bankingdb` | MongoDB connection URI |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka bootstrap servers |
| `SERVER_PORT` | `8081` | Application port |
| `KAFKA_TOPIC_ACCOUNT_CREATED` | `BA_ACCOUNT_CREATED` | Topic for account creation events |
| `KAFKA_TOPIC_TRANSACTION` | `BA_TRANSACTION_EVENT` | Topic for transaction events |
| `KAFKA_TOPIC_AUDIT` | `BA_AUDIT_LOG` | Topic for audit log events |
| `KAFKA_GROUP_ACCOUNT` | `BA_ACCOUNT_GRP` | Consumer group for account events |
| `KAFKA_GROUP_TRANSACTION` | `BA_TXN_GRP` | Consumer group for transaction events |
| `KAFKA_CONCURRENCY` | `3` | Kafka listener thread count |

---

## API Endpoints

### Accounts

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/accounts` | Create a new bank account |
| `GET` | `/api/accounts/{accountId}` | Get account by ID |
| `GET` | `/api/accounts/owner/{ownerId}` | Get all accounts for an owner |

#### Create Account

```bash
curl -X POST http://localhost:8081/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "ownerId": "OWNER-001",
    "accountType": "SAVINGS",
    "initialDeposit": 1000.00,
    "currency": "USD"
  }'
```

### Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/accounts/{accountId}/deposit` | Deposit funds |
| `POST` | `/api/accounts/{accountId}/withdraw` | Withdraw funds |
| `POST` | `/api/accounts/{accountId}/transfer` | Transfer to another account |
| `GET` | `/api/transactions/{accountId}` | Get transaction history |

#### Deposit Funds

```bash
curl -X POST http://localhost:8081/api/accounts/{accountId}/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 500.00,
    "description": "Salary credit"
  }'
```

#### Transfer Funds

```bash
curl -X POST http://localhost:8081/api/accounts/{accountId}/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "targetAccountId": "ACC-002",
    "amount": 200.00,
    "description": "Rent payment"
  }'
```

---

## Kafka Topics

| Topic | Direction | Description |
|-------|-----------|-------------|
| `BA_ACCOUNT_CREATED` | Outbound | Published when a new account is created |
| `BA_TRANSACTION_EVENT` | Outbound | Published on deposit / withdrawal / transfer |
| `BA_AUDIT_LOG` | Outbound | Reserved for audit log writes |

---

## MongoDB Collections

| Collection | Description |
|------------|-------------|
| `accounts` | Account data — id, ownerId, balance, status, timestamps |
| `transaction_audit` | Audit log of all transaction events consumed from Kafka |

---

## Swagger / API Docs

| Resource | URL |
|----------|-----|
| Swagger UI | http://localhost:8081/swagger-ui.html |
| OpenAPI JSON | http://localhost:8081/api-docs |

---

## Health & Monitoring

| Endpoint | Description |
|----------|-------------|
| `GET /actuator/health` | Application health status |
| `GET /actuator/info` | Application info |
| `GET /actuator/metrics` | Application metrics |

```bash
curl http://localhost:8081/actuator/health
```

---

## Running Tests

```bash
./gradlew test
```

Test reports are generated at `build/reports/tests/test/index.html`.

---

## Project Structure

```
src/main/java/com/mohit/banking/account/
├── controller/    AccountController.java
│                  TransactionController.java
├── event/         AccountCreatedEvent.java
│                  TransactionEvent.java
├── listener/      AccountEventListener.java
│                  TransactionEventListener.java
├── model/         Account.java
│                  TransactionAudit.java
├── producer/      AccountEventProducer.java
│                  TransactionEventProducer.java
├── repository/    AccountRepository.java
│                  TransactionAuditRepository.java
└── service/       AccountService.java
                   TransactionService.java
```

---

## Author

Built by **Mohit** — Senior Java Backend Developer | [Portfolio](https://mohitshaky.github.io) | [GitHub](https://github.com/mohitshaky)
