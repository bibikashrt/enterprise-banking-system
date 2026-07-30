package com.bank.controller;

import com.bank.dto.request.LoginRequest;
import com.bank.dto.response.LoginResponse;
import com.bank.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
}