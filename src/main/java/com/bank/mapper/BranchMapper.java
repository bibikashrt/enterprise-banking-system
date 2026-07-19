package com.bank.mapper;

import com.bank.entity.Branch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BranchMapper {

    int insert(Branch branch);

    Branch findById(Long branchId);

    Branch findByBranchCode(String branchCode);

    Branch findByBranchName(String branchName);

    Branch findByBranchEmail(String branchEmail);

    List<Branch> findAll();

    List<Branch> search(@Param("keyword") String keyword);

    int update(Branch branch);

    int closeBranch(Long branchId);

}