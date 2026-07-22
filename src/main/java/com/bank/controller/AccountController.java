package com.bank.controller;

import com.bank.dto.request.CreateAccountRequest;
import com.bank.dto.request.UpdateAccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.model.ApiResponse;
import com.bank.security.Secured;
import com.bank.service.AccountService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/accounts")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    @Inject
    private AccountService accountService;



    @POST
    public Response createAccount(@Valid CreateAccountRequest request) {

        AccountResponse response =
                accountService.createAccount(request);

        ApiResponse<AccountResponse> apiResponse =
                ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account created successfully.")
                        .code(Response.Status.CREATED.getStatusCode())
                        .data(response)
                        .build();

        return Response.status(Response.Status.CREATED)
                .entity(apiResponse)
                .build();
    }

    @GET
    public Response getAllAccounts() {

        List<AccountResponse> response =
                accountService.getAllAccounts();

        ApiResponse<List<AccountResponse>> apiResponse =
                ApiResponse.<List<AccountResponse>>builder()
                        .success(true)
                        .message("Accounts fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAccountById(
            @PathParam("accountId") Long accountId) {

        AccountResponse response =
                accountService.getAccountById(accountId);

        ApiResponse<AccountResponse> apiResponse =
                ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/number/{accountNumber}")
    public Response getAccountByNumber(
            @PathParam("accountNumber") String accountNumber) {

        AccountResponse response =
                accountService.getAccountByAccountNumber(accountNumber);

        ApiResponse<AccountResponse> apiResponse =
                ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getAccountsByCustomer(
            @PathParam("customerId") Long customerId) {

        List<AccountResponse> response =
                accountService.getAccountsByCustomer(customerId);

        ApiResponse<List<AccountResponse>> apiResponse =
                ApiResponse.<List<AccountResponse>>builder()
                        .success(true)
                        .message("Accounts fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/branch/{branchId}")
    public Response getAccountsByBranch(
            @PathParam("branchId") Long branchId) {

        List<AccountResponse> response =
                accountService.getAccountsByBranch(branchId);

        ApiResponse<List<AccountResponse>> apiResponse =
                ApiResponse.<List<AccountResponse>>builder()
                        .success(true)
                        .message("Accounts fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/search")
    public Response searchAccounts(
            @QueryParam("keyword") String keyword) {

        List<AccountResponse> response =
                accountService.searchAccounts(keyword);

        ApiResponse<List<AccountResponse>> apiResponse =
                ApiResponse.<List<AccountResponse>>builder()
                        .success(true)
                        .message("Accounts fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path("/{accountId}")
    public Response updateAccount(
            @PathParam("accountId") Long accountId,
            @Valid UpdateAccountRequest request) {

        AccountResponse response =
                accountService.updateAccount(accountId, request);

        ApiResponse<AccountResponse> apiResponse =
                ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account updated successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @DELETE
    @Path("/{accountId}")
    public Response closeAccount(
            @PathParam("accountId") Long accountId) {

        accountService.closeAccount(accountId);

        ApiResponse<Void> apiResponse =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Account closed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(apiResponse).build();
    }
}