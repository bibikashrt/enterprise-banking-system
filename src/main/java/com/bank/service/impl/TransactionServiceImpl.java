package com.bank.service.impl;

import com.bank.dao.AccountDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dto.request.DepositRequest;
import com.bank.dto.request.WithdrawRequest;
import com.bank.dto.request.TransferRequest;
import com.bank.dto.response.TransactionResponse;
import com.bank.entity.AuditLog;
import com.bank.entity.Transaction;
import com.bank.entity.Account;
import com.bank.enums.AccountStatus;
import com.bank.enums.TransactionStatus;
import com.bank.enums.TransactionType;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.exception.TransactionNotFoundException;
import com.bank.service.TransactionService;
import com.bank.util.TransactionReferenceGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class TransactionServiceImpl implements TransactionService {

    @Inject
    private TransactionDAO transactionDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse deposit(DepositRequest request) {

        log.info("Depositing {} into account ID: {}",
                request.getAmount(),
                request.getAccountId());

        Account account = accountDAO.findById(request.getAccountId());

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found with ID: " + request.getAccountId());
        }

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Account is not active.");
        }

        BigDecimal newBalance =
                account.getAvailableBalance().add(request.getAmount());

        account.setAvailableBalance(newBalance);
        account.setLedgerBalance(newBalance);

        accountDAO.updateBalance(account);

        Transaction transaction = Transaction.builder()
                .transactionReference(
                        TransactionReferenceGenerator.generate())
                .accountId(account.getAccountId())
                .transactionType(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .balanceAfter(newBalance)
                .description(request.getDescription())
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transactionDAO.save(transaction);

        AuditLog auditLog = AuditLog.builder()
                .action("DEPOSIT")
                .entityName("ACCOUNT")
                .entityId(account.getAccountId())
                .description("Deposit completed successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info("Deposit completed successfully. Transaction Reference: {}",
                transaction.getTransactionReference());

        return mapToResponse(transaction);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse withdraw(WithdrawRequest request) {

        log.info("Withdrawing {} from account ID: {}",
                request.getAmount(),
                request.getAccountId());

        Account account = accountDAO.findById(request.getAccountId());

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found with ID: " + request.getAccountId());
        }

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Account is not active.");
        }

        if (account.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidOperationException(
                    "Insufficient balance.");
        }

        BigDecimal newBalance =
                account.getAvailableBalance().subtract(request.getAmount());

        account.setAvailableBalance(newBalance);
        account.setLedgerBalance(newBalance);

        accountDAO.updateBalance(account);

        Transaction transaction = Transaction.builder()
                .transactionReference(
                        TransactionReferenceGenerator.generate())
                .accountId(account.getAccountId())
                .transactionType(TransactionType.WITHDRAW)
                .amount(request.getAmount())
                .balanceAfter(newBalance)
                .description(request.getDescription())
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transactionDAO.save(transaction);

        AuditLog auditLog = AuditLog.builder()
                .action("WITHDRAW")
                .entityName("ACCOUNT")
                .entityId(account.getAccountId())
                .description("Withdrawal completed successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info("Withdrawal completed successfully. Transaction Reference: {}",
                transaction.getTransactionReference());

        return mapToResponse(transaction);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse transfer(TransferRequest request) {
        log.info("Transferring {} from account {} to account {}",
                request.getAmount(),
                request.getFromAccountId(),
                request.getToAccountId());

        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new InvalidOperationException(
                    "Source and destination accounts cannot be the same.");
        }

        Account fromAccount = accountDAO.findById(request.getFromAccountId());

        if (fromAccount == null) {
            throw new AccountNotFoundException(
                    "Source account not found.");
        }

        Account toAccount = accountDAO.findById(request.getToAccountId());

        if (toAccount == null) {
            throw new AccountNotFoundException(
                    "Destination account not found.");
        }

        if (fromAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Source account is not active.");
        }

        if (toAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException(
                    "Destination account is not active.");
        }

        if (fromAccount.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidOperationException(
                    "Insufficient balance.");
        }

        BigDecimal senderBalance =
                fromAccount.getAvailableBalance().subtract(request.getAmount());

        BigDecimal receiverBalance =
                toAccount.getAvailableBalance().add(request.getAmount());

        fromAccount.setAvailableBalance(senderBalance);
        fromAccount.setLedgerBalance(senderBalance);

        toAccount.setAvailableBalance(receiverBalance);
        toAccount.setLedgerBalance(receiverBalance);

        if (accountDAO.updateBalance(fromAccount) == 0) {
            throw new InvalidOperationException(
                    "Failed to update source account balance.");
        }

        if (accountDAO.updateBalance(toAccount) == 0) {
            throw new InvalidOperationException(
                    "Failed to update destination account balance.");
        }

        String reference =
                TransactionReferenceGenerator.generate();

        Transaction debitTransaction = Transaction.builder()
                .transactionReference(reference)
                .accountId(fromAccount.getAccountId())
                .counterpartyAccountId(toAccount.getAccountId())
                .transactionType(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .balanceAfter(senderBalance)
                .description("Transfer to Account ID: "
                        + toAccount.getAccountId()
                        + ". "
                        + request.getDescription())
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transactionDAO.save(debitTransaction);

        Transaction creditTransaction = Transaction.builder()
                .transactionReference(reference)
                .accountId(toAccount.getAccountId())
                .counterpartyAccountId(fromAccount.getAccountId())
                .transactionType(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .balanceAfter(receiverBalance)
                .description("Transfer from Account ID: "
                        + fromAccount.getAccountId()
                        + ". "
                        + request.getDescription())
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transactionDAO.save(creditTransaction);

        AuditLog auditLog = AuditLog.builder()
                .action("TRANSFER")
                .entityName("ACCOUNT")
                .entityId(fromAccount.getAccountId())
                .description("Fund transfer completed successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info(
                "Transfer completed successfully. Reference: {}, Amount: {}",
                reference,
                request.getAmount());

        return mapToResponse(debitTransaction);
    }


    @Override
    public TransactionResponse getTransactionById(Long transactionId) {

        log.info("Fetching transaction with ID: {}", transactionId);

        Transaction transaction = getTransactionOrThrow(transactionId);

        return mapToResponse(transaction);
    }

    @Override
    public TransactionResponse getTransactionByReference(
            String transactionReference) {

        log.info("Fetching transaction with reference: {}",
                transactionReference);

        Transaction transaction =
                transactionDAO.findByReference(transactionReference);

        if (transaction == null) {
            throw new TransactionNotFoundException(
                    "Transaction not found with reference: "
                            + transactionReference);
        }

        return mapToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccount(Long accountId) {

        log.info("Fetching transactions for account ID: {}", accountId);

        List<Transaction> transactions =
                transactionDAO.findByAccountId(accountId);

        List<TransactionResponse> responses = new ArrayList<>();

        for (Transaction transaction : transactions) {
            responses.add(mapToResponse(transaction));
        }

        return responses;
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {

        log.info("Fetching all transactions.");

        List<Transaction> transactions = transactionDAO.findAll();

        List<TransactionResponse> responses = new ArrayList<>();

        for (Transaction transaction : transactions) {
            responses.add(mapToResponse(transaction));
        }

        return responses;
    }

    private TransactionResponse mapToResponse(Transaction transaction) {

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

    private Transaction getTransactionOrThrow(Long transactionId) {

        Transaction transaction = transactionDAO.findById(transactionId);

        if (transaction == null) {
            throw new TransactionNotFoundException(
                    "Transaction not found with ID: " + transactionId);
        }

        return transaction;
    }
}