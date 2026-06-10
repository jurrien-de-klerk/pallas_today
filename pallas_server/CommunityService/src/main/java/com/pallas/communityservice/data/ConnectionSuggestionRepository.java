package com.pallas.communityservice.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ConnectionSuggestionRepository extends JpaRepository<ConnectionSuggestionEntity, UUID> {

  @org.springframework.data.jpa.repository.Query(
      "SELECT e FROM ConnectionSuggestionEntity e"
          + " WHERE (e.initiatorId = :memberId OR e.targetId = :memberId)"
          + " AND e.status = :status")
  List<ConnectionSuggestionEntity> findByParticipantAndStatus(
      UUID memberId, SuggestionStatusEntity status);
}
