package com.bank.service;

import com.bank.dto.request.DepositRequest;
import com.bank.dto.request.WithdrawRequest;
import com.bank.dto.request.TransferRequest;
import com.bank.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse deposit(DepositRequest request);

    TransactionResponse withdraw(WithdrawRequest request);

    TransactionResponse transfer(TransferRequest request);

    TransactionResponse getTransactionById(Long transactionId);

    TransactionResponse getTransactionByReference(String transactionReference);

    List<TransactionResponse> getTransactionsByAccount(Long accountId);

    List<TransactionResponse> getAllTransactions();

}