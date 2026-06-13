package com.pallas.storyservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Port for persisting and querying stories. */
public interface StoryPort {

  /**
   * Persist a new story.
   *
   * @param story the story to save
   * @return the saved story (id and timestamps populated)
   */
  Story save(Story story);

  /**
   * Find a story by its unique ID.
   *
   * @param id the story UUID
   * @return the story, or empty if not found
   */
  Optional<Story> findById(UUID id);
}
