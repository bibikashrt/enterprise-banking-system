package com.bank.usecase.transaction;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dto.request.WithdrawRequest;
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
public class WithdrawUseCase {


    @Inject
    private AccountDAO accountDAO;


    @Inject
    private TransactionDAO transactionDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse execute(
            WithdrawRequest request) {


        log.info(
                "Processing withdrawal for account ID: {}",
                request.getAccountId()
        );


        Account account =
                accountDAO.findById(request.getAccountId());


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



        // Check available balance

        if (account.getAvailableBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new InvalidOperationException(
                    "Insufficient balance."
            );
        }



        BigDecimal newBalance =
                account.getAvailableBalance()
                        .subtract(request.getAmount());



        account.setAvailableBalance(newBalance);

        account.setLedgerBalance(newBalance);



        accountDAO.updateBalance(account);



        Transaction transaction =
                Transaction.builder()
                        .transactionReference(
                                TransactionReferenceGenerator.generate()
                        )
                        .accountId(
                                account.getAccountId()
                        )
                        .transactionType(
                                TransactionType.WITHDRAW
                        )
                        .amount(
                                request.getAmount()
                        )
                        .balanceAfter(
                                newBalance
                        )
                        .description(
                                request.getDescription()
                        )
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .build();



        transactionDAO.save(transaction);



        AuditLog auditLog =
                AuditLog.builder()
                        .action("WITHDRAW")
                        .entityName("TRANSACTION")
                        .entityId(
                                transaction.getTransactionId()
                        )
                        .description(
                                "Withdrawal completed successfully."
                        )
                        .build();



        auditLogDAO.save(auditLog);



        log.info(
                "Withdrawal completed successfully. Reference: {}",
                transaction.getTransactionReference()
        );



        return mapToResponse(transaction);

    }



    private TransactionResponse mapToResponse(
            Transaction transaction) {


        return TransactionResponse.builder()
                .transactionId(
                        transaction.getTransactionId()
                )
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .accountId(
                        transaction.getAccountId()
                )
                .counterpartyAccountId(
                        transaction.getCounterpartyAccountId()
                )
                .transactionType(
                        transaction.getTransactionType()
                )
                .amount(
                        transaction.getAmount()
                )
                .balanceAfter(
                        transaction.getBalanceAfter()
                )
                .description(
                        transaction.getDescription()
                )
                .transactionStatus(
                        transaction.getTransactionStatus()
                )
                .transactionTime(
                        transaction.getTransactionTime()
                )
                .build();
    }

}