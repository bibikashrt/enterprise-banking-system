package com.bank.usecase.loan;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.LoanDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dto.response.LoanResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.Loan;
import com.bank.entity.Transaction;
import com.bank.enums.AccountStatus;
import com.bank.enums.LoanStatus;
import com.bank.enums.TransactionStatus;
import com.bank.enums.TransactionType;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.exception.LoanNotFoundException;
import com.bank.util.TransactionReferenceGenerator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


@Slf4j
@ApplicationScoped
public class DisburseLoanUseCase {


    @Inject
    private LoanDAO loanDAO;


    @Inject
    private AccountDAO accountDAO;


    @Inject
    private TransactionDAO transactionDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public LoanResponse execute(Long loanId) {


        log.info(
                "Disbursing loan with ID: {}",
                loanId
        );


        Loan loan =
                loanDAO.findById(loanId);



        if (loan == null) {

            throw new LoanNotFoundException(
                    "Loan not found with ID: " + loanId
            );
        }



        if (loan.getLoanStatus()
                != LoanStatus.APPROVED) {

            throw new InvalidOperationException(
                    "Only approved loans can be disbursed."
            );
        }



        Account account =
                accountDAO.findById(
                        loan.getAccountId()
                );



        if (account == null) {

            throw new AccountNotFoundException(
                    "Account not found with ID: "
                            + loan.getAccountId()
            );
        }



        if (account.getAccountStatus()
                != AccountStatus.ACTIVE) {

            throw new InvalidOperationException(
                    "Account is not active."
            );
        }



        // Credit loan amount

        BigDecimal currentBalance =
                account.getAvailableBalance() == null
                        ? BigDecimal.ZERO
                        : account.getAvailableBalance();


        BigDecimal newBalance =
                currentBalance.add(
                        loan.getPrincipalAmount()
                );


        account.setAvailableBalance(newBalance);


        account.setLedgerBalance(
                account.getLedgerBalance() == null
                        ? loan.getPrincipalAmount()
                        : account.getLedgerBalance()
                        .add(loan.getPrincipalAmount())
        );


        accountDAO.update(account);



        // Update loan

        loan.setOutstandingBalance(
                loan.getPrincipalAmount()
        );


        int updatedRows = loanDAO.activate(loan);


        if (updatedRows == 0) {

            throw new InvalidOperationException(
                    "Loan could not be disbursed."
            );
        }

        loan = loanDAO.findById(loanId);



        // Create transaction

        Transaction transaction =
                Transaction.builder()
                        .transactionReference(
                                TransactionReferenceGenerator.generate()
                        )
                        .accountId(
                                account.getAccountId()
                        )
                        .transactionType(
                                TransactionType.LOAN_DISBURSEMENT
                        )
                        .amount(
                                loan.getPrincipalAmount()
                        )
                        .balanceAfter(
                                newBalance
                        )
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .description(
                                "Loan amount disbursed."
                        )
                        .build();



        transactionDAO.save(transaction);



        // Audit Log

        AuditLog auditLog =
                AuditLog.builder()
                        .action("DISBURSE")
                        .entityName("LOAN")
                        .entityId(
                                loan.getLoanId()
                        )
                        .description(
                                "Loan disbursed successfully."
                        )
                        .build();



        auditLogDAO.save(auditLog);



        log.info(
                "Loan disbursed successfully. Loan ID: {}",
                loanId
        );


        return mapToResponse(loan);

    }



    private LoanResponse mapToResponse(
            Loan loan) {


        return LoanResponse.builder()
                .loanId(loan.getLoanId())
                .loanNumber(loan.getLoanNumber())
                .customerId(loan.getCustomerId())
                .accountId(loan.getAccountId())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .interestRate(loan.getInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .outstandingBalance(loan.getOutstandingBalance())
                .loanStatus(loan.getLoanStatus())
                .approvedAt(loan.getApprovedAt())
                .disbursedAt(loan.getDisbursedAt())
                .closedAt(loan.getClosedAt())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }

}