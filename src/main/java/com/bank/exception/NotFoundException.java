package com.bank.exception;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when a requested resource is not found.
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String resource, Object identifier) {
        super(
                String.format("%s not found with identifier: %s", resource, identifier),
                Response.Status.NOT_FOUND.getStatusCode()
        );
    }

}