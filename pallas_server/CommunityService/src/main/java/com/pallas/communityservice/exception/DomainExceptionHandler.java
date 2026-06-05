package com.pallas.communityservice.exception;

import com.pallas.communityservice.domain.ConnectionSuggestionNotFoundException;
import com.pallas.communityservice.domain.NotSuggestionRecipientException;
import com.pallas.communityservice.domain.SuggestionAlreadyRespondedException;
import com.pallas.communityservice.model.Error;
import lombok.CustomLog;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Handles domain-layer exceptions. */
@CustomLog
@RestControllerAdvice
public final class DomainExceptionHandler {

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

  private ResponseEntity<Error> errorResponse(HttpStatus status, String message) {
    Error error = new Error();
    error.setMessage(message);
    error.setCode(status.toString());
    return ResponseEntity.status(status).body(error);
  }
}
