package com.bank.usecase.account;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.UpdateAccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.enums.AccountStatus;
import com.bank.exception.AccountNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class UpdateAccountUseCase {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public AccountResponse execute(UpdateAccountRequest request) {
        Account account = accountDAO.findById(request.getAccountId());
        if (account == null) throw new AccountNotFoundException("Account not found");

        account.setAccountStatus(request.getAccountStatus());
        accountDAO.update(account);

        AuditLog log = AuditLog.builder()
                .action("UPDATE")
                .entityName("ACCOUNT")
                .entityId(account.getAccountId())
                .description("Account updated successfully")
                .build();
        auditLogDAO.save(log);

        return mapToResponse(account);
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .accountStatus(account.getAccountStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
 }