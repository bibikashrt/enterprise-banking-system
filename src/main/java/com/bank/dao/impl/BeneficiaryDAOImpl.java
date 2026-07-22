package com.bank.dao.impl;

import com.bank.dao.BeneficiaryDAO;
import com.bank.entity.Beneficiary;
import com.bank.mapper.BeneficiaryMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
@ApplicationScoped
public class BeneficiaryDAOImpl implements BeneficiaryDAO {

    @Inject
    private SqlSession sqlSession;

    private BeneficiaryMapper mapper() {
        return sqlSession.getMapper(BeneficiaryMapper.class);
    }

    @Override
    public int save(Beneficiary beneficiary) {

        log.debug("Saving beneficiary for customer ID: {}",
                beneficiary.getCustomerId());

        return mapper().insert(beneficiary);
    }

    @Override
    public Beneficiary findById(Long beneficiaryId) {

        return mapper().findById(beneficiaryId);
    }

    @Override
    public List<Beneficiary> findByCustomerId(Long customerId) {

        return mapper().findByCustomerId(customerId);
    }

    @Override
    public Beneficiary findByCustomerAndAccount(
            Long customerId,
            Long beneficiaryAccountId) {

        return mapper().findByCustomerAndAccount(
                customerId,
                beneficiaryAccountId);
    }

    @Override
    public List<Beneficiary> findAll() {

        return mapper().findAll();
    }

    @Override
    public int update(Beneficiary beneficiary) {

        log.debug("Updating beneficiary: {}",
                beneficiary.getBeneficiaryId());

        return mapper().update(beneficiary);
    }

    @Override
    public int deactivate(Long beneficiaryId) {

        log.debug("Deactivating beneficiary: {}", beneficiaryId);

        return mapper().deactivate(beneficiaryId);
    }
}