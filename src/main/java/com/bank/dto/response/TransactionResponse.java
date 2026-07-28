package com.bank.dto.response;

import com.bank.enums.TransactionStatus;
import com.bank.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(

        Long transactionId,

        String transactionReference,

        Long accountId,

        Long counterpartyAccountId,

        TransactionType transactionType,

        BigDecimal amount,

        BigDecimal balanceAfter,

        String description,

        TransactionStatus transactionStatus,

        LocalDateTime transactionTime

) {
}