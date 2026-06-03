package com.pallas.communityservice.exception;

import com.pallas.communityservice.data.MemberServiceUnavailableException;
import com.pallas.communityservice.model.Error;
import lombok.CustomLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

/** Handles generic web and infrastructure exceptions. */
@CustomLog
@RestControllerAdvice
public final class GlobalExceptionHandler {

  @ExceptionHandler(MemberServiceUnavailableException.class)
  public ResponseEntity<Error> handleMemberServiceError(MemberServiceUnavailableException ex) {
    log.error("Member Service unavailable");
    log.backtrace();
    return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not reach the Member Service");
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Error> handleResponseStatusException(ResponseStatusException ex) {
    if (log.isWarnEnabled()) {
      log.warn("Request failed with status {}", ex.getStatusCode());
    }
    Error error = new Error();
    error.setMessage(ex.getReason() != null ? ex.getReason() : ex.getMessage());
    error.setCode(ex.getStatusCode().toString());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handleValidationException(MethodArgumentNotValidException ex) {
    if (log.isWarnEnabled()) {
      log.warn(
          "Request validation failed: {} field error(s)", ex.getBindingResult().getErrorCount());
    }
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
    return errorResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Error> handleIllegalArgument(IllegalArgumentException ex) {
    if (log.isWarnEnabled()) {
      log.warn("Illegal argument: {}", ex.getMessage());
    }
    return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Error> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    if (log.isWarnEnabled()) {
      log.warn("Invalid path parameter: {}", ex.getValue());
    }
    return errorResponse(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + ex.getValue());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleUnexpectedException(Exception ex) {
    if (log.isErrorEnabled()) {
      log.error("Unhandled exception of type {}", ex.getClass().getName());
      log.backtrace();
    }
    return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
  }

  private ResponseEntity<Error> errorResponse(HttpStatus status, String message) {
    Error error = new Error();
    error.setMessage(message);
    error.setCode(status.toString());
    return ResponseEntity.status(status).body(error);
  }
}
