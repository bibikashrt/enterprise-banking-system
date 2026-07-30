package com.bank.usecase.loan;

import com.bank.dao.AuditLogDAO;
import com.bank.dao.LoanDAO;
import com.bank.dto.request.UpdateLoanRequest;
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
public class UpdateLoanUseCase {


    @Inject
    private LoanDAO loanDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public LoanResponse execute(
            UpdateLoanRequest request) {


        log.info(
                "Updating loan with ID: {}",
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



        // Only pending loans can be updated

        if (loan.getLoanStatus()
                != LoanStatus.PENDING) {

            throw new InvalidOperationException(
                    "Only pending loans can be updated."
            );
        }



        loan.setLoanType(
                request.getLoanType()
        );


        loan.setPrincipalAmount(
                request.getPrincipalAmount()
        );


        loan.setInterestRate(
                request.getInterestRate()
        );


        loan.setTenureMonths(
                request.getTenureMonths()
        );



        int updatedRows =
                loanDAO.update(loan);



        if (updatedRows == 0) {

            throw new InvalidOperationException(
                    "Loan could not be updated."
            );
        }



        AuditLog auditLog =
                AuditLog.builder()
                        .action("UPDATE")
                        .entityName("LOAN")
                        .entityId(
                                loan.getLoanId()
                        )
                        .description(
                                "Loan updated successfully."
                        )
                        .build();


        auditLogDAO.save(auditLog);



        log.info(
                "Loan updated successfully. ID: {}",
                loan.getLoanId()
        );


        return mapToResponse(loan);

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