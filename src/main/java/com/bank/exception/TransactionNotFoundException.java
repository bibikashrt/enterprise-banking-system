package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class TransactionNotFoundException extends BusinessException {

    public TransactionNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND.getStatusCode());
    }
}