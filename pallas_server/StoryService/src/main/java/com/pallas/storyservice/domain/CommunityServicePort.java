package com.pallas.storyservice.domain;

import java.util.UUID;

/** Port for accessing community membership information via the Community Service. */
public interface CommunityServicePort {

  /**
   * Get the relationship type between the authenticated member and another member.
   *
   * @param memberId the Member Service UUID of the other member
   * @param bearerToken the bearer token from the authenticated request
   * @return the relationship type
   */
  RelationshipType getRelationship(UUID memberId, String bearerToken);
}
