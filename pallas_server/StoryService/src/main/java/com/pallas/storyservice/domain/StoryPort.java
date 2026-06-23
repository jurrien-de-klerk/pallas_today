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
   * Find all stories written by members in the given list, with offset/count pagination.
   *
   * @param authorIds the list of member UUIDs
   * @param offset the number of stories to skip
   * @param count the maximum number of stories to return
   * @return a list of stories for the requested page
   */
  List<Story> findStoriesByAuthorIds(List<UUID> authorIds, int offset, int count);
}
