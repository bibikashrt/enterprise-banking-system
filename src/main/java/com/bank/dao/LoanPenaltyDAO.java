package com.bank.dao;


import com.bank.entity.LoanPenalty;

import java.util.List;


public interface LoanPenaltyDAO {


    int save(
            LoanPenalty penalty
    );


    List<LoanPenalty> findByLoanId(
            Long loanId
    );


    LoanPenalty findByScheduleId(
            Long scheduleId
    );

    int updatePaidStatus(
            LoanPenalty penalty
    );

}