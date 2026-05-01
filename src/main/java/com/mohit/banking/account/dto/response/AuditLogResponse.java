package com.mohit.banking.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AuditLogResponse - DTO for returning audit log information.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    /** Audit log identifier. */
    private String id;

    /** Entity type. */
    private String entityType;

    /** Entity identifier. */
    private String entityId;

    /** Action performed. */
    private String action;

    /** Action details. */
    private String details;

    /** Audit timestamp. */
    private LocalDateTime timestamp;
}
