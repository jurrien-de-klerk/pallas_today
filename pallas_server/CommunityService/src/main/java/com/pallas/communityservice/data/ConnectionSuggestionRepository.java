package com.pallas.communityservice.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ConnectionSuggestionRepository extends JpaRepository<ConnectionSuggestionEntity, UUID> {

  @Query(
      "SELECT e FROM ConnectionSuggestionEntity e"
          + " WHERE (e.initiatorId = :memberId OR e.targetId = :memberId)"
          + " AND e.status = :status")
  List<ConnectionSuggestionEntity> findByParticipantAndStatus(
      @Param("memberId") UUID memberId, @Param("status") SuggestionStatusEntity status);
}
