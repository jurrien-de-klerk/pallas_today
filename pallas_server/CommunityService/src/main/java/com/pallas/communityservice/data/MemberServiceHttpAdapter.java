package com.pallas.communityservice.data;

import com.pallas.communityservice.client.ApiClient;
import com.pallas.communityservice.client.ApiException;
import com.pallas.communityservice.client.memberservice.MembersApi;
import com.pallas.communityservice.domain.MemberServicePort;
import com.pallas.communityservice.exception.MemberServiceUnavailableException;
import java.util.UUID;
import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * HTTP adapter that calls the Member Service to resolve the authenticated user's domain memberId.
 *
 * <p>Uses the generated OpenAPI client to call {@code GET /members/me} with the current user's
 * bearer token forwarded as-is, so the Member Service can identify the principal from the JWT.
 */
@CustomLog
@Component
public class MemberServiceHttpAdapter implements MemberServicePort {

  private final String baseUrl;

  public MemberServiceHttpAdapter(
      @Value("${member-service.base-url:http://localhost:8081}") String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public UUID resolveCurrentMemberId(String bearerToken) {
    log.debug("resolveCurrentMemberId: calling member service");
    try {
      ApiClient apiClient = new ApiClient();
      apiClient.setBasePath(baseUrl);
      apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, bearerToken);

      MembersApi api = new MembersApi(apiClient);
      var member = api.getAuthenticatedMember();

      if (member == null || member.getMemberId() == null) {
        throw new MemberServiceUnavailableException("Member Service returned empty response");
      }
      log.debug("resolveCurrentMemberId: resolved");
      return member.getMemberId();
    } catch (ApiException e) {
      throw new MemberServiceUnavailableException(
          "Member Service returned error: " + e.getCode(), e);
    }
  }
}
