package com.finflow.portfolio.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * Maps to HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a detail message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException for a specific resource type and ID.
     *
     * @param resourceType the type of resource (e.g., "User", "Portfolio")
     * @param id the ID of the resource that wasn't found
     */
    public ResourceNotFoundException(String resourceType, String id) {
        super(String.format("%s not found with id: %s", resourceType, id));
    }

    /**
     * Constructs a new ResourceNotFoundException for a specific resource with field criteria.
     *
     * @param resourceType the type of resource (e.g., "User", "Portfolio")
     * @param fieldName the field name used in the search (e.g., "email", "username")
     * @param fieldValue the value that wasn't found
     */
    public ResourceNotFoundException(String resourceType, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: %s", resourceType, fieldName, fieldValue));
    }

    /**
     * Constructs a new ResourceNotFoundException with a detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
