package com.electronicstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public abstract class BaseController {

    protected <T> ResponseEntity<Map<String, Object>> createSuccessResponse(T data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }

    protected ResponseEntity<Map<String, Object>> createErrorResponse(String error, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        return new ResponseEntity<>(response, status);
    }

    protected void checkManagerRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Authentication required");
        }

        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));
        if (!isManager) {
            throw new SecurityException("Only a manager can perform this operation.");
        }
    }

    protected ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        if (e instanceof SecurityException) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } else if (e instanceof IllegalArgumentException) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            System.out.println("Unexpected exception: " + e.getMessage());
            response.put("error", "An unexpected error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
