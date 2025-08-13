package com.rattrapage.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ApiError(int status, String error, String message, String path) {}

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> conflict(IllegalStateException ex, HttpServletRequest req) {
        return ResponseEntity.status(409)
                .body(new ApiError(409, "Conflict", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(new ApiError(400, "Bad Request", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiError> forbidden(SecurityException ex, HttpServletRequest req) {
        return ResponseEntity.status(403)
                .body(new ApiError(403, "Forbidden", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> notFound(NoSuchElementException ex, HttpServletRequest req) {
        return ResponseEntity.status(404)
                .body(new ApiError(404, "Not Found", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("Validation error");
        return ResponseEntity.badRequest()
                .body(new ApiError(400, "Validation Error", msg, req.getRequestURI()));
    }
}
