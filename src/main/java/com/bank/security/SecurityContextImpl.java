package com.bank.security;

import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class SecurityContextImpl implements SecurityContext {

    private final JwtPrincipal principal;

    private final String authenticationScheme;

    private final boolean secure;

    public SecurityContextImpl(
            JwtPrincipal principal,
            String authenticationScheme,
            boolean secure
    ) {
        this.principal = principal;
        this.authenticationScheme = authenticationScheme;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {

        return principal.getRole().name().equals(role);

    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
}