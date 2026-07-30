package com.bank.usecase.transaction;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dto.request.TransferRequest;
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
public class TransferUseCase {


    @Inject
    private AccountDAO accountDAO;


    @Inject
    private TransactionDAO transactionDAO;


    @Inject
    private AuditLogDAO auditLogDAO;



    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse execute(
            TransferRequest request) {


        log.info(
                "Processing transfer from account {} to account {}",
                request.getFromAccountId(),
                request.getToAccountId()
        );


        // Validate same account transfer

        if (request.getFromAccountId()
                .equals(request.getToAccountId())) {

            throw new InvalidOperationException(
                    "Source and destination accounts cannot be same."
            );
        }



        Account fromAccount =
                accountDAO.findById(
                        request.getFromAccountId()
                );


        if (fromAccount == null) {

            throw new AccountNotFoundException(
                    "Source account not found."
            );
        }



        Account toAccount =
                accountDAO.findById(
                        request.getToAccountId()
                );


        if (toAccount == null) {

            throw new AccountNotFoundException(
                    "Destination account not found."
            );
        }



        // Validate account status

        if (fromAccount.getAccountStatus()
                != AccountStatus.ACTIVE) {

            throw new InvalidOperationException(
                    "Source account is not active."
            );
        }


        if (toAccount.getAccountStatus()
                != AccountStatus.ACTIVE) {

            throw new InvalidOperationException(
                    "Destination account is not active."
            );
        }



        // Check balance

        if (fromAccount.getAvailableBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new InvalidOperationException(
                    "Insufficient balance."
            );
        }



        // Calculate balances

        BigDecimal senderBalance =
                fromAccount.getAvailableBalance()
                        .subtract(request.getAmount());


        BigDecimal receiverBalance =
                toAccount.getAvailableBalance()
                        .add(request.getAmount());



        // Update sender

        fromAccount.setAvailableBalance(senderBalance);

        fromAccount.setLedgerBalance(senderBalance);



        // Update receiver

        toAccount.setAvailableBalance(receiverBalance);

        toAccount.setLedgerBalance(receiverBalance);



        if(accountDAO.updateBalance(fromAccount) == 0){

            throw new InvalidOperationException(
                    "Failed to update sender account."
            );
        }



        if(accountDAO.updateBalance(toAccount) == 0){

            throw new InvalidOperationException(
                    "Failed to update receiver account."
            );
        }



        String reference =
                TransactionReferenceGenerator.generate();



        // Debit transaction

        Transaction debitTransaction =
                Transaction.builder()
                        .transactionReference(reference)
                        .accountId(
                                fromAccount.getAccountId()
                        )
                        .counterpartyAccountId(
                                toAccount.getAccountId()
                        )
                        .transactionType(
                                TransactionType.TRANSFER
                        )
                        .amount(
                                request.getAmount()
                        )
                        .balanceAfter(
                                senderBalance
                        )
                        .description(
                                "Transfer to account ID: "
                                        + toAccount.getAccountId()
                                        + ". "
                                        + request.getDescription()
                        )
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .build();



        transactionDAO.save(debitTransaction);



        // Credit transaction

        Transaction creditTransaction =
                Transaction.builder()
                        .transactionReference(reference)
                        .accountId(
                                toAccount.getAccountId()
                        )
                        .counterpartyAccountId(
                                fromAccount.getAccountId()
                        )
                        .transactionType(
                                TransactionType.TRANSFER
                        )
                        .amount(
                                request.getAmount()
                        )
                        .balanceAfter(
                                receiverBalance
                        )
                        .description(
                                "Transfer from account ID: "
                                        + fromAccount.getAccountId()
                                        + ". "
                                        + request.getDescription()
                        )
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .build();



        transactionDAO.save(creditTransaction);



        // Audit

        AuditLog auditLog =
                AuditLog.builder()
                        .action("TRANSFER")
                        .entityName("TRANSACTION")
                        .entityId(
                                debitTransaction.getTransactionId()
                        )
                        .description(
                                "Fund transfer completed successfully."
                        )
                        .build();


        auditLogDAO.save(auditLog);



        log.info(
                "Transfer completed. Reference: {}",
                reference
        );


        return mapToResponse(debitTransaction);

    }



    private TransactionResponse mapToResponse(
            Transaction transaction){

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