package com.bank.service;

import com.bank.entity.LoanRepayment;

import java.util.List;

public interface LoanRepaymentHistoryService {


    List<LoanRepayment> getByLoanId(
            Long loanId
    );

}