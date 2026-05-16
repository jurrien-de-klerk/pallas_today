package com.pallas.memberservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Port for persisting and querying the sub → MemberId identity mapping. */
public interface MemberMappingPort {

  /**
   * Find a mapping by Keycloak subject claim.
   *
   * @param keycloakSub the {@code sub} claim from the access token
   * @return the mapping, or empty if not yet registered
   */
  Optional<MemberMapping> findBySub(String keycloakSub);

  /**
   * Find a mapping by domain-owned member ID.
   *
   * @param memberId the domain member ID
   * @return the mapping, or empty if not found
   */
  Optional<MemberMapping> findByMemberId(UUID memberId);

  /**
   * Find mappings for a batch of domain-owned member IDs. Unknown IDs are silently omitted.
   *
   * @param memberIds the domain member IDs to look up
   * @return mappings found; IDs without a mapping are omitted
   */
  List<MemberMapping> findByMemberIds(List<UUID> memberIds);

  /**
   * Persist a new mapping. Must only be called when no mapping exists for the given sub.
   *
   * @param mapping the new mapping to save
   * @return the saved mapping
   */
  MemberMapping save(MemberMapping mapping);
}
