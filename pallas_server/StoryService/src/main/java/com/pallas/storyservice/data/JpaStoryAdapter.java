package com.pallas.storyservice.data;

import com.pallas.storyservice.domain.SharedWith;
import com.pallas.storyservice.domain.Story;
import com.pallas.storyservice.domain.StoryPort;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

/** JPA adapter implementing {@link StoryPort}. */
@CustomLog
@Component
public class JpaStoryAdapter implements StoryPort {

  private final StoryRepository repository;

  public JpaStoryAdapter(StoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public Story save(Story story) {
    log.debug("save: persisting new story");
    StoryEntity entity = toEntity(story);
    entity.setPublishedAt(OffsetDateTime.now());
    Story saved = toDomain(repository.save(entity));
    log.debug("save: story persisted with id {}", saved.getId());
    return saved;
  }

  @Override
  public Optional<Story> findById(UUID id) {
    log.debug("findById: querying story");
    Optional<Story> result = repository.findById(id).map(this::toDomain);
    log.debug("findById: story {}", result.isPresent() ? "found" : "not found");
    return result;
  }

  // -------------------------------------------------------------------------
  // Mapping helpers
  // -------------------------------------------------------------------------

  private StoryEntity toEntity(Story domain) {
    StoryEntity entity = new StoryEntity();
    entity.setId(domain.getId());
    entity.setAuthorId(domain.getAuthorId());
    entity.setContent(domain.getContent());
    entity.setSharedWith(toEntitySharedWith(domain.getSharedWith()));
    entity.setPublishedAt(domain.getPublishedAt());
    return entity;
  }

  private Story toDomain(StoryEntity entity) {
    return new Story(
        entity.getId(),
        entity.getAuthorId(),
        entity.getContent(),
        toDomainSharedWith(entity.getSharedWith()),
        entity.getPublishedAt());
  }

  private SharedWithEntity toEntitySharedWith(SharedWith domain) {
    return switch (domain) {
      case TRUSTED -> SharedWithEntity.TRUSTED;
      case CONNECTED -> SharedWithEntity.CONNECTED;
      case COMMUNITY -> SharedWithEntity.COMMUNITY;
      case PUBLIC -> SharedWithEntity.PUBLIC;
    };
  }

  private SharedWith toDomainSharedWith(SharedWithEntity entity) {
    return switch (entity) {
      case TRUSTED -> SharedWith.TRUSTED;
      case CONNECTED -> SharedWith.CONNECTED;
      case COMMUNITY -> SharedWith.COMMUNITY;
      case PUBLIC -> SharedWith.PUBLIC;
    };
  }
}
