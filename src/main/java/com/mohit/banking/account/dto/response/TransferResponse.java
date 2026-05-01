package com.mohit.banking.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TransferResponse - DTO for returning transfer result information.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    /** Source account identifier. */
    private String sourceAccountId;

    /** Target account identifier. */
    private String targetAccountId;

    /** Transfer amount. */
    private BigDecimal amount;

    /** Transfer status. */
    private String status;

    /** Transaction date. */
    private LocalDateTime transactionDate;
}
