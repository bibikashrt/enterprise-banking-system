package com.bank.dao;

import com.bank.entity.Branch;

import java.util.List;

public interface BranchDAO {

    int save(Branch branch);

    Branch findById(Long branchId);

    Branch findByBranchCode(String branchCode);

    Branch findByBranchName(String branchName);

    Branch findByBranchEmail(String branchEmail);

    List<Branch> findAll();

    List<Branch> search(String keyword);

    int update(Branch branch);

    int closeBranch(Long branchId);

}