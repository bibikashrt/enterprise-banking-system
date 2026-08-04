package com.bank.security;

import com.bank.config.JwtProperties;
import com.bank.entity.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class JwtUtil {



    @Inject
    private JwtProperties jwtProperties;

    private SecretKey key;

    @PostConstruct
    private void init() {

//        System.out.println(
//                "JWT SECRET = " + jwtProperties.getSecret()
//        );
//
//        System.out.println("JWT EXPIRATION = "
//                + jwtProperties.getExpiration());
        key = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );

        log.info(
                "JWT initialized successfully. Expiration: {} ms",
                jwtProperties.getExpiration()
        );
    }

    public String generateToken(Employee employee) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(employee.getEmail())
                .issuer(jwtProperties.getIssuer())
                .claim(JwtConstants.CLAIM_EMPLOYEE_ID, employee.getEmployeeId())
                .claim(JwtConstants.CLAIM_EMPLOYEE_NUMBER, employee.getEmployeeNumber())
                .claim(JwtConstants.CLAIM_ROLE, employee.getEmployeeRole().name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtProperties.getExpiration()))
                .signWith(key)
                .compact();
    }

    public Claims validateToken(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }



}