package com.bank.usecase.branch;

import com.bank.dao.BranchDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.UpdateBranchRequest;
import com.bank.dto.response.BranchResponse;
import com.bank.entity.AuditLog;
import com.bank.entity.Branch;
import com.bank.exception.BranchNotFoundException;
import com.bank.exception.DuplicateBranchException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateBranchUseCase {

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public BranchResponse execute(UpdateBranchRequest request) {

        Branch branch = branchDAO.findById(request.getBranchId());

        if (branch == null) {
            throw new BranchNotFoundException("Branch not found with ID: " + request.getBranchId());
        }

        // Validate duplicates
        if (!branch.getBranchName().equals(request.getBranchName()) &&
                branchDAO.findByBranchName(request.getBranchName()) != null) {
            throw new DuplicateBranchException("Branch name already exists.");
        }

        if (!branch.getBranchEmail().equals(request.getBranchEmail()) &&
                branchDAO.findByBranchEmail(request.getBranchEmail()) != null) {
            throw new DuplicateBranchException("Branch email already exists.");
        }

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
}