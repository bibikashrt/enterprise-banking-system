package com.bank.mapper;


import com.bank.entity.LoanRepayment;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LoanRepaymentMapper {


    int insert(
            LoanRepayment repayment
    );


    LoanRepayment findById(
            @Param("repaymentId") Long repaymentId
    );


    List<LoanRepayment> findByLoanId(
            @Param("loanId") Long loanId
    );


    List<LoanRepayment> findByScheduleId(
            @Param("scheduleId") Long scheduleId
    );


}