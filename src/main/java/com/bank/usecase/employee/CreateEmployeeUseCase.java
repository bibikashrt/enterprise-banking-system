package com.bank.usecase.employee;

import com.bank.dao.EmployeeDAO;
import com.bank.dao.BranchDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.CreateEmployeeRequest;
import com.bank.dto.response.EmployeeResponse;
import com.bank.entity.Employee;
import com.bank.entity.Branch;
import com.bank.entity.AuditLog;
import com.bank.enums.EmployeeStatus;
import com.bank.exception.InvalidOperationException;
import com.bank.util.EmployeeNumberGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateEmployeeUseCase {

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Transactional(rollbackOn = Exception.class)
    public EmployeeResponse execute(CreateEmployeeRequest request) {

        Branch branch = branchDAO.findById(request.getBranchId());
        if (branch == null) throw new InvalidOperationException("Branch not found");

        if (employeeDAO.findByEmail(request.getEmail()) != null)
            throw new InvalidOperationException("Employee with email exists");

        Employee employee = Employee.builder()
                .employeeNumber(EmployeeNumberGenerator.generate())
                .branchId(request.getBranchId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .employeeStatus(EmployeeStatus.ACTIVE)
                .build();

        employeeDAO.save(employee);

        AuditLog auditLog = AuditLog.builder()
                .action("CREATE")
                .entityName("EMPLOYEE")
                .entityId(employee.getEmployeeId())
                .description("Employee created successfully")
                .build();
        auditLogDAO.save(auditLog);

        return mapToResponse(employee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeNumber(employee.getEmployeeNumber())
                .branchId(employee.getBranchId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .employeeStatus(employee.getEmployeeStatus())
                .createdAt(employee.getCreatedAt())
                .build();
    }
}