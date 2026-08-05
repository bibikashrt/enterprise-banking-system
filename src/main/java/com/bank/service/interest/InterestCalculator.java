package com.bank.service.interest;

import com.bank.entity.LoanRepaymentSchedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InterestCalculator {


    List<LoanRepaymentSchedule> calculateSchedule(

            Long loanId,

            BigDecimal principalAmount,

            BigDecimal annualInterestRate,

            Integer tenureMonths,

            LocalDate startDate
    );

}