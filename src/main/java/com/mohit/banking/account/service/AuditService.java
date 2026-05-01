package com.mohit.banking.account.service;

import com.mohit.banking.account.dto.response.AuditLogResponse;
import com.mohit.banking.account.entity.AuditLog;
import com.mohit.banking.account.event.AuditEvent;
import com.mohit.banking.account.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AuditService - Service for persisting and retrieving audit logs.
 * @author mohit
 */
@Service
public class AuditService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditService.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    /**
     * Saves an audit event to the database.
     * @param event the AuditEvent to save
     */
    public void saveAuditLog(AuditEvent event) {
        LOG.info("AuditService:: saveAuditLog method started");
        AuditLog auditLog = AuditLog.builder()
                .entityType(event.getEntityType())
                .entityId(event.getEntityId())
                .action(event.getAction())
                .details(event.getDetails())
                .timestamp(event.getEventTimestamp() != null ? event.getEventTimestamp() : LocalDateTime.now())
                .build();
        auditLogRepository.save(auditLog);
    }

    /**
     * Retrieves audit logs for a given entity ID.
     * @param entityId the entity ID
     * @return list of AuditLogResponse
     */
    public List<AuditLogResponse> getAuditLogsByEntityId(String entityId) {
        LOG.info("AuditService:: getAuditLogsByEntityId method started");
        return auditLogRepository.findByEntityId(entityId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps AuditLog entity to AuditLogResponse DTO.
     * @param log the AuditLog entity
     * @return AuditLogResponse DTO
     */
    private AuditLogResponse mapToResponse(AuditLog log) {
        LOG.info("AuditService:: mapToResponse method started");
        return AuditLogResponse.builder()
                .id(log.getId())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .action(log.getAction())
                .details(log.getDetails())
                .timestamp(log.getTimestamp())
                .build();
    }
}
