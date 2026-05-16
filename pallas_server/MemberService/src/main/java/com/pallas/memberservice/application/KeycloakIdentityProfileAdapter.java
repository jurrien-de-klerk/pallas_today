package com.pallas.memberservice.application;

import com.pallas.memberservice.domain.IdentityProfile;
import com.pallas.memberservice.domain.IdentityProfilePort;
import jakarta.ws.rs.NotFoundException;
import java.util.Optional;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Keycloak adapter for {@link IdentityProfilePort}. Fetches profile data from the Keycloak Admin
 * REST API using the subject claim (which equals the Keycloak user UUID).
 */
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
      return Optional.empty();
    }
  }
}
