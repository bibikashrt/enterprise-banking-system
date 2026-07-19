package com.bank.dto.response;

import com.bank.enums.BranchStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BranchResponse(

        Long branchId,

        String branchCode,

        String branchName,

        String branchAddress,

        String branchPhone,

        String branchEmail,

        BranchStatus branchStatus,

        LocalDateTime createdAt

) {
}