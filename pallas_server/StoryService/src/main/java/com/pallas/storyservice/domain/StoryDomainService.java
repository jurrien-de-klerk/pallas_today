package com.pallas.storyservice.domain;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Domain service for the story bounded context.
 *
 * <p>All business rules live here. Persistence and external integrations are accessed through
 * ports, keeping the domain layer free of infrastructure concerns (ADR-0010).
 */
@CustomLog
@Service
@RequiredArgsConstructor
public class StoryDomainService {

  private final StoryPort storyPort;
  private final CommunityServicePort communityServicePort;

  /**
   * Create and persist a new story.
   *
   * @param authorId the Member Service UUID of the story author
   * @param content the story content
   * @param sharedWith the visibility level for this story
   * @return the persisted story (with id and publishedAt populated)
   */
  @Transactional
  public Story createStory(UUID authorId, String content, SharedWith sharedWith) {
    log.debug("createStory: creating story with shared with level {}", sharedWith);
    Story story =
        new Story(UUID.randomUUID(), authorId, content, sharedWith, OffsetDateTime.now());
    Story persisted = storyPort.save(story);
    log.debug("createStory: story persisted with id {}", persisted.getId());
    return persisted;
  }

  /**
   * Get a story if the requesting member has permission to view it.
   *
   * <p>A story is accessible if:
   * <ul>
   *   <li>The requesting member is the author, OR
   *   <li>The story's shared level permits access based on the relationship type:
   *       <ul>
   *         <li>TRUSTED: only trusted members can access
   *         <li>CONNECTED: only trusted or connected members can access
   *         <li>COMMUNITY: all community members can access
   *         <li>PUBLIC: anyone can access
   *       </ul>
   * </ul>
   *
   * @param storyId the ID of the story to retrieve
   * @param requesterId the Member Service UUID of the requesting member
   * @param bearerToken the bearer token from the authenticated request
   * @return the story if accessible
   * @throws StoryAccessDeniedException if the story is not accessible to the requester
   */
  @Transactional(readOnly = true)
  public Story getStory(UUID storyId, UUID requesterId, String bearerToken) {
    log.debug("getStory: retrieving story {}", storyId);
    Story story = storyPort.findById(storyId)
        .orElseThrow(
            () -> {
              log.debug("getStory: story not found");
              return new StoryAccessDeniedException("Story not found");
            });

    // Author can always view their own stories
    if (story.getAuthorId().equals(requesterId)) {
      log.debug("getStory: requester is the author, access granted");
      return story;
    }

    // Check relationship and shared with level
    RelationshipType relationship = communityServicePort.getRelationship(story.getAuthorId(), bearerToken);
    boolean accessDenied = isAccessDeniedByRelationship(story.getSharedWith(), relationship);

    if (accessDenied) {
      log.debug(
          "getStory: access denied — shared with {} but relationship is {}",
          story.getSharedWith(),
          relationship);
      throw new StoryAccessDeniedException("Story is not accessible to you");
    }

    log.debug("getStory: access granted by relationship {}", relationship);
    return story;
  }

  // -------------------------------------------------------------------------
  // Private helpers
  // -------------------------------------------------------------------------

  /**
   * Determine if a story with a given shared level is denied access based on the relationship type.
   *
   * @param sharedWith the story's visibility level
   * @param relationship the relationship between the story author and the requester
   * @return true if access is denied
   */
  private boolean isAccessDeniedByRelationship(SharedWith sharedWith, RelationshipType relationship) {
    return switch (sharedWith) {
      case PUBLIC -> false;
      case COMMUNITY -> false;
      case CONNECTED -> relationship != RelationshipType.TRUSTED && relationship != RelationshipType.CONNECTED;
      case TRUSTED -> relationship != RelationshipType.TRUSTED;
    };
  }
}
