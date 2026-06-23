package com.pallas.storyservice.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
  @Query(
      value =
          "SELECT * FROM story_entity "
              + "WHERE (author_id IN :trustedAuthorIds AND shared_with = 'TRUSTED') "
              + "OR (author_id IN :connectedAuthorIds AND shared_with = 'CONNECTED') "
              + "ORDER BY published_at DESC "
              + "LIMIT :count OFFSET :offset",
      nativeQuery = true)
  List<StoryEntity> findByAuthorId(
      @Param("authorIds") List<UUID> trustedAuthorIds,
      @Param("authorIds") List<UUID> connectedAuthorIds,
      @Param("offset") int offset,
      @Param("count") int count);
}
