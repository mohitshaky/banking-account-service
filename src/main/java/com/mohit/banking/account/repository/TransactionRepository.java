package com.mohit.banking.account.repository;

import com.mohit.banking.account.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TransactionRepository - JPA repository for Transaction entity.
 * @author mohit
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    /**
     * Find all transactions by account ID.
     * @param accountId the account ID
     * @return List of transactions
     */
    List<Transaction> findByAccountId(String accountId);
}
