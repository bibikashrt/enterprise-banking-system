package com.bank.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBranchRequest {

    @NotBlank(message = "Branch name is required")
    private String branchName;

    @NotBlank(message = "Branch address is required")
    private String branchAddress;

    @NotBlank(message = "Branch phone is required")
    private String branchPhone;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Branch email is required")
    private String branchEmail;
}