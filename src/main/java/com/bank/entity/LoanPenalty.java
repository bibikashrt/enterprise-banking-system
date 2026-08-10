package com.bank.entity;

import com.bank.enums.PenaltyType;
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
public class LoanPenalty {


    private Long penaltyId;


    private Long loanId;


    private Long scheduleId;


    private BigDecimal penaltyAmount;


    private PenaltyType penaltyType;


    private Integer overdueDays;


    private BigDecimal penaltyRate;


    private LocalDate calculatedDate;


    private Boolean paid;


    private LocalDateTime createdAt;

}