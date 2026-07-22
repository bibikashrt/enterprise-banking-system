package com.bank.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBeneficiaryRequest {

    @NotNull(message = "Customer ID is required.")
    private Long customerId;

    @NotNull(message = "Beneficiary account ID is required.")
    private Long beneficiaryAccountId;

    @NotBlank(message = "Beneficiary name is required.")
    @Size(max = 150, message = "Beneficiary name must not exceed 150 characters.")
    private String beneficiaryName;

    @Size(max = 100, message = "Nickname must not exceed 100 characters.")
    private String nickname;
}