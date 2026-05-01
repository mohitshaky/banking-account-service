package com.mohit.banking.account.controller;

import com.mohit.banking.account.constants.Constants;
import com.mohit.banking.account.dto.request.CreateAccountRequest;
import com.mohit.banking.account.dto.request.DepositRequest;
import com.mohit.banking.account.dto.request.TransferRequest;
import com.mohit.banking.account.dto.request.WithdrawRequest;
import com.mohit.banking.account.dto.response.AccountResponse;
import com.mohit.banking.account.dto.response.TransferResponse;
import com.mohit.banking.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AccountController - REST controller for bank account operations.
 * @author mohit
 */
@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * Creates a new bank account.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @param request the CreateAccountRequest
     * @return ResponseEntity with AccountResponse
     */
    @Operation(summary = "Create a new bank account", description = "Creates a new bank account and publishes a Kafka event")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Account already exists", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TRANSACTION_ID,
                required = true, description = "Unique transaction identifier")
            @RequestHeader(Constants.HEADER_TRANSACTION_ID) String transactionId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_CORRELATION_ID,
                required = true, description = "Request correlation identifier")
            @RequestHeader(Constants.HEADER_CORRELATION_ID) String correlationId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_SOURCE_CHANNEL,
                required = true, description = "Originating channel")
            @RequestHeader(Constants.HEADER_SOURCE_CHANNEL) String sourceChannel,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TENANT_ID,
                required = true, description = "Tenant identifier")
            @RequestHeader(Constants.HEADER_TENANT_ID) String tenantId,
            @Valid @RequestBody CreateAccountRequest request) {
        LOG.info("AccountController:: createAccount method started");
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves an account by ID.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @param accountId the account ID
     * @return ResponseEntity with AccountResponse
     */
    @Operation(summary = "Get account by ID", description = "Retrieves account details by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid account ID", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TRANSACTION_ID,
                required = true, description = "Unique transaction identifier")
            @RequestHeader(Constants.HEADER_TRANSACTION_ID) String transactionId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_CORRELATION_ID,
                required = true, description = "Request correlation identifier")
            @RequestHeader(Constants.HEADER_CORRELATION_ID) String correlationId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_SOURCE_CHANNEL,
                required = true, description = "Originating channel")
            @RequestHeader(Constants.HEADER_SOURCE_CHANNEL) String sourceChannel,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TENANT_ID,
                required = true, description = "Tenant identifier")
            @RequestHeader(Constants.HEADER_TENANT_ID) String tenantId,
            @PathVariable String accountId) {
        LOG.info("AccountController:: getAccountById method started");
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    /**
     * Retrieves all accounts.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @return ResponseEntity with list of AccountResponse
     */
    @Operation(summary = "Get all accounts", description = "Retrieves all bank accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "No accounts found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TRANSACTION_ID,
                required = true, description = "Unique transaction identifier")
            @RequestHeader(Constants.HEADER_TRANSACTION_ID) String transactionId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_CORRELATION_ID,
                required = true, description = "Request correlation identifier")
            @RequestHeader(Constants.HEADER_CORRELATION_ID) String correlationId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_SOURCE_CHANNEL,
                required = true, description = "Originating channel")
            @RequestHeader(Constants.HEADER_SOURCE_CHANNEL) String sourceChannel,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TENANT_ID,
                required = true, description = "Tenant identifier")
            @RequestHeader(Constants.HEADER_TENANT_ID) String tenantId) {
        LOG.info("AccountController:: getAllAccounts method started");
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    /**
     * Deposits funds into an account.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @param accountId the account ID
     * @param request the DepositRequest
     * @return ResponseEntity with AccountResponse
     */
    @Operation(summary = "Deposit funds", description = "Deposits funds into an existing bank account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deposit successful",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid deposit request", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TRANSACTION_ID,
                required = true, description = "Unique transaction identifier")
            @RequestHeader(Constants.HEADER_TRANSACTION_ID) String transactionId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_CORRELATION_ID,
                required = true, description = "Request correlation identifier")
            @RequestHeader(Constants.HEADER_CORRELATION_ID) String correlationId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_SOURCE_CHANNEL,
                required = true, description = "Originating channel")
            @RequestHeader(Constants.HEADER_SOURCE_CHANNEL) String sourceChannel,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TENANT_ID,
                required = true, description = "Tenant identifier")
            @RequestHeader(Constants.HEADER_TENANT_ID) String tenantId,
            @PathVariable String accountId,
            @Valid @RequestBody DepositRequest request) {
        LOG.info("AccountController:: deposit method started");
        return ResponseEntity.ok(accountService.deposit(accountId, request));
    }

    /**
     * Withdraws funds from an account.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @param accountId the account ID
     * @param request the WithdrawRequest
     * @return ResponseEntity with AccountResponse
     */
    @Operation(summary = "Withdraw funds", description = "Withdraws funds from an existing bank account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Withdrawal successful",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid withdrawal request or insufficient funds", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TRANSACTION_ID,
                required = true, description = "Unique transaction identifier")
            @RequestHeader(Constants.HEADER_TRANSACTION_ID) String transactionId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_CORRELATION_ID,
                required = true, description = "Request correlation identifier")
            @RequestHeader(Constants.HEADER_CORRELATION_ID) String correlationId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_SOURCE_CHANNEL,
                required = true, description = "Originating channel")
            @RequestHeader(Constants.HEADER_SOURCE_CHANNEL) String sourceChannel,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TENANT_ID,
                required = true, description = "Tenant identifier")
            @RequestHeader(Constants.HEADER_TENANT_ID) String tenantId,
            @PathVariable String accountId,
            @Valid @RequestBody WithdrawRequest request) {
        LOG.info("AccountController:: withdraw method started");
        return ResponseEntity.ok(accountService.withdraw(accountId, request));
    }

    /**
     * Transfers funds between accounts.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @param accountId the source account ID
     * @param request the TransferRequest
     * @return ResponseEntity with TransferResponse
     */
    @Operation(summary = "Transfer funds", description = "Transfers funds between two bank accounts, publishing two TransactionEvents and two AuditEvents")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer successful",
            content = @Content(schema = @Schema(implementation = TransferResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request or insufficient funds", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TRANSACTION_ID,
                required = true, description = "Unique transaction identifier")
            @RequestHeader(Constants.HEADER_TRANSACTION_ID) String transactionId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_CORRELATION_ID,
                required = true, description = "Request correlation identifier")
            @RequestHeader(Constants.HEADER_CORRELATION_ID) String correlationId,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_SOURCE_CHANNEL,
                required = true, description = "Originating channel")
            @RequestHeader(Constants.HEADER_SOURCE_CHANNEL) String sourceChannel,
            @Parameter(in = ParameterIn.HEADER, name = Constants.HEADER_TENANT_ID,
                required = true, description = "Tenant identifier")
            @RequestHeader(Constants.HEADER_TENANT_ID) String tenantId,
            @PathVariable String accountId,
            @Valid @RequestBody TransferRequest request) {
        LOG.info("AccountController:: transfer method started");
        return ResponseEntity.ok(accountService.transfer(accountId, request));
    }
}
