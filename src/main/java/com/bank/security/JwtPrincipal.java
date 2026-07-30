package com.bank.security;

import com.bank.enums.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

@Getter
@AllArgsConstructor
public class JwtPrincipal implements Principal {

    private final Long employeeId;

    private final String employeeNumber;

    private final String email;

    private final EmployeeRole role;

    @Override
    public String getName() {
        return email;
    }
}