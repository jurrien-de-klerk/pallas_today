package com.pallas.communityservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommunityDomainServiceTest {

  private CommunityDomainService service;

  @Mock private ConnectionSuggestionPort connectionSuggestionPort;
  @Mock private CircleMembershipPort circleMembershipPort;

  private UUID initiatorId;
  private UUID targetId;
  private UUID suggestionId;
  private ConnectionSuggestion pendingSuggestion;

  @BeforeEach
  void setUp() {
    service = new CommunityDomainService(connectionSuggestionPort, circleMembershipPort);

    // Create test UUIDs
    initiatorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    targetId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    suggestionId = UUID.fromString("33333333-3333-3333-3333-333333333333");

    // Create a pending suggestion for reuse in tests
    pendingSuggestion =
        ConnectionSuggestion.builder()
            .id(suggestionId)
            .initiatorId(initiatorId)
            .targetId(targetId)
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(OffsetDateTime.now())
            .respondedAt(null)
            .build();
  }

  // =========================================================================
  // createConnectionSuggestion tests
  // =========================================================================

  @Test
  void createConnectionSuggestion_WithValidInput_SavesAndReturnsSuggestion() {
    // Given
    ConnectionSuggestion expectedSuggestion =
        ConnectionSuggestion.builder()
            .id(suggestionId)
            .initiatorId(initiatorId)
            .targetId(targetId)
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(OffsetDateTime.now())
            .respondedAt(null)
            .build();
    when(connectionSuggestionPort.save(any())).thenReturn(expectedSuggestion);

    // When
    ConnectionSuggestion result =
        service.createConnectionSuggestion(initiatorId, targetId, CircleType.TRUSTED);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getInitiatorId()).isEqualTo(initiatorId);
    assertThat(result.getTargetId()).isEqualTo(targetId);
    assertThat(result.getTargetCircle()).isEqualTo(CircleType.TRUSTED);
    assertThat(result.getStatus()).isEqualTo(SuggestionStatus.PENDING);
    assertThat(result.getRespondedAt()).isNull();

    // Verify port was called
    ArgumentCaptor<ConnectionSuggestion> captor =
        ArgumentCaptor.forClass(ConnectionSuggestion.class);
    verify(connectionSuggestionPort).save(captor.capture());
    ConnectionSuggestion saved = captor.getValue();
    assertThat(saved.getInitiatorId()).isEqualTo(initiatorId);
    assertThat(saved.getStatus()).isEqualTo(SuggestionStatus.PENDING);
  }

  @Test
  void createConnectionSuggestion_WithConnectedCircle_CreatesPendingSuggestion() {
    // Given
    ConnectionSuggestion connectedSuggestion =
        ConnectionSuggestion.builder()
            .id(UUID.randomUUID())
            .initiatorId(initiatorId)
            .targetId(targetId)
            .targetCircle(CircleType.CONNECTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(OffsetDateTime.now())
            .respondedAt(null)
            .build();
    when(connectionSuggestionPort.save(any())).thenReturn(connectedSuggestion);

    // When
    ConnectionSuggestion result =
        service.createConnectionSuggestion(initiatorId, targetId, CircleType.CONNECTED);

    // Then
    assertThat(result.getTargetCircle()).isEqualTo(CircleType.CONNECTED);
  }

  // =========================================================================
  // listIncomingSuggestions tests
  // =========================================================================

  @Test
  void listIncomingSuggestions_WithPendingSuggestions_ReturnsList() {
    // Given
    UUID receiverId = UUID.fromString("44444444-4444-4444-4444-444444444444");
    ConnectionSuggestion suggestion1 =
        ConnectionSuggestion.builder()
            .id(UUID.randomUUID())
            .initiatorId(pendingSuggestion.getInitiatorId())
            .targetId(receiverId)
            .targetCircle(pendingSuggestion.getTargetCircle())
            .status(pendingSuggestion.getStatus())
            .createdAt(pendingSuggestion.getCreatedAt())
            .respondedAt(null)
            .build();
    ConnectionSuggestion suggestion2 =
        ConnectionSuggestion.builder()
            .id(UUID.randomUUID())
            .initiatorId(pendingSuggestion.getInitiatorId())
            .targetId(receiverId)
            .targetCircle(pendingSuggestion.getTargetCircle())
            .status(pendingSuggestion.getStatus())
            .createdAt(pendingSuggestion.getCreatedAt())
            .respondedAt(null)
            .build();
    when(connectionSuggestionPort.findPendingByParticipantId(receiverId))
        .thenReturn(List.of(suggestion1, suggestion2));

    // When
    List<ConnectionSuggestion> result = service.listIncomingSuggestions(receiverId);

    // Then
    assertThat(result).hasSize(2).contains(suggestion1, suggestion2);
    verify(connectionSuggestionPort).findPendingByParticipantId(receiverId);
  }

  @Test
  void listIncomingSuggestions_WithNoSuggestions_ReturnsEmptyList() {
    // Given
    UUID receiverId = UUID.fromString("44444444-4444-4444-4444-444444444444");
    when(connectionSuggestionPort.findPendingByParticipantId(receiverId)).thenReturn(List.of());

    // When
    List<ConnectionSuggestion> result = service.listIncomingSuggestions(receiverId);

    // Then
    assertThat(result).isEmpty();
  }

  // =========================================================================
  // respondToSuggestion tests
  // =========================================================================

  @Test
  void respondToSuggestion_WithAcceptedDecision_UpdatesSuggestionAndCreatesMembership() {
    // Given
    when(connectionSuggestionPort.findById(suggestionId))
        .thenReturn(Optional.of(pendingSuggestion));
    OffsetDateTime now = OffsetDateTime.now();
    ConnectionSuggestion acceptedSuggestion =
        ConnectionSuggestion.builder()
            .id(pendingSuggestion.getId())
            .initiatorId(pendingSuggestion.getInitiatorId())
            .targetId(pendingSuggestion.getTargetId())
            .targetCircle(pendingSuggestion.getTargetCircle())
            .status(SuggestionStatus.ACCEPTED)
            .createdAt(pendingSuggestion.getCreatedAt())
            .respondedAt(now)
            .build();
    when(connectionSuggestionPort.update(any())).thenReturn(acceptedSuggestion);

    // When
    ConnectionSuggestion result =
        service.respondToSuggestion(suggestionId, targetId, SuggestionDecision.ACCEPTED);

    // Then
    assertThat(result.getStatus()).isEqualTo(SuggestionStatus.ACCEPTED);
    assertThat(result.getRespondedAt()).isNotNull();
    verify(connectionSuggestionPort).findById(suggestionId);
    verify(connectionSuggestionPort).update(any());
    verify(circleMembershipPort).save(any());
  }

  @Test
  void respondToSuggestion_WithRejectedDecision_UpdatesSuggestionButNoMembership() {
    // Given
    when(connectionSuggestionPort.findById(suggestionId))
        .thenReturn(Optional.of(pendingSuggestion));
    OffsetDateTime now = OffsetDateTime.now();
    ConnectionSuggestion rejectedSuggestion =
        ConnectionSuggestion.builder()
            .id(pendingSuggestion.getId())
            .initiatorId(pendingSuggestion.getInitiatorId())
            .targetId(pendingSuggestion.getTargetId())
            .targetCircle(pendingSuggestion.getTargetCircle())
            .status(SuggestionStatus.REJECTED)
            .createdAt(pendingSuggestion.getCreatedAt())
            .respondedAt(now)
            .build();
    when(connectionSuggestionPort.update(any())).thenReturn(rejectedSuggestion);

    // When
    ConnectionSuggestion result =
        service.respondToSuggestion(suggestionId, targetId, SuggestionDecision.REJECTED);

    // Then
    assertThat(result.getStatus()).isEqualTo(SuggestionStatus.REJECTED);
    verify(connectionSuggestionPort).findById(suggestionId);
    verify(connectionSuggestionPort).update(any());
    verify(circleMembershipPort, times(0)).save(any());
  }

  @Test
  void respondToSuggestion_WithAcceptedDecision_SavesMembershipInCanonicalOrder() {
    // Given - uuidA < uuidB in comparison
    UUID uuidA = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    UUID uuidB = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    ConnectionSuggestion suggestion =
        ConnectionSuggestion.builder()
            .id(pendingSuggestion.getId())
            .initiatorId(uuidB)
            .targetId(uuidA)
            .targetCircle(pendingSuggestion.getTargetCircle())
            .status(pendingSuggestion.getStatus())
            .createdAt(pendingSuggestion.getCreatedAt())
            .respondedAt(null)
            .build();
    when(connectionSuggestionPort.findById(suggestionId)).thenReturn(Optional.of(suggestion));
    when(connectionSuggestionPort.update(any()))
        .thenReturn(
            ConnectionSuggestion.builder()
                .id(suggestion.getId())
                .initiatorId(suggestion.getInitiatorId())
                .targetId(suggestion.getTargetId())
                .targetCircle(suggestion.getTargetCircle())
                .status(SuggestionStatus.ACCEPTED)
                .createdAt(suggestion.getCreatedAt())
                .respondedAt(OffsetDateTime.now())
                .build());

    // When
    service.respondToSuggestion(suggestionId, uuidA, SuggestionDecision.ACCEPTED);

    // Then
    ArgumentCaptor<CircleMembership> captor = ArgumentCaptor.forClass(CircleMembership.class);
    verify(circleMembershipPort).save(captor.capture());
    CircleMembership membership = captor.getValue();
    // Canonical order: smaller UUID is A
    assertThat(membership.getMemberIdA()).isEqualTo(uuidA);
    assertThat(membership.getMemberIdB()).isEqualTo(uuidB);
    assertThat(membership.getCircleType()).isEqualTo(CircleType.TRUSTED);
  }

  @Test
  void respondToSuggestion_WhenSuggestionNotFound_ThrowsNotFoundException() {
    // Given
    when(connectionSuggestionPort.findById(suggestionId)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(
            () -> service.respondToSuggestion(suggestionId, targetId, SuggestionDecision.ACCEPTED))
        .isInstanceOf(ConnectionSuggestionNotFoundException.class);
  }

  @Test
  void respondToSuggestion_WhenUserIsNotRecipient_ThrowsNotRecipientException() {
    // Given
    UUID otherUserId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    when(connectionSuggestionPort.findById(suggestionId))
        .thenReturn(Optional.of(pendingSuggestion));

    // When/Then
    assertThatThrownBy(
            () ->
                service.respondToSuggestion(suggestionId, otherUserId, SuggestionDecision.ACCEPTED))
        .isInstanceOf(NotSuggestionRecipientException.class);
  }

  @Test
  void respondToSuggestion_WhenAlreadyAccepted_ThrowsAlreadyRespondedException() {
    // Given
    ConnectionSuggestion acceptedSuggestion =
        ConnectionSuggestion.builder()
            .id(pendingSuggestion.getId())
            .initiatorId(pendingSuggestion.getInitiatorId())
            .targetId(pendingSuggestion.getTargetId())
            .targetCircle(pendingSuggestion.getTargetCircle())
            .status(SuggestionStatus.ACCEPTED)
            .createdAt(pendingSuggestion.getCreatedAt())
            .respondedAt(OffsetDateTime.now())
            .build();
    when(connectionSuggestionPort.findById(suggestionId))
        .thenReturn(Optional.of(acceptedSuggestion));

    // When/Then
    assertThatThrownBy(
            () -> service.respondToSuggestion(suggestionId, targetId, SuggestionDecision.ACCEPTED))
        .isInstanceOf(SuggestionAlreadyRespondedException.class);
  }

  @Test
  void respondToSuggestion_WhenAlreadyRejected_ThrowsAlreadyRespondedException() {
    // Given
    ConnectionSuggestion rejectedSuggestion =
        ConnectionSuggestion.builder()
            .id(pendingSuggestion.getId())
            .initiatorId(pendingSuggestion.getInitiatorId())
            .targetId(pendingSuggestion.getTargetId())
            .targetCircle(pendingSuggestion.getTargetCircle())
            .status(SuggestionStatus.REJECTED)
            .createdAt(pendingSuggestion.getCreatedAt())
            .respondedAt(OffsetDateTime.now())
            .build();
    when(connectionSuggestionPort.findById(suggestionId))
        .thenReturn(Optional.of(rejectedSuggestion));

    // When/Then
    assertThatThrownBy(
            () -> service.respondToSuggestion(suggestionId, targetId, SuggestionDecision.REJECTED))
        .isInstanceOf(SuggestionAlreadyRespondedException.class);
  }

  // =========================================================================
  // getTrustedCircle tests
  // =========================================================================

  @Test
  void getTrustedCircle_WithMembers_ReturnsTrustedMemberships() {
    // Given
    UUID currentId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    CircleMembership member1 = createCircleMembership(currentId, CircleType.TRUSTED);
    CircleMembership member2 = createCircleMembership(currentId, CircleType.TRUSTED);
    when(circleMembershipPort.findAllByMemberIdAndCircleType(currentId, CircleType.TRUSTED))
        .thenReturn(List.of(member1, member2));

    // When
    List<CircleMembership> result = service.getTrustedCircle(currentId);

    // Then
    assertThat(result).hasSize(2).contains(member1, member2);
    verify(circleMembershipPort).findAllByMemberIdAndCircleType(currentId, CircleType.TRUSTED);
  }

  @Test
  void getTrustedCircle_WithNoMembers_ReturnsEmptyList() {
    // Given
    UUID currentId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    when(circleMembershipPort.findAllByMemberIdAndCircleType(currentId, CircleType.TRUSTED))
        .thenReturn(List.of());

    // When
    List<CircleMembership> result = service.getTrustedCircle(currentId);

    // Then
    assertThat(result).isEmpty();
  }

  // =========================================================================
  // getConnectedCircle tests
  // =========================================================================

  @Test
  void getConnectedCircle_WithMembers_ReturnsConnectedMemberships() {
    // Given
    UUID currentId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    CircleMembership member1 = createCircleMembership(currentId, CircleType.CONNECTED);
    CircleMembership member2 = createCircleMembership(currentId, CircleType.CONNECTED);
    when(circleMembershipPort.findAllByMemberIdAndCircleType(currentId, CircleType.CONNECTED))
        .thenReturn(List.of(member1, member2));

    // When
    List<CircleMembership> result = service.getConnectedCircle(currentId);

    // Then
    assertThat(result).hasSize(2).contains(member1, member2);
    verify(circleMembershipPort).findAllByMemberIdAndCircleType(currentId, CircleType.CONNECTED);
  }

  @Test
  void getConnectedCircle_WithNoMembers_ReturnsEmptyList() {
    // Given
    UUID currentId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    when(circleMembershipPort.findAllByMemberIdAndCircleType(currentId, CircleType.CONNECTED))
        .thenReturn(List.of());

    // When
    List<CircleMembership> result = service.getConnectedCircle(currentId);

    // Then
    assertThat(result).isEmpty();
  }

  // =========================================================================
  // getRelationship tests
  // =========================================================================

  @Test
  void getRelationship_WithTrustedMembership_ReturnsTrustedRelationship() {
    // Given
    UUID currentId = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
    UUID targetId = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
    CircleMembership membership = createCircleMembership(currentId, CircleType.TRUSTED);
    when(circleMembershipPort.findByPair(currentId, targetId)).thenReturn(Optional.of(membership));

    // When
    RelationshipType result = service.getRelationship(currentId, targetId);

    // Then
    assertThat(result).isEqualTo(RelationshipType.TRUSTED);
  }

  @Test
  void getRelationship_WithConnectedMembership_ReturnsConnectedRelationship() {
    // Given
    UUID currentId = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
    UUID targetId = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
    CircleMembership membership = createCircleMembership(currentId, CircleType.CONNECTED);
    when(circleMembershipPort.findByPair(currentId, targetId)).thenReturn(Optional.of(membership));

    // When
    RelationshipType result = service.getRelationship(currentId, targetId);

    // Then
    assertThat(result).isEqualTo(RelationshipType.CONNECTED);
  }

  @Test
  void getRelationship_WithNoMembership_ReturnsCommunityRelationship() {
    // Given
    UUID currentId = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
    UUID targetId = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
    when(circleMembershipPort.findByPair(currentId, targetId)).thenReturn(Optional.empty());

    // When
    RelationshipType result = service.getRelationship(currentId, targetId);

    // Then
    assertThat(result).isEqualTo(RelationshipType.COMMUNITY);
  }

  // =========================================================================
  // Helpers
  // =========================================================================

  private CircleMembership createCircleMembership(UUID currentId, CircleType circleType) {
    UUID partnerId = UUID.randomUUID();
    UUID memberIdA;
    UUID memberIdB;
    if (currentId.toString().compareTo(partnerId.toString()) < 0) {
      memberIdA = currentId;
      memberIdB = partnerId;
    } else {
      memberIdA = partnerId;
      memberIdB = currentId;
    }
    return CircleMembership.builder()
        .memberIdA(memberIdA)
        .memberIdB(memberIdB)
        .circleType(circleType)
        .memberSince(OffsetDateTime.now())
        .build();
  }
}
