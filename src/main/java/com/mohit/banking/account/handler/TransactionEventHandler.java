package com.mohit.banking.account.handler;

import com.mohit.banking.account.event.TransactionEvent;
import com.mohit.banking.account.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TransactionEventHandler - Handles TransactionEvent messages from Kafka.
 * @author mohit
 */
@Component
public class TransactionEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionEventHandler.class);

    @Autowired
    private TransactionService transactionService;

    /**
     * Handles the TransactionEvent by persisting transaction records.
     * @param event the TransactionEvent
     */
    public void handle(TransactionEvent event) {
        LOG.info("TransactionEventHandler:: handle method started");
        try {
            transactionService.saveTransaction(event);
            LOG.info("TransactionEventHandler:: Transaction saved for accountId: {}", event.getAccountId());
        } catch (Exception e) {
            LOG.error("Exception in handle: ", e);
            throw e;
        }
    }
}
