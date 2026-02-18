package org.acme.adapters.in.rest;

/**
 * Standard error response format for REST API.
 * Used for consistent error messages across all endpoints.
 */
public class ErrorResponse {
    public String errorCode;
    public String message;
    public long timestamp;

    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters for JSON serialization
    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

