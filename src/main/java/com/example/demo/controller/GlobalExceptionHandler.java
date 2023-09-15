package com.example.demo.controller;

import com.example.demo.entities.ApplicationLogger;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = ApplicationLogger.getLogger();
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder("List of constraint violations:\n");
        e.getConstraintViolations().forEach(violation -> sb.append(violation.getMessage()).append("\n"));
        logger.warning("Validation process exception");
        return ResponseEntity.badRequest().body(sb.toString());
    }
}
