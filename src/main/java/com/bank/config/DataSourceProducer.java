package com.bank.config;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import javax.sql.DataSource;

/**
 * Produces the WildFly managed DataSource.
 *
 * The datasource is configured in WildFly and exposed
 * through the JNDI name:
 *
 * java:/jdbc/EnterpriseBankingDS
 */
@ApplicationScoped
public class DataSourceProducer {

    @Resource(lookup = "java:/jdbc/EnterpriseBankingDS")
    private DataSource dataSource;

    @Produces
    public DataSource produceDataSource() {
        return dataSource;
    }

}