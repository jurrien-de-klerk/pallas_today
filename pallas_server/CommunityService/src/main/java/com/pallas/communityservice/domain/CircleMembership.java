package com.pallas.communityservice.domain;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Value object representing a bidirectional circle membership between two members.
 *
 * <p>The pair is stored in canonical order (idA &lt; idB as UUIDs).
 */
@Value
@Builder
public class CircleMembership {

  /** Member Service UUID of the member with the lexicographically smaller UUID. */
  UUID memberIdA;

  /** Member Service UUID of the member with the lexicographically larger UUID. */
  UUID memberIdB;

  CircleType circleType;
  OffsetDateTime memberSince;

  /**
   * Returns the UUID of the member who is the "other" member relative to {@code currentId}.
   *
   * @param currentId the UUID of the member making the request
   * @return the UUID of the other member in the membership
   */
  public UUID partnerIdFor(UUID currentId) {
    return currentId.equals(memberIdA) ? memberIdB : memberIdA;
  }
}
