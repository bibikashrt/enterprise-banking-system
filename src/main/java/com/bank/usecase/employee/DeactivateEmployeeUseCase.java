package com.bank.usecase.employee;

import com.bank.dao.EmployeeDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.entity.AuditLog;
import com.bank.entity.Employee;
import com.bank.enums.EmployeeStatus;
import com.bank.exception.EmployeeNotFoundException;
import com.bank.exception.InvalidOperationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeactivateEmployeeUseCase {

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public void execute(Long employeeId) {

        Employee employee = employeeDAO.findById(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + employeeId
            );
        }

        if (employee.getEmployeeStatus() == EmployeeStatus.INACTIVE) {
            throw new InvalidOperationException("Employee is already inactive.");
        }

        employee.setEmployeeStatus(EmployeeStatus.INACTIVE);
        employeeDAO.update(employee);

        // Save audit log
        AuditLog log = AuditLog.builder()
                .action("DEACTIVATE")
                .entityName("EMPLOYEE")
                .entityId(employee.getEmployeeId())
                .description("Employee deactivated successfully")
                .build();

        auditLogDAO.save(log);
    }
}