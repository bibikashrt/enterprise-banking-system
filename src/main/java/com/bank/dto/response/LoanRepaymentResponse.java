package com.bank.dto.response;

import com.bank.enums.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRepaymentResponse {

    private Long loanId;

    private String loanNumber;

    private BigDecimal amountPaid;

    private BigDecimal remainingBalance;

    private LoanStatus loanStatus;

    private String transactionReference;

    private LocalDateTime transactionTime;
}