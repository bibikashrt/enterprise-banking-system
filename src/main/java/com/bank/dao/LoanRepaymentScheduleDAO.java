package com.bank.dao;

import com.bank.entity.LoanRepaymentSchedule;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepaymentScheduleDAO {


    int save(
            LoanRepaymentSchedule schedule
    );


    List<LoanRepaymentSchedule> findByLoanId(
            Long loanId
    );


    LoanRepaymentSchedule findNextPendingSchedule(
            Long loanId
    );

    List<LoanRepaymentSchedule> findOverdueSchedules(
            LocalDate date
    );

    LoanRepaymentSchedule findById(
            Long scheduleId
    );


    int updateStatus(
            LoanRepaymentSchedule schedule
    );


    int updatePaidAmount(
            LoanRepaymentSchedule schedule
    );

}