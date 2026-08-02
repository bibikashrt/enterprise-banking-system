package com.bank.service;

import com.bank.dto.request.ChangePasswordRequest;
import com.bank.dto.request.LoginRequest;
import com.bank.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void changePassword(
            Long employeeId,
            ChangePasswordRequest request
    );

}