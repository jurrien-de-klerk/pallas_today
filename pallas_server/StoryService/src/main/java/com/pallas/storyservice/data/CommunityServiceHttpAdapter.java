package com.pallas.storyservice.data;

import com.pallas.communityservice.client.ApiClient;
import com.pallas.communityservice.client.ApiException;
import com.pallas.communityservice.client.communityservice.CirclesApi;
import com.pallas.communityservice.client.communityservice.RelationshipsApi;
import com.pallas.communityservice.client.communityservice.model.MemberReference;
import com.pallas.storyservice.domain.CommunityServicePort;
import com.pallas.storyservice.domain.MyCircles;
import com.pallas.storyservice.domain.RelationshipType;
import java.util.List;
import java.util.Optional;
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

  private final String baseUrl;

  public CommunityServiceHttpAdapter(
      @Value("${community-service.base-url:http://localhost:8082}") String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public RelationshipType getRelationship(UUID memberId, String bearerToken) {
    log.debug("getRelationship: calling community service for member {}", memberId);
    try {
      ApiClient apiClient = createAuthenticatedApiClient(bearerToken);
      var response = new RelationshipsApi(apiClient).getRelationship(memberId);
      RelationshipType result = toDomainRelationshipType(response.getRelationshipType());
      log.debug("getRelationship: community service returned {}", result);
      return result;
    } catch (ApiException ex) {
      log.error("getRelationship: community service call failed: {}", ex.getMessage());
      throw new CommunityServiceException("Failed to get relationship from community service", ex);
    }
  }

  @Override
  public MyCircles getMyCircles(String bearerToken) {
    log.debug("getMyCircles: calling community service");
    try {
      ApiClient apiClient = createAuthenticatedApiClient(bearerToken);
      var response = new CirclesApi(apiClient).getMyCircles();
      MyCircles result = toDomainMyCircles(response);
      log.debug(
          "getMyCircles: community service returned circles with {} trusted and {} connected members",
          result.trustedMembers().size(),
          result.connectedMembers().size());
      return result;
    } catch (ApiException ex) {
      log.error("getMyCircles: community service call failed: {}", ex.getMessage());
      throw new CommunityServiceException("Failed to get circles from community service", ex);
    }
  }

  /**
   * Creates a new ApiClient instance with the bearer token set as a default header. Each request
   * gets its own ApiClient to avoid token leakage between concurrent requests.
   */
  private ApiClient createAuthenticatedApiClient(String bearerToken) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(baseUrl);
    apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, bearerToken);
    return apiClient;
  }

  // -------------------------------------------------------------------------
  // Mapping helpers
  // -------------------------------------------------------------------------

  private MyCircles toDomainMyCircles(
      com.pallas.communityservice.client.communityservice.model.Circles apiCircles) {
    return new MyCircles(
        apiCircles.getConnectedCircle().stream()
            .map(MemberReference::getMemberId)
            .toList(),
        apiCircles.getTrustedCircle().stream()
            .map(MemberReference::getMemberId)
            .toList());
  }

  private RelationshipType toDomainRelationshipType(
      com.pallas.communityservice.client.communityservice.model.RelationshipType apiType) {
    return switch (apiType) {
      case TRUSTED -> RelationshipType.TRUSTED;
      case CONNECTED -> RelationshipType.CONNECTED;
      case COMMUNITY -> RelationshipType.COMMUNITY;
    };
  }
}
