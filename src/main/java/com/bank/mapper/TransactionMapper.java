package com.bank.mapper;

import com.bank.entity.Transaction;

import java.util.List;

public interface TransactionMapper {

    int insert(Transaction transaction);

    Transaction findById(Long transactionId);

    Transaction findByReference(String transactionReference);

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findAll();

}