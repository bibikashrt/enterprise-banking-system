package com.bank.dao;

import com.bank.entity.Beneficiary;

import java.util.List;

public interface BeneficiaryDAO {

    int save(Beneficiary beneficiary);

    Beneficiary findById(Long beneficiaryId);

    List<Beneficiary> findByCustomerId(Long customerId);

    Beneficiary findByCustomerAndAccount(
            Long customerId,
            Long beneficiaryAccountId
    );

    List<Beneficiary> findAll();

    int update(Beneficiary beneficiary);

    int deactivate(Long beneficiaryId);
}