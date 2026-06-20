package com.pallas.storyservice.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stories")
@Getter
@Setter
@NoArgsConstructor
public class StoryEntity {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "author_id", nullable = false, updatable = false)
  private UUID authorId;

  @Column(name = "story_content", nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "shared_with", nullable = false, length = 50)
  private SharedWithEntity sharedWith;

  @Column(name = "published_at", nullable = false, updatable = false)
  private OffsetDateTime publishedAt;
}
