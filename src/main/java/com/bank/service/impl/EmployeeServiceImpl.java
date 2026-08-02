package com.bank.service.impl;

import com.bank.dao.AuditLogDAO;
import com.bank.dao.BranchDAO;
import com.bank.dao.EmployeeDAO;
import com.bank.dto.request.CreateEmployeeRequest;
import com.bank.dto.request.UpdateEmployeeRequest;
import com.bank.dto.response.CreateEmployeeResponse;
import com.bank.dto.response.EmployeeResponse;
import com.bank.entity.Branch;
import com.bank.entity.Employee;
import com.bank.exception.BranchNotFoundException;
import com.bank.exception.EmployeeNotFoundException;
import com.bank.exception.InvalidOperationException;
import com.bank.service.EmployeeService;
import com.bank.usecase.employee.CreateEmployeeUseCase;
import com.bank.usecase.employee.DeactivateEmployeeUseCase;
import com.bank.usecase.employee.UpdateEmployeeUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class EmployeeServiceImpl implements EmployeeService {

    @Inject
    private CreateEmployeeUseCase createEmployeeUseCase;

    @Inject
    private UpdateEmployeeUseCase updateEmployeeUseCase;

    @Inject
    private DeactivateEmployeeUseCase deactivateEmployeeUseCase;

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private BranchDAO branchDAO;

    @Inject
    private AuditLogDAO auditLogDAO;

    @Override
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {
        return createEmployeeUseCase.execute(request);
    }

    @Override
    public EmployeeResponse getEmployeeById(
            Long employeeId) {

        log.info("Fetching employee with ID: {}", employeeId);

        Employee employee =
                employeeDAO.findById(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + employeeId);
        }

        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByEmployeeNumber(
            String employeeNumber) {

        log.info("Fetching employee with number: {}",
                employeeNumber);

        Employee employee =
                employeeDAO.findByEmployeeNumber(employeeNumber);

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "Employee not found with number: "
                            + employeeNumber);
        }

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getEmployeesByBranch(
            Long branchId) {

        log.info("Fetching employees for branch ID: {}", branchId);

        Branch branch = branchDAO.findById(branchId);

        if (branch == null) {
            throw new BranchNotFoundException(
                    "Branch not found with ID: " + branchId);
        }

        List<Employee> employees =
                employeeDAO.findByBranchId(branchId);

        List<EmployeeResponse> responses = new ArrayList<>();

        for (Employee employee : employees) {
            responses.add(mapToResponse(employee));
        }

        log.info("Total employees found: {}", responses.size());

        return responses;
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        log.info("Fetching all employees.");

        List<Employee> employees =
                employeeDAO.findAll();

        List<EmployeeResponse> responses = new ArrayList<>();

        for (Employee employee : employees) {
            responses.add(mapToResponse(employee));
        }

        log.info("Total employees found: {}", responses.size());

        return responses;
    }

    @Override
    public List<EmployeeResponse> searchEmployees(
            String keyword) {

        log.info("Searching employees with keyword: {}", keyword);

        if (keyword == null || keyword.isBlank()) {
            throw new InvalidOperationException(
                    "Search keyword is required.");
        }

        List<Employee> employees =
                employeeDAO.search(keyword);

        List<EmployeeResponse> responses = new ArrayList<>();

        for (Employee employee : employees) {
            responses.add(mapToResponse(employee));
        }

        log.info("Total employees found: {}", responses.size());

        return responses;
    }


    @Override
    public EmployeeResponse updateEmployee(Long employeeId, UpdateEmployeeRequest request) {
        request.setEmployeeId(employeeId);
        return updateEmployeeUseCase.execute(request);
    }

    @Override
    public void deactivateEmployee(Long employeeId) {
        deactivateEmployeeUseCase.execute(employeeId);
    }

    private EmployeeResponse mapToResponse(
            Employee employee) {

        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeNumber(employee.getEmployeeNumber())
                .branchId(employee.getBranchId())
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