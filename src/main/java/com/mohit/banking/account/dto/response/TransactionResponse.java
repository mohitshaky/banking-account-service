package com.mohit.banking.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TransactionResponse - DTO for returning transaction information.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    /** Transaction identifier. */
    private String id;

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

    /** Transaction date. */
    private LocalDateTime transactionDate;
}
