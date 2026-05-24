package com.pallas.communityservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Port for persisting and querying circle memberships. */
public interface CircleMembershipPort {

  /**
   * Persist a circle membership. If a membership between the pair already exists, the circle type
   * is updated (upsert semantics).
   *
   * @param membership the membership to save or update
   * @return the persisted membership
   */
  CircleMembership save(CircleMembership membership);

  /**
   * Return all circle memberships of a specific type where the given UUID appears as one of the two
   * parties.
   *
   * @param memberId the Member Service UUID of the member
   * @param circleType the type of circle to filter by
   * @return all memberships of the specified type involving this member
   */
  List<CircleMembership> findAllByMemberIdAndCircleType(UUID memberId, CircleType circleType);

  /**
   * Return the circle membership between two members if one exists.
   *
   * @param idA one member's UUID
   * @param idB the other member's UUID
   * @return the membership, or empty if the pair is not in a circle together
   */
  Optional<CircleMembership> findByPair(UUID idA, UUID idB);
}
