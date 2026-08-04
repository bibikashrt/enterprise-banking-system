package com.bank.entity;

import com.bank.enums.AccountStatus;
import com.bank.enums.AccountType;
import com.bank.enums.Currency;
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
public class Account {

    private Long accountId;

    private String accountNumber;

    private Long customerId;

    private Long branchId;

    private AccountType accountType;

    private Currency currency;

    private BigDecimal availableBalance;

    private BigDecimal ledgerBalance;

    private AccountStatus accountStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long version;

}