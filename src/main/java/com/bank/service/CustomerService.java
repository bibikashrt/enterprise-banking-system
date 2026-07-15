package com.bank.service;

import com.bank.dto.request.CreateCustomerRequest;
import com.bank.dto.request.UpdateCustomerRequest;
import com.bank.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse updateCustomer(
            Long customerId,
            UpdateCustomerRequest request);

    CustomerResponse getCustomerById(Long customerId);

    CustomerResponse getCustomerByCustomerNumber(String customerNumber);

    List<CustomerResponse> getAllCustomers();

    List<CustomerResponse> searchCustomers(String keyword);



    void closeCustomer(Long customerId);

}