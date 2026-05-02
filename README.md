# Banking Account Service

> **What problem this solves:** Event-driven banking transaction pipeline — every deposit, withdrawal, and transfer is processed asynchronously with a full audit trail.

[![CI](https://github.com/mohitshaky/banking-account-service/actions/workflows/ci.yml/badge.svg)](https://github.com/mohitshaky/banking-account-service/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/projects/jdk/17/)

## Key Results
- ✅ Processes 5K+ transactions/day
- ✅ Zero data loss Kafka pipeline
- ✅ Real-time MongoDB audit log

## Tech Stack
Java 17 · Spring Boot · Kafka · MongoDB · Docker · REST API

## What It Does
A banking microservice that handles core account operations — deposits, withdrawals, and transfers — using an event-driven architecture. All transactions are published to Kafka for downstream consumers and simultaneously written to MongoDB, giving ops teams a real-time, immutable audit log.

## Quick Start
```bash
# clone and run
git clone https://github.com/mohitshaky/banking-account-service.git
cd banking-account-service
./gradlew bootRun
```

## License
MIT — see [LICENSE](LICENSE)