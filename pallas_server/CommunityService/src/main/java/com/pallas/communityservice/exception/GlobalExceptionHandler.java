package com.pallas.communityservice.exception;

import com.pallas.communityservice.data.MemberServiceUnavailableException;
import com.pallas.communityservice.domain.ConnectionSuggestionNotFoundException;
import com.pallas.communityservice.domain.NotSuggestionRecipientException;
import com.pallas.communityservice.domain.SuggestionAlreadyRespondedException;
import com.pallas.communityservice.model.Error;
import lombok.CustomLog;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@CustomLog
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConnectionSuggestionNotFoundException.class)
  public ResponseEntity<Error> handleNotFound(ConnectionSuggestionNotFoundException ex) {
    log.warn("Connection suggestion not found");
    return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(NotSuggestionRecipientException.class)
  public ResponseEntity<Error> handleForbidden(NotSuggestionRecipientException ex) {
    log.warn("Forbidden: not suggestion recipient");
    return errorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(SuggestionAlreadyRespondedException.class)
  public ResponseEntity<Error> handleConflict(SuggestionAlreadyRespondedException ex) {
    log.warn("Conflict: suggestion already responded to");
    return errorResponse(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Error> handleDuplicateSuggestion(DataIntegrityViolationException ex) {
    log.warn("DataIntegrityViolation — likely duplicate pending suggestion");
    return errorResponse(
        HttpStatus.CONFLICT, "A pending suggestion between these members already exists");
  }

  @ExceptionHandler(MemberServiceUnavailableException.class)
  public ResponseEntity<Error> handleMemberServiceError(MemberServiceUnavailableException ex) {
    log.error("Member Service unavailable");
    log.backtrace();
    return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not reach the Member Service");
  }

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
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
    return errorResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleUnexpectedException(Exception ex) {
    log.error("Unhandled exception of type {}", ex.getClass().getName());
    log.backtrace();
    return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
  }

  private ResponseEntity<Error> errorResponse(HttpStatus status, String message) {
    Error error = new Error();
    error.setMessage(message);
    error.setCode(status.toString());
    return ResponseEntity.status(status).body(error);
  }
}
