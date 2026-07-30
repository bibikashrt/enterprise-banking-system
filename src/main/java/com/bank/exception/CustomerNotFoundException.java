package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class CustomerNotFoundException extends BusinessException {

    public CustomerNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND.getStatusCode());
    }

}