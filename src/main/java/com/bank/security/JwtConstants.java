package com.bank.security;

public final class JwtConstants {

    private JwtConstants() {
    }



    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER = "Authorization";

    public static final String AUTHENTICATION_SCHEME = "Bearer";

    public static final String CLAIM_EMPLOYEE_ID = "employeeId";

    public static final String CLAIM_EMPLOYEE_NUMBER = "employeeNumber";

    public static final String CLAIM_ROLE = "role";
}