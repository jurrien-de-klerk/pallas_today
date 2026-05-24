package com.pallas.communityservice.domain;

import java.util.UUID;

/** Thrown when no connection suggestion with the given ID is found. */
public class ConnectionSuggestionNotFoundException extends RuntimeException {

  public ConnectionSuggestionNotFoundException(UUID id) {
    super("Connection suggestion not found: " + id);
  }
}
