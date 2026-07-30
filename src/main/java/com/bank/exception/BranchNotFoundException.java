package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class BranchNotFoundException extends BusinessException {

    public BranchNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND.getStatusCode());
    }
}