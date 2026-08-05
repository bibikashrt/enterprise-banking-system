package com.bank.service;

import com.bank.entity.LoanRepaymentSchedule;

import java.util.List;

public interface LoanRepaymentScheduleService {

    List<LoanRepaymentSchedule> getByLoanId(
            Long loanId
    );

}