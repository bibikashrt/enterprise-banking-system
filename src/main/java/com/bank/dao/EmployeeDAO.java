package com.bank.dao;

import com.bank.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    int save(Employee employee);

    Employee findById(Long employeeId);

    Employee findByEmployeeNumber(String employeeNumber);

    Employee findByEmail(String email);

    List<Employee> findByBranchId(Long branchId);

    List<Employee> findAll();

    List<Employee> search(String keyword);

    int update(Employee employee);

    int deactivate(Long employeeId);

    void changePassword(Long employeeId,String passwordHash);
}