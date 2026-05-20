package com.pallas.memberservice.application;

import com.pallas.memberservice.domain.IdentityProfile;
import com.pallas.memberservice.domain.IdentityProfilePort;
import jakarta.ws.rs.NotFoundException;
import java.util.Optional;
import lombok.CustomLog;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Keycloak adapter for {@link IdentityProfilePort}. Fetches profile data from the Keycloak Admin
 * REST API using the subject claim (which equals the Keycloak user UUID).
 */
@CustomLog
@Component
public class KeycloakIdentityProfileAdapter implements IdentityProfilePort {

  private final Keycloak keycloak;
  private final String realm;

  public KeycloakIdentityProfileAdapter(
      Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
    this.keycloak = keycloak;
    this.realm = realm;
  }

  @Override
  public Optional<IdentityProfile> findBySub(String keycloakSub) {
    try {
      UserRepresentation user = keycloak.realm(realm).users().get(keycloakSub).toRepresentation();
      return Optional.of(new IdentityProfile(user.getFirstName(), user.getLastName()));
    } catch (NotFoundException e) {
      // A subject that exists in our mapping table has no corresponding user in Keycloak.
      // This is a data consistency failure, not a normal 404 — log at error so a backtrace
      // is written to aid diagnosis (ADR-0014).
      log.error("Keycloak user not found for subject present in member mapping");
      log.backtrace();
      return Optional.empty();
    }
  }
}
