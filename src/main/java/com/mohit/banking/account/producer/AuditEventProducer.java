package com.mohit.banking.account.producer;

import com.mohit.banking.account.constants.Constants;
import com.mohit.banking.account.event.AuditEvent;
import com.mohit.banking.account.event.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * AuditEventProducer - Kafka producer for audit and transaction events.
 * @author mohit
 */
@Component
public class AuditEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(AuditEventProducer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes a TransactionEvent to Kafka topic BA_TRANSACTION_EVENT.
     * @param event the TransactionEvent to publish
     */
    public void publishTransactionEvent(TransactionEvent event) {
        LOG.info("AuditEventProducer:: publishTransactionEvent method started");
        try {
            kafkaTemplate.send(Constants.TOPIC_TRANSACTION_EVENT, event.getAccountId(), event);
            LOG.info("AuditEventProducer:: TransactionEvent published for accountId: {}", event.getAccountId());
        } catch (Exception e) {
            LOG.error("Exception in publishTransactionEvent: ", e);
            throw e;
        }
    }

    /**
     * Publishes an AuditEvent to Kafka topic BA_AUDIT_LOG.
     * @param event the AuditEvent to publish
     */
    public void publishAuditEvent(AuditEvent event) {
        LOG.info("AuditEventProducer:: publishAuditEvent method started");
        try {
            kafkaTemplate.send(Constants.TOPIC_AUDIT_LOG, event.getEntityId(), event);
            LOG.info("AuditEventProducer:: AuditEvent published for entityId: {}", event.getEntityId());
        } catch (Exception e) {
            LOG.error("Exception in publishAuditEvent: ", e);
            throw e;
        }
    }
}
