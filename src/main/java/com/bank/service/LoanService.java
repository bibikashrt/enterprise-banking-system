package com.bank.service;

import com.bank.dto.request.CreateLoanRequest;
import com.bank.dto.request.UpdateLoanRequest;
import com.bank.dto.request.CreateLoanRepaymentRequest;
import com.bank.dto.response.LoanResponse;
import com.bank.dto.response.LoanRepaymentResponse;

import java.util.List;

public interface LoanService {

    LoanResponse createLoan(
            CreateLoanRequest request);

    LoanResponse getLoanById(
            Long loanId);

    LoanResponse getLoanByLoanNumber(
            String loanNumber);

    List<LoanResponse> getLoansByCustomer(
            Long customerId);

    List<LoanResponse> getLoansByAccount(
            Long accountId);

    List<LoanResponse> getAllLoans();

    List<LoanResponse> searchLoans(
            String keyword);

    LoanResponse updateLoan(
            Long loanId,
            UpdateLoanRequest request);

    LoanResponse approveLoan(
            Long loanId);

    LoanResponse rejectLoan(
            Long loanId);

    LoanResponse disburseLoan(
            Long loanId);

    LoanRepaymentResponse repayLoan(
            CreateLoanRepaymentRequest request);
}