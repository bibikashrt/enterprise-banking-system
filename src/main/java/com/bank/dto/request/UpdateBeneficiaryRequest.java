package com.bank.dto.request;

import com.bank.enums.BeneficiaryStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBeneficiaryRequest {

    @Size(max = 100, message = "Nickname must not exceed 100 characters.")
    private String nickname;

    @NotNull(message = "Beneficiary status is required.")
    private BeneficiaryStatus beneficiaryStatus;
}