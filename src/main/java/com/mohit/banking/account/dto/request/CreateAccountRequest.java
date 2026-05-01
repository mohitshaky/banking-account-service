package com.mohit.banking.account.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * CreateAccountRequest - DTO for creating a new bank account.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    /** Account holder full name. */
    @NotBlank(message = "Account holder name is required")
    private String accountHolderName;

    /** Account type (SAVINGS, CURRENT). */
    @NotBlank(message = "Account type is required")
    private String accountType;

    /** Initial deposit amount. */
    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", message = "Initial balance must be non-negative")
    private BigDecimal initialBalance;

    /** Currency code (USD, EUR). */
    @NotBlank(message = "Currency is required")
    private String currency;
}
