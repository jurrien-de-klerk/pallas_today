package com.pallas.memberservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  private static final String SUB = "keycloak-sub-123";
  private static final UUID MEMBER_ID = UUID.randomUUID();
  private static final IdentityProfile PROFILE = new IdentityProfile("Alice", "Smith");
  private static final MemberMapping MAPPING =
      new MemberMapping(MEMBER_ID, SUB, OffsetDateTime.now());

  @Mock private MemberMappingPort memberMappingPort;
  @Mock private IdentityProfilePort identityProfilePort;

  @InjectMocks private MemberService memberService;

  // --- getCurrentMember ---

  @Test
  void getCurrentMember_existingMapping_returnsMemberWithoutCreatingNewMapping() {
    when(memberMappingPort.findOrCreateBySub(eq(SUB), any())).thenReturn(MAPPING);
    when(identityProfilePort.findBySub(SUB)).thenReturn(Optional.of(PROFILE));

    Member result = memberService.getCurrentMember(SUB);

    assertThat(result.getMemberId()).isEqualTo(MEMBER_ID);
    assertThat(result.getFirstName()).isEqualTo(PROFILE.firstName());
    assertThat(result.getLastName()).isEqualTo(PROFILE.lastName());
    verify(memberMappingPort).findOrCreateBySub(eq(SUB), any());
  }

  @Test
  void getCurrentMember_noExistingMapping_createsAndPersistsNewMapping() {
    MemberMapping newMapping = MemberMapping.newMapping(SUB);
    when(memberMappingPort.findOrCreateBySub(eq(SUB), any())).thenReturn(newMapping);
    when(identityProfilePort.findBySub(SUB)).thenReturn(Optional.of(PROFILE));

    Member result = memberService.getCurrentMember(SUB);

    verify(memberMappingPort).findOrCreateBySub(eq(SUB), any());
    assertThat(result.getFirstName()).isEqualTo(PROFILE.firstName());
    assertThat(result.getLastName()).isEqualTo(PROFILE.lastName());
  }

  @Test
  void getCurrentMember_subNotFoundInIdentityProvider_throwsMemberNotFoundException() {
    when(memberMappingPort.findOrCreateBySub(eq(SUB), any())).thenReturn(MAPPING);
    when(identityProfilePort.findBySub(SUB)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> memberService.getCurrentMember(SUB))
        .isInstanceOf(MemberNotFoundException.class);
  }

  // --- getMember ---

  @Test
  void getMember_knownId_returnsMember() {
    when(memberMappingPort.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(MAPPING));
    when(identityProfilePort.findBySub(SUB)).thenReturn(Optional.of(PROFILE));

    Member result = memberService.getMember(MEMBER_ID);

    assertThat(result.getMemberId()).isEqualTo(MEMBER_ID);
    assertThat(result.getFirstName()).isEqualTo(PROFILE.firstName());
  }

  @Test
  void getMember_unknownId_throwsMemberNotFoundException() {
    when(memberMappingPort.findByMemberId(MEMBER_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> memberService.getMember(MEMBER_ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @Test
  void getMember_mappingExistsButSubGoneFromIdp_throwsMemberNotFoundException() {
    when(memberMappingPort.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(MAPPING));
    when(identityProfilePort.findBySub(SUB)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> memberService.getMember(MEMBER_ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  // --- getMembers ---

  @Test
  void getMembers_mixedIds_returnsOnlyFoundMembers() {
    UUID unknownId = UUID.randomUUID();
    UUID knownId2 = UUID.randomUUID();
    String sub2 = "sub-2";
    MemberMapping mapping2 = new MemberMapping(knownId2, sub2, OffsetDateTime.now());
    IdentityProfile profile2 = new IdentityProfile("Bob", "Jones");

    when(memberMappingPort.findByMemberIds(List.of(MEMBER_ID, unknownId, knownId2)))
        .thenReturn(List.of(MAPPING, mapping2));
    when(identityProfilePort.findBySub(SUB)).thenReturn(Optional.of(PROFILE));
    when(identityProfilePort.findBySub(sub2)).thenReturn(Optional.of(profile2));

    List<Member> results = memberService.getMembers(List.of(MEMBER_ID, unknownId, knownId2));

    assertThat(results).hasSize(2);
    assertThat(results)
        .extracting(Member::getFirstName)
        .containsExactlyInAnyOrder(PROFILE.firstName(), profile2.firstName());
  }

  @Test
  void getMembers_allUnknownIds_returnsEmptyList() {
    when(memberMappingPort.findByMemberIds(any())).thenReturn(List.of());

    List<Member> results = memberService.getMembers(List.of(UUID.randomUUID()));

    assertThat(results).isEmpty();
  }
}
