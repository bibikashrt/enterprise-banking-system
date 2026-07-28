package com.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    private Long auditLogId;

    private String action;

    private String entityName;

    private Long entityId;

    private String description;

    private LocalDateTime createdAt;
}