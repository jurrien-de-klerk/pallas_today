package com.pallas.memberservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

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
   * Return the existing mapping for {@code keycloakSub}, or create and persist one using {@code
   * creator} if none exists yet.
   *
   * <p>Creation is not atomic with respect to the surrounding transaction: a concurrent caller that
   * wins the INSERT will cause this call to throw {@link
   * org.springframework.dao.DataIntegrityViolationException} (unique-constraint violation on {@code
   * keycloak_sub}). The {@code GlobalExceptionHandler} maps that to HTTP 409 so the client can
   * retry; on retry the mapping will already exist and be returned immediately.
   *
   * @param keycloakSub the {@code sub} claim from the validated access token
   * @param creator supplier that produces the new {@link MemberMapping} when none exists yet;
   *     invoked at most once per call
   * @return the persisted mapping — either the pre-existing one or the newly created one
   * @throws org.springframework.dao.DataIntegrityViolationException on concurrent first-creation
   */
  MemberMapping findOrCreateBySub(String keycloakSub, Supplier<MemberMapping> creator);
}
