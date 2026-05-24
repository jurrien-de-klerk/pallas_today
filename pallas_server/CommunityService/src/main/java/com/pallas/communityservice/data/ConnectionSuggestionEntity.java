package com.pallas.communityservice.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "connection_suggestions")
@Getter
@Setter
@NoArgsConstructor
public class ConnectionSuggestionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "initiator_id", nullable = false, updatable = false)
  private UUID initiatorId;

  @Column(name = "target_id", nullable = false, updatable = false)
  private UUID targetId;

  @Enumerated(EnumType.STRING)
  @Column(name = "target_circle", nullable = false, updatable = false, length = 50)
  private CircleTypeEntity targetCircle;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 50)
  private SuggestionStatusEntity status;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "responded_at")
  private OffsetDateTime respondedAt;
}
