package com.bank.entity;


import com.bank.enums.RepaymentStatus;

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
public class LoanRepayment {


    private Long repaymentId;


    private Long loanId;


    private Long scheduleId;


    private BigDecimal amountPaid;


    private LocalDateTime paymentDate;


    private String paymentMethod;


    private RepaymentStatus repaymentStatus;


    private String transactionReference;


    private LocalDateTime createdAt;

}