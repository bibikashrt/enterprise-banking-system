package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateEmployeeException extends BusinessException {

    public DuplicateEmployeeException(String message) {
        super(message, Response.Status.CONFLICT.getStatusCode());
    }
}