package com.bank.security;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.SecurityContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtil {

    public JwtPrincipal getCurrentUser(SecurityContext securityContext) {

        if (securityContext == null ||
                securityContext.getUserPrincipal() == null) {
            throw new NotAuthorizedException("User is not authenticated.");
        }

        return (JwtPrincipal) securityContext.getUserPrincipal();
    }
}