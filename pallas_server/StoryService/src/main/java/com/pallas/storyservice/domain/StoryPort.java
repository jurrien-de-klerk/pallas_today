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

  /**
   * Find all stories written by trusted and connected members, with offset/count pagination.
   *
   * @param trustedAuthorIds the list of member UUIDs in the trusted circle
   * @param connectedAuthorIds the list of member UUIDs in the connected circle
   * @param offset the number of stories to skip
   * @param count the maximum number of stories to return
   * @return a list of stories for the requested page, ordered by most recent first
   */
  List<Story> findStoriesByAuthorIds(
      List<UUID> trustedAuthorIds, List<UUID> connectedAuthorIds, int offset, int count);
}
