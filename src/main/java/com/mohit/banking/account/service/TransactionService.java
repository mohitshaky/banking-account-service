package com.mohit.banking.account.service;

import com.mohit.banking.account.dto.response.TransactionResponse;
import com.mohit.banking.account.entity.Transaction;
import com.mohit.banking.account.event.TransactionEvent;
import com.mohit.banking.account.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TransactionService - Service for persisting and retrieving transaction records.
 * @author mohit
 */
@Service
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Saves a transaction event to the database.
     * @param event the TransactionEvent to save
     */
    public void saveTransaction(TransactionEvent event) {
        LOG.info("TransactionService:: saveTransaction method started");
        Transaction transaction = Transaction.builder()
                .accountId(event.getAccountId())
                .transactionType(event.getTransactionType())
                .amount(event.getAmount())
                .description(event.getDescription())
                .status(event.getStatus())
                .transactionDate(event.getEventTimestamp() != null ? event.getEventTimestamp() : LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
    }

    /**
     * Retrieves all transactions for a given account ID.
     * @param accountId the account ID
     * @return list of TransactionResponse
     */
    public List<TransactionResponse> getTransactionsByAccountId(String accountId) {
        LOG.info("TransactionService:: getTransactionsByAccountId method started");
        return transactionRepository.findByAccountId(accountId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps Transaction entity to TransactionResponse DTO.
     * @param transaction the Transaction entity
     * @return TransactionResponse DTO
     */
    private TransactionResponse mapToResponse(Transaction transaction) {
        LOG.info("TransactionService:: mapToResponse method started");
        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccountId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
