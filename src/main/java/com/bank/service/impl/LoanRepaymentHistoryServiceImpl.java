package com.bank.service.impl;


import com.bank.dao.LoanRepaymentDAO;
import com.bank.entity.LoanRepayment;
import com.bank.service.LoanRepaymentHistoryService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;


@ApplicationScoped
public class LoanRepaymentHistoryServiceImpl
        implements LoanRepaymentHistoryService {


    @Inject
    private LoanRepaymentDAO repaymentDAO;



    @Override
    public List<LoanRepayment> getByLoanId(
            Long loanId) {


        return repaymentDAO.findByLoanId(
                loanId
        );
    }

}