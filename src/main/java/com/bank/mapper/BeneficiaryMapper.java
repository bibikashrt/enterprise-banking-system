package com.bank.mapper;

import com.bank.entity.Beneficiary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BeneficiaryMapper {

    int insert(Beneficiary beneficiary);

    Beneficiary findById(Long beneficiaryId);

    List<Beneficiary> findByCustomerId(Long customerId);

    Beneficiary findByCustomerAndAccount(
            @Param("customerId") Long customerId,
            @Param("beneficiaryAccountId") Long beneficiaryAccountId
    );

    List<Beneficiary> findAll();

    int update(Beneficiary beneficiary);

    int deactivate(Long beneficiaryId);
}