package com.bank.dao.impl;


import com.bank.dao.LoanRepaymentScheduleDAO;
import com.bank.entity.LoanRepaymentSchedule;
import com.bank.mapper.LoanRepaymentScheduleMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.session.SqlSession;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@ApplicationScoped
public class LoanRepaymentScheduleDAOImpl
        implements LoanRepaymentScheduleDAO {


    @Inject
    private SqlSession sqlSession;



    private LoanRepaymentScheduleMapper mapper() {

        return sqlSession.getMapper(
                LoanRepaymentScheduleMapper.class
        );

    }



    @Override
    public int save(
            LoanRepaymentSchedule schedule) {


        log.debug(
                "Saving repayment schedule for loan: {}",
                schedule.getLoanId()
        );


        return mapper().insert(schedule);
    }



    @Override
    public List<LoanRepaymentSchedule> findByLoanId(
            Long loanId) {


        return mapper()
                .findByLoanId(loanId);
    }



    @Override
    public LoanRepaymentSchedule findNextPendingSchedule(
            Long loanId) {


        return mapper()
                .findNextPendingSchedule(loanId);
    }

    @Override
    public List<LoanRepaymentSchedule> findOverdueSchedules(
            LocalDate date) {


        return sqlSession.getMapper(
                        LoanRepaymentScheduleMapper.class
                )
                .findOverdueSchedules(date);

    }



    @Override
    public int updateStatus(
            LoanRepaymentSchedule schedule) {


        return mapper()
                .updateStatus(schedule);
    }



    @Override
    public int updatePaidAmount(
            LoanRepaymentSchedule schedule) {


        return mapper()
                .updatePaidAmount(schedule);
    }

}