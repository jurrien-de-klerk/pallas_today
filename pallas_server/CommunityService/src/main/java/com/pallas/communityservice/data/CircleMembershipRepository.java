package com.pallas.communityservice.data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface CircleMembershipRepository
    extends JpaRepository<CircleMembershipEntity, CircleMembershipId> {

  @Query(
      "SELECT m FROM CircleMembershipEntity m "
          + "WHERE (m.id.memberIdA = :id OR m.id.memberIdB = :id) "
          + "AND m.circleType = :circleType")
  List<CircleMembershipEntity> findAllByMemberIdAndCircleType(
      @Param("id") UUID id, @Param("circleType") CircleTypeEntity circleType);

  @Query(
      "SELECT m FROM CircleMembershipEntity m "
          + "WHERE (m.id.memberIdA = :idA AND m.id.memberIdB = :idB) "
          + "   OR (m.id.memberIdA = :idB AND m.id.memberIdB = :idA)")
  Optional<CircleMembershipEntity> findByEitherPair(@Param("idA") UUID idA, @Param("idB") UUID idB);
}
