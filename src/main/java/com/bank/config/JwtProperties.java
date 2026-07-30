package com.bank.config;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.Config;

@Getter
@Dependent
public class JwtProperties {

    private final String secret;
    private final Long expiration;
    private final String issuer;


    @Inject
    public JwtProperties(Config config) {

        this.secret = config.getValue(
                "jwt.secret",
                String.class
        );

        this.expiration = config.getValue(
                "jwt.expiration",
                Long.class
        );

        this.issuer = config.getValue(
                "jwt.issuer",
                String.class
        );
    }
}