package com.pallas.storyservice.application;

import com.pallas.integrations.member.MemberServicePort;
import com.pallas.storyservice.domain.SharedWith;
import com.pallas.storyservice.domain.Story;
import com.pallas.storyservice.domain.StoryDomainService;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Application service for the story context.
 *
 * <p>Bridges the API layer and the domain layer (ADR-0013). Responsibilities:
 *
 * <ul>
 *   <li>Extract the authenticated principal from the Spring Security context
 *   <li>Resolve the domain UUID of the current user via {@link MemberServicePort}
 *   <li>Delegate all business logic to {@link StoryDomainService}
 * </ul>
 */
@CustomLog
@Service
@RequiredArgsConstructor
public class StoryApplicationService {

  private final StoryDomainService storyDomainService;
  private final MemberServicePort memberServicePort;

  /**
   * Create and persist a new story for the authenticated member.
   *
   * @param content the story content
   * @param sharedWith the visibility level for this story
   * @return the persisted story
   */
  public Story createStory(String content, SharedWith sharedWith) {
    log.debug("createStory: resolving current user UUID");
    UUID authorId = getCurrentUserUuid();
    log.debug("createStory: delegating to domain service for author {}", authorId);
    return storyDomainService.createStory(authorId, content, sharedWith);
  }

  /**
   * Get a story if the authenticated member has permission to view it.
   *
   * @param storyId the ID of the story to retrieve
   * @return the story if accessible
   * @throws com.pallas.storyservice.domain.StoryAccessDeniedException if not accessible
   */
  public Story getStory(UUID storyId) {
    log.debug("getStory: resolving current user and retrieving bearer token");
    UUID requesterId = getCurrentUserUuid();
    String bearerToken = bearerToken(currentJwt());
    log.debug(
        "getStory: delegating to domain service for requester {} and story {}",
        requesterId,
        storyId);
    return storyDomainService.getStory(storyId, requesterId, bearerToken);
  }

  // -------------------------------------------------------------------------
  // Private helpers
  // -------------------------------------------------------------------------

  /**
   * Resolve the current authenticated user's UUID.
   *
   * @return the Member Service UUID of the current user
   */
  private UUID getCurrentUserUuid() {
    log.debug("getCurrentUserUuid: resolving member ID via member service");
    UUID memberId = memberServicePort.resolveCurrentMemberId(bearerToken(currentJwt()));
    log.debug("getCurrentUserUuid: resolved to {}", memberId);
    return memberId;
  }

  private Jwt currentJwt() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return (Jwt) auth.getPrincipal();
  }

  private String bearerToken(Jwt jwt) {
    return "Bearer " + jwt.getTokenValue();
  }
}
