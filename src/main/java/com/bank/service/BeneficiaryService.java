package com.bank.service;

import com.bank.dto.request.CreateBeneficiaryRequest;
import com.bank.dto.request.UpdateBeneficiaryRequest;
import com.bank.dto.response.BeneficiaryResponse;

import java.util.List;

public interface BeneficiaryService {

    BeneficiaryResponse createBeneficiary(
            CreateBeneficiaryRequest request);

    BeneficiaryResponse getBeneficiaryById(
            Long beneficiaryId);

    List<BeneficiaryResponse> getBeneficiariesByCustomer(
            Long customerId);

    List<BeneficiaryResponse> getAllBeneficiaries();

    BeneficiaryResponse updateBeneficiary(
            Long beneficiaryId,
            UpdateBeneficiaryRequest request);

    void deactivateBeneficiary(
            Long beneficiaryId);
}