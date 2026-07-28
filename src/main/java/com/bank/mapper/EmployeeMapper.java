package com.bank.mapper;

import com.bank.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {

    int insert(Employee employee);

    Employee findById(
            @Param("employeeId") Long employeeId);

    Employee findByEmployeeNumber(
            @Param("employeeNumber") String employeeNumber);

    Employee findByEmail(
            @Param("email") String email);

    List<Employee> findByBranchId(
            @Param("branchId") Long branchId);

    List<Employee> findAll();

    List<Employee> search(
            @Param("keyword") String keyword);

    int update(Employee employee);

    int deactivate(
            @Param("employeeId") Long employeeId);
}