package com.bank.service.impl;


import com.bank.dto.request.CreateLoanRepaymentRequest;
import com.bank.dto.response.LoanRepaymentResponse;
import com.bank.service.LoanRepaymentService;
import com.bank.usecase.loan.RepayLoanUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class LoanRepaymentServiceImpl
        implements LoanRepaymentService {

    @Inject
    private RepayLoanUseCase repayLoanUseCase;



    @Override
    public LoanRepaymentResponse repayLoan(
            CreateLoanRepaymentRequest request) {


        return repayLoanUseCase.execute(request);
    }

}