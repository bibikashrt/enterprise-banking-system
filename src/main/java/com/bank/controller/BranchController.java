package com.bank.controller;

import com.bank.dto.request.CreateBranchRequest;
import com.bank.dto.request.UpdateBranchRequest;
import com.bank.dto.response.BranchResponse;
import com.bank.model.ApiResponse;
import com.bank.security.Secured;
import com.bank.service.BranchService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/branches")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BranchController {

    @Inject
    private BranchService branchService;

    @POST
    public Response createBranch(@Valid CreateBranchRequest request) {

        BranchResponse response =
                branchService.createBranch(request);

        ApiResponse<BranchResponse> apiResponse =
                ApiResponse.<BranchResponse>builder()
                        .success(true)
                        .message("Branch created successfully.")
                        .code(Response.Status.CREATED.getStatusCode())
                        .data(response)
                        .build();

        return Response.status(Response.Status.CREATED)
                .entity(apiResponse)
                .build();
    }

    @GET
    public Response getAllBranches() {

        List<BranchResponse> response =
                branchService.getAllBranches();

        ApiResponse<List<BranchResponse>> apiResponse =
                ApiResponse.<List<BranchResponse>>builder()
                        .success(true)
                        .message("Branches fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/{branchId}")
    public Response getBranchById(@PathParam("branchId") Long branchId) {

        BranchResponse response =
                branchService.getBranchById(branchId);

        ApiResponse<BranchResponse> apiResponse =
                ApiResponse.<BranchResponse>builder()
                        .success(true)
                        .message("Branch fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/code/{branchCode}")
    public Response getBranchByCode(
            @PathParam("branchCode") String branchCode) {

        BranchResponse response =
                branchService.getBranchByBranchCode(branchCode);

        ApiResponse<BranchResponse> apiResponse =
                ApiResponse.<BranchResponse>builder()
                        .success(true)
                        .message("Branch fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/search")
    public Response searchBranches(
            @QueryParam("keyword") String keyword) {

        List<BranchResponse> response =
                branchService.searchBranches(keyword);

        ApiResponse<List<BranchResponse>> apiResponse =
                ApiResponse.<List<BranchResponse>>builder()
                        .success(true)
                        .message("Branches fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path("/{branchId}")
    public Response updateBranch(
            @PathParam("branchId") Long branchId,
            @Valid UpdateBranchRequest request) {

        BranchResponse response =
                branchService.updateBranch(branchId, request);

        ApiResponse<BranchResponse> apiResponse =
                ApiResponse.<BranchResponse>builder()
                        .success(true)
                        .message("Branch updated successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @DELETE
    @Path("/{branchId}")
    public Response closeBranch(
            @PathParam("branchId") Long branchId) {

        branchService.closeBranch(branchId);

        ApiResponse<Void> apiResponse =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Branch closed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(apiResponse).build();
    }
}