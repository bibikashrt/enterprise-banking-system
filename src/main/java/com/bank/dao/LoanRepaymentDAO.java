package com.bank.dao;

import com.bank.entity.LoanRepayment;

import java.util.List;

public interface LoanRepaymentDAO {


    int save(
            LoanRepayment repayment
    );


    LoanRepayment findById(
            Long repaymentId
    );


    List<LoanRepayment> findByLoanId(
            Long loanId
    );


    List<LoanRepayment> findByScheduleId(
            Long scheduleId
    );

}