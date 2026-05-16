package com.pallas.memberservice.exception;

import com.pallas.logger.PallasBacktrace;
import com.pallas.memberservice.domain.MemberNotFoundException;
import com.pallas.memberservice.model.Error;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Error> handleResponseStatusException(ResponseStatusException ex) {
    log.warn("Request failed with status {}", ex.getStatusCode());
    Error error = new Error();
    error.setMessage(ex.getReason() != null ? ex.getReason() : ex.getMessage());
    error.setCode(ex.getStatusCode().toString());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handleValidationException(MethodArgumentNotValidException ex) {
    log.warn("Request validation failed: {} field error(s)", ex.getBindingResult().getErrorCount());
    Error error = new Error();
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
    error.setMessage(message);
    error.setCode(HttpStatus.BAD_REQUEST.toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MemberNotFoundException.class)
  public ResponseEntity<Error> handleMemberNotFoundException(MemberNotFoundException ex) {
    log.warn("Member not found: {}", ex.getMessage());
    Error error = new Error();
    error.setMessage(ex.getMessage());
    error.setCode(HttpStatus.NOT_FOUND.toString());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleGenericException(Exception ex) {
    log.error("Unhandled exception of type {}: {}", ex.getClass().getName(), ex.getMessage());
    Throwable cause = ex.getCause();
    if (cause != null) {
      log.error("Caused by {}: {}", cause.getClass().getName(), cause.getMessage());
    }
    PallasBacktrace.backtrace(log);
    Error error = new Error();
    error.setMessage("Internal server error");
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
