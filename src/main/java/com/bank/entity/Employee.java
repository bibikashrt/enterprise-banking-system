package com.bank.entity;

import com.bank.enums.EmployeeRole;
import com.bank.enums.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private Long employeeId;

    private String employeeNumber;

    private Long branchId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String passwordHash;

    private Boolean passwordChanged;

    private EmployeeRole employeeRole;

    private EmployeeStatus employeeStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}