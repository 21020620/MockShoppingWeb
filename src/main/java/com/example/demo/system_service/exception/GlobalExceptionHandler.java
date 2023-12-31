package com.example.demo.system_service.exception;

import com.example.demo.system_service.entity.ApplicationLogger;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = ApplicationLogger.getLogger();

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.warning(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder("List of constraint violations:\n");
        e.getConstraintViolations().forEach(violation -> sb.append(violation.getMessage()).append("\n"));
        logger.warning("Validation process exception");
        return ResponseEntity.badRequest().body(sb.toString());
    }

    @ExceptionHandler(value = CustomerException.class)
    public ResponseEntity<?> handleCustomerException(CustomerException e) {
        logger.warning("Customer exception");
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
