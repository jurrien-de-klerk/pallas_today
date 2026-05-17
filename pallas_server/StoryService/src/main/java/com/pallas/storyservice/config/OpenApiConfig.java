package com.pallas.storyservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class OpenApiConfig {

  @Value("${keycloak.public-url}/realms/${keycloak.realm}")
  private String issuerUri;

  @Bean
  public OpenAPI openAPI() {
    final String authUrl = issuerUri + "/protocol/openid-connect";
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    "oauth2",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(
                            new OAuthFlows()
                                .authorizationCode(
                                    new OAuthFlow()
                                        .authorizationUrl(authUrl + "/auth")
                                        .tokenUrl(authUrl + "/token")
                                        .scopes(new Scopes())))));
  }

  @Bean
  public OpenApiCustomizer securityRequirementCustomizer() {
    return openApi ->
        openApi
            .getPaths()
            .values()
            .forEach(
                pathItem ->
                    pathItem
                        .readOperations()
                        .forEach(
                            operation ->
                                operation.addSecurityItem(
                                    new SecurityRequirement().addList("oauth2"))));
  }
}
