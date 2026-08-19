package com.bank.usecase.loan;

import com.bank.dao.*;
import com.bank.dto.request.CreateLoanRepaymentRequest;
import com.bank.dto.response.LoanRepaymentResponse;
import com.bank.entity.*;
import com.bank.enums.*;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.ConcurrentUpdateException;
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

    @Inject
    private LoanRepaymentScheduleDAO scheduleDAO;

    @Inject
    private LoanRepaymentDAO repaymentDAO;

    @Inject
    private LoanPenaltyDAO penaltyDAO;



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

        LoanRepaymentSchedule schedule =
                scheduleDAO.findById(
                        request.getScheduleId()
                );



        if(schedule == null){

            throw new InvalidOperationException(
                    "No pending repayment schedule found."
            );
        }

        if(schedule.getScheduleStatus()
                == ScheduleStatus.PAID){

            throw new InvalidOperationException(
                    "Installment already paid."
            );
        }

        LoanPenalty penalty =
                penaltyDAO.findUnpaidByScheduleId(
                        schedule.getScheduleId()
                );

        log.info(
                "Penalty fetched: {}",
                penalty
        );



        BigDecimal penaltyAmount =
                penalty != null
                        ? penalty.getPenaltyAmount()
                        : BigDecimal.ZERO;


        BigDecimal totalPayableAmount =
                schedule.getTotalAmount()
                        .add(penaltyAmount);

        log.info(
                "Installment: {}, Penalty: {}, Total Payable: {}",
                schedule.getTotalAmount(),
                penaltyAmount,
                totalPayableAmount
        );


        BigDecimal repaymentAmount =
                request.getAmount();

        if(repaymentAmount.compareTo(
                totalPayableAmount) < 0){

            throw new InvalidOperationException(
                    "Repayment amount is less than total payable amount including penalty."
            );
        }

        BigDecimal maximumPayable =
                totalPayableAmount;


        if (repaymentAmount.compareTo(maximumPayable) > 0) {

            throw new InvalidOperationException(
                    "Repayment amount cannot be greater than installment payable amount."
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


        int updatedAccount =
                accountDAO.updateBalance(account);


        if(updatedAccount == 0){

            throw new ConcurrentUpdateException(
                    "Account balance was updated by another transaction."
            );
        }

        // Update repayment schedule

        schedule.setScheduleStatus(
                ScheduleStatus.PAID
        );

        int updatedSchedule =
                scheduleDAO.updateStatus(schedule);


        if(updatedSchedule == 0){

            throw new InvalidOperationException(
                    "Repayment schedule update failed."
            );
        }





        // Update loan balance

        BigDecimal newOutstandingBalance =
                loan.getOutstandingBalance()
                        .subtract(
                                schedule.getPrincipalAmount()
                        );



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



        int updatedLoan =
                loanDAO.repay(loan);


        if(updatedLoan == 0){

            throw new InvalidOperationException(
                    "Loan balance update failed."
            );
        }

        if(penalty != null){

            penalty.setPaid(true);

            int updatedPenalty =
                    penaltyDAO.updatePaidStatus(
                            penalty
                    );


            if(updatedPenalty == 0){

                throw new InvalidOperationException(
                        "Penalty update failed."
                );
            }

        }




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
                                "Loan installment "
                                        + schedule.getInstallmentNumber()
                                        + " repayment completed."
                        )
                        .build();



        transactionDAO.save(transaction);

        LoanRepayment repayment =
                LoanRepayment.builder()
                        .loanId(
                                loan.getLoanId()
                        )
                        .scheduleId(
                                schedule.getScheduleId()
                        )
                        .amountPaid(
                                repaymentAmount
                        )
                        .paymentMethod(
                                "ACCOUNT"
                        )
                        .repaymentStatus(
                                RepaymentStatus.PAID
                        )
                        .transactionReference(
                                transaction.getTransactionReference()
                        )
                        .build();


        int savedRepayment =
                repaymentDAO.save(repayment);


        if(savedRepayment == 0){

            throw new InvalidOperationException(
                    "Loan repayment record could not be saved."
            );
        }



        // Audit Log

        AuditLog auditLog =
                AuditLog.builder()
                        .action("REPAY")
                        .entityName("LOAN")
                        .entityId(
                                loan.getLoanId()
                        )
                        .description(
                                "Loan installment "
                                        + schedule.getInstallmentNumber()
                                        + " repayment completed."
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