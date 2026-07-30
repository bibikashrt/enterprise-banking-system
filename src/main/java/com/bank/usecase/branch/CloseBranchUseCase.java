package com.bank.usecase.branch;

import com.bank.dao.BranchDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.entity.AuditLog;
import com.bank.entity.Branch;
import com.bank.enums.BranchStatus;
import com.bank.exception.BranchNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CloseBranchUseCase {

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public void execute(Long branchId) {

        Branch branch = branchDAO.findById(branchId);

        if (branch == null) {
            throw new BranchNotFoundException("Branch not found with ID: " + branchId);
        }

        branch.setBranchStatus(BranchStatus.CLOSED);

        branchDAO.update(branch);

        AuditLog auditLog = AuditLog.builder()
                .action("CLOSE")
                .entityName("BRANCH")
                .entityId(branch.getBranchId())
                .description("Branch closed successfully.")
                .build();

        auditLogDAO.save(auditLog);
    }
}