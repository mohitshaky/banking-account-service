package com.mohit.banking.account.producer;

import com.mohit.banking.account.constants.Constants;
import com.mohit.banking.account.event.AccountCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * AccountEventProducer - Kafka producer for account events.
 * @author mohit
 */
@Component
public class AccountEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(AccountEventProducer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes an AccountCreatedEvent to Kafka topic BA_ACCOUNT_CREATED.
     * @param event the AccountCreatedEvent to publish
     */
    public void publishAccountCreatedEvent(AccountCreatedEvent event) {
        LOG.info("AccountEventProducer:: publishAccountCreatedEvent method started");
        try {
            kafkaTemplate.send(Constants.TOPIC_ACCOUNT_CREATED, event.getAccountId(), event);
            LOG.info("AccountEventProducer:: AccountCreatedEvent published for accountId: {}", event.getAccountId());
        } catch (Exception e) {
            LOG.error("Exception in publishAccountCreatedEvent: ", e);
            throw e;
        }
    }
}
