package com.mohit.banking.account.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AccountCreatedEvent - Kafka event published when a bank account is created.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent {

    /** Account identifier. */
    private String accountId;

    /** Account holder name. */
    private String accountHolderName;

    /** Account number. */
    private String accountNumber;

    /** Account type. */
    private String accountType;

    /** Initial balance. */
    private BigDecimal initialBalance;

    /** Currency. */
    private String currency;

    /** Event timestamp. */
    private LocalDateTime eventTimestamp;
}
