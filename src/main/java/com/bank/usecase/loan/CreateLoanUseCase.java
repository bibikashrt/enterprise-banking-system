package com.bank.usecase.loan;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dao.LoanDAO;
import com.bank.dto.request.CreateLoanRequest;
import com.bank.dto.response.LoanResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.Customer;
import com.bank.entity.Loan;
import com.bank.enums.AccountStatus;
import com.bank.enums.CustomerStatus;
import com.bank.enums.LoanStatus;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.util.LoanNumberGenerator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


@Slf4j
@ApplicationScoped
public class CreateLoanUseCase {


    @Inject
    private LoanDAO loanDAO;


    @Inject
    private CustomerDAO customerDAO;


    @Inject
    private AccountDAO accountDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public LoanResponse execute(
            CreateLoanRequest request) {


        log.info(
                "Creating loan application for customer ID: {}",
                request.getCustomerId()
        );


        // Validate customer

        Customer customer =
                customerDAO.findById(
                        request.getCustomerId()
                );


        if (customer == null) {

            throw new CustomerNotFoundException(
                    "Customer not found with ID: "
                            + request.getCustomerId()
            );
        }



        if (customer.getCustomerStatus()
                != CustomerStatus.ACTIVE) {

            throw new InvalidOperationException(
                    "Customer is not active."
            );
        }



        // Validate account

        Account account =
                accountDAO.findById(
                        request.getAccountId()
                );


        if (account == null) {

            throw new AccountNotFoundException(
                    "Account not found with ID: "
                            + request.getAccountId()
            );
        }



        if (account.getAccountStatus()
                != AccountStatus.ACTIVE) {

            throw new InvalidOperationException(
                    "Account is not active."
            );
        }



        // Check ownership

        if (!account.getCustomerId()
                .equals(request.getCustomerId())) {


            throw new InvalidOperationException(
                    "Account does not belong to customer."
            );
        }



        // Create loan

        Loan loan =
                Loan.builder()
                        .loanNumber(
                                LoanNumberGenerator.generate()
                        )
                        .customerId(
                                request.getCustomerId()
                        )
                        .accountId(
                                request.getAccountId()
                        )
                        .loanType(
                                request.getLoanType()
                        )
                        .principalAmount(
                                request.getPrincipalAmount()
                        )
                        .interestRate(
                                request.getInterestRate()
                        )
                        .tenureMonths(
                                request.getTenureMonths()
                        )
                        .outstandingBalance(
                                BigDecimal.ZERO
                        )
                        .loanStatus(
                                LoanStatus.PENDING
                        )
                        .build();



        loanDAO.save(loan);



        // Audit Log

        AuditLog auditLog =
                AuditLog.builder()
                        .action("CREATE")
                        .entityName("LOAN")
                        .entityId(
                                loan.getLoanId()
                        )
                        .description(
                                "Loan application created successfully."
                        )
                        .build();


        auditLogDAO.save(auditLog);



        log.info(
                "Loan created successfully. Loan ID: {}",
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
                .createdAt(
                        loan.getCreatedAt()
                )
                .updatedAt(
                        loan.getUpdatedAt()
                )
                .build();
    }

}