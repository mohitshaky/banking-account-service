package com.mohit.banking.account.repository;

import com.mohit.banking.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AccountRepository - JPA repository for Account entity.
 * @author mohit
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Find account by account number.
     * @param accountNumber the account number
     * @return Optional of Account
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Check if account number exists.
     * @param accountNumber the account number
     * @return true if exists
     */
    boolean existsByAccountNumber(String accountNumber);
}
