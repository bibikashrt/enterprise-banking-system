package com.bank.usecase.loan;

import com.bank.dao.AuditLogDAO;
import com.bank.dao.LoanDAO;
import com.bank.dto.response.LoanResponse;
import com.bank.entity.AuditLog;
import com.bank.entity.Loan;
import com.bank.enums.LoanStatus;
import com.bank.exception.InvalidOperationException;
import com.bank.exception.LoanNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ApplicationScoped
public class RejectLoanUseCase {


    @Inject
    private LoanDAO loanDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public LoanResponse execute(Long loanId) {


        log.info(
                "Rejecting loan with ID: {}",
                loanId
        );


        Loan loan =
                loanDAO.findById(loanId);



        if (loan == null) {

            throw new LoanNotFoundException(
                    "Loan not found with ID: "
                            + loanId
            );
        }



        if (loan.getLoanStatus()
                != LoanStatus.PENDING) {


            throw new InvalidOperationException(
                    "Only pending loans can be rejected."
            );
        }



        int updatedRows =
                loanDAO.reject(loanId);



        if (updatedRows == 0) {

            throw new InvalidOperationException(
                    "Loan could not be rejected."
            );
        }



        AuditLog auditLog =
                AuditLog.builder()
                        .action("REJECT")
                        .entityName("LOAN")
                        .entityId(loanId)
                        .description(
                                "Loan rejected successfully."
                        )
                        .build();



        auditLogDAO.save(auditLog);



        log.info(
                "Loan rejected successfully. Loan ID: {}",
                loanId
        );



        Loan updatedLoan =
                loanDAO.findById(loanId);



        return mapToResponse(updatedLoan);

    }



    private LoanResponse mapToResponse(
            Loan loan) {


        return LoanResponse.builder()
                .loanId(
                        loan.getLoanId()
                )
                .loanNumber(
                        loan.getLoanNumber()
                )
                .customerId(
                        loan.getCustomerId()
                )
                .accountId(
                        loan.getAccountId()
                )
                .loanType(
                        loan.getLoanType()
                )
                .principalAmount(
                        loan.getPrincipalAmount()
                )
                .interestRate(
                        loan.getInterestRate()
                )
                .tenureMonths(
                        loan.getTenureMonths()
                )
                .outstandingBalance(
                        loan.getOutstandingBalance()
                )
                .loanStatus(
                        loan.getLoanStatus()
                )
                .approvedAt(
                        loan.getApprovedAt()
                )
                .disbursedAt(
                        loan.getDisbursedAt()
                )
                .closedAt(
                        loan.getClosedAt()
                )
                .createdAt(
                        loan.getCreatedAt()
                )
                .updatedAt(
                        loan.getUpdatedAt()
                )
                .build();
    }

}