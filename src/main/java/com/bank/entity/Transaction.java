package com.bank.entity;

import com.bank.enums.TransactionStatus;
import com.bank.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private Long transactionId;

    private String transactionReference;

    private Long accountId;

    private Long counterpartyAccountId;

    private TransactionType transactionType;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private String description;

    private TransactionStatus transactionStatus;

    private LocalDateTime transactionTime;

}