package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND.getStatusCode());
    }
}