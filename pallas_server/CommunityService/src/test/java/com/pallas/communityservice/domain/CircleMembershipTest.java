package com.pallas.communityservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CircleMembershipTest {

  @Test
  void partnerIdFor_WhenCalledWithMemberIdA_ReturnsMemberIdB() {
    // Given
    UUID memberIdA = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID memberIdB = UUID.fromString("22222222-2222-2222-2222-222222222222");
    CircleMembership membership =
        CircleMembership.builder()
            .memberIdA(memberIdA)
            .memberIdB(memberIdB)
            .circleType(CircleType.TRUSTED)
            .memberSince(OffsetDateTime.now())
            .build();

    // When
    UUID partnerId = membership.partnerIdFor(memberIdA);

    // Then
    assertThat(partnerId).isEqualTo(memberIdB);
  }

  @Test
  void partnerIdFor_WhenCalledWithMemberIdB_ReturnsMemberIdA() {
    // Given
    UUID memberIdA = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID memberIdB = UUID.fromString("22222222-2222-2222-2222-222222222222");
    CircleMembership membership =
        CircleMembership.builder()
            .memberIdA(memberIdA)
            .memberIdB(memberIdB)
            .circleType(CircleType.CONNECTED)
            .memberSince(OffsetDateTime.now())
            .build();

    // When
    UUID partnerId = membership.partnerIdFor(memberIdB);

    // Then
    assertThat(partnerId).isEqualTo(memberIdA);
  }

  @Test
  void builder_WithAllFields_CreatesImmutableValueObject() {
    // Given
    UUID memberIdA = UUID.randomUUID();
    UUID memberIdB = UUID.randomUUID();
    CircleType circleType = CircleType.TRUSTED;
    OffsetDateTime memberSince = OffsetDateTime.now();

    // When
    CircleMembership membership =
        CircleMembership.builder()
            .memberIdA(memberIdA)
            .memberIdB(memberIdB)
            .circleType(circleType)
            .memberSince(memberSince)
            .build();

    // Then
    assertThat(membership.getMemberIdA()).isEqualTo(memberIdA);
    assertThat(membership.getMemberIdB()).isEqualTo(memberIdB);
    assertThat(membership.getCircleType()).isEqualTo(circleType);
    assertThat(membership.getMemberSince()).isEqualTo(memberSince);
  }

  @Test
  void toBuilder_AllowsCopyingWithChanges() {
    // Given
    UUID memberIdA = UUID.randomUUID();
    UUID memberIdB = UUID.randomUUID();
    OffsetDateTime memberSince = OffsetDateTime.now();

    CircleMembership original =
        CircleMembership.builder()
            .memberIdA(memberIdA)
            .memberIdB(memberIdB)
            .circleType(CircleType.TRUSTED)
            .memberSince(memberSince)
            .build();

    // When
    CircleMembership modified = original.toBuilder().circleType(CircleType.CONNECTED).build();

    // Then
    assertThat(modified.getMemberIdA()).isEqualTo(original.getMemberIdA());
    assertThat(modified.getMemberIdB()).isEqualTo(original.getMemberIdB());
    assertThat(modified.getMemberSince()).isEqualTo(original.getMemberSince());
    assertThat(modified.getCircleType()).isEqualTo(CircleType.CONNECTED);
  }

  @Test
  void partnerIdFor_WithSameIdAsEitherMember_ReturnsCorrectPartner() {
    // Given
    UUID singleId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    UUID otherId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    CircleMembership membership1 =
        CircleMembership.builder()
            .memberIdA(singleId)
            .memberIdB(otherId)
            .circleType(CircleType.TRUSTED)
            .memberSince(OffsetDateTime.now())
            .build();

    CircleMembership membership2 =
        CircleMembership.builder()
            .memberIdA(otherId)
            .memberIdB(singleId)
            .circleType(CircleType.CONNECTED)
            .memberSince(OffsetDateTime.now())
            .build();

    // When/Then - both should return the other member correctly
    assertThat(membership1.partnerIdFor(singleId)).isEqualTo(otherId);
    assertThat(membership2.partnerIdFor(singleId)).isEqualTo(otherId);
    assertThat(membership1.partnerIdFor(otherId)).isEqualTo(singleId);
    assertThat(membership2.partnerIdFor(otherId)).isEqualTo(singleId);
  }
}
