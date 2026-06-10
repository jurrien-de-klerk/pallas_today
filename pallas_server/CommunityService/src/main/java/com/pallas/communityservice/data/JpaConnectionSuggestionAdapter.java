package com.pallas.communityservice.data;

import com.pallas.communityservice.domain.CircleType;
import com.pallas.communityservice.domain.ConnectionSuggestion;
import com.pallas.communityservice.domain.ConnectionSuggestionPort;
import com.pallas.communityservice.domain.SuggestionStatus;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

/** JPA adapter implementing {@link ConnectionSuggestionPort}. */
@CustomLog
@Component
public class JpaConnectionSuggestionAdapter implements ConnectionSuggestionPort {

  private final ConnectionSuggestionRepository repository;

  public JpaConnectionSuggestionAdapter(ConnectionSuggestionRepository repository) {
    this.repository = repository;
  }

  @Override
  public ConnectionSuggestion save(ConnectionSuggestion suggestion) {
    log.debug("save: persisting new connection suggestion");
    ConnectionSuggestionEntity entity = toEntity(suggestion);
    entity.setCreatedAt(OffsetDateTime.now());
    ConnectionSuggestion saved = toDomain(repository.save(entity));
    log.debug("save: connection suggestion persisted");
    return saved;
  }

  @Override
  public Optional<ConnectionSuggestion> findById(UUID id) {
    log.debug("findById: querying connection suggestion");
    Optional<ConnectionSuggestion> result = repository.findById(id).map(this::toDomain);
    log.debug("findById: suggestion {}", result.isPresent() ? "found" : "not found");
    return result;
  }

  @Override
  public List<ConnectionSuggestion> findPendingByParticipantId(UUID memberId) {
    log.debug("findPendingByParticipantId: querying pending suggestions");
    List<ConnectionSuggestion> results =
        repository.findByParticipantAndStatus(memberId, SuggestionStatusEntity.PENDING).stream()
            .map(this::toDomain)
            .toList();
    log.debug("findPendingByParticipantId");
    return results;
  }

  @Override
  public ConnectionSuggestion update(ConnectionSuggestion suggestion) {
    log.debug("update: loading suggestion for update");
    ConnectionSuggestionEntity entity =
        repository
            .findById(suggestion.getId())
            .orElseThrow(
                () -> {
                  log.debug("update: suggestion not found — illegal state");
                  return new IllegalStateException(
                      "Cannot update non-existent suggestion: " + suggestion.getId());
                });
    log.debug("update: applying new status {}", suggestion.getStatus());
    entity.setStatus(toEntityStatus(suggestion.getStatus()));
    entity.setRespondedAt(suggestion.getRespondedAt());
    ConnectionSuggestion updated = toDomain(repository.save(entity));
    log.debug("update: suggestion updated");
    return updated;
  }

  // -------------------------------------------------------------------------
  // Mapping helpers
  // -------------------------------------------------------------------------

  private ConnectionSuggestionEntity toEntity(ConnectionSuggestion domain) {
    ConnectionSuggestionEntity entity = new ConnectionSuggestionEntity();
    entity.setId(domain.getId());
    entity.setInitiatorId(domain.getInitiatorId());
    entity.setTargetId(domain.getTargetId());
    entity.setTargetCircle(toEntityCircleType(domain.getTargetCircle()));
    entity.setStatus(toEntityStatus(domain.getStatus()));
    entity.setCreatedAt(domain.getCreatedAt());
    entity.setRespondedAt(domain.getRespondedAt());
    return entity;
  }

  private ConnectionSuggestion toDomain(ConnectionSuggestionEntity entity) {
    return ConnectionSuggestion.builder()
        .id(entity.getId())
        .initiatorId(entity.getInitiatorId())
        .targetId(entity.getTargetId())
        .targetCircle(toDomainCircleType(entity.getTargetCircle()))
        .status(toDomainStatus(entity.getStatus()))
        .createdAt(entity.getCreatedAt())
        .respondedAt(entity.getRespondedAt())
        .build();
  }

  private CircleTypeEntity toEntityCircleType(CircleType domain) {
    return switch (domain) {
      case TRUSTED -> CircleTypeEntity.TRUSTED;
      case CONNECTED -> CircleTypeEntity.CONNECTED;
    };
  }

  private CircleType toDomainCircleType(CircleTypeEntity entity) {
    return switch (entity) {
      case TRUSTED -> CircleType.TRUSTED;
      case CONNECTED -> CircleType.CONNECTED;
    };
  }

  private SuggestionStatusEntity toEntityStatus(SuggestionStatus domain) {
    return switch (domain) {
      case PENDING -> SuggestionStatusEntity.PENDING;
      case ACCEPTED -> SuggestionStatusEntity.ACCEPTED;
      case REJECTED -> SuggestionStatusEntity.REJECTED;
    };
  }

  private SuggestionStatus toDomainStatus(SuggestionStatusEntity entity) {
    return switch (entity) {
      case PENDING -> SuggestionStatus.PENDING;
      case ACCEPTED -> SuggestionStatus.ACCEPTED;
      case REJECTED -> SuggestionStatus.REJECTED;
    };
  }
}
