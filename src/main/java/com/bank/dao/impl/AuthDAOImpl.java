package com.bank.dao.impl;

import com.bank.dao.AuthDAO;
import com.bank.entity.Employee;
import com.bank.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Slf4j
@ApplicationScoped
public class AuthDAOImpl implements AuthDAO {

    @Inject
    private SqlSession sqlSession;

    private AuthMapper mapper() {
        return sqlSession.getMapper(AuthMapper.class);
    }

    @Override
    public Employee findByEmail(String email) {

        log.info("Finding employee by email: {}", email);

        return mapper().findByEmail(email);
    }
}