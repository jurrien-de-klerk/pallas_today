package com.pallas.memberservice.exception;

import com.pallas.logger.PallasBacktrace;
import com.pallas.memberservice.domain.MemberNotFoundException;
import com.pallas.memberservice.model.Error;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Error> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    log.warn("Missing required request parameter: {}", ex.getParameterName());
    Error error = new Error();
    error.setMessage("Missing required parameter: " + ex.getParameterName());
    error.setCode(HttpStatus.BAD_REQUEST.toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MemberNotFoundException.class)
  public ResponseEntity<Error> handleMemberNotFoundException(MemberNotFoundException ex) {
    // Log only the exception type — not the message — to avoid recording member identifiers
    // or identity-provider path info in log output (ADR-0007). The member ID is still
    // returned to the caller in the HTTP response body below.
    log.warn("Handling {}", ex.getClass().getSimpleName());
    Error error = new Error();
    error.setMessage(ex.getMessage());
    error.setCode(HttpStatus.NOT_FOUND.toString());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * A concurrent first-registration for the same subject violates the unique constraint on
   * keycloak_sub. Return 409 so the caller can retry; the second attempt will find the existing
   * mapping.
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Error> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    log.warn("Data integrity violation: {}", ex.getClass().getSimpleName());
    Error error = new Error();
    error.setMessage("Conflict: please retry");
    error.setCode(HttpStatus.CONFLICT.toString());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleGenericException(Exception ex) {
    // Log only the exception type — not the message or cause message — to avoid
    // recording identity data in log output (ADR-0007).
    log.error("Unhandled exception of type {}", ex.getClass().getName());
    PallasBacktrace.backtrace(log);
    Error error = new Error();
    error.setMessage("Internal server error");
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
