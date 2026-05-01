package com.mohit.banking.account.consumer;

import com.mohit.banking.account.constants.Constants;
import com.mohit.banking.account.event.AccountCreatedEvent;
import com.mohit.banking.account.handler.AccountEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * AccountEventConsumer - Kafka consumer for account created events.
 * @author mohit
 */
@Component
@KafkaListener(topics = Constants.TOPIC_ACCOUNT_CREATED, groupId = Constants.GROUP_ACCOUNT)
public class AccountEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(AccountEventConsumer.class);

    @Autowired
    private AccountEventHandler accountEventHandler;

    /**
     * Consumes AccountCreatedEvent from Kafka topic BA_ACCOUNT_CREATED.
     * @param event the AccountCreatedEvent
     */
    @KafkaHandler
    public void consume(AccountCreatedEvent event) {
        LOG.info("AccountEventConsumer:: consume method started");
        try {
            accountEventHandler.handle(event);
        } catch (Exception e) {
            LOG.error("Exception in consume: ", e);
        }
    }
}
