package com.mohit.banking.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AccountResponse - DTO for returning account information.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    /** Account identifier. */
    private String id;

    /** Account holder full name. */
    private String accountHolderName;

    /** Account number. */
    private String accountNumber;

    /** Account type. */
    private String accountType;

    /** Current balance. */
    private BigDecimal balance;

    /** Account status. */
    private String status;

    /** Currency code. */
    private String currency;

    /** Creation timestamp. */
    private LocalDateTime createdAt;

    /** Last updated timestamp. */
    private LocalDateTime updatedAt;
}
