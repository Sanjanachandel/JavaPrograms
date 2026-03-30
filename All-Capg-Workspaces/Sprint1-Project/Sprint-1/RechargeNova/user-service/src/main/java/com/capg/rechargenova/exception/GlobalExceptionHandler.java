package com.capg.rechargenova.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
//	@ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ErrorResponse(404, "Not Found", ex.getMessage()));
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
