package com.pallas.memberservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configures the Keycloak Admin Client used by the identity-profile adapter. */
@Configuration
public class KeycloakAdminConfig {

  @Bean
  public Keycloak keycloakAdminClient(
      @Value("${keycloak.auth-server-url}") String authServerUrl,
      @Value("${keycloak.realm}") String realm,
      @Value("${keycloak.admin-client-id}") String clientId,
      @Value("${keycloak.admin-client-secret}") String clientSecret) {
    return KeycloakBuilder.builder()
        .serverUrl(authServerUrl)
        .realm(realm)
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .build();
  }
}
