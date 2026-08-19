package com.bank.dto.response;

import com.bank.enums.ScheduleStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRepaymentScheduleResponse {


    private Long scheduleId;

    private Long loanId;

    private Integer installmentNumber;

    private LocalDate dueDate;

    private BigDecimal principalAmount;

    private BigDecimal interestAmount;

    private BigDecimal totalAmount;


    // Additional charge
    private BigDecimal penaltyAmount;

    // totalAmount + penaltyAmount
    private BigDecimal payableAmount;


    private ScheduleStatus scheduleStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}