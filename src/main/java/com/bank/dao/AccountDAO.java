package com.bank.dao;

import com.bank.entity.Account;

import java.util.List;

public interface AccountDAO {

    int save(Account account);

    Account findById(Long accountId);

    Account findByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(Long customerId);

    List<Account> findByBranchId(Long branchId);

    List<Account> findAll();

    List<Account> search(String keyword);

    int update(Account account);

    int updateBalance(Account account);

    int closeAccount(Long accountId);

}