package com.pallas.communityservice.exception;

/** Thrown when the Member Service is unavailable or returns an error. */
public class MemberServiceUnavailableException extends RuntimeException {

  public MemberServiceUnavailableException(String message) {
    super(message);
  }

  public MemberServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
