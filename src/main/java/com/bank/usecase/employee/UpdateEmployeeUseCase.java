package com.bank.usecase.employee;

import com.bank.dao.EmployeeDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.UpdateEmployeeRequest;
import com.bank.dto.response.EmployeeResponse;
import com.bank.entity.AuditLog;
import com.bank.entity.Employee;
import com.bank.exception.DuplicateEmployeeException;
import com.bank.exception.EmployeeNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateEmployeeUseCase {

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public EmployeeResponse execute(UpdateEmployeeRequest request) {

        Employee employee = employeeDAO.findById(request.getEmployeeId());

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + request.getEmployeeId()
            );
        }

        // Check for duplicate email
        Employee existing = employeeDAO.findByEmail(request.getEmail());
        if (existing != null && !existing.getEmployeeId().equals(employee.getEmployeeId())) {
            throw new DuplicateEmployeeException(
                    "Email already exists for another employee."
            );
        }

        // Update fields
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setEmployeeRole(request.getEmployeeRole());
        employee.setEmployeeStatus(request.getEmployeeStatus());

        employeeDAO.update(employee);

        // Save audit log
        AuditLog log = AuditLog.builder()
                .action("UPDATE")
                .entityName("EMPLOYEE")
                .entityId(employee.getEmployeeId())
                .description("Employee updated successfully")
                .build();

        auditLogDAO.save(log);

        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeNumber(employee.getEmployeeNumber())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .employeeRole(employee.getEmployeeRole())
                .employeeStatus(employee.getEmployeeStatus())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}