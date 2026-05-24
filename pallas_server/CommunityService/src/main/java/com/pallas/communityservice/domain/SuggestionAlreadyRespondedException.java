package com.pallas.communityservice.domain;

import java.util.UUID;

/** Thrown when a connection suggestion has already been responded to. */
public class SuggestionAlreadyRespondedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public SuggestionAlreadyRespondedException(UUID suggestionId) {
    super("Connection suggestion has already been responded to: " + suggestionId);
  }
}
