package com.bank.service;

import com.bank.dto.request.CreateLoanRepaymentRequest;
import com.bank.dto.response.LoanRepaymentResponse;

public interface LoanRepaymentService {

    LoanRepaymentResponse repayLoan(
            CreateLoanRepaymentRequest request);
}