package com.bank.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class ConfigTest {


    @PostConstruct
    public void test(){

        Config config = ConfigProvider.getConfig();

        System.out.println("=======================");
        System.out.println(
                config.getValue("jwt.secret", String.class)
        );
        System.out.println("=======================");
    }
}