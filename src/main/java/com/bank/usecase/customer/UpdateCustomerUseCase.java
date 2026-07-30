package com.bank.usecase.customer;

import com.bank.dao.CustomerDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.UpdateCustomerRequest;
import com.bank.dto.response.CustomerResponse;
import com.bank.entity.Customer;
import com.bank.entity.AuditLog;
import com.bank.enums.CustomerStatus;
import com.bank.exception.CustomerNotFoundException;
import com.bank.exception.DuplicateCustomerException;
import com.bank.usecase.UseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class UpdateCustomerUseCase implements UseCase<CustomerResponse, UpdateCustomerRequest> {

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerResponse execute(UpdateCustomerRequest request) {

        log.info("Executing update for customer with email: {}", request.getEmail());

        // Fetch the existing customer by ID or throw exception
        Customer customer = customerDAO.findById(request.getCustomerId());
        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: " + request.getCustomerId()
            );
        }

        // Validate duplicates
        validateDuplicateCustomer(customer, request);

        // Update fields
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

        // Persist
        customerDAO.update(customer);

        // Audit log
        AuditLog auditLog = AuditLog.builder()
                .action("UPDATE")
                .entityName("CUSTOMER")
                .entityId(customer.getCustomerId())
                .description("Customer updated successfully")
                .build();
        auditLogDAO.save(auditLog);

        log.info("Customer updated successfully. Customer ID: {}", customer.getCustomerId());

        // Return response
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

    private void validateDuplicateCustomer(Customer customer, UpdateCustomerRequest request) {
        // Citizenship
        if (!customer.getCitizenshipNumber().equals(request.getCitizenshipNumber())) {
            Customer existing = customerDAO.findByCitizenshipNumber(request.getCitizenshipNumber());
            if (existing != null && !existing.getCustomerId().equals(customer.getCustomerId())) {
                throw new DuplicateCustomerException("Citizenship number already exists.");
            }
        }

        // PAN
        if (request.getPanNumber() != null && !request.getPanNumber().equals(customer.getPanNumber())) {
            Customer existing = customerDAO.findByPanNumber(request.getPanNumber());
            if (existing != null && !existing.getCustomerId().equals(customer.getCustomerId())) {
                throw new DuplicateCustomerException("PAN number already exists.");
            }
        }

        // Email
        if (!customer.getEmail().equals(request.getEmail())) {
            Customer existing = customerDAO.findByEmail(request.getEmail());
            if (existing != null && !existing.getCustomerId().equals(customer.getCustomerId())) {
                throw new DuplicateCustomerException("Email already exists.");
            }
        }
    }
}