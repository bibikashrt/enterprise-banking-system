package com.bank.dao;

import com.bank.entity.Transaction;

import java.util.List;

public interface TransactionDAO {

    int save(Transaction transaction);

    Transaction findById(Long transactionId);

    Transaction findByReference(String transactionReference);

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findAll();

}