package com.bank.mapper;

import com.bank.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AccountMapper {

    int insert(Account account);

    Account findById(Long accountId);

    Account findByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(Long customerId);

    List<Account> findByBranchId(Long branchId);

    List<Account> findAll();

    List<Account> search(@Param("keyword") String keyword);

    int update(Account account);

    int updateBalance(Account account);

    int closeAccount(Long accountId);

}