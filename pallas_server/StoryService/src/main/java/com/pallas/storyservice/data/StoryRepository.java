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
   * @param trustedAuthorIds the list of trusted author UUIDs
   * @param connectedAuthorIds the list of connected author UUIDs
   * @param offset the number of stories to skip
   * @param count the maximum number of stories to return
   * @return a list of stories
   */
  default List<StoryEntity> findByAuthorId(
      List<UUID> trustedAuthorIds,
      List<UUID> connectedAuthorIds,
      int offset,
      int count) {
    return findByAuthorIdWithSaveGuards(trustedAuthorIds, trustedAuthorIds.isEmpty(),
        connectedAuthorIds, connectedAuthorIds.isEmpty(), offset, count);
  }

  @Query(
          value =
                  "SELECT * FROM stories "
                          + "WHERE (:trustedAuthorIdsEmpty = FALSE AND author_id IN :trustedAuthorIds ) "
                          + "OR (:connectedAuthorIdsEmpty = FALSE AND author_id IN :connectedAuthorIds "
                            + "AND shared_with IN ('CONNECTED', 'COMMUNITY', 'PUBLIC')) "
                          + "ORDER BY published_at DESC "
                          + "LIMIT :count OFFSET :offset",
          nativeQuery = true)
  List<StoryEntity> findByAuthorIdWithSaveGuards(
          @Param("trustedAuthorIds") List<UUID> trustedAuthorIds,
          @Param("trustedAuthorIdsEmpty") boolean trustedAuthorIdsEmpty,
          @Param("connectedAuthorIds") List<UUID> connectedAuthorIds,
          @Param("connectedAuthorIdsEmpty") boolean connectedAuthorIdsEmpty,
          @Param("offset") int offset,
          @Param("count") int count);
}
