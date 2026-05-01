package com.mohit.banking.account.consumer;

import com.mohit.banking.account.constants.Constants;
import com.mohit.banking.account.event.TransactionEvent;
import com.mohit.banking.account.handler.TransactionEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * TransactionEventConsumer - Kafka consumer for transaction events.
 * @author mohit
 */
@Component
@KafkaListener(topics = Constants.TOPIC_TRANSACTION_EVENT, groupId = Constants.GROUP_TRANSACTION)
public class TransactionEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionEventConsumer.class);

    @Autowired
    private TransactionEventHandler transactionEventHandler;

    /**
     * Consumes TransactionEvent from Kafka topic BA_TRANSACTION_EVENT.
     * @param event the TransactionEvent
     */
    @KafkaHandler
    public void consume(TransactionEvent event) {
        LOG.info("TransactionEventConsumer:: consume method started");
        try {
            transactionEventHandler.handle(event);
        } catch (Exception e) {
            LOG.error("Exception in consume: ", e);
        }
    }
}
