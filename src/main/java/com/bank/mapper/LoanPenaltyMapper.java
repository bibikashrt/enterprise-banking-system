package com.bank.mapper;


import com.bank.entity.LoanPenalty;

import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LoanPenaltyMapper {


    int insert(
            LoanPenalty penalty
    );


    List<LoanPenalty> findByLoanId(
            @Param("loanId") Long loanId
    );


    LoanPenalty findByScheduleId(
            @Param("scheduleId") Long scheduleId
    );

    LoanPenalty findUnpaidByScheduleId(
            @Param( "scheduleId")Long scheduleId
    );

    int updatePaidStatus(
            LoanPenalty penalty
    );

}