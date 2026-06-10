package com.pallas.communityservice.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Domain service for the community bounded context.
 *
 * <p>All business rules live here. Persistence and external integrations are accessed through
 * ports, keeping the domain layer free of infrastructure concerns (ADR-0010).
 */
@CustomLog
@Service
@RequiredArgsConstructor
public class CommunityDomainService {

  private final ConnectionSuggestionPort connectionSuggestionPort;
  private final CircleMembershipPort circleMembershipPort;

  // -------------------------------------------------------------------------
  // Connection suggestions
  // -------------------------------------------------------------------------

  /**
   * Create a new connection suggestion.
   *
   * @param initiatorId Member Service UUID of the authenticated initiator
   * @param targetId Member Service UUID of the intended target
   * @param targetCircle the circle into which the initiator wishes to add the target
   * @return the persisted suggestion
   */
  @Transactional
  public ConnectionSuggestion createConnectionSuggestion(
      UUID initiatorId, UUID targetId, CircleType targetCircle) {
    log.debug("createConnectionSuggestion: creating suggestion");
    if (initiatorId.equals(targetId)) {
      log.debug("createConnectionSuggestion: rejected — self-suggestion");
      throw new IllegalArgumentException("A member cannot suggest a connection with themselves");
    }
    log.debug(
        "createConnectionSuggestion: self-check passed, building suggestion for circle {}",
        targetCircle);
    ConnectionSuggestion suggestion =
        ConnectionSuggestion.builder()
            .initiatorId(initiatorId)
            .targetId(targetId)
            .targetCircle(targetCircle)
            .status(SuggestionStatus.PENDING)
            .createdAt(OffsetDateTime.now())
            .respondedAt(null)
            .build();
    log.debug("createConnectionSuggestion: persisting");
    ConnectionSuggestion saved = connectionSuggestionPort.save(suggestion);
    log.debug("createConnectionSuggestion: suggestion saved");
    return saved;
  }

  /**
   * List all pending connection suggestions that involve the given member, either as the initiator
   * or as the target.
   *
   * @param currentId Member Service UUID of the authenticated member
   * @return pending suggestions visible to this member
   */
  @Transactional(readOnly = true)
  public List<ConnectionSuggestion> listIncomingSuggestions(UUID currentId) {
    log.debug("listIncomingSuggestions: fetching for member");
    List<ConnectionSuggestion> results =
        connectionSuggestionPort.findPendingByParticipantId(currentId);
    log.debug("listIncomingSuggestions: found {} pending suggestion(s)", results.size());
    return results;
  }

  /**
   * Respond to a connection suggestion.
   *
   * <p>If the decision is {@link SuggestionDecision#ACCEPTED} the circle membership between the two
   * members is created or upgraded to the suggested circle type.
   *
   * @param suggestionId the ID of the suggestion to respond to
   * @param currentId Member Service UUID of the authenticated member (must be the recipient)
   * @param decision whether the suggestion is accepted or rejected
   * @return the updated suggestion
   * @throws ConnectionSuggestionNotFoundException when no suggestion with the given ID exists
   * @throws NotSuggestionRecipientException when the authenticated member is not the recipient
   * @throws SuggestionAlreadyRespondedException when the suggestion is no longer pending
   */
  @Transactional
  public ConnectionSuggestion respondToSuggestion(
      UUID suggestionId, UUID currentId, SuggestionDecision decision) {

    log.debug("respondToSuggestion: looking up suggestion");
    ConnectionSuggestion suggestion =
        connectionSuggestionPort
            .findById(suggestionId)
            .orElseThrow(
                () -> {
                  log.debug("respondToSuggestion: suggestion not found");
                  return new ConnectionSuggestionNotFoundException(suggestionId);
                });
    log.debug("respondToSuggestion: suggestion found with status {}", suggestion.getStatus());

    if (!suggestion.getTargetId().equals(currentId)) {
      log.debug("respondToSuggestion: rejected — caller is not the recipient");
      throw new NotSuggestionRecipientException(suggestionId);
    }
    if (!suggestion.isPending()) {
      log.debug("respondToSuggestion: rejected — suggestion already responded");
      throw new SuggestionAlreadyRespondedException(suggestionId);
    }
    log.debug("respondToSuggestion: guards passed, applying decision {}", decision);

    SuggestionStatus newStatus =
        decision == SuggestionDecision.ACCEPTED
            ? SuggestionStatus.ACCEPTED
            : SuggestionStatus.REJECTED;

    ConnectionSuggestion updated =
        ConnectionSuggestion.builder()
            .id(suggestion.getId())
            .initiatorId(suggestion.getInitiatorId())
            .targetId(suggestion.getTargetId())
            .targetCircle(suggestion.getTargetCircle())
            .status(newStatus)
            .createdAt(suggestion.getCreatedAt())
            .respondedAt(OffsetDateTime.now())
            .build();

    log.debug("respondToSuggestion: persisting updated suggestion");
    ConnectionSuggestion saved = connectionSuggestionPort.update(updated);
    log.debug("respondToSuggestion: suggestion updated");

    if (decision == SuggestionDecision.ACCEPTED) {
      log.debug("respondToSuggestion: accepted — creating circle membership");
      acceptSuggestion(suggestion, currentId);
    } else {
      log.debug("respondToSuggestion: rejected — no circle membership created");
    }

    log.debug("respondToSuggestion: done");
    return saved;
  }

  // -------------------------------------------------------------------------
  // Circles
  // -------------------------------------------------------------------------

  /**
   * Return all trusted circle members for the given member.
   *
   * @param currentId Member Service UUID of the authenticated member
   * @return circle memberships where the type is {@link CircleType#TRUSTED}
   */
  @Transactional(readOnly = true)
  public List<CircleMembership> getTrustedCircle(UUID currentId) {
    log.debug("getTrustedCircle: fetching trusted circle members");
    List<CircleMembership> results =
        circleMembershipPort.findAllByMemberIdAndCircleType(currentId, CircleType.TRUSTED);
    log.debug("getTrustedCircle: found {} member(s)", results.size());
    return results;
  }

  /**
   * Return all connected circle members for the given member.
   *
   * @param currentId Member Service UUID of the authenticated member
   * @return circle memberships where the type is {@link CircleType#CONNECTED}
   */
  @Transactional(readOnly = true)
  public List<CircleMembership> getConnectedCircle(UUID currentId) {
    log.debug("getConnectedCircle: fetching connected circle members");
    List<CircleMembership> results =
        circleMembershipPort.findAllByMemberIdAndCircleType(currentId, CircleType.CONNECTED);
    log.debug("getConnectedCircle: found {} member(s)", results.size());
    return results;
  }

  // -------------------------------------------------------------------------
  // Relationships
  // -------------------------------------------------------------------------

  /**
   * Determine the relationship type between the authenticated member and another member.
   *
   * @param currentId Member Service UUID of the authenticated member
   * @param targetId Member Service UUID of the other member
   * @return the relationship type; {@link RelationshipType#COMMUNITY} if no circle membership
   *     exists
   */
  @Transactional(readOnly = true)
  public RelationshipType getRelationship(UUID currentId, UUID targetId) {
    log.debug("getRelationship: looking up circle membership between pair");
    Optional<CircleMembership> membership = circleMembershipPort.findByPair(currentId, targetId);
    RelationshipType result =
        membership
            .map(
                m ->
                    m.getCircleType() == CircleType.TRUSTED
                        ? RelationshipType.TRUSTED
                        : RelationshipType.CONNECTED)
            .orElse(RelationshipType.COMMUNITY);
    log.debug("getRelationship: resolved to {}", result);
    return result;
  }

  // -------------------------------------------------------------------------
  // Private helpers
  // -------------------------------------------------------------------------

  private void acceptSuggestion(ConnectionSuggestion suggestion, UUID acceptorId) {
    log.debug(
        "acceptSuggestion: building circle membership for circle {}", suggestion.getTargetCircle());
    UUID initiatorId = suggestion.getInitiatorId();

    // Canonical ordering: member with the smaller UUID is A.
    // Use string comparison to match PostgreSQL's unsigned UUID ordering;
    // UUID.compareTo() uses signed long arithmetic and can disagree with the DB.
    UUID memberIdA;
    UUID memberIdB;
    if (initiatorId.toString().compareTo(acceptorId.toString()) < 0) {
      memberIdA = initiatorId;
      memberIdB = acceptorId;
    } else {
      memberIdA = acceptorId;
      memberIdB = initiatorId;
    }

    CircleMembership membership =
        CircleMembership.builder()
            .memberIdA(memberIdA)
            .memberIdB(memberIdB)
            .circleType(suggestion.getTargetCircle())
            .memberSince(OffsetDateTime.now())
            .build();
    log.debug("acceptSuggestion: persisting circle membership");
    circleMembershipPort.save(membership);
    log.debug("acceptSuggestion: circle membership saved");
  }
}
