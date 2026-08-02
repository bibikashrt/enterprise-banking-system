package com.bank.usecase.employee;

import com.bank.dao.EmployeeDAO;
import com.bank.dao.BranchDAO;
import com.bank.dao.AuditLogDAO;
import com.bank.dto.request.CreateEmployeeRequest;
import com.bank.dto.response.CreateEmployeeResponse;
import com.bank.entity.Employee;
import com.bank.entity.Branch;
import com.bank.entity.AuditLog;
import com.bank.enums.EmployeeStatus;
import com.bank.exception.InvalidOperationException;
import com.bank.util.EmployeeNumberGenerator;
import com.bank.util.PasswordGenerator;
import com.bank.util.PasswordUtil;
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
    public CreateEmployeeResponse execute(CreateEmployeeRequest request) {
        String temporaryPassword =
                PasswordGenerator.generate();

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
                .passwordHash(
                        PasswordUtil.hashPassword(temporaryPassword)
                )
                .passwordChanged(false)
                .employeeRole(request.getEmployeeRole())
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

        return CreateEmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeNumber(employee.getEmployeeNumber())
                .email(employee.getEmail())
                .temporaryPassword(temporaryPassword)
                .build();
    }


}