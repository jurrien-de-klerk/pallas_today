package com.pallas.storyservice.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<StoryEntity, UUID> {

  /**
   * Find stories by multiple author IDs, ordered by most recent first, with offset/count
   * pagination.
   *
   * @param authorIds the list of author UUIDs
   * @param offset the number of stories to skip
   * @param count the maximum number of stories to return
   * @return a list of stories
   */
  List<StoryEntity> findByAuthorIdInOrderByPublishedAtDesc(
      List<UUID> authorIds, int offset, int count);
}
