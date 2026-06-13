package com.pallas.integrations.member;

import java.util.UUID;

/**
 * Port for resolving the domain Member identifier (UUID) from the authenticated principal's
 * Keycloak sub claim by delegating to the Member Service.
 *
 * <p>Only the current authenticated user's UUID can be resolved via the Member Service because the
 * only available lookup is {@code GET /members/me}, which requires the target user's bearer token.
 * For the other party in a connection suggestion the UUID is therefore stored at the time the
 * suggestion is created or accepted, while the bearer token is available.
 */
public interface MemberServicePort {

  /**
   * Resolve the domain memberId for the currently authenticated user.
   *
   * @param bearerToken the raw {@code Bearer …} header value (or just the token)
   * @return the Member Service UUID for the authenticated principal
   */
  UUID resolveCurrentMemberId(String bearerToken);
}
