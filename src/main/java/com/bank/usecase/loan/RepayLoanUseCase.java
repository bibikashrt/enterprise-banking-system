package com.bank.usecase.loan;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.LoanDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dto.request.CreateLoanRepaymentRequest;
import com.bank.dto.response.LoanRepaymentResponse;
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
public class RepayLoanUseCase {


    @Inject
    private LoanDAO loanDAO;


    @Inject
    private AccountDAO accountDAO;


    @Inject
    private TransactionDAO transactionDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public LoanRepaymentResponse execute(
            CreateLoanRepaymentRequest request) {


        log.info(
                "Processing loan repayment for loan ID: {}",
                request.getLoanId()
        );


        Loan loan =
                loanDAO.findById(
                        request.getLoanId()
                );


        if (loan == null) {

            throw new LoanNotFoundException(
                    "Loan not found with ID: "
                            + request.getLoanId()
            );
        }



        if (loan.getLoanStatus()
                != LoanStatus.ACTIVE) {

            throw new InvalidOperationException(
                    "Only active loans can be repaid."
            );
        }



        Account account =
                accountDAO.findById(
                        loan.getAccountId()
                );


        if (account == null) {

            throw new AccountNotFoundException(
                    "Account not found."
            );
        }



        if (account.getAccountStatus()
                != AccountStatus.ACTIVE) {

            throw new InvalidOperationException(
                    "Account is not active."
            );
        }



        BigDecimal repaymentAmount =
                request.getAmount();

        if (repaymentAmount.compareTo(
                loan.getOutstandingBalance()) > 0) {

            throw new InvalidOperationException(
                    "Repayment amount cannot be greater than outstanding balance."
            );
        }



        if (account.getAvailableBalance()
                .compareTo(repaymentAmount) < 0) {

            throw new InvalidOperationException(
                    "Insufficient balance for repayment."
            );
        }



        // Deduct amount from account

        BigDecimal newAccountBalance =
                account.getAvailableBalance()
                        .subtract(repaymentAmount);



        account.setAvailableBalance(
                newAccountBalance
        );


        account.setLedgerBalance(
                account.getLedgerBalance()
                        .subtract(repaymentAmount)
        );


        accountDAO.updateBalance(account);



        // Update loan balance

        BigDecimal newOutstandingBalance =
                loan.getOutstandingBalance()
                        .subtract(repaymentAmount);



        if(newOutstandingBalance.compareTo(BigDecimal.ZERO) <= 0){

            loan.setOutstandingBalance(
                    BigDecimal.ZERO
            );

            loan.setLoanStatus(
                    LoanStatus.CLOSED
            );

        } else {

            loan.setOutstandingBalance(
                    newOutstandingBalance
            );
        }



        loanDAO.repay(loan);



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
                                TransactionType.LOAN_REPAYMENT
                        )
                        .amount(
                                repaymentAmount
                        )
                        .balanceAfter(
                                newAccountBalance
                        )
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .description(
                                "Loan repayment completed."
                        )
                        .build();



        transactionDAO.save(transaction);



        // Audit Log

        AuditLog auditLog =
                AuditLog.builder()
                        .action("REPAY")
                        .entityName("LOAN")
                        .entityId(
                                loan.getLoanId()
                        )
                        .description(
                                "Loan repayment completed successfully."
                        )
                        .build();



        auditLogDAO.save(auditLog);



        log.info(
                "Loan repayment completed. Loan ID: {}",
                loan.getLoanId()
        );



        return mapToResponse(transaction, loan);

    }



    private LoanRepaymentResponse mapToResponse(
            Transaction transaction,
            Loan loan) {


        return LoanRepaymentResponse.builder()
                .loanId(
                        loan.getLoanId()
                )
                .loanNumber(
                        loan.getLoanNumber()
                )
                .amountPaid(
                        transaction.getAmount()
                )
                .remainingBalance(
                        loan.getOutstandingBalance()
                )
                .loanStatus(
                        loan.getLoanStatus()
                )
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .transactionTime(
                        transaction.getTransactionTime()
                )
                .build();
    }

}