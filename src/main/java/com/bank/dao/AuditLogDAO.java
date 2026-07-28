package com.bank.dao;

import com.bank.entity.AuditLog;

public interface AuditLogDAO {

    int save(AuditLog auditLog);

}