package com.mohit.banking.account.handler;

import com.mohit.banking.account.event.AccountCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AccountEventHandler - Handles AccountCreatedEvent messages from Kafka.
 * @author mohit
 */
@Component
public class AccountEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AccountEventHandler.class);

    /**
     * Handles the AccountCreatedEvent.
     * @param event the AccountCreatedEvent
     */
    public void handle(AccountCreatedEvent event) {
        LOG.info("AccountEventHandler:: handle method started");
        LOG.info("AccountEventHandler:: Processing AccountCreatedEvent for accountId: {}", event.getAccountId());
    }
}
