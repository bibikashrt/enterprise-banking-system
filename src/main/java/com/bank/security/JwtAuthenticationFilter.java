package com.bank.security;

import com.bank.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import jakarta.inject.Inject;
import java.io.IOException;

@Slf4j
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private JwtUtil jwtUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader =
                requestContext.getHeaderString(JwtConstants.HEADER);

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {

            log.warn("Missing Authorization header.");

            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Authorization token is required.")
                            .build()
            );

            return;
        }

        String token = authorizationHeader.substring(
                JwtConstants.TOKEN_PREFIX.length()
        );

        try {

            Claims claims = jwtUtil.validateToken(token);

            JwtPrincipal principal = new JwtPrincipal(

                    claims.get(JwtConstants.CLAIM_EMPLOYEE_ID,Long.class),

                    claims.get(JwtConstants.CLAIM_EMPLOYEE_NUMBER, String.class),

                    claims.getSubject(),

                    Enum.valueOf(
                            com.bank.enums.EmployeeRole.class,
                            claims.get(JwtConstants.CLAIM_ROLE, String.class)
                    )

            );

            SecurityContextImpl securityContext =
                    new SecurityContextImpl(
                            principal,
                            JwtConstants.AUTHENTICATION_SCHEME,
                            requestContext.getSecurityContext().isSecure()
                    );

            requestContext.setSecurityContext(securityContext);




        } catch (Exception ex) {

            log.error("JWT validation failed.", ex);

            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Invalid or expired token.")
                            .build()
            );
        }
    }
}