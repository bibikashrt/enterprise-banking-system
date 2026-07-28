package com.bank.dao.impl;

import com.bank.dao.AuditLogDAO;
import com.bank.entity.AuditLog;
import com.bank.mapper.AuditLogMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

@Slf4j
@ApplicationScoped
public class AuditLogDAOImpl implements AuditLogDAO {

    @Inject
    private SqlSession sqlSession;

    private AuditLogMapper mapper() {
        return sqlSession.getMapper(AuditLogMapper.class);
    }

    @Override
    public int save(AuditLog auditLog) {

        log.debug("Saving audit log for entity: {}",
                auditLog.getEntityName());

        return mapper().insert(auditLog);
    }
}