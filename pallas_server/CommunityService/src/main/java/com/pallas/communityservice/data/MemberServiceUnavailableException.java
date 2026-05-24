package com.pallas.communityservice.data;

/** Thrown when the Member Service cannot be reached or returns an unexpected error. */
public class MemberServiceUnavailableException extends RuntimeException {

  public MemberServiceUnavailableException(String message) {
    super(message);
  }
}
