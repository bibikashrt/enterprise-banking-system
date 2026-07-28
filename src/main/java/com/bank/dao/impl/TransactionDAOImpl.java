package com.bank.dao.impl;

import com.bank.dao.TransactionDAO;
import com.bank.entity.Transaction;
import com.bank.mapper.TransactionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
@ApplicationScoped
public class TransactionDAOImpl implements TransactionDAO {

    @Inject
    private SqlSession sqlSession;

    private TransactionMapper mapper() {
        return sqlSession.getMapper(TransactionMapper.class);
    }

    @Override
    public int save(Transaction transaction) {

        log.debug("Saving transaction: {}",
                transaction.getTransactionReference());

        return mapper().insert(transaction);
    }

    @Override
    public Transaction findById(Long transactionId) {

        return mapper().findById(transactionId);
    }

    @Override
    public Transaction findByReference(String transactionReference) {

        return mapper().findByReference(transactionReference);
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {

        return mapper().findByAccountId(accountId);
    }

    @Override
    public List<Transaction> findAll() {

        return mapper().findAll();
    }
}