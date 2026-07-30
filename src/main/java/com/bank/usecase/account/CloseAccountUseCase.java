package com.bank.usecase.account;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.entity.AuditLog;
import com.bank.entity.Account;
import com.bank.exception.AccountNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CloseAccountUseCase {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public void execute(Long accountId) {

        log.info("Closing account with ID: {}", accountId);

        Account account = accountDAO.findById(accountId);
        if (account == null)
            throw new AccountNotFoundException("Account not found with ID: " + accountId);

        accountDAO.closeAccount(accountId);

        AuditLog auditLog = AuditLog.builder()
                .action("CLOSE")
                .entityName("ACCOUNT")
                .entityId(accountId)
                .description("Account closed successfully.")
                .build();
        auditLogDAO.save(auditLog);

        log.info("Account closed successfully. Account ID: {}", accountId);
    }
}