package com.bank.dao.impl;


import com.bank.dao.LoanRepaymentDAO;
import com.bank.entity.LoanRepayment;
import com.bank.mapper.LoanRepaymentMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.session.SqlSession;

import java.util.List;


@Slf4j
@ApplicationScoped
public class LoanRepaymentDAOImpl
        implements LoanRepaymentDAO {


    @Inject
    private SqlSession sqlSession;



    private LoanRepaymentMapper mapper(){

        return sqlSession.getMapper(
                LoanRepaymentMapper.class
        );
    }



    @Override
    public int save(
            LoanRepayment repayment) {


        log.debug(
                "Saving loan repayment for loan ID: {}",
                repayment.getLoanId()
        );


        return mapper()
                .insert(repayment);
    }



    @Override
    public LoanRepayment findById(
            Long repaymentId) {


        return mapper()
                .findById(repaymentId);
    }



    @Override
    public List<LoanRepayment> findByLoanId(
            Long loanId) {


        return mapper()
                .findByLoanId(loanId);
    }



    @Override
    public List<LoanRepayment> findByScheduleId(
            Long scheduleId) {


        return mapper()
                .findByScheduleId(scheduleId);
    }

}