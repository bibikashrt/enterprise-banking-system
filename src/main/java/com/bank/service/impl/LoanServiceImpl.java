package com.bank.service.impl;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dao.LoanDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dto.request.CreateLoanRequest;
import com.bank.dto.request.UpdateLoanRequest;
import com.bank.dto.request.CreateLoanRepaymentRequest;
import com.bank.dto.response.LoanRepaymentResponse;
import com.bank.dto.response.LoanResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.Customer;
import com.bank.entity.Loan;
import com.bank.entity.Transaction;
import com.bank.enums.AccountStatus;
import com.bank.enums.CustomerStatus;
import com.bank.enums.LoanStatus;
import com.bank.enums.TransactionStatus;
import com.bank.enums.TransactionType;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.exception.LoanNotFoundException;
import com.bank.service.LoanService;
import com.bank.usecase.loan.*;
import com.bank.util.LoanNumberGenerator;
import com.bank.util.TransactionReferenceGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@ApplicationScoped
public class LoanServiceImpl implements LoanService {

    @Inject
    private CreateLoanUseCase createLoanUseCase;

    @Inject
    private UpdateLoanUseCase updateLoanUseCase;

    @Inject
    private ApproveLoanUseCase approveLoanUseCase;

    @Inject
    private RejectLoanUseCase rejectLoanUseCase;

    @Inject
    private DisburseLoanUseCase disburseLoanUseCase;

    @Inject
    private RepayLoanUseCase repayLoanUseCase;

    @Inject
    private LoanDAO loanDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private TransactionDAO transactionDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    public LoanResponse createLoan(
            CreateLoanRequest request) {

        return createLoanUseCase.execute(request);
    }

    @Override
    public LoanResponse getLoanById(Long loanId) {

        log.info("Fetching loan with ID: {}", loanId);

        Loan loan = loanDAO.findById(loanId);

        if (loan == null) {
            throw new LoanNotFoundException(
                    "Loan not found with ID: " + loanId);
        }

        return mapToResponse(loan);
    }

    @Override
    public LoanResponse getLoanByLoanNumber(
            String loanNumber) {

        log.info(
                "Fetching loan with number: {}",
                loanNumber);

        Loan loan =
                loanDAO.findByLoanNumber(loanNumber);

        if (loan == null) {
            throw new LoanNotFoundException(
                    "Loan not found with number: "
                            + loanNumber);
        }

        return mapToResponse(loan);
    }

    @Override
    public List<LoanResponse> getLoansByCustomer(
            Long customerId) {

        log.info(
                "Fetching loans for customer ID: {}",
                customerId);

        Customer customer =
                customerDAO.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: "
                            + customerId);
        }

        List<Loan> loans =
                loanDAO.findByCustomerId(customerId);

        List<LoanResponse> responses =
                new ArrayList<>();

        for (Loan loan : loans) {
            responses.add(mapToResponse(loan));
        }

        log.info(
                "Total loans found: {}",
                responses.size());

        return responses;
    }

    @Override
    public List<LoanResponse> getLoansByAccount(
            Long accountId) {

        log.info(
                "Fetching loans for account ID: {}",
                accountId);

        Account account =
                accountDAO.findById(accountId);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found with ID: "
                            + accountId);
        }

        List<Loan> loans =
                loanDAO.findByAccountId(accountId);

        List<LoanResponse> responses =
                new ArrayList<>();

        for (Loan loan : loans) {
            responses.add(mapToResponse(loan));
        }

        log.info(
                "Total loans found: {}",
                responses.size());

        return responses;
    }

    @Override
    public List<LoanResponse> getAllLoans() {

        log.info("Fetching all loans.");

        List<Loan> loans =
                loanDAO.findAll();

        List<LoanResponse> responses =
                new ArrayList<>();

        for (Loan loan : loans) {
            responses.add(mapToResponse(loan));
        }

        log.info(
                "Total loans found: {}",
                responses.size());

        return responses;
    }

    @Override
    public List<LoanResponse> searchLoans(
            String keyword) {

        log.info(
                "Searching loans with keyword: {}",
                keyword);

        if (keyword == null || keyword.isBlank()) {
            throw new InvalidOperationException(
                    "Search keyword is required.");
        }

        List<Loan> loans =
                loanDAO.search(keyword);

        List<LoanResponse> responses =
                new ArrayList<>();

        for (Loan loan : loans) {
            responses.add(mapToResponse(loan));
        }

        log.info(
                "Total loans found: {}",
                responses.size());

        return responses;
    }

    @Override
    public LoanResponse updateLoan(
            Long loanId,
            UpdateLoanRequest request) {

        request.setLoanId(loanId);

        return updateLoanUseCase.execute(request);
    }

    @Override
    public LoanResponse approveLoan(Long loanId) {

        return approveLoanUseCase.execute(loanId);
    }

    @Override
    public LoanResponse rejectLoan(Long loanId) {

        return rejectLoanUseCase.execute(loanId);
    }

    @Override
    public LoanResponse disburseLoan(Long loanId) {

        return disburseLoanUseCase.execute(loanId);
    }

    @Override
    public LoanRepaymentResponse repayLoan(
            CreateLoanRepaymentRequest request) {

        return repayLoanUseCase.execute(request);
    }

    private LoanResponse mapToResponse(Loan loan) {

        return LoanResponse.builder()
                .loanId(loan.getLoanId())
                .loanNumber(loan.getLoanNumber())
                .customerId(loan.getCustomerId())
                .accountId(loan.getAccountId())
                .loanType(loan.getLoanType())
                .principalAmount(
                        loan.getPrincipalAmount())
                .interestRate(
                        loan.getInterestRate())
                .tenureMonths(
                        loan.getTenureMonths())
                .outstandingBalance(
                        loan.getOutstandingBalance())
                .loanStatus(loan.getLoanStatus())
                .approvedAt(loan.getApprovedAt())
                .disbursedAt(loan.getDisbursedAt())
                .closedAt(loan.getClosedAt())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }
}