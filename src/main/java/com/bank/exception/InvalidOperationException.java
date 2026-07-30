package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class InvalidOperationException extends BusinessException {

    public InvalidOperationException(String message) {
        super(message, Response.Status.BAD_REQUEST.getStatusCode());
    }
}