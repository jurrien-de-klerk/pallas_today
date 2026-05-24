package com.pallas.communityservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Port for persisting and querying connection suggestions. */
public interface ConnectionSuggestionPort {

  /**
   * Persist a new connection suggestion.
   *
   * @param suggestion the suggestion to save
   * @return the saved suggestion (id and timestamps populated)
   */
  ConnectionSuggestion save(ConnectionSuggestion suggestion);

  /**
   * Find a suggestion by its unique ID.
   *
   * @param id the suggestion UUID
   * @return the suggestion, or empty if not found
   */
  Optional<ConnectionSuggestion> findById(UUID id);

  /**
   * Returns all pending suggestions whose recipient is {@code targetId}.
   *
   * @param targetId the Member Service UUID of the intended recipient
   * @return incoming pending suggestions
   */
  List<ConnectionSuggestion> findPendingByTargetId(UUID targetId);

  /**
   * Update the status and responded-at timestamp of a suggestion.
   *
   * @param suggestion the suggestion with updated status
   * @return the updated suggestion
   */
  ConnectionSuggestion update(ConnectionSuggestion suggestion);
}
