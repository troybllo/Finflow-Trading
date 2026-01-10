package com.finflow.portfolio.exception;

/**
 * Exception thrown when a request conflicts with existing data.
 * Common use cases: duplicate email, duplicate username, resource already exists.
 * Maps to HTTP 409 Conflict.
 */
public class ConflictException extends RuntimeException {

    /**
     * Constructs a new ConflictException with a detail message.
     *
     * @param message the detail message
     */
    public ConflictException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConflictException for a duplicate resource.
     *
     * @param resourceType the type of resource (e.g., "User", "Portfolio")
     * @param fieldName the field that has a duplicate value (e.g., "email", "username")
     * @param fieldValue the duplicate value
     */
    public ConflictException(String resourceType, String fieldName, String fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceType, fieldName, fieldValue));
    }

    /**
     * Constructs a new ConflictException with a detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
