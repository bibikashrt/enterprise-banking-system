package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class EmployeeNotFoundException extends BusinessException {

    public EmployeeNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND.getStatusCode());
    }
}