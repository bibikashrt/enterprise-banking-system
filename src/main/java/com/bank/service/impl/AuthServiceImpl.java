package com.bank.service.impl;

import com.bank.config.JwtProperties;
import com.bank.dao.AuthDAO;
import com.bank.dto.request.ChangePasswordRequest;
import com.bank.dto.request.LoginRequest;
import com.bank.dto.response.LoginResponse;
import com.bank.entity.Employee;
import com.bank.enums.EmployeeStatus;
import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidOperationException;
import com.bank.service.AuthService;
import com.bank.security.JwtUtil;
import com.bank.usecase.employee.ChangePasswordUseCase;
import com.bank.util.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    private AuthDAO authDAO;

    @Inject
    private JwtUtil jwtUtil;

    @Inject
    private JwtProperties jwtProperties;

    @Inject
    private ChangePasswordUseCase changePasswordUseCase;

    @Override
    public LoginResponse login(LoginRequest request) {

        Employee employee = authDAO.findByEmail(request.email());

        if (employee == null) {
            throw new AuthenticationException("Invalid email or password.");
        }

        boolean validPassword = PasswordUtil.verifyPassword(
                request.password(),
                employee.getPasswordHash()
        );

        if (!validPassword) {
            throw new AuthenticationException("Invalid email or password.");
        }

        if (employee.getEmployeeStatus() != EmployeeStatus.ACTIVE) {
            throw new InvalidOperationException(
                      "Employee account is inactive.");
        }


        String token = jwtUtil.generateToken(employee);

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .employeeId(employee.getEmployeeId())
                .employeeNumber(employee.getEmployeeNumber())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .role(employee.getEmployeeRole())
                .expiresIn(jwtProperties.getExpiration() / 1000)
                .forcePasswordChange(Boolean.FALSE.equals(employee.getPasswordChanged()))
                .build();
    }

    @Override
    public void changePassword(
            Long employeeId,
            ChangePasswordRequest request
    ) {

        changePasswordUseCase.execute(
                employeeId,
                request
        );
    }
}