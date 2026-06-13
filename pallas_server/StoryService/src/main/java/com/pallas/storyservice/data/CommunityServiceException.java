package com.pallas.storyservice.data;

/** Exception thrown when communication with Community Service fails. */
public class CommunityServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public CommunityServiceException(String message) {
    super(message);
  }

  public CommunityServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
