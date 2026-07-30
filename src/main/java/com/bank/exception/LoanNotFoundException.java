package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class LoanNotFoundException extends BusinessException {

    public LoanNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND.getStatusCode());
    }
}