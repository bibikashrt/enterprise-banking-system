package com.bank.dto.response;

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
public class EmployeeResponse {

    private Long employeeId;

    private String employeeNumber;

    private Long branchId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private EmployeeRole employeeRole;

    private EmployeeStatus employeeStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}