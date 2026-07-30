package com.bank.dto.response;

import com.bank.enums.LoanStatus;
import com.bank.enums.LoanType;
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
public class LoanResponse {

    private Long loanId;

    private String loanNumber;

    private Long customerId;

    private Long accountId;

    private LoanType loanType;

    private BigDecimal principalAmount;

    private BigDecimal interestRate;

    private Integer tenureMonths;

    private BigDecimal outstandingBalance;

    private LoanStatus loanStatus;

    private LocalDateTime approvedAt;

    private LocalDateTime disbursedAt;

    private LocalDateTime closedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}