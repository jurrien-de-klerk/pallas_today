package com.pallas.memberservice.domain;

import java.util.Optional;

/** Port for retrieving identity profile data from the identity provider (Keycloak). */
public interface IdentityProfilePort {

  /**
   * Fetch the profile for the given identity-provider subject claim.
   *
   * @param keycloakSub the {@code sub} claim from the access token
   * @return the profile, or empty if the subject is not found in the identity provider
   */
  Optional<IdentityProfile> findBySub(String keycloakSub);
}
