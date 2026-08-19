package com.bank.service;

import com.bank.dto.response.LoanRepaymentScheduleResponse;
import com.bank.entity.LoanRepaymentSchedule;

import java.util.List;

public interface LoanRepaymentScheduleService {

    List<LoanRepaymentScheduleResponse> getByLoanId(
            Long loanId
    );

}