package com.pallas.storyservice.exception;

import com.pallas.storyservice.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Error> handleResponseStatusException(ResponseStatusException ex) {
    Error error = new Error();
    error.setMessage(ex.getReason() != null ? ex.getReason() : ex.getMessage());
    error.setCode(ex.getStatusCode().toString());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handleValidationException(MethodArgumentNotValidException ex) {
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleGenericException(Exception ex) {
    Error error = new Error();
    error.setMessage("Internal server error");
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    error.setDetails(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
