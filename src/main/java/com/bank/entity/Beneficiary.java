package com.bank.entity;

import com.bank.enums.BeneficiaryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {

    private Long beneficiaryId;

    private Long customerId;

    private Long beneficiaryAccountId;

    private String beneficiaryName;

    private String nickname;

    private BeneficiaryStatus beneficiaryStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}