package com.epr.error;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ApiResponse {

    private ApiResponse() {
        // Prevent instantiation
    }

    /**
     * Returns a standardized error response map
     */
    public static Map<String, Object> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Returns a standardized error response with custom HTTP status code
     */
    public static Map<String, Object> error(String message, int statusCode) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("message", message);
        error.put("status", statusCode);
        error.put("timestamp", Instant.now().toString());
        return error;
    }

    /**
     * Returns a standardized success response (optional - for consistency)
     */
    public static Map<String, Object> success(Object data) {
        return success(data, "Success");
    }

    public static Map<String, Object> success(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", false);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", Instant.now().toString());
        return response;
    }
}