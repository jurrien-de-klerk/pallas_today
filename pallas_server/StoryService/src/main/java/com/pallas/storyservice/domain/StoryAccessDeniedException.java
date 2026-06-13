package com.pallas.storyservice.domain;

/** Exception thrown when a story is not accessible to the requesting member. */
public class StoryAccessDeniedException extends RuntimeException {

  public StoryAccessDeniedException(String message) {
    super(message);
  }

  public StoryAccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }
}
