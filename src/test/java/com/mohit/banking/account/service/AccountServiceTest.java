package com.mohit.banking.account.service;

import com.mohit.banking.account.dto.request.CreateAccountRequest;
import com.mohit.banking.account.dto.request.DepositRequest;
import com.mohit.banking.account.dto.request.WithdrawRequest;
import com.mohit.banking.account.dto.response.AccountResponse;
import com.mohit.banking.account.entity.Account;
import com.mohit.banking.account.exception.BankingException;
import com.mohit.banking.account.producer.AccountEventProducer;
import com.mohit.banking.account.producer.AuditEventProducer;
import com.mohit.banking.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AccountServiceTest - Unit tests for AccountService.
 * @author mohit
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountEventProducer accountEventProducer;

    @Mock
    private AuditEventProducer auditEventProducer;

    @InjectMocks
    private AccountService accountService;

    private Account mockAccount;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        mockAccount = Account.builder()
                .id("acc-001")
                .accountHolderName("John Doe")
                .accountNumber("ACC12345678")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000.0))
                .status("ACTIVE")
                .currency("USD")
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Tests successful account creation.
     */
    @Test
    void testCreateAccount_Success() {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .accountHolderName("John Doe")
                .accountType("SAVINGS")
                .initialBalance(BigDecimal.valueOf(1000.0))
                .currency("USD")
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals("John Doe", response.getAccountHolderName());
        verify(accountEventProducer, times(1)).publishAccountCreatedEvent(any());
        verify(auditEventProducer, times(1)).publishAuditEvent(any());
    }

    /**
     * Tests retrieval of account by ID when found.
     */
    @Test
    void testGetAccountById_Found() {
        when(accountRepository.findById("acc-001")).thenReturn(Optional.of(mockAccount));

        AccountResponse response = accountService.getAccountById("acc-001");

        assertNotNull(response);
        assertEquals("acc-001", response.getId());
    }

    /**
     * Tests retrieval of account by ID when not found.
     */
    @Test
    void testGetAccountById_NotFound() {
        when(accountRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(BankingException.class, () -> accountService.getAccountById("unknown"));
    }

    /**
     * Tests successful deposit into an account.
     */
    @Test
    void testDeposit_Success() {
        DepositRequest request = DepositRequest.builder()
                .amount(BigDecimal.valueOf(500.0))
                .description("Test deposit")
                .build();

        when(accountRepository.findById("acc-001")).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

        AccountResponse response = accountService.deposit("acc-001", request);

        assertNotNull(response);
        verify(auditEventProducer, times(1)).publishTransactionEvent(any());
        verify(auditEventProducer, times(1)).publishAuditEvent(any());
    }

    /**
     * Tests withdrawal failure due to insufficient balance.
     */
    @Test
    void testWithdraw_InsufficientBalance() {
        WithdrawRequest request = WithdrawRequest.builder()
                .amount(BigDecimal.valueOf(5000.0))
                .description("Test withdrawal")
                .build();

        when(accountRepository.findById("acc-001")).thenReturn(Optional.of(mockAccount));

        assertThrows(BankingException.class, () -> accountService.withdraw("acc-001", request));
    }
}
