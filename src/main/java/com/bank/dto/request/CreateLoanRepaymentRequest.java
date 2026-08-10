package com.bank.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoanRepaymentRequest {

    @NotNull(message = "Loan ID is required.")
    private Long loanId;

    @NotNull(message = "Schedule ID is required.")
    private Long scheduleId;

    @NotNull(message = "Repayment amount is required.")
    @DecimalMin(value = "0.01",
            message = "Repayment amount must be greater than zero.")
    private BigDecimal amount;

    private String description;
}