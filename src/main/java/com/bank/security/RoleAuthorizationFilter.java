package com.bank.security;

import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Priorities;

import java.lang.reflect.Method;


@Provider
@Priority(Priorities.AUTHORIZATION)
public class RoleAuthorizationFilter
        implements ContainerRequestFilter {


    @Context
    private ResourceInfo resourceInfo;


    @Override
    public void filter(
            ContainerRequestContext requestContext) {


        Method method =
                resourceInfo.getResourceMethod();


        RolesAllowed rolesAllowed =
                method.getAnnotation(
                        RolesAllowed.class
                );



        if (rolesAllowed == null) {
            rolesAllowed =
                    resourceInfo
                            .getResourceClass()
                            .getAnnotation(
                                    RolesAllowed.class
                            );
        }

        if (rolesAllowed == null) {
            return;
        }



        SecurityContext securityContext =
                requestContext.getSecurityContext();


        boolean allowed = false;


        for(String role : rolesAllowed.value()) {


            if(securityContext
                    .isUserInRole(role)) {

                allowed = true;
                break;
            }

        }


        if(!allowed) {

            throw new ForbiddenException(
                    "You do not have permission to access this resource."
            );
        }

    }
}