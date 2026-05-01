package com.mohit.banking.account.producer;

import com.mohit.banking.account.event.AccountCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * AccountEventProducerTest - Unit tests for AccountEventProducer.
 * @author mohit
 */
@ExtendWith(MockitoExtension.class)
public class AccountEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private AccountEventProducer accountEventProducer;

    /**
     * Tests successful publishing of AccountCreatedEvent to Kafka.
     */
    @Test
    void testPublishAccountCreatedEvent_Success() {
        AccountCreatedEvent event = AccountCreatedEvent.builder()
                .accountId("acc-001")
                .accountHolderName("John Doe")
                .accountNumber("ACC12345678")
                .accountType("SAVINGS")
                .initialBalance(BigDecimal.valueOf(1000.0))
                .currency("USD")
                .eventTimestamp(LocalDateTime.now())
                .build();

        accountEventProducer.publishAccountCreatedEvent(event);

        verify(kafkaTemplate, times(1)).send(eq("BA_ACCOUNT_CREATED"), eq("acc-001"), any(AccountCreatedEvent.class));
    }
}
