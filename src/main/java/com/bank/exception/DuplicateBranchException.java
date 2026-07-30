package com.bank.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateBranchException extends BusinessException {

    public DuplicateBranchException(String message) {
        super(message, Response.Status.CONFLICT.getStatusCode());
    }
}