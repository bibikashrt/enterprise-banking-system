package com.bank.dao.impl;

import com.bank.dao.LoanDAO;
import com.bank.entity.Loan;
import com.bank.mapper.LoanMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
@ApplicationScoped
public class LoanDAOImpl implements LoanDAO {

    @Inject
    private SqlSession sqlSession;

    private LoanMapper mapper() {
        return sqlSession.getMapper(LoanMapper.class);
    }

    @Override
    public int save(Loan loan) {

        log.debug("Saving loan: {}",
                loan.getLoanNumber());

        return mapper().insert(loan);
    }

    @Override
    public Loan findById(Long loanId) {

        return mapper().findById(loanId);
    }

    @Override
    public Loan findByLoanNumber(String loanNumber) {

        return mapper().findByLoanNumber(loanNumber);
    }

    @Override
    public List<Loan> findByCustomerId(Long customerId) {

        return mapper().findByCustomerId(customerId);
    }

    @Override
    public List<Loan> findByAccountId(Long accountId) {

        return mapper().findByAccountId(accountId);
    }

    @Override
    public List<Loan> findAll() {

        return mapper().findAll();
    }

    @Override
    public List<Loan> search(String keyword) {

        return mapper().search(keyword);
    }

    @Override
    public int update(Loan loan) {

        log.debug("Updating loan: {}",
                loan.getLoanNumber());

        return mapper().update(loan);
    }

    @Override
    public int approve(Long loanId) {

        log.debug("Approving loan: {}", loanId);

        return mapper().approve(loanId);
    }

    @Override
    public int reject(Long loanId) {

        log.debug("Rejecting loan: {}", loanId);

        return mapper().reject(loanId);
    }

    @Override
    public int activate(Loan loan) {

        log.debug("Activating loan: {}",
                loan.getLoanNumber());

        return mapper().activate(loan);
    }

    @Override
    public int repay(Loan loan) {

        log.debug("Updating repayment for loan: {}",
                loan.getLoanNumber());

        return mapper().repay(loan);
    }
}