package com.bank.usecase.customer;

import com.bank.dao.CustomerDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.CreateCustomerRequest;
import com.bank.dto.response.CustomerResponse;
import com.bank.entity.Customer;
import com.bank.entity.AuditLog;
import com.bank.enums.CustomerStatus;
import com.bank.exception.DuplicateCustomerException;
import com.bank.util.CustomerNumberGenerator;
import com.bank.usecase.UseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateCustomerUseCase implements UseCase<CustomerResponse, CreateCustomerRequest> {

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerResponse execute(CreateCustomerRequest request) {

        // Duplicate checks
        if (customerDAO.findByCitizenshipNumber(request.getCitizenshipNumber()) != null)
            throw new DuplicateCustomerException("Citizenship number already exists.");
        if (request.getPanNumber() != null && customerDAO.findByPanNumber(request.getPanNumber()) != null)
            throw new DuplicateCustomerException("PAN number already exists.");
        if (customerDAO.findByEmail(request.getEmail()) != null)
            throw new DuplicateCustomerException("Email already exists.");

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

        return mapToResponse(customer);
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
}