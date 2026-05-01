package com.mohit.banking.account.repository;

import com.mohit.banking.account.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AuditLogRepository - JPA repository for AuditLog entity.
 * @author mohit
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    /**
     * Find all audit logs by entity ID.
     * @param entityId the entity ID
     * @return List of audit logs
     */
    List<AuditLog> findByEntityId(String entityId);
}
