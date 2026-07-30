package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateCustomerException extends BusinessException {

    public DuplicateCustomerException(String message) {
        super(message, Response.Status.CONFLICT.getStatusCode());
    }

}