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
 * Handles all exceptions globally across Recharge Service.
 * ================================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * ================================================================
     * Handles Recharge Not Found Exception
     * ================================================================
     */
    @ExceptionHandler(RechargeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRechargeNotFoundException(
            RechargeNotFoundException ex) {

        logger.error("Exception: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * ================================================================
     * Handles Invalid Recharge Exception
     * ================================================================
     */
    @ExceptionHandler(InvalidRechargeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRechargeException(
            InvalidRechargeException ex) {

        logger.error("Exception: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * ================================================================
     * Handles Generic Exception
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