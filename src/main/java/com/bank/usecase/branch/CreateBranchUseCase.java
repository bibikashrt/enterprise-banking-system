package com.bank.usecase.branch;

import com.bank.dao.BranchDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.CreateBranchRequest;
import com.bank.dto.response.BranchResponse;
import com.bank.entity.AuditLog;
import com.bank.entity.Branch;
import com.bank.enums.BranchStatus;
import com.bank.exception.DuplicateBranchException;
import com.bank.util.BranchCodeGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateBranchUseCase {

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public BranchResponse execute(CreateBranchRequest request) {

        // Check duplicates
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