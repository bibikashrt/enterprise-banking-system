package com.bank.exception;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when attempting to create a duplicate record.
 */
public class DuplicateRecordException extends BusinessException {

    public DuplicateRecordException(String resource, String field) {
        super(
                String.format("%s already exists: %s", resource, field),
                Response.Status.CONFLICT.getStatusCode()
        );
    }

}