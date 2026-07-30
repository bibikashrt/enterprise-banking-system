package com.bank.usecase.account;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.BranchDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dto.request.CreateAccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.Branch;
import com.bank.entity.Customer;
import com.bank.enums.AccountStatus;
import com.bank.enums.BranchStatus;
import com.bank.enums.CustomerStatus;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BranchNotFoundException;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.util.AccountNumberGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CreateAccountUseCase {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public AccountResponse execute(CreateAccountRequest request) {

        log.info("Creating account for customer ID: {}", request.getCustomerId());

        // Validate customer
        Customer customer = customerDAO.findById(request.getCustomerId());
        if (customer == null) throw new CustomerNotFoundException("Customer not found");
        if (customer.getCustomerStatus() != CustomerStatus.ACTIVE)
            throw new InvalidOperationException("Customer is not active");

        // Validate branch
        Branch branch = branchDAO.findById(request.getBranchId());
        if (branch == null) throw new BranchNotFoundException("Branch not found");
        if (branch.getBranchStatus() != BranchStatus.ACTIVE)
            throw new InvalidOperationException("Branch is not active");

        // Create account
        Account account = Account.builder()
                .accountNumber(AccountNumberGenerator.generate())
                .customerId(customer.getCustomerId())
                .branchId(branch.getBranchId())
                .accountType(request.getAccountType())
                .currency(request.getCurrency())
                .accountStatus(AccountStatus.ACTIVE)
                .build();

        accountDAO.save(account);

        // Audit log
        AuditLog auditLog = AuditLog.builder()
                .action("CREATE")
                .entityName("ACCOUNT")
                .entityId(account.getAccountId())
                .description("Account created successfully.")
                .build();
        auditLogDAO.save(auditLog);

        log.info("Account created successfully. Account Number: {}", account.getAccountNumber());

        return mapToResponse(account);
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .branchId(account.getBranchId())
                .accountType(account.getAccountType())
                .currency(account.getCurrency())
                .accountStatus(account.getAccountStatus())
                .build();
    }
}