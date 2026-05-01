package com.mohit.banking.account.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TransactionEvent - Kafka event published for financial transactions.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {

    /** Account identifier. */
    private String accountId;

    /** Transaction type (DEBIT/CREDIT). */
    private String transactionType;

    /** Transaction amount. */
    private BigDecimal amount;

    /** Transaction description. */
    private String description;

    /** Transaction status. */
    private String status;

    /** Event timestamp. */
    private LocalDateTime eventTimestamp;
}
