package com.example.demo.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = CustomerException.class)
    public ResponseEntity<?> handleCustomerException(CustomerException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder("List of constraint violations:\n");
        e.getConstraintViolations().forEach(violation -> sb.append(violation.getMessage()).append("\n"));
        return ResponseEntity.badRequest().body(sb.toString());
    }
}
