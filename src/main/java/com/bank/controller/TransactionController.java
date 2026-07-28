package com.bank.controller;

import com.bank.dto.request.DepositRequest;
import com.bank.dto.request.WithdrawRequest;
import com.bank.dto.request.TransferRequest;
import com.bank.dto.response.TransactionResponse;
import com.bank.model.ApiResponse;
import com.bank.security.Secured;
import com.bank.service.TransactionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/transactions")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    @Inject
    private TransactionService transactionService;

    @POST
    @Path("/deposit")
    @RolesAllowed({"MANAGER","TELLER"})
    public Response deposit(@Valid DepositRequest request) {

        TransactionResponse response =
                transactionService.deposit(request);

        ApiResponse<TransactionResponse> apiResponse =
                ApiResponse.<TransactionResponse>builder()
                        .success(true)
                        .message("Deposit completed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @POST
    @Path("/withdraw")
    public Response withdraw(@Valid WithdrawRequest request) {

        TransactionResponse response =
                transactionService.withdraw(request);

        ApiResponse<TransactionResponse> apiResponse =
                ApiResponse.<TransactionResponse>builder()
                        .success(true)
                        .message("Withdrawal completed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @POST
    @Path("/transfer")
    public Response transfer(@Valid TransferRequest request) {

        TransactionResponse response =
                transactionService.transfer(request);

        ApiResponse<TransactionResponse> apiResponse =
                ApiResponse.<TransactionResponse>builder()
                        .success(true)
                        .message("Fund transfer completed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    public Response getAllTransactions() {

        List<TransactionResponse> response =
                transactionService.getAllTransactions();

        ApiResponse<List<TransactionResponse>> apiResponse =
                ApiResponse.<List<TransactionResponse>>builder()
                        .success(true)
                        .message("Transactions fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/{transactionId}")
    public Response getTransactionById(
            @PathParam("transactionId") Long transactionId) {

        TransactionResponse response =
                transactionService.getTransactionById(transactionId);

        ApiResponse<TransactionResponse> apiResponse =
                ApiResponse.<TransactionResponse>builder()
                        .success(true)
                        .message("Transaction fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/reference/{transactionReference}")
    public Response getTransactionByReference(
            @PathParam("transactionReference") String transactionReference) {

        TransactionResponse response =
                transactionService.getTransactionByReference(transactionReference);

        ApiResponse<TransactionResponse> apiResponse =
                ApiResponse.<TransactionResponse>builder()
                        .success(true)
                        .message("Transaction fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/account/{accountId}")
    public Response getTransactionsByAccount(
            @PathParam("accountId") Long accountId) {

        List<TransactionResponse> response =
                transactionService.getTransactionsByAccount(accountId);

        ApiResponse<List<TransactionResponse>> apiResponse =
                ApiResponse.<List<TransactionResponse>>builder()
                        .success(true)
                        .message("Transactions fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

}