package com.pallas.communityservice.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ConnectionSuggestionRepository extends JpaRepository<ConnectionSuggestionEntity, UUID> {

  List<ConnectionSuggestionEntity> findByTargetIdAndStatus(
      UUID targetId, SuggestionStatusEntity status);
}
