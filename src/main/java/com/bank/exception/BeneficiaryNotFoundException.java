package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class BeneficiaryNotFoundException extends BusinessException {

    public BeneficiaryNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND.getStatusCode());
    }
}