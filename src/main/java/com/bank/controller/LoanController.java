package com.bank.controller;

import com.bank.dto.request.CreateLoanRequest;
import com.bank.dto.request.UpdateLoanRequest;
import com.bank.dto.request.CreateLoanRepaymentRequest;
import com.bank.dto.response.LoanRepaymentResponse;
import com.bank.dto.response.LoanResponse;
import com.bank.model.ApiResponse;
import com.bank.security.Secured;
import com.bank.service.LoanService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/loans")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoanController {

    @Inject
    private LoanService loanService;

    @POST
    public Response createLoan(
            @Valid CreateLoanRequest request) {

        LoanResponse response =
                loanService.createLoan(request);

        ApiResponse<LoanResponse> apiResponse =
                ApiResponse.<LoanResponse>builder()
                        .success(true)
                        .message("Loan application created successfully.")
                        .code(Response.Status.CREATED.getStatusCode())
                        .data(response)
                        .build();

        return Response.status(Response.Status.CREATED)
                .entity(apiResponse)
                .build();
    }

    @GET

    public Response getAllLoans() {

        List<LoanResponse> response =
                loanService.getAllLoans();

        ApiResponse<List<LoanResponse>> apiResponse =
                ApiResponse.<List<LoanResponse>>builder()
                        .success(true)
                        .message("Loans fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/{loanId}")
    public Response getLoanById(
            @PathParam("loanId") Long loanId) {

        LoanResponse response =
                loanService.getLoanById(loanId);

        ApiResponse<LoanResponse> apiResponse =
                ApiResponse.<LoanResponse>builder()
                        .success(true)
                        .message("Loan fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/number/{loanNumber}")
    public Response getLoanByLoanNumber(
            @PathParam("loanNumber") String loanNumber) {

        LoanResponse response =
                loanService.getLoanByLoanNumber(loanNumber);

        ApiResponse<LoanResponse> apiResponse =
                ApiResponse.<LoanResponse>builder()
                        .success(true)
                        .message("Loan fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getLoansByCustomer(
            @PathParam("customerId") Long customerId) {

        List<LoanResponse> response =
                loanService.getLoansByCustomer(customerId);

        ApiResponse<List<LoanResponse>> apiResponse =
                ApiResponse.<List<LoanResponse>>builder()
                        .success(true)
                        .message("Loans fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/account/{accountId}")
    public Response getLoansByAccount(
            @PathParam("accountId") Long accountId) {

        List<LoanResponse> response =
                loanService.getLoansByAccount(accountId);

        ApiResponse<List<LoanResponse>> apiResponse =
                ApiResponse.<List<LoanResponse>>builder()
                        .success(true)
                        .message("Loans fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/search")
    public Response searchLoans(
            @QueryParam("keyword") String keyword) {

        List<LoanResponse> response =
                loanService.searchLoans(keyword);

        ApiResponse<List<LoanResponse>> apiResponse =
                ApiResponse.<List<LoanResponse>>builder()
                        .success(true)
                        .message("Loans fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path("/{loanId}")
    public Response updateLoan(
            @PathParam("loanId") Long loanId,
            @Valid UpdateLoanRequest request) {

        LoanResponse response =
                loanService.updateLoan(
                        loanId,
                        request);

        ApiResponse<LoanResponse> apiResponse =
                ApiResponse.<LoanResponse>builder()
                        .success(true)
                        .message("Loan updated successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @RolesAllowed("MANAGER")
    @Path("/{loanId}/approve")
    public Response approveLoan(
            @PathParam("loanId") Long loanId) {

        LoanResponse response =
                loanService.approveLoan(loanId);

        ApiResponse<LoanResponse> apiResponse =
                ApiResponse.<LoanResponse>builder()
                        .success(true)
                        .message("Loan approved successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path("/{loanId}/reject")
    @RolesAllowed("MANAGER")
    public Response rejectLoan(
            @PathParam("loanId") Long loanId) {

        LoanResponse response =
                loanService.rejectLoan(loanId);

        ApiResponse<LoanResponse> apiResponse =
                ApiResponse.<LoanResponse>builder()
                        .success(true)
                        .message("Loan rejected successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path("/{loanId}/disburse")
    @RolesAllowed("MANAGER")
    public Response disburseLoan(
            @PathParam("loanId") Long loanId) {

        LoanResponse response =
                loanService.disburseLoan(loanId);

        ApiResponse<LoanResponse> apiResponse =
                ApiResponse.<LoanResponse>builder()
                        .success(true)
                        .message("Loan disbursed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @POST
    @Path("/repay")
    public Response repayLoan(
            @Valid CreateLoanRepaymentRequest request) {

        LoanRepaymentResponse response =
                loanService.repayLoan(request);

        ApiResponse<LoanRepaymentResponse> apiResponse =
                ApiResponse.<LoanRepaymentResponse>builder()
                        .success(true)
                        .message("Loan repayment completed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }
}