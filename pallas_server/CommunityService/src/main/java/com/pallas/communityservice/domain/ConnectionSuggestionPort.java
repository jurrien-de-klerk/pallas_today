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
   * Returns all pending suggestions in which {@code memberId} participates, either as the initiator
   * or as the target.
   *
   * @param memberId the Member Service UUID of the member
   * @return pending suggestions visible to this member
   */
  List<ConnectionSuggestion> findPendingByParticipantId(UUID memberId);

  /**
   * Update the status and responded-at timestamp of a suggestion.
   *
   * @param suggestion the suggestion with updated status
   * @return the updated suggestion
   */
  ConnectionSuggestion update(ConnectionSuggestion suggestion);
}
