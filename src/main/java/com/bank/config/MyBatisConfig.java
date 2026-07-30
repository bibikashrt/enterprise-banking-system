//package com.bank.config;
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import org.apache.ibatis.builder.xml.XMLConfigBuilder;
//import org.apache.ibatis.mapping.Environment;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
//
//import javax.sql.DataSource;
//import java.io.InputStream;
//
//@ApplicationScoped
//public class MyBatisConfig {
//
//    @Inject
//    private DataSource dataSource;
//
//    private SqlSessionFactory sqlSessionFactory;
//
//    public SqlSessionFactory getSqlSessionFactory() {
//
//        if (sqlSessionFactory == null) {
//
//            InputStream inputStream =
//                    getClass().getClassLoader()
//                            .getResourceAsStream("mybatis-config.xml");
//
//            XMLConfigBuilder parser =
//                    new XMLConfigBuilder(inputStream);
//
//            Configuration configuration = parser.parse();
//
//            Environment environment = new Environment(
//                    "development",
//                    new ManagedTransactionFactory(),
//                    dataSource
//            );
//
//            configuration.setEnvironment(environment);
//
//            sqlSessionFactory =
//                    new SqlSessionFactoryBuilder().build(configuration);
//        }
//
//        return sqlSessionFactory;
//    }
//}