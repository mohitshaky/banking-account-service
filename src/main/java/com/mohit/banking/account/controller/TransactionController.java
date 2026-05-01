package com.mohit.banking.account.controller;

import com.mohit.banking.account.constants.Constants;
import com.mohit.banking.account.dto.response.AuditLogResponse;
import com.mohit.banking.account.dto.response.TransactionResponse;
import com.mohit.banking.account.service.AuditService;
import com.mohit.banking.account.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TransactionController - REST controller for transaction and audit log queries.
 * @author mohit
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction Management", description = "APIs for querying transactions and audit logs")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuditService auditService;

    /**
     * Retrieves all transactions for an account.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @param accountId the account ID
     * @return ResponseEntity with list of TransactionResponse
     */
    @Operation(summary = "Get transactions by account ID", description = "Retrieves all transactions for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid account ID", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountId(
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
        LOG.info("TransactionController:: getTransactionsByAccountId method started");
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    /**
     * Retrieves audit logs for a given entity.
     * @param transactionId unique transaction identifier
     * @param correlationId request correlation identifier
     * @param sourceChannel originating channel
     * @param tenantId tenant identifier
     * @param entityId the entity ID
     * @return ResponseEntity with list of AuditLogResponse
     */
    @Operation(summary = "Get audit logs by entity ID", description = "Retrieves all audit logs for a specific entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully",
            content = @Content(schema = @Schema(implementation = AuditLogResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid entity ID", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/audit/{entityId}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogs(
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
            @PathVariable String entityId) {
        LOG.info("TransactionController:: getAuditLogs method started");
        return ResponseEntity.ok(auditService.getAuditLogsByEntityId(entityId));
    }
}
