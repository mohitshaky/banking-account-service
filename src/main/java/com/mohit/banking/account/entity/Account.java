package com.mohit.banking.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account - JPA entity representing a bank account.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    /** Primary key - UUID generated. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Account holder full name. */
    @Column(nullable = false)
    private String accountHolderName;

    /** Unique account number. */
    @Column(unique = true, nullable = false)
    private String accountNumber;

    /** Account type (SAVINGS, CURRENT, etc). */
    @Column(nullable = false)
    private String accountType;

    /** Current account balance. */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    /** Account status (ACTIVE, INACTIVE, SUSPENDED). */
    @Column(nullable = false)
    private String status;

    /** Currency code (USD, EUR, etc). */
    @Column(nullable = false)
    private String currency;

    /** Timestamp when account was created. */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** Timestamp when account was last updated. */
    @Column
    private LocalDateTime updatedAt;
}
