package com.bank.service.impl;

import com.bank.dao.AuditLogDAO;
import com.bank.dao.BranchDAO;
import com.bank.dto.request.CreateBranchRequest;
import com.bank.dto.request.UpdateBranchRequest;
import com.bank.dto.response.BranchResponse;
import com.bank.entity.AuditLog;
import com.bank.entity.Branch;
import com.bank.enums.BranchStatus;
import com.bank.exception.BranchNotFoundException;
import com.bank.exception.DuplicateBranchException;
import com.bank.service.BranchService;
import com.bank.util.BranchCodeGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class BranchServiceImpl implements BranchService {

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BranchResponse createBranch(CreateBranchRequest request) {

        log.info("Creating branch: {}", request.getBranchName());

        if (branchDAO.findByBranchName(request.getBranchName()) != null) {
            throw new DuplicateBranchException("Branch name already exists.");
        }

        if (branchDAO.findByBranchEmail(request.getBranchEmail()) != null) {
            throw new DuplicateBranchException("Branch email already exists.");
        }

        Branch branch = Branch.builder()
                .branchCode(BranchCodeGenerator.generate())
                .branchName(request.getBranchName())
                .branchAddress(request.getBranchAddress())
                .branchPhone(request.getBranchPhone())
                .branchEmail(request.getBranchEmail())
                .branchStatus(BranchStatus.ACTIVE)
                .build();

        branchDAO.save(branch);

        AuditLog auditLog = AuditLog.builder()
                .action("CREATE")
                .entityName("BRANCH")
                .entityId(branch.getBranchId())
                .description("Branch created successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info("Branch created successfully. Branch Code: {}",
                branch.getBranchCode());

        return mapToResponse(branch);
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
    @Transactional(rollbackOn = Exception.class)
    public BranchResponse updateBranch(Long branchId,
                                       UpdateBranchRequest request) {

        log.info("Updating branch with ID: {}", branchId);

        Branch branch = getBranchOrThrow(branchId);

        validateDuplicateBranch(branch, request);

        branch.setBranchName(request.getBranchName());
        branch.setBranchAddress(request.getBranchAddress());
        branch.setBranchPhone(request.getBranchPhone());
        branch.setBranchEmail(request.getBranchEmail());

        branchDAO.update(branch);

        AuditLog auditLog = AuditLog.builder()
                .action("UPDATE")
                .entityName("BRANCH")
                .entityId(branch.getBranchId())
                .description("Branch updated successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info("Branch updated successfully.");

        return mapToResponse(branch);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void closeBranch(Long branchId) {

        log.info("Closing branch with ID: {}", branchId);

        Branch branch = getBranchOrThrow(branchId);

        branchDAO.closeBranch(branchId);

        AuditLog auditLog = AuditLog.builder()
                .action("CLOSE")
                .entityName("BRANCH")
                .entityId(branchId)
                .description("Branch closed successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info("Branch closed successfully.");
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

    private void validateDuplicateBranch(
            Branch branch,
            UpdateBranchRequest request) {

        if (!branch.getBranchName().equals(request.getBranchName())) {

            Branch existingBranch =
                    branchDAO.findByBranchName(request.getBranchName());

            if (existingBranch != null) {
                throw new DuplicateBranchException(
                        "Branch name already exists.");
            }
        }

        if (!branch.getBranchEmail().equals(request.getBranchEmail())) {

            Branch existingBranch =
                    branchDAO.findByBranchEmail(request.getBranchEmail());

            if (existingBranch != null) {
                throw new DuplicateBranchException(
                        "Branch email already exists.");
            }
        }
    }
}