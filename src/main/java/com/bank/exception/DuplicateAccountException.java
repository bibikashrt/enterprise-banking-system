package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateAccountException extends BusinessException {

    public DuplicateAccountException(String message) {
        super(message, Response.Status.CONFLICT.getStatusCode());
    }
}