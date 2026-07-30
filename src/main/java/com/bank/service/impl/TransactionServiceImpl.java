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
import com.bank.usecase.transaction.DepositUseCase;
import com.bank.usecase.transaction.TransferUseCase;
import com.bank.usecase.transaction.WithdrawUseCase;
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
    private DepositUseCase depositUseCase;

    @Inject
    private WithdrawUseCase withdrawUseCase;

    @Inject
    private TransferUseCase transferUseCase;

    @Inject
    private TransactionDAO transactionDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    public TransactionResponse deposit(DepositRequest request) {
        return depositUseCase.execute(request);
    }

    @Override
    public TransactionResponse withdraw(WithdrawRequest request) {
        return withdrawUseCase.execute(request);
    }

    @Override
    public TransactionResponse transfer(TransferRequest request) {
        return transferUseCase.execute(request);
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