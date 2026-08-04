package com.bank.exception;

import com.bank.model.ApiResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {

        LOGGER.error("Unhandled exception occurred.", exception);

        if (exception instanceof BusinessException businessException) {

            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message(businessException.getMessage())
                    .code(businessException.getStatusCode())
                    .build();

            return Response.status(businessException.getStatusCode())
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (exception instanceof ConcurrentUpdateException) {

            LOGGER.warn(
                    "Concurrent update detected: {}",
                    exception.getMessage()
            );

            return Response.status(
                            Response.Status.CONFLICT
                    )
                    .entity(
                            ApiResponse.<Void>builder()
                                    .success(false)
                                    .message(exception.getMessage())
                                    .code(409)
                                    .build()
                    )
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("An unexpected error occurred.")
                .code(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}