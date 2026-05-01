package com.mohit.banking.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction - JPA entity representing a financial transaction.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    /** Primary key - UUID generated. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Account identifier for this transaction. */
    @Column(nullable = false)
    private String accountId;

    /** Transaction type (DEBIT, CREDIT). */
    @Column(nullable = false)
    private String transactionType;

    /** Transaction amount. */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    /** Optional transaction description. */
    @Column
    private String description;

    /** Transaction status (COMPLETED, FAILED, PENDING). */
    @Column(nullable = false)
    private String status;

    /** Timestamp when transaction occurred. */
    @Column(nullable = false)
    private LocalDateTime transactionDate;
}
