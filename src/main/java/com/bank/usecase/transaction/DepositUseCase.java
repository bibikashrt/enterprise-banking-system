package com.bank.usecase.transaction;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dto.request.DepositRequest;
import com.bank.dto.response.TransactionResponse;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.Transaction;
import com.bank.enums.AccountStatus;
import com.bank.enums.TransactionStatus;
import com.bank.enums.TransactionType;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidOperationException;

import com.bank.util.TransactionReferenceGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


@Slf4j
@ApplicationScoped
public class DepositUseCase {


    @Inject
    private AccountDAO accountDAO;


    @Inject
    private TransactionDAO transactionDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse execute(
            DepositRequest request) {


        log.info(
                "Processing deposit for account ID: {}",
                request.getAccountId()
        );


        // Find account
        Account account =
                accountDAO.findById(request.getAccountId());


        if(account == null){
            throw new AccountNotFoundException(
                    "Account not found with ID: "
                            + request.getAccountId()
            );
        }



        // Check account status
        if(account.getAccountStatus()
                != AccountStatus.ACTIVE){

            throw new InvalidOperationException(
                    "Account is not active"
            );
        }



        // Update balance

        BigDecimal newBalance =
                account.getAvailableBalance()
                        .add(request.getAmount());


        account.setAvailableBalance(newBalance);

        account.setLedgerBalance(
                account.getLedgerBalance()
                        .add(request.getAmount())
        );


        accountDAO.updateBalance(account);



        // Create transaction record

        Transaction transaction =
                Transaction.builder()
                        .transactionReference(
                                TransactionReferenceGenerator.generate()
                        )
                        .accountId(account.getAccountId())
                        .transactionType(
                                TransactionType.DEPOSIT
                        )
                        .amount(request.getAmount())
                        .balanceAfter(newBalance)
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .description(request.getDescription())
                        .build();



        transactionDAO.save(transaction);



        // Audit Log

        AuditLog auditLog =
                AuditLog.builder()
                        .action("DEPOSIT")
                        .entityName("TRANSACTION")
                        .entityId(transaction.getTransactionId())
                        .description(
                                "Deposit transaction completed"
                        )
                        .build();


        auditLogDAO.save(auditLog);



        log.info(
                "Deposit completed successfully. Transaction ID: {}",
                transaction.getTransactionId()
        );



        return mapToResponse(transaction);

    }



    private TransactionResponse mapToResponse(
            Transaction transaction){

        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .transactionReference(transaction.getTransactionReference())
                .accountId(transaction.getAccountId())
                .counterpartyAccountId(transaction.getCounterpartyAccountId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .description(transaction.getDescription())
                .transactionStatus(transaction.getTransactionStatus())
                .transactionTime(transaction.getTransactionTime())
                .build();
    }

}