package com.bank.dto.response;

import com.bank.enums.AccountStatus;
import com.bank.enums.AccountType;
import com.bank.enums.Currency;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AccountResponse(

        Long accountId,

        String accountNumber,

        Long customerId,

        Long branchId,

        AccountType accountType,

        Currency currency,

        BigDecimal availableBalance,

        BigDecimal ledgerBalance,

        AccountStatus accountStatus,

        LocalDateTime createdAt

) {
}