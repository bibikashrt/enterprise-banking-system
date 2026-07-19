package com.bank.entity;

import com.bank.enums.BranchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    private Long branchId;

    private String branchCode;

    private String branchName;

    private String branchAddress;

    private String branchPhone;

    private String branchEmail;

    private BranchStatus branchStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}