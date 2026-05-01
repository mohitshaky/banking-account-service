package com.mohit.banking.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohit.banking.account.dto.request.CreateAccountRequest;
import com.mohit.banking.account.dto.response.AccountResponse;
import com.mohit.banking.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AccountControllerTest - Integration tests for AccountController.
 * @author mohit
 */
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests successful creation of a bank account via REST endpoint.
     * @throws Exception if request processing fails
     */
    @Test
    void testCreateAccount_Success() throws Exception {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .accountHolderName("John Doe")
                .accountType("SAVINGS")
                .initialBalance(BigDecimal.valueOf(1000.0))
                .currency("USD")
                .build();

        AccountResponse response = AccountResponse.builder()
                .id("acc-001")
                .accountHolderName("John Doe")
                .accountNumber("ACC12345678")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000.0))
                .status("ACTIVE")
                .currency("USD")
                .createdAt(LocalDateTime.now())
                .build();

        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("transactionId", "txn-001")
                        .header("correlationId", "corr-001")
                        .header("sourceChannel", "WEB")
                        .header("tenantId", "tenant-001"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }

    /**
     * Tests account retrieval by ID via REST endpoint.
     * @throws Exception if request processing fails
     */
    @Test
    void testGetAccountById_Success() throws Exception {
        AccountResponse response = AccountResponse.builder()
                .id("acc-001")
                .accountHolderName("John Doe")
                .accountNumber("ACC12345678")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000.0))
                .status("ACTIVE")
                .currency("USD")
                .createdAt(LocalDateTime.now())
                .build();

        when(accountService.getAccountById("acc-001")).thenReturn(response);

        mockMvc.perform(get("/api/v1/accounts/acc-001")
                        .header("transactionId", "txn-001")
                        .header("correlationId", "corr-001")
                        .header("sourceChannel", "WEB")
                        .header("tenantId", "tenant-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("acc-001"))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"));
    }
}
