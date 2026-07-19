package com.bank.dao.impl;

import com.bank.dao.BranchDAO;
import com.bank.entity.Branch;
import com.bank.mapper.BranchMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
@ApplicationScoped
public class BranchDAOImpl implements BranchDAO {

    @Inject
    private SqlSession sqlSession;

    private BranchMapper mapper() {
        return sqlSession.getMapper(BranchMapper.class);
    }

    @Override
    public int save(Branch branch) {

        log.debug("Saving branch: {}", branch.getBranchCode());

        return mapper().insert(branch);
    }

    @Override
    public Branch findById(Long branchId) {

        return mapper().findById(branchId);
    }

    @Override
    public Branch findByBranchCode(String branchCode) {

        return mapper().findByBranchCode(branchCode);
    }

    @Override
    public Branch findByBranchName(String branchName) {

        return mapper().findByBranchName(branchName);
    }

    @Override
    public Branch findByBranchEmail(String branchEmail) {

        return mapper().findByBranchEmail(branchEmail);
    }

    @Override
    public List<Branch> findAll() {

        return mapper().findAll();
    }

    @Override
    public List<Branch> search(String keyword) {

        return mapper().search(keyword);
    }

    @Override
    public int update(Branch branch) {

        log.debug("Updating branch: {}", branch.getBranchCode());

        return mapper().update(branch);
    }

    @Override
    public int closeBranch(Long branchId) {

        log.debug("Closing branch: {}", branchId);

        return mapper().closeBranch(branchId);
    }
}