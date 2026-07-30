package com.bank.usecase.customer;

import com.bank.dao.CustomerDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.response.CustomerResponse;
import com.bank.entity.Customer;
import com.bank.entity.AuditLog;
import com.bank.exception.CustomerNotFoundException;
import com.bank.enums.CustomerStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CloseCustomerUseCase {

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public void execute(Long customerId) {

        log.info("Closing customer with ID: {}", customerId);

        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        customerDAO.closeCustomer(customerId);

        AuditLog auditLog = AuditLog.builder()
                .action("CLOSE")
                .entityName("CUSTOMER")
                .entityId(customer.getCustomerId())
                .description("Customer closed successfully.")
                .build();

        auditLogDAO.save(auditLog);

        log.info("Customer closed successfully. Customer ID: {}", customerId);
    }
}