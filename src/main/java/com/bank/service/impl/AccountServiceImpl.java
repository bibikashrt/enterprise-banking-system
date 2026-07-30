package com.bank.service.impl;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.BranchDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dto.request.CreateAccountRequest;
import com.bank.dto.request.UpdateAccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.Branch;
import com.bank.entity.Customer;
import com.bank.enums.BranchStatus;
import com.bank.enums.CustomerStatus;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BranchNotFoundException;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.service.AccountService;
import com.bank.usecase.account.CloseAccountUseCase;
import com.bank.usecase.account.CreateAccountUseCase;
import com.bank.usecase.account.UpdateAccountUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    @Inject
    private CreateAccountUseCase createAccountUseCase;

    @Inject
    private UpdateAccountUseCase updateAccountUseCase;

    @Inject
    private CloseAccountUseCase closeAccountUseCase;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        return createAccountUseCase.execute(request);
    }


    @Override
    public AccountResponse getAccountById(Long accountId) {

        log.info("Fetching account with ID: {}", accountId);

        Account account = getAccountOrThrow(accountId);

        return mapToResponse(account);
    }

    @Override
    public AccountResponse getAccountByAccountNumber(String accountNumber) {

        log.info("Fetching account with account number: {}", accountNumber);

        Account account = accountDAO.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found with account number: " + accountNumber);
        }

        return mapToResponse(account);
    }

    @Override
    public List<AccountResponse> getAccountsByCustomer(Long customerId) {

        log.info("Fetching accounts for customer ID: {}", customerId);

        List<Account> accounts = accountDAO.findByCustomerId(customerId);

        List<AccountResponse> responses = new ArrayList<>();

        for (Account account : accounts) {

            responses.add(mapToResponse(account));
        }

        log.info("Total accounts found: {}", responses.size());

        return responses;
    }

    @Override
    public List<AccountResponse> getAccountsByBranch(Long branchId) {

        log.info("Fetching accounts for branch ID: {}", branchId);

        List<Account> accounts = accountDAO.findByBranchId(branchId);

        List<AccountResponse> responses = new ArrayList<>();

        for (Account account : accounts) {

            responses.add(mapToResponse(account));
        }

        log.info("Total accounts found: {}", responses.size());

        return responses;
    }

    @Override
    public List<AccountResponse> getAllAccounts() {

        log.info("Fetching all accounts.");

        List<Account> accounts = accountDAO.findAll();

        List<AccountResponse> responses = new ArrayList<>();

        for (Account account : accounts) {

            responses.add(mapToResponse(account));
        }

        log.info("Total accounts found: {}", responses.size());

        return responses;
    }

    @Override
    public List<AccountResponse> searchAccounts(String keyword) {

        log.info("Searching accounts with keyword: {}", keyword);

        List<Account> accounts = accountDAO.search(keyword);

        List<AccountResponse> responses = new ArrayList<>();

        for (Account account : accounts) {

            responses.add(mapToResponse(account));
        }

        log.info("Total accounts found: {}", responses.size());

        return responses;
    }

    @Override
    public AccountResponse updateAccount(Long accountId, UpdateAccountRequest request) {
        request.setAccountId(accountId);
        return updateAccountUseCase.execute(request);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void closeAccount(Long accountId) {
        closeAccountUseCase.execute(accountId);
    }

    private AccountResponse mapToResponse(Account account) {

        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .branchId(account.getBranchId())
                .accountType(account.getAccountType())
                .currency(account.getCurrency())
                .availableBalance(account.getAvailableBalance())
                .ledgerBalance(account.getLedgerBalance())
                .accountStatus(account.getAccountStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }

    private Account getAccountOrThrow(Long accountId) {

        Account account = accountDAO.findById(accountId);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found with ID: " + accountId);
        }

        return account;
    }

    private Customer validateCustomer(Long customerId) {

        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        if (customer.getCustomerStatus() != CustomerStatus.ACTIVE) {
            throw new InvalidOperationException("Customer is not active.");
        }

        return customer;
    }

    private Branch validateBranch(Long branchId) {

        Branch branch = branchDAO.findById(branchId);

        if (branch == null) {
            throw new BranchNotFoundException(
                    "Branch not found with ID: " + branchId);
        }

        if (branch.getBranchStatus() != BranchStatus.ACTIVE) {
            throw new InvalidOperationException("Branch is not active.");
        }

        return branch;
    }

    private void createAuditLog(
            String action,
            Long entityId,
            String description) {

        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .entityName("ACCOUNT")
                .entityId(entityId)
                .description(description)
                .build();

        auditLogDAO.save(auditLog);
    }
}

