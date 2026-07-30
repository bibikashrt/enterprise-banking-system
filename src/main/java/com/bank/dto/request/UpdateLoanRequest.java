package com.bank.dto.request;

import com.bank.enums.LoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLoanRequest {

    private Long loanId;

    @NotNull(message = "Loan type is required.")
    private LoanType loanType;

    @NotNull(message = "Principal amount is required.")
    @DecimalMin(
            value = "0.01",
            message = "Principal amount must be greater than zero."
    )
    private BigDecimal principalAmount;

    @NotNull(message = "Interest rate is required.")
    @DecimalMin(
            value = "0.00",
            message = "Interest rate cannot be negative."
    )
    private BigDecimal interestRate;

    @NotNull(message = "Tenure months is required.")
    @Positive(message = "Tenure months must be greater than zero.")
    private Integer tenureMonths;
}