package com.pallas.communityservice.domain;

import java.util.UUID;

/** Thrown when the authenticated member is not the recipient of a connection suggestion. */
public class NotSuggestionRecipientException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotSuggestionRecipientException(UUID suggestionId) {
    super("The authenticated member is not the recipient of suggestion: " + suggestionId);
  }
}
