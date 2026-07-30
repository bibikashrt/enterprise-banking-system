package com.bank.config;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@RequestScoped
public class SqlSessionProducer {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @Produces
    @RequestScoped
    public SqlSession produceSqlSession() {
        return sqlSessionFactory.openSession();
    }

    public void closeSqlSession(@Disposes SqlSession sqlSession) {

        if (sqlSession != null) {
            sqlSession.close();
        }
    }
}