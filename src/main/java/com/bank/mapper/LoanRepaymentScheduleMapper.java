package com.bank.mapper;

import com.bank.entity.LoanRepaymentSchedule;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepaymentScheduleMapper {


    int insert(
            LoanRepaymentSchedule schedule
    );


    List<LoanRepaymentSchedule> findByLoanId(
            @Param("loanId") Long loanId
    );

    List<LoanRepaymentSchedule> findOverdueSchedules(
            @Param("date") LocalDate date
    );


    LoanRepaymentSchedule findNextPendingSchedule(
            @Param("loanId") Long loanId
    );

    LoanRepaymentSchedule findById(
            @Param("scheduleId") Long scheduleId
    );


    int updateStatus(
            LoanRepaymentSchedule schedule
    );


    int updatePaidAmount(
            LoanRepaymentSchedule schedule
    );

}