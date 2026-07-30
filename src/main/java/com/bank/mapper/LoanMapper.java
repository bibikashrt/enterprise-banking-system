package com.bank.mapper;

import com.bank.entity.Loan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanMapper {

    int insert(Loan loan);

    Loan findById(
            @Param("loanId") Long loanId);

    Loan findByLoanNumber(
            @Param("loanNumber") String loanNumber);

    List<Loan> findByCustomerId(
            @Param("customerId") Long customerId);

    List<Loan> findByAccountId(
            @Param("accountId") Long accountId);

    List<Loan> findAll();

    List<Loan> search(
            @Param("keyword") String keyword);

    int update(Loan loan);

    int approve(
            @Param("loanId") Long loanId);

    int reject(
            @Param("loanId") Long loanId);

    int activate(Loan loan);

    int repay(Loan loan);
}