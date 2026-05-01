package com.mohit.banking.account.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AuditEvent - Kafka event published for audit logging.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    /** Entity type being audited. */
    private String entityType;

    /** Entity identifier. */
    private String entityId;

    /** Action performed. */
    private String action;

    /** Event details. */
    private String details;

    /** Event timestamp. */
    private LocalDateTime eventTimestamp;
}
