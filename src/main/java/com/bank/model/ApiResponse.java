package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Standard API response wrapper used across the application.
 *
 * @param <T> Response payload type.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * Indicates whether the request was successful.
     */
    private boolean success;

    /**
     * Response message.
     */
    private String message;

    /**
     * HTTP status code.
     */
    private int code;

    /**
     * Response timestamp.
     */
    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();

    /**
     * Response payload.
     */
    private T data;

}