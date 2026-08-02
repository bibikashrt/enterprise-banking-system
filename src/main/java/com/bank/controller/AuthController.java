package com.bank.controller;

import com.bank.dto.request.LoginRequest;
import com.bank.dto.request.ChangePasswordRequest;
import com.bank.dto.response.LoginResponse;
import com.bank.model.ApiResponse;
import com.bank.security.Secured;
import com.bank.service.AuthService;
import com.bank.security.JwtPrincipal;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {

        LoginResponse response = authService.login(request);

        return Response.ok(response).build();
    }

    @Secured
    @PUT
    @Path("/change-password")
    public Response changePassword(
            @Valid ChangePasswordRequest request,
            @Context SecurityContext securityContext
    ) {

        JwtPrincipal principal =
                (JwtPrincipal) securityContext.getUserPrincipal();


        Long employeeId =
                principal.getEmployeeId();


        authService.changePassword(
                employeeId,
                request
        );


        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Password changed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .build();


        return Response.ok(response).build();
    }
}