package com.capg.rechargenova.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: GlobalExceptionHandler
 * DESCRIPTION:
 * Handles all exceptions globally across the application.
 * Provides meaningful and structured error responses for
 * better debugging and client communication.
 * ================================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * ================================================================
     * METHOD: handleNotificationNotFoundException
     * DESCRIPTION:
     * Handles NotificationNotFoundException when a notification
     * is not found in the database.
     * ================================================================
     */
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotificationNotFoundException(
            NotificationNotFoundException ex) {

        logger.error("Exception: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * ================================================================
     * METHOD: handleGenericException
     * DESCRIPTION:
     * Handles all other exceptions globally.
     * ================================================================
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        logger.error("Unexpected Error: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}