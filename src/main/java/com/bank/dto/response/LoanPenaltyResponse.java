package com.bank.dto.response;


import lombok.*;
import com.bank.enums.PenaltyType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanPenaltyResponse {


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