package com.pallas.communityservice.application;

import com.pallas.communityservice.domain.CircleMembership;
import com.pallas.communityservice.domain.CircleType;
import com.pallas.communityservice.domain.CommunityDomainService;
import com.pallas.communityservice.domain.ConnectionSuggestion;
import com.pallas.communityservice.domain.MemberServicePort;
import com.pallas.communityservice.domain.RelationshipType;
import com.pallas.communityservice.domain.SuggestionDecision;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Application service for the community context.
 *
 * <p>Bridges the API layer and the domain layer (ADR-0013). Responsibilities:
 *
 * <ul>
 *   <li>Extract the authenticated principal from the Spring Security context
 *   <li>Resolve the domain UUID of the current user via {@link MemberServicePort}
 *   <li>Delegate all business logic to {@link CommunityDomainService}
 * </ul>
 */
@CustomLog
@Service
@RequiredArgsConstructor
public class CommunityApplicationService {

  private final CommunityDomainService communityDomainService;
  private final MemberServicePort memberServicePort;

  // -------------------------------------------------------------------------
  // Connection suggestions
  // -------------------------------------------------------------------------

  /**
   * Create a connection suggestion from the authenticated member to the specified target.
   *
   * @param targetMemberId the member ID of the target member (as issued by Member Service)
   * @param targetCircle the circle to which the target should be added
   * @return the created suggestion
   */
  public ConnectionSuggestion createConnectionSuggestion(
      UUID targetMemberId, CircleType targetCircle) {
    log.info("createConnectionSuggestion: target={} circle={}", targetMemberId, targetCircle);
    UUID initiatorUuid = getCurrentUserUuid();
    return communityDomainService.createConnectionSuggestion(
        initiatorUuid, targetMemberId, targetCircle);
  }

  /**
   * List all incoming pending connection suggestions for the authenticated member.
   *
   * @return pending suggestions received by the current member
   */
  public List<ConnectionSuggestion> listIncomingSuggestions() {
    UUID currentUuid = getCurrentUserUuid();
    log.info("listIncomingSuggestions");
    return communityDomainService.listIncomingSuggestions(currentUuid);
  }

  /**
   * Respond to a connection suggestion.
   *
   * @param suggestionId the ID of the suggestion
   * @param decision the authenticated member's decision
   * @return the updated suggestion
   */
  public ConnectionSuggestion respondToSuggestion(UUID suggestionId, SuggestionDecision decision) {
    log.info("respondToSuggestion: id={} decision={}", suggestionId, decision);
    UUID currentUuid = getCurrentUserUuid();
    return communityDomainService.respondToSuggestion(suggestionId, currentUuid, decision);
  }

  // -------------------------------------------------------------------------
  // Circles
  // -------------------------------------------------------------------------

  /**
   * Return the trusted and connected circles of the authenticated member.
   *
   * @return a pair of circle membership lists; first is trusted, second is connected
   */
  public Circles getMyCircles() {
    UUID currentUuid = getCurrentUserUuid();
    log.info("getMyCircles");
    List<CircleMembership> trusted = communityDomainService.getTrustedCircle(currentUuid);
    List<CircleMembership> connected = communityDomainService.getConnectedCircle(currentUuid);
    return new Circles(trusted, connected);
  }

  // -------------------------------------------------------------------------
  // Relationships
  // -------------------------------------------------------------------------

  /**
   * Return the relationship type between the authenticated member and the given member.
   *
   * @param targetMemberId the member ID of the other member (as issued by Member Service)
   * @return the relationship type
   */
  public RelationshipType getRelationship(UUID targetMemberId) {
    UUID currentUuid = getCurrentUserUuid();
    log.info("getRelationship: querying relationship");
    return communityDomainService.getRelationship(currentUuid, targetMemberId);
  }

  // -------------------------------------------------------------------------
  // Private helpers
  // -------------------------------------------------------------------------

  /**
   * Resolve the current authenticated user's UUID.
   *
   * @return the Member Service UUID of the current user
   */
  public UUID getCurrentUserUuid() {
    return memberServicePort.resolveCurrentMemberId(bearerToken(currentJwt()));
  }

  private Jwt currentJwt() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return (Jwt) auth.getPrincipal();
  }

  private String bearerToken(Jwt jwt) {
    return "Bearer " + jwt.getTokenValue();
  }

  /**
   * Value carrier for the two circle lists returned by {@link #getMyCircles()}. Returns
   * unmodifiable views to prevent callers from modifying the lists.
   *
   * @param trusted memberships in the trusted circle
   * @param connected memberships in the connected circle
   */
  public record Circles(List<CircleMembership> trusted, List<CircleMembership> connected) {
    // Compact constructor makes defensive copies of mutable input lists
    public Circles {
      trusted = Collections.unmodifiableList(new java.util.ArrayList<>(trusted));
      connected = Collections.unmodifiableList(new java.util.ArrayList<>(connected));
    }

    @Override
    public List<CircleMembership> trusted() {
      return trusted;
    }

    @Override
    public List<CircleMembership> connected() {
      return connected;
    }
  }
}
