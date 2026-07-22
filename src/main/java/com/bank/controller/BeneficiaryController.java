package com.bank.controller;

import com.bank.dto.request.CreateBeneficiaryRequest;
import com.bank.dto.request.UpdateBeneficiaryRequest;
import com.bank.dto.response.BeneficiaryResponse;
import com.bank.model.ApiResponse;
import com.bank.security.Secured;
import com.bank.service.BeneficiaryService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/beneficiaries")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BeneficiaryController {

    @Inject
    private BeneficiaryService beneficiaryService;

    @POST
    public Response createBeneficiary(
            @Valid CreateBeneficiaryRequest request) {

        BeneficiaryResponse response =
                beneficiaryService.createBeneficiary(request);

        ApiResponse<BeneficiaryResponse> apiResponse =
                ApiResponse.<BeneficiaryResponse>builder()
                        .success(true)
                        .message("Beneficiary created successfully.")
                        .code(Response.Status.CREATED.getStatusCode())
                        .data(response)
                        .build();

        return Response.status(Response.Status.CREATED)
                .entity(apiResponse)
                .build();
    }

    @GET
    public Response getAllBeneficiaries() {

        List<BeneficiaryResponse> response =
                beneficiaryService.getAllBeneficiaries();

        ApiResponse<List<BeneficiaryResponse>> apiResponse =
                ApiResponse.<List<BeneficiaryResponse>>builder()
                        .success(true)
                        .message("Beneficiaries fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/{beneficiaryId}")
    public Response getBeneficiaryById(
            @PathParam("beneficiaryId") Long beneficiaryId) {

        BeneficiaryResponse response =
                beneficiaryService.getBeneficiaryById(beneficiaryId);

        ApiResponse<BeneficiaryResponse> apiResponse =
                ApiResponse.<BeneficiaryResponse>builder()
                        .success(true)
                        .message("Beneficiary fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getBeneficiariesByCustomer(
            @PathParam("customerId") Long customerId) {

        List<BeneficiaryResponse> response =
                beneficiaryService.getBeneficiariesByCustomer(customerId);

        ApiResponse<List<BeneficiaryResponse>> apiResponse =
                ApiResponse.<List<BeneficiaryResponse>>builder()
                        .success(true)
                        .message("Beneficiaries fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path("/{beneficiaryId}")
    public Response updateBeneficiary(
            @PathParam("beneficiaryId") Long beneficiaryId,
            @Valid UpdateBeneficiaryRequest request) {

        BeneficiaryResponse response =
                beneficiaryService.updateBeneficiary(
                        beneficiaryId,
                        request);

        ApiResponse<BeneficiaryResponse> apiResponse =
                ApiResponse.<BeneficiaryResponse>builder()
                        .success(true)
                        .message("Beneficiary updated successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @DELETE
    @Path("/{beneficiaryId}")
    public Response deactivateBeneficiary(
            @PathParam("beneficiaryId") Long beneficiaryId) {

        beneficiaryService.deactivateBeneficiary(beneficiaryId);

        ApiResponse<Void> apiResponse =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Beneficiary deactivated successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(apiResponse).build();
    }
}