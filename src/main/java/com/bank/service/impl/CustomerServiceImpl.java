package com.bank.service.impl;

import com.bank.dao.CustomerDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.CreateCustomerRequest;
import com.bank.dto.request.UpdateCustomerRequest;
import com.bank.dto.response.CustomerResponse;
import com.bank.entity.Customer;
import com.bank.entity.AuditLog;
import com.bank.enums.CustomerStatus;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.DuplicateCustomerException;
import com.bank.service.CustomerService;
import com.bank.usecase.customer.CloseCustomerUseCase;
import com.bank.usecase.customer.CreateCustomerUseCase;
import com.bank.usecase.customer.UpdateCustomerUseCase;
import com.bank.util.CustomerNumberGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;


@Slf4j
@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {

    @Inject
    private CreateCustomerUseCase createCustomerUseCase;

    @Inject
    private UpdateCustomerUseCase updateCustomerUseCase;

    @Inject
    private CloseCustomerUseCase closeCustomerUseCase;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        return createCustomerUseCase.execute(request);
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {

        log.info("Fetching customer with ID: {}", customerId);

        Customer customer = getCustomerOrThrow(customerId);

        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByCustomerNumber(String customerNumber) {

        log.info("Fetching customer with customer number: {}", customerNumber);

        Customer customer = customerDAO.findByCustomerNumber(customerNumber);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with customer number : " + customerNumber);
        }

        return mapToResponse(customer);

    }

    @Override
    public List<CustomerResponse> getAllCustomers() {

        log.info("Fetching all customers.");

        List<Customer> customers = customerDAO.findAll();

        List<CustomerResponse> responses = new ArrayList<>();

        for (Customer customer : customers) {
            responses.add(mapToResponse(customer));
        }

        log.info("Total customers found: {}", responses.size());

        return responses;
    }

    @Override
    public List<CustomerResponse> searchCustomers(String keyword) {

        log.info("Searching customers with keyword: {}", keyword);

        List<Customer> customers = customerDAO.search(keyword);

        List<CustomerResponse> responses = new ArrayList<>();

        for (Customer customer : customers) {
            responses.add(mapToResponse(customer));
        }

        log.info("Total customers found: {}", responses.size());

        return responses;
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        request.setCustomerId(customerId); // Pass the ID to the use case
        return updateCustomerUseCase.execute(request);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void closeCustomer(Long customerId) {
        closeCustomerUseCase.execute(customerId);
    }

    private CustomerResponse mapToResponse(Customer customer) {

        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .customerNumber(customer.getCustomerNumber())
                .firstName(customer.getFirstName())
                .middleName(customer.getMiddleName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .citizenshipNumber(customer.getCitizenshipNumber())
                .panNumber(customer.getPanNumber())
                .email(customer.getEmail())
                .mobileNumber(customer.getMobileNumber())
                .address(customer.getAddress())
                .customerStatus(customer.getCustomerStatus())
                .createdAt(customer.getCreatedAt())
                .build();
    }

    private Customer getCustomerOrThrow(Long customerId) {

        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID : " + customerId);
        }

        return customer;
    }



}