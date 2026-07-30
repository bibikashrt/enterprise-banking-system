package com.bank.dto.response;

import com.bank.enums.EmployeeRole;
import lombok.Builder;

@Builder
public record LoginResponse(

        String token,

        String tokenType,

        Long employeeId,

        String employeeNumber,

        String employeeName,

        EmployeeRole role,

        Long expiresIn

) {
}