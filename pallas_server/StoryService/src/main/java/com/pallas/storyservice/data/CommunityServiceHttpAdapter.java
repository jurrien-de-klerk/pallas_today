package com.pallas.storyservice.data;

import com.pallas.communityservice.client.ApiClient;
import com.pallas.communityservice.client.ApiException;
import com.pallas.communityservice.client.communityservice.RelationshipsApi;
import com.pallas.storyservice.domain.CommunityServicePort;
import com.pallas.storyservice.domain.RelationshipType;
import java.util.UUID;
import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * HTTP adapter that calls the Community Service to retrieve relationship information.
 *
 * <p>Uses the generated OpenAPI client to call {@code GET
 * /communities/members/{memberId}/relationship} with the current user's bearer token forwarded
 * as-is, so the Community Service can identify the principal from the JWT.
 */
@CustomLog
@Component
public class CommunityServiceHttpAdapter implements CommunityServicePort {

  private final ApiClient apiClient;
  private final RelationshipsApi relationshipsApi;

  public CommunityServiceHttpAdapter(
      @Value("${community-service.base-url:http://localhost:8082}") String baseUrl) {
    this.apiClient = new ApiClient();
    this.apiClient.setBasePath(baseUrl);
    this.relationshipsApi = new RelationshipsApi(apiClient);
  }

  @Override
  public RelationshipType getRelationship(UUID memberId, String bearerToken) {
    log.debug("getRelationship: querying relationship with member {}", memberId);
    try {
      apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, bearerToken);
      var response = relationshipsApi.getRelationship(memberId);
      RelationshipType result = toDomainRelationshipType(response.getRelationshipType());
      log.debug("getRelationship: relationship type is {}", result);
      return result;
    } catch (ApiException ex) {
      log.error("getRelationship: error communicating with community service", ex);
      throw new CommunityServiceException("Failed to get relationship from community service", ex);
    }
  }

  // -------------------------------------------------------------------------
  // Mapping helpers
  // -------------------------------------------------------------------------

  private RelationshipType toDomainRelationshipType(
      com.pallas.communityservice.client.communityservice.model.RelationshipType apiType) {
    return switch (apiType) {
      case TRUSTED -> RelationshipType.TRUSTED;
      case CONNECTED -> RelationshipType.CONNECTED;
      case COMMUNITY -> RelationshipType.COMMUNITY;
    };
  }
}
