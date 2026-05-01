package com.mohit.banking.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AuditLog - JPA entity representing an audit log entry.
 * @author mohit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLog {

    /** Primary key - UUID generated. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Type of entity being audited. */
    @Column(nullable = false)
    private String entityType;

    /** Identifier of the entity being audited. */
    @Column(nullable = false)
    private String entityId;

    /** Action performed (ACCOUNT_CREATED, DEPOSIT, etc). */
    @Column(nullable = false)
    private String action;

    /** Additional details about the action. */
    @Column
    private String details;

    /** Timestamp when the audit event occurred. */
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
