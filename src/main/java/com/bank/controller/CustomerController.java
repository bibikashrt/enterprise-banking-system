package com.bank.controller;

import com.bank.dto.request.CreateCustomerRequest;
import com.bank.dto.request.UpdateCustomerRequest;
import com.bank.dto.response.CustomerResponse;
import com.bank.model.ApiResponse;
import com.bank.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    private CustomerService customerService;

    /**
     * Create Customer
     */
    @POST
    public Response createCustomer(
            @Valid CreateCustomerRequest request) {

        CustomerResponse customer =
                customerService.createCustomer(request);

        ApiResponse<CustomerResponse> response =
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer created successfully.")
                        .data(customer)
                        .code(Response.Status.CREATED.getStatusCode())
                        .build();

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    /**
     * Get Customer By ID
     */
    @GET
    @Path("/{customerId}")
    public Response getCustomerById(
            @PathParam("customerId") Long customerId) {

        CustomerResponse customer =
                customerService.getCustomerById(customerId);

        ApiResponse<CustomerResponse> response =
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer fetched successfully.")
                        .data(customer)
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(response).build();
    }

    /**
     * Get All Customers
     */
    @GET
    public Response getAllCustomers() {

        List<CustomerResponse> customers =
                customerService.getAllCustomers();

        ApiResponse<List<CustomerResponse>> response =
                ApiResponse.<List<CustomerResponse>>builder()
                        .success(true)
                        .message("Customers fetched successfully.")
                        .data(customers)
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(response).build();
    }

    @GET
    @Path("/number/{customerNumber}")
    public Response getCustomerByCustomerNumber(
            @PathParam("customerNumber") String customerNumber) {

        CustomerResponse response =
                customerService.getCustomerByCustomerNumber(customerNumber);

        ApiResponse<CustomerResponse> apiResponse =
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer fetched successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .data(response)
                        .build();

        return Response.ok(apiResponse).build();
    }

    /**
     * Search Customers
     */
    @GET
    @Path("/search")
    public Response searchCustomers(
            @QueryParam("keyword") String keyword) {

        List<CustomerResponse> customers =
                customerService.searchCustomers(keyword);

        ApiResponse<List<CustomerResponse>> response =
                ApiResponse.<List<CustomerResponse>>builder()
                        .success(true)
                        .message("Customers fetched successfully.")
                        .data(customers)
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(response).build();
    }

    /**
     * Update Customer
     */
    @PUT
    @Path("/{customerId}")
    public Response updateCustomer(
            @PathParam("customerId") Long customerId,
            @Valid UpdateCustomerRequest request) {

        CustomerResponse customer =
                customerService.updateCustomer(customerId, request);

        ApiResponse<CustomerResponse> response =
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer updated successfully.")
                        .data(customer)
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(response).build();
    }

    /**
     * Close Customer
     */
    @DELETE
    @Path("/{customerId}")
    public Response closeCustomer(
            @PathParam("customerId") Long customerId) {

        customerService.closeCustomer(customerId);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Customer closed successfully.")
                        .code(Response.Status.OK.getStatusCode())
                        .build();

        return Response.ok(response).build();
    }

}