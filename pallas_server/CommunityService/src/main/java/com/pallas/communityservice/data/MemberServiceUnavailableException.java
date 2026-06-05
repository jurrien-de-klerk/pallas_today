package com.pallas.communityservice.data;

/** Thrown when the Member Service cannot be reached or returns an unexpected error. */
public class MemberServiceUnavailableException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MemberServiceUnavailableException(String message) {
    super(message);
  }

  public MemberServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
