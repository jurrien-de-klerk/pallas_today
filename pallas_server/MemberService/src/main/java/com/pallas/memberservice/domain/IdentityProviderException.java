package com.pallas.memberservice.domain;

/**
 * Thrown when the identity provider returns an unexpected response that indicates a data
 * consistency failure. This is distinct from {@link MemberNotFoundException}, which signals a
 * normal not-found outcome. This exception triggers error-level logging with a backtrace.
 */
public class IdentityProviderException extends RuntimeException {

  public IdentityProviderException(String message) {
    super(message);
  }
}
