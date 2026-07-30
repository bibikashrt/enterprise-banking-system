package com.bank.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import lombok.Getter;

@Getter
@ApplicationScoped
public class JwtConfig {

    @ConfigProperty(name = "jwt.secret")
    private String secret;

    @ConfigProperty(name = "jwt.expiration")
    private Long expiration;

    @ConfigProperty(name = "jwt.issuer")
    private String issuer;
}