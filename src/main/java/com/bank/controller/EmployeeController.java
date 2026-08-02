package com.bank.controller;

import com.bank.dto.request.CreateEmployeeRequest;
import com.bank.dto.request.UpdateEmployeeRequest;
import com.bank.dto.response.EmployeeResponse;
import com.bank.dto.response.CreateEmployeeResponse;
import com.bank.model.ApiResponse;
import com.bank.security.Secured;
import com.bank.service.EmployeeService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/employees")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeController {

    @Inject
    private EmployeeService employeeService;

    @POST
    @RolesAllowed("MANAGER")
    public Response createEmployee(
            @Valid CreateEmployeeRequest request) {

        CreateEmployeeResponse response =
                employeeService.createEmployee(request);

        ApiResponse<CreateEmployeeResponse> apiResponse =
                ApiResponse.<CreateEmployeeResponse>builder()
                        .success(true)
                        .message("Employee created successfully.")
                        .code(Response.Status.CREATED.getStatusCode())
                        .data(response)
                        .build();

        return Response.status(Response.Status.CREATED)
                .entity(apiResponse)
                .build();
    }

    @GET
    public Response getAllEmployees() {

        List<EmployeeResponse> response =
                employeeService.getAllEmployees();

        ApiResponse<List<EmployeeResponse>> apiResponse =
                ApiResponse.<List<EmployeeResponse>>builder()
                        .success(true)
                        .message("Employees fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/{employeeId}")
    public Response getEmployeeById(
            @PathParam("employeeId") Long employeeId) {

        EmployeeResponse response =
                employeeService.getEmployeeById(employeeId);

        ApiResponse<EmployeeResponse> apiResponse =
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/number/{employeeNumber}")
    public Response getEmployeeByEmployeeNumber(
            @PathParam("employeeNumber") String employeeNumber) {

        EmployeeResponse response =
                employeeService.getEmployeeByEmployeeNumber(
                        employeeNumber);

        ApiResponse<EmployeeResponse> apiResponse =
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/branch/{branchId}")
    public Response getEmployeesByBranch(
            @PathParam("branchId") Long branchId) {

        List<EmployeeResponse> response =
                employeeService.getEmployeesByBranch(branchId);

        ApiResponse<List<EmployeeResponse>> apiResponse =
                ApiResponse.<List<EmployeeResponse>>builder()
                        .success(true)
                        .message("Employees fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @GET
    @Path("/search")
    public Response searchEmployees(
            @QueryParam("keyword") String keyword) {

        List<EmployeeResponse> response =
                employeeService.searchEmployees(keyword);

        ApiResponse<List<EmployeeResponse>> apiResponse =
                ApiResponse.<List<EmployeeResponse>>builder()
                        .success(true)
                        .message("Employees fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path("/{employeeId}")
    public Response updateEmployee(
            @PathParam("employeeId") Long employeeId,
            @Valid UpdateEmployeeRequest request) {

        EmployeeResponse response =
                employeeService.updateEmployee(
                        employeeId,
                        request);

        ApiResponse<EmployeeResponse> apiResponse =
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee updated successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    @DELETE
    @Path("/{employeeId}")
    public Response deactivateEmployee(
            @PathParam("employeeId") Long employeeId) {

        employeeService.deactivateEmployee(employeeId);

        ApiResponse<Void> apiResponse =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Employee deactivated successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(apiResponse).build();
    }
}