package com.bank.dao;

import com.bank.entity.Loan;

import java.util.List;

public interface LoanDAO {

    int save(Loan loan);

    Loan findById(Long loanId);

    Loan findByLoanNumber(String loanNumber);

    List<Loan> findByCustomerId(Long customerId);

    List<Loan> findByAccountId(Long accountId);

    List<Loan> findAll();

    List<Loan> search(String keyword);

    int update(Loan loan);

    int approve(Long loanId);

    int reject(Long loanId);

    int activate(Loan loan);

    int repay(Loan loan);
}