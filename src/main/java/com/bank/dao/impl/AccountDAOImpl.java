package com.bank.dao.impl;

import com.bank.dao.AccountDAO;
import com.bank.entity.Account;
import com.bank.mapper.AccountMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
@ApplicationScoped
public class AccountDAOImpl implements AccountDAO {

    @Inject
    private SqlSession sqlSession;

    private AccountMapper mapper() {
        return sqlSession.getMapper(AccountMapper.class);
    }

    @Override
    public int save(Account account) {

        log.debug("Saving account: {}", account.getAccountNumber());

        return mapper().insert(account);
    }

    @Override
    public Account findById(Long accountId) {

        return mapper().findById(accountId);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {

        return mapper().findByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> findByCustomerId(Long customerId) {

        return mapper().findByCustomerId(customerId);
    }

    @Override
    public List<Account> findByBranchId(Long branchId) {

        return mapper().findByBranchId(branchId);
    }

    @Override
    public List<Account> findAll() {

        return mapper().findAll();
    }

    @Override
    public List<Account> search(String keyword) {

        return mapper().search(keyword);
    }

    @Override
    public int update(Account account) {

        log.debug("Updating account: {}", account.getAccountNumber());

        return mapper().update(account);
    }

    @Override
    public int updateBalance(Account account) {

        log.debug("Updating balance for account: {}",
                account.getAccountNumber());

        return mapper().updateBalance(account);
    }

    @Override
    public int closeAccount(Long accountId) {

        log.debug("Closing account: {}", accountId);

        return mapper().closeAccount(accountId);
    }
}