package com.pallas.communityservice.domain;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Aggregate root representing a connection suggestion sent from one member to another.
 *
 * <p>All member identities are Member Service UUIDs. The CommunityService never stores Keycloak sub
 * claims; those are translated to UUIDs by the API layer using the Member Service.
 */
@Value
@Builder
public class ConnectionSuggestion {

  UUID id;

  /** Member Service UUID of the member who started the suggestion. */
  UUID initiatorId;

  /** Member Service UUID of the member who received the suggestion. */
  UUID targetId;

  CircleType targetCircle;
  SuggestionStatus status;
  OffsetDateTime createdAt;
  OffsetDateTime respondedAt;

  /** Returns {@code true} when the suggestion is still awaiting a response. */
  public boolean isPending() {
    return status == SuggestionStatus.PENDING;
  }
}
