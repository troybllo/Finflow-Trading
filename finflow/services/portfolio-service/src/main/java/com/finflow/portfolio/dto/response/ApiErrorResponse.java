package com.finflow.portfolio.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Standard error response for API errors.
 * Provides consistent error format across all endpoints.
 */
public record ApiErrorResponse(
    int status,
    String error,
    String message,
    String path,
    LocalDateTime timestamp,
    List<FieldError> fieldErrors,
    Map<String, Object> details
) {
    /**
     * Field-level validation error
     */
    public record FieldError(
        String field,
        String message,
        Object rejectedValue
    ) {}

    /**
     * Create a simple error response
     */
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(
            status,
            error,
            message,
            path,
            LocalDateTime.now(),
            null,
            null
        );
    }

    /**
     * Create an error response with field validation errors
     */
    public static ApiErrorResponse withFieldErrors(
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors
    ) {
        return new ApiErrorResponse(
            status,
            error,
            message,
            path,
            LocalDateTime.now(),
            fieldErrors,
            null
        );
    }

    /**
     * Create an error response with additional details
     */
    public static ApiErrorResponse withDetails(
        int status,
        String error,
        String message,
        String path,
        Map<String, Object> details
    ) {
        return new ApiErrorResponse(
            status,
            error,
            message,
            path,
            LocalDateTime.now(),
            null,
            details
        );
    }

    // Common error factory methods

    /**
     * Create a 400 Bad Request error
     */
    public static ApiErrorResponse badRequest(String message, String path) {
        return of(400, "Bad Request", message, path);
    }

    /**
     * Create a 401 Unauthorized error
     */
    public static ApiErrorResponse unauthorized(String message, String path) {
        return of(401, "Unauthorized", message, path);
    }

    /**
     * Create a 403 Forbidden error
     */
    public static ApiErrorResponse forbidden(String message, String path) {
        return of(403, "Forbidden", message, path);
    }

    /**
     * Create a 404 Not Found error
     */
    public static ApiErrorResponse notFound(String message, String path) {
        return of(404, "Not Found", message, path);
    }

    /**
     * Create a 409 Conflict error
     */
    public static ApiErrorResponse conflict(String message, String path) {
        return of(409, "Conflict", message, path);
    }

    /**
     * Create a 500 Internal Server Error
     */
    public static ApiErrorResponse internalServerError(String message, String path) {
        return of(500, "Internal Server Error", message, path);
    }
}
