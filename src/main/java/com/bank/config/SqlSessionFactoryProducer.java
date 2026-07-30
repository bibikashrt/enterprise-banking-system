package com.bank.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import javax.sql.DataSource;
import java.io.InputStream;

@ApplicationScoped
public class SqlSessionFactoryProducer {

    @Inject
    private DataSource dataSource;

    private SqlSessionFactory sqlSessionFactory;

    @Produces
    @ApplicationScoped
    public SqlSessionFactory sqlSessionFactory() {

        if (sqlSessionFactory == null) {

            InputStream inputStream =
                    getClass().getClassLoader()
                            .getResourceAsStream("mybatis-config.xml");

            XMLConfigBuilder parser =
                    new XMLConfigBuilder(inputStream);

            Configuration configuration = parser.parse();

            configuration.setEnvironment(
                    new Environment(
                            "default",
                            new ManagedTransactionFactory(),
                            dataSource
                    )
            );

            sqlSessionFactory =
                    new SqlSessionFactoryBuilder().build(configuration);
        }

        return sqlSessionFactory;
    }
}