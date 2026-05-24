package com.pallas.communityservice.domain;

import java.util.UUID;

/** Thrown when a connection suggestion has already been responded to. */
public class SuggestionAlreadyRespondedException extends RuntimeException {

  public SuggestionAlreadyRespondedException(UUID suggestionId) {
    super("Connection suggestion has already been responded to: " + suggestionId);
  }
}
