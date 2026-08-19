package com.bank.dao.impl;


import com.bank.dao.LoanPenaltyDAO;
import com.bank.entity.LoanPenalty;
import com.bank.mapper.LoanPenaltyMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.session.SqlSession;

import java.util.List;


@Slf4j
@ApplicationScoped
public class LoanPenaltyDAOImpl
        implements LoanPenaltyDAO {


    @Inject
    private SqlSession sqlSession;



    private LoanPenaltyMapper mapper() {

        return sqlSession.getMapper(
                LoanPenaltyMapper.class
        );

    }



    @Override
    public int save(
            LoanPenalty penalty) {


        log.debug(
                "Saving loan penalty for loan ID: {}",
                penalty.getLoanId()
        );


        return mapper()
                .insert(penalty);
    }



    @Override
    public List<LoanPenalty> findByLoanId(
            Long loanId) {


        return mapper()
                .findByLoanId(loanId);

    }



    @Override
    public LoanPenalty findByScheduleId(
            Long scheduleId) {


        return mapper()
                .findByScheduleId(scheduleId);

    }

    @Override
    public LoanPenalty findUnpaidByScheduleId(
            Long scheduleId){
        return mapper()
                .findUnpaidByScheduleId(scheduleId);
    }

    @Override
    public int updatePaidStatus(
            LoanPenalty penalty) {


        return mapper()
                .updatePaidStatus(
                        penalty
                );

    }

}