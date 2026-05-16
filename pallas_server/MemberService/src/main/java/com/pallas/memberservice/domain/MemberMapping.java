package com.pallas.memberservice.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Immutable domain representation of the sub → MemberId mapping. */
public record MemberMapping(UUID memberId, String keycloakSub, OffsetDateTime createdAt) {

  public static MemberMapping newMapping(String keycloakSub) {
    return new MemberMapping(UUID.randomUUID(), keycloakSub, OffsetDateTime.now());
  }
}
