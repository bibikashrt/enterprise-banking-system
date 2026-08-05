package com.bank.service.impl;

import com.bank.dao.LoanRepaymentScheduleDAO;
import com.bank.entity.LoanRepaymentSchedule;
import com.bank.service.LoanRepaymentScheduleService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;


@ApplicationScoped
public class LoanRepaymentScheduleServiceImpl
        implements LoanRepaymentScheduleService {


    @Inject
    private LoanRepaymentScheduleDAO scheduleDAO;



    @Override
    public List<LoanRepaymentSchedule> getByLoanId(
            Long loanId) {

        return scheduleDAO.findByLoanId(
                loanId
        );
    }

}