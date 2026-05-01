package com.mohit.banking.account.service;

import com.mohit.banking.account.dto.request.CreateAccountRequest;
import com.mohit.banking.account.dto.request.DepositRequest;
import com.mohit.banking.account.dto.request.TransferRequest;
import com.mohit.banking.account.dto.request.WithdrawRequest;
import com.mohit.banking.account.dto.response.AccountResponse;
import com.mohit.banking.account.dto.response.TransferResponse;
import com.mohit.banking.account.entity.Account;
import com.mohit.banking.account.event.AccountCreatedEvent;
import com.mohit.banking.account.event.AuditEvent;
import com.mohit.banking.account.event.TransactionEvent;
import com.mohit.banking.account.exception.BankingException;
import com.mohit.banking.account.producer.AccountEventProducer;
import com.mohit.banking.account.producer.AuditEventProducer;
import com.mohit.banking.account.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * AccountService - Core business service for bank account operations.
 * Publishes Kafka events on every mutating operation.
 * @author mohit
 */
@Service
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountEventProducer accountEventProducer;

    @Autowired
    private AuditEventProducer auditEventProducer;

    /**
     * Creates a new bank account and publishes AccountCreatedEvent and AuditEvent to Kafka.
     * @param request the CreateAccountRequest
     * @return AccountResponse with created account details
     */
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        LOG.info("AccountService:: createAccount method started");
        try {
            String accountNumber = generateAccountNumber();
            Account account = Account.builder()
                    .accountHolderName(request.getAccountHolderName())
                    .accountNumber(accountNumber)
                    .accountType(request.getAccountType())
                    .balance(request.getInitialBalance())
                    .status("ACTIVE")
                    .currency(request.getCurrency())
                    .createdAt(LocalDateTime.now())
                    .build();

            Account saved = accountRepository.save(account);

            AccountCreatedEvent accountEvent = AccountCreatedEvent.builder()
                    .accountId(saved.getId())
                    .accountHolderName(saved.getAccountHolderName())
                    .accountNumber(saved.getAccountNumber())
                    .accountType(saved.getAccountType())
                    .initialBalance(saved.getBalance())
                    .currency(saved.getCurrency())
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            accountEventProducer.publishAccountCreatedEvent(accountEvent);

            AuditEvent auditEvent = AuditEvent.builder()
                    .entityType("ACCOUNT")
                    .entityId(saved.getId())
                    .action("ACCOUNT_CREATED")
                    .details("Account created for: " + saved.getAccountHolderName())
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishAuditEvent(auditEvent);

            return mapToResponse(saved);
        } catch (BankingException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Exception in createAccount: ", e);
            throw new BankingException("Failed to create account", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Retrieves an account by its ID.
     * @param accountId the account ID
     * @return AccountResponse with account details
     */
    public AccountResponse getAccountById(String accountId) {
        LOG.info("AccountService:: getAccountById method started");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BankingException(
                        "Account not found: " + accountId, HttpStatus.NOT_FOUND.value()));
        return mapToResponse(account);
    }

    /**
     * Retrieves all accounts.
     * @return list of AccountResponse
     */
    public List<AccountResponse> getAllAccounts() {
        LOG.info("AccountService:: getAllAccounts method started");
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deposits funds into an account and publishes TransactionEvent and AuditEvent to Kafka.
     * @param accountId the account ID
     * @param request the DepositRequest
     * @return AccountResponse with updated balance
     */
    @Transactional
    public AccountResponse deposit(String accountId, DepositRequest request) {
        LOG.info("AccountService:: deposit method started");
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new BankingException(
                            "Account not found: " + accountId, HttpStatus.NOT_FOUND.value()));

            account.setBalance(account.getBalance().add(request.getAmount()));
            account.setUpdatedAt(LocalDateTime.now());
            Account updated = accountRepository.save(account);

            TransactionEvent txEvent = TransactionEvent.builder()
                    .accountId(accountId)
                    .transactionType("CREDIT")
                    .amount(request.getAmount())
                    .description(request.getDescription())
                    .status("COMPLETED")
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishTransactionEvent(txEvent);

            AuditEvent auditEvent = AuditEvent.builder()
                    .entityType("ACCOUNT")
                    .entityId(accountId)
                    .action("DEPOSIT")
                    .details("Deposited " + request.getAmount() + " to account: " + accountId)
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishAuditEvent(auditEvent);

            return mapToResponse(updated);
        } catch (BankingException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Exception in deposit: ", e);
            throw new BankingException("Failed to deposit", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Withdraws funds from an account and publishes TransactionEvent and AuditEvent to Kafka.
     * @param accountId the account ID
     * @param request the WithdrawRequest
     * @return AccountResponse with updated balance
     */
    @Transactional
    public AccountResponse withdraw(String accountId, WithdrawRequest request) {
        LOG.info("AccountService:: withdraw method started");
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new BankingException(
                            "Account not found: " + accountId, HttpStatus.NOT_FOUND.value()));

            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new BankingException("Insufficient balance", HttpStatus.BAD_REQUEST.value());
            }

            account.setBalance(account.getBalance().subtract(request.getAmount()));
            account.setUpdatedAt(LocalDateTime.now());
            Account updated = accountRepository.save(account);

            TransactionEvent txEvent = TransactionEvent.builder()
                    .accountId(accountId)
                    .transactionType("DEBIT")
                    .amount(request.getAmount().negate())
                    .description(request.getDescription())
                    .status("COMPLETED")
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishTransactionEvent(txEvent);

            AuditEvent auditEvent = AuditEvent.builder()
                    .entityType("ACCOUNT")
                    .entityId(accountId)
                    .action("WITHDRAWAL")
                    .details("Withdrew " + request.getAmount() + " from account: " + accountId)
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishAuditEvent(auditEvent);

            return mapToResponse(updated);
        } catch (BankingException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Exception in withdraw: ", e);
            throw new BankingException("Failed to withdraw", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Transfers funds between accounts, publishing two TransactionEvents and two AuditEvents to Kafka.
     * @param sourceAccountId the source account ID
     * @param request the TransferRequest
     * @return TransferResponse with transfer details
     */
    @Transactional
    public TransferResponse transfer(String sourceAccountId, TransferRequest request) {
        LOG.info("AccountService:: transfer method started");
        try {
            Account source = accountRepository.findById(sourceAccountId)
                    .orElseThrow(() -> new BankingException(
                            "Source account not found: " + sourceAccountId, HttpStatus.NOT_FOUND.value()));
            Account target = accountRepository.findById(request.getTargetAccountId())
                    .orElseThrow(() -> new BankingException(
                            "Target account not found: " + request.getTargetAccountId(), HttpStatus.NOT_FOUND.value()));

            if (source.getBalance().compareTo(request.getAmount()) < 0) {
                throw new BankingException("Insufficient balance in source account", HttpStatus.BAD_REQUEST.value());
            }

            source.setBalance(source.getBalance().subtract(request.getAmount()));
            source.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(source);

            target.setBalance(target.getBalance().add(request.getAmount()));
            target.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(target);

            TransactionEvent debitEvent = TransactionEvent.builder()
                    .accountId(sourceAccountId)
                    .transactionType("DEBIT")
                    .amount(request.getAmount().negate())
                    .description("Transfer to: " + request.getTargetAccountId())
                    .status("COMPLETED")
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishTransactionEvent(debitEvent);

            TransactionEvent creditEvent = TransactionEvent.builder()
                    .accountId(request.getTargetAccountId())
                    .transactionType("CREDIT")
                    .amount(request.getAmount())
                    .description("Transfer from: " + sourceAccountId)
                    .status("COMPLETED")
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishTransactionEvent(creditEvent);

            AuditEvent sourceAuditEvent = AuditEvent.builder()
                    .entityType("ACCOUNT")
                    .entityId(sourceAccountId)
                    .action("TRANSFER_DEBIT")
                    .details("Transferred " + request.getAmount() + " to: " + request.getTargetAccountId())
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishAuditEvent(sourceAuditEvent);

            AuditEvent targetAuditEvent = AuditEvent.builder()
                    .entityType("ACCOUNT")
                    .entityId(request.getTargetAccountId())
                    .action("TRANSFER_CREDIT")
                    .details("Received " + request.getAmount() + " from: " + sourceAccountId)
                    .eventTimestamp(LocalDateTime.now())
                    .build();
            auditEventProducer.publishAuditEvent(targetAuditEvent);

            return TransferResponse.builder()
                    .sourceAccountId(sourceAccountId)
                    .targetAccountId(request.getTargetAccountId())
                    .amount(request.getAmount())
                    .status("COMPLETED")
                    .transactionDate(LocalDateTime.now())
                    .build();
        } catch (BankingException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Exception in transfer: ", e);
            throw new BankingException("Failed to transfer funds", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Maps Account entity to AccountResponse DTO.
     * @param account the Account entity
     * @return AccountResponse DTO
     */
    private AccountResponse mapToResponse(Account account) {
        LOG.info("AccountService:: mapToResponse method started");
        return AccountResponse.builder()
                .id(account.getId())
                .accountHolderName(account.getAccountHolderName())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    /**
     * Generates a unique account number with ACC prefix.
     * @return generated account number string
     */
    private String generateAccountNumber() {
        LOG.info("AccountService:: generateAccountNumber method started");
        return "ACC" + System.currentTimeMillis() + (new Random().nextInt(9000) + 1000);
    }
}
