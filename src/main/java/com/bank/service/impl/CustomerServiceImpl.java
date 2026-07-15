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
    private CustomerDAO customerDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer with citizenship number: {}",
                request.getCitizenshipNumber());

        // Check duplicate citizenship number
        if (customerDAO.findByCitizenshipNumber(request.getCitizenshipNumber()) != null) {
            throw new DuplicateCustomerException("Citizenship number already exists.");
        }

        // Check duplicate PAN
        if (request.getPanNumber() != null &&
                customerDAO.findByPanNumber(request.getPanNumber()) != null) {

            throw new DuplicateCustomerException("PAN number already exists.");
        }

        // Check duplicate email
        if (customerDAO.findByEmail(request.getEmail()) != null) {
            throw new DuplicateCustomerException("Email already exists.");
        }

        Customer customer = Customer.builder()
                .customerNumber(CustomerNumberGenerator.generate())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .citizenshipNumber(request.getCitizenshipNumber())
                .panNumber(request.getPanNumber())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .address(request.getAddress())
                .customerStatus(CustomerStatus.ACTIVE)
                .build();

        customerDAO.save(customer);

        AuditLog auditLog = AuditLog.builder()
                .action("CREATE")
                .entityName("CUSTOMER")
                .entityId(customer.getCustomerId())
                .description("Customer created successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info("Customer created successfully. Customer Number: {}",
                customer.getCustomerNumber());

        return mapToResponse(customer);
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
    @Transactional(rollbackOn = Exception.class)
    public CustomerResponse updateCustomer(Long customerId,
                                           UpdateCustomerRequest request) {

        log.info("Updating customer with ID: {}", customerId);

        Customer customer = getCustomerOrThrow(customerId);

        validateDuplicateCustomer(customer, request);

        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setCitizenshipNumber(request.getCitizenshipNumber());
        customer.setPanNumber(request.getPanNumber());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setAddress(request.getAddress());

        customerDAO.update(customer);

        log.info("Customer updated successfully. Customer ID: {}", customerId);

        return mapToResponse(customer);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void closeCustomer(Long customerId) {

        log.info("Closing customer with ID: {}", customerId);

        Customer customer = getCustomerOrThrow(customerId);

        customerDAO.closeCustomer(customerId);

        log.info("Customer closed successfully. Customer ID: {}", customerId);
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

    /**
     * Validates duplicate customer information.
     *
     * @param customer Existing customer
     * @param request Customer request
     */
    private void validateDuplicateCustomer(
            Customer customer,
            UpdateCustomerRequest request) {

        // Validate Citizenship Number
        if (!customer.getCitizenshipNumber().equals(request.getCitizenshipNumber())) {

            Customer existingCustomer =
                    customerDAO.findByCitizenshipNumber(request.getCitizenshipNumber());

            if (existingCustomer != null) {
                throw new DuplicateCustomerException(
                        "Citizenship number already exists.");
            }
        }

        // Validate PAN Number
        if (request.getPanNumber() != null
                && !request.getPanNumber().equals(customer.getPanNumber())) {

            Customer existingCustomer =
                    customerDAO.findByPanNumber(request.getPanNumber());

            if (existingCustomer != null) {
                throw new DuplicateCustomerException(
                        "PAN number already exists.");
            }
        }

        // Validate Email
        if (!customer.getEmail().equals(request.getEmail())) {

            Customer existingCustomer =
                    customerDAO.findByEmail(request.getEmail());

            if (existingCustomer != null) {
                throw new DuplicateCustomerException(
                        "Email already exists.");
            }
        }
    }

    /**
     * Returns customer if found otherwise throws exception.
     */
    private Customer getCustomerOrThrow(Long customerId) {

        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID : " + customerId);
        }

        return customer;
    }



}