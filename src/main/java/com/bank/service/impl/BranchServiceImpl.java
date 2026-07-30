package com.bank.service.impl;

import com.bank.dao.AuditLogDAO;
import com.bank.dao.BranchDAO;
import com.bank.dto.request.CreateBranchRequest;
import com.bank.dto.request.UpdateBranchRequest;
import com.bank.dto.response.BranchResponse;
import com.bank.entity.Branch;
import com.bank.exception.BranchNotFoundException;
import com.bank.service.BranchService;
import com.bank.usecase.branch.CloseBranchUseCase;
import com.bank.usecase.branch.CreateBranchUseCase;
import com.bank.usecase.branch.UpdateBranchUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class BranchServiceImpl implements BranchService {

    @Inject
    private CreateBranchUseCase createBranchUseCase;

    @Inject
    private UpdateBranchUseCase updateBranchUseCase;

    @Inject
    private CloseBranchUseCase closeBranchUseCase;

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    public BranchResponse createBranch(CreateBranchRequest request) {
        return createBranchUseCase.execute(request);
    }

    @Override
    public BranchResponse getBranchById(Long branchId) {

        log.info("Fetching branch with ID: {}", branchId);

        Branch branch = getBranchOrThrow(branchId);

        return mapToResponse(branch);
    }

    @Override
    public BranchResponse getBranchByBranchCode(String branchCode) {

        log.info("Fetching branch with code: {}", branchCode);

        Branch branch = branchDAO.findByBranchCode(branchCode);

        if (branch == null) {
            throw new BranchNotFoundException(
                    "Branch not found with code: " + branchCode);
        }

        return mapToResponse(branch);
    }

    @Override
    public List<BranchResponse> getAllBranches() {

        log.info("Fetching all branches.");

        List<Branch> branches = branchDAO.findAll();

        List<BranchResponse> responses = new ArrayList<>();

        for (Branch branch : branches) {
            responses.add(mapToResponse(branch));
        }

        return responses;
    }

    @Override
    public List<BranchResponse> searchBranches(String keyword) {

        log.info("Searching branches with keyword: {}", keyword);

        List<Branch> branches = branchDAO.search(keyword);

        List<BranchResponse> responses = new ArrayList<>();

        for (Branch branch : branches) {
            responses.add(mapToResponse(branch));
        }

        return responses;
    }

    @Override
    public BranchResponse updateBranch(Long branchId, UpdateBranchRequest request) {
        request.setBranchId(branchId); // pass ID to use case
        return updateBranchUseCase.execute(request);
    }

    @Override
    public void closeBranch(Long branchId) {
        closeBranchUseCase.execute(branchId);
    }

    private BranchResponse mapToResponse(Branch branch) {

        return BranchResponse.builder()
                .branchId(branch.getBranchId())
                .branchCode(branch.getBranchCode())
                .branchName(branch.getBranchName())
                .branchAddress(branch.getBranchAddress())
                .branchPhone(branch.getBranchPhone())
                .branchEmail(branch.getBranchEmail())
                .branchStatus(branch.getBranchStatus())
                .createdAt(branch.getCreatedAt())
                .build();
    }

    private Branch getBranchOrThrow(Long branchId) {

        Branch branch = branchDAO.findById(branchId);

        if (branch == null) {
            throw new BranchNotFoundException(
                    "Branch not found with ID: " + branchId);
        }

        return branch;
    }


}