package com.bank.service;

import com.bank.dto.request.CreateEmployeeRequest;
import com.bank.dto.request.UpdateEmployeeRequest;
import com.bank.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(
            CreateEmployeeRequest request);

    EmployeeResponse getEmployeeById(
            Long employeeId);

    EmployeeResponse getEmployeeByEmployeeNumber(
            String employeeNumber);

    List<EmployeeResponse> getEmployeesByBranch(
            Long branchId);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> searchEmployees(
            String keyword);

    EmployeeResponse updateEmployee(
            Long employeeId,
            UpdateEmployeeRequest request);

    void deactivateEmployee(
            Long employeeId);
}