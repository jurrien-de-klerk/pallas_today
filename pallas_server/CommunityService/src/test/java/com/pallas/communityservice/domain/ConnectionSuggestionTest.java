package com.pallas.communityservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConnectionSuggestionTest {

  @Test
  void isPending_WithPendingStatus_ReturnsTrue() {
    // Given
    ConnectionSuggestion suggestion =
        ConnectionSuggestion.builder()
            .id(UUID.randomUUID())
            .initiatorId(UUID.randomUUID())
            .targetId(UUID.randomUUID())
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(OffsetDateTime.now())
            .respondedAt(null)
            .build();

    // When
    boolean result = suggestion.isPending();

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void isPending_WithAcceptedStatus_ReturnsFalse() {
    // Given
    ConnectionSuggestion suggestion =
        ConnectionSuggestion.builder()
            .id(UUID.randomUUID())
            .initiatorId(UUID.randomUUID())
            .targetId(UUID.randomUUID())
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.ACCEPTED)
            .createdAt(OffsetDateTime.now())
            .respondedAt(OffsetDateTime.now())
            .build();

    // When
    boolean result = suggestion.isPending();

    // Then
    assertThat(result).isFalse();
  }

  @Test
  void isPending_WithRejectedStatus_ReturnsFalse() {
    // Given
    ConnectionSuggestion suggestion =
        ConnectionSuggestion.builder()
            .id(UUID.randomUUID())
            .initiatorId(UUID.randomUUID())
            .targetId(UUID.randomUUID())
            .targetCircle(CircleType.CONNECTED)
            .status(SuggestionStatus.REJECTED)
            .createdAt(OffsetDateTime.now())
            .respondedAt(OffsetDateTime.now())
            .build();

    // When
    boolean result = suggestion.isPending();

    // Then
    assertThat(result).isFalse();
  }

  @Test
  void builder_WithAllFields_CreatesImmutableAggregate() {
    // Given
    UUID id = UUID.randomUUID();
    UUID initiatorId = UUID.randomUUID();
    UUID targetId = UUID.randomUUID();
    CircleType circle = CircleType.TRUSTED;
    SuggestionStatus status = SuggestionStatus.PENDING;
    OffsetDateTime createdAt = OffsetDateTime.now();

    // When
    ConnectionSuggestion suggestion =
        ConnectionSuggestion.builder()
            .id(id)
            .initiatorId(initiatorId)
            .targetId(targetId)
            .targetCircle(circle)
            .status(status)
            .createdAt(createdAt)
            .respondedAt(null)
            .build();

    // Then
    assertThat(suggestion.getId()).isEqualTo(id);
    assertThat(suggestion.getInitiatorId()).isEqualTo(initiatorId);
    assertThat(suggestion.getTargetId()).isEqualTo(targetId);
    assertThat(suggestion.getTargetCircle()).isEqualTo(circle);
    assertThat(suggestion.getStatus()).isEqualTo(status);
    assertThat(suggestion.getCreatedAt()).isEqualTo(createdAt);
    assertThat(suggestion.getRespondedAt()).isNull();
  }

  @Test
  void builder_AllowsCreatingModifiedCopy() {
    // Given
    UUID id = UUID.randomUUID();
    UUID initiatorId = UUID.randomUUID();
    UUID targetId = UUID.randomUUID();
    OffsetDateTime createdAt = OffsetDateTime.now();
    OffsetDateTime newTime = OffsetDateTime.now();

    ConnectionSuggestion original =
        ConnectionSuggestion.builder()
            .id(id)
            .initiatorId(initiatorId)
            .targetId(targetId)
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(createdAt)
            .respondedAt(null)
            .build();

    // When
    ConnectionSuggestion modified =
        ConnectionSuggestion.builder()
            .id(original.getId())
            .initiatorId(original.getInitiatorId())
            .targetId(original.getTargetId())
            .targetCircle(original.getTargetCircle())
            .createdAt(original.getCreatedAt())
            .status(SuggestionStatus.ACCEPTED)
            .respondedAt(newTime)
            .build();

    // Then
    assertThat(modified.getId()).isEqualTo(original.getId());
    assertThat(modified.getInitiatorId()).isEqualTo(original.getInitiatorId());
    assertThat(modified.getTargetId()).isEqualTo(original.getTargetId());
    assertThat(modified.getTargetCircle()).isEqualTo(original.getTargetCircle());
    assertThat(modified.getCreatedAt()).isEqualTo(original.getCreatedAt());
    assertThat(modified.getStatus()).isEqualTo(SuggestionStatus.ACCEPTED);
    assertThat(modified.getRespondedAt()).isEqualTo(newTime);
  }
}
