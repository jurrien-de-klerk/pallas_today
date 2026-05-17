package com.pallas.memberservice.domain;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberMappingPort memberMappingPort;
  private final IdentityProfilePort identityProfilePort;

  /**
   * Returns the member for the currently authenticated principal. If this is the first request for
   * the given subject, a new domain MemberId is minted and persisted.
   *
   * @param keycloakSub the {@code sub} claim from the validated access token
   * @return the member's profile
   * @throws MemberNotFoundException if the subject is not found in the identity provider
   */
  @Transactional
  public Member getCurrentMember(String keycloakSub) {
    MemberMapping mapping =
        memberMappingPort.findOrCreateBySub(
            keycloakSub, () -> MemberMapping.newMapping(keycloakSub));
    log.debug("getCurrentMember: mapping resolved");
    return buildMember(mapping);
  }

  /**
   * Returns the member for the given domain-owned member ID.
   *
   * @param memberId the domain member ID
   * @return the member's profile
   * @throws MemberNotFoundException if no member exists for the given ID
   */
  @Transactional(readOnly = true)
  public Member getMember(UUID memberId) {
    log.debug("getMember: looking up mapping");
    MemberMapping mapping =
        memberMappingPort
            .findByMemberId(memberId)
            .orElseThrow(() -> new MemberNotFoundException(memberId));

    return buildMember(mapping);
  }

  /**
   * Returns profiles for a batch of domain-owned member IDs. Unknown IDs are silently omitted.
   *
   * @param memberIds the domain member IDs to look up
   * @return profiles found; IDs without a mapping are omitted
   */
  @Transactional(readOnly = true)
  public List<Member> getMembers(List<UUID> memberIds) {
    log.debug("getMembers: {} ids requested", memberIds.size());
    List<Member> result =
        memberMappingPort.findByMemberIds(memberIds).stream().map(this::buildMember).toList();
    log.debug("getMembers: {} members resolved", result.size());
    return result;
  }

  private Member buildMember(MemberMapping mapping) {
    log.debug("buildMember: fetching identity profile");
    IdentityProfile profile =
        identityProfilePort
            .findBySub(mapping.keycloakSub())
            .orElseThrow(
                () ->
                    new MemberNotFoundException(
                        "Identity profile not found for member: " + mapping.memberId()));

    log.debug("buildMember: profile fetched");
    return new Member(mapping.memberId(), profile.firstName(), profile.lastName());
  }
}
