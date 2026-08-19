package com.bank.entity;

import com.bank.enums.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRepaymentSchedule {


    private Long scheduleId;


    private Long loanId;


    private Integer installmentNumber;


    private LocalDate dueDate;


    private BigDecimal principalAmount;


    private BigDecimal interestAmount;


    private BigDecimal totalAmount;


    private BigDecimal paidAmount;


    private ScheduleStatus scheduleStatus;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}