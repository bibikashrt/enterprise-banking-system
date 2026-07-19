package com.bank.service;

import com.bank.dto.request.CreateBranchRequest;
import com.bank.dto.request.UpdateBranchRequest;
import com.bank.dto.response.BranchResponse;

import java.util.List;

public interface BranchService {

    BranchResponse createBranch(CreateBranchRequest request);

    BranchResponse updateBranch(
            Long branchId,
            UpdateBranchRequest request);

    BranchResponse getBranchById(Long branchId);

    BranchResponse getBranchByBranchCode(String branchCode);

    List<BranchResponse> getAllBranches();

    List<BranchResponse> searchBranches(String keyword);

    void closeBranch(Long branchId);

}