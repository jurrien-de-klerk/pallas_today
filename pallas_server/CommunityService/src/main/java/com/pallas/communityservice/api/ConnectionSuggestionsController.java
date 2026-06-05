package com.pallas.communityservice.api;

import com.pallas.communityservice.application.CommunityApplicationService;
import com.pallas.communityservice.domain.CircleType;
import com.pallas.communityservice.domain.ConnectionSuggestion;
import com.pallas.communityservice.domain.SuggestionDecision;
import com.pallas.communityservice.model.ConnectionSuggestionInput;
import com.pallas.communityservice.model.ConnectionSuggestionResponse;
import com.pallas.communityservice.model.ListConnectionSuggestions200Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** REST controller implementing the generated {@link ConnectionSuggestionsApi} interface. */
@CustomLog
@RestController
@RequiredArgsConstructor
public class ConnectionSuggestionsController implements ConnectionSuggestionsApi {

  private final CommunityApplicationService applicationService;

  @Override
  public ResponseEntity<com.pallas.communityservice.model.ConnectionSuggestion>
      createConnectionSuggestion(ConnectionSuggestionInput input) {
    log.info("POST /communities/connection-suggestions");
    CircleType circle = toCircleType(input.getTargetCircle());
    ConnectionSuggestion created =
        applicationService.createConnectionSuggestion(input.getTargetMemberId(), circle);
    com.pallas.communityservice.model.ConnectionSuggestion model = toModel(created);
    URI location = URI.create("/communities/connection-suggestions/" + created.getId());
    return ResponseEntity.created(location).body(model);
  }

  @Override
  public ResponseEntity<ListConnectionSuggestions200Response> listConnectionSuggestions() {
    log.info("GET /communities/connection-suggestions");
    List<com.pallas.communityservice.model.ConnectionSuggestion> models =
        applicationService.listIncomingSuggestions().stream().map(this::toModel).toList();
    ListConnectionSuggestions200Response response = new ListConnectionSuggestions200Response();
    response.setSuggestions(models);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<com.pallas.communityservice.model.ConnectionSuggestion>
      respondToConnectionSuggestion(
          UUID id, ConnectionSuggestionResponse connectionSuggestionResponse) {
    log.info("PUT /communities/connection-suggestions/{id}/response");
    SuggestionDecision decision = toDecision(connectionSuggestionResponse.getDecision());
    ConnectionSuggestion updated = applicationService.respondToSuggestion(id, decision);
    return ResponseEntity.ok(toModel(updated));
  }

  // -------------------------------------------------------------------------
  // Mapping helpers
  // -------------------------------------------------------------------------

  private com.pallas.communityservice.model.ConnectionSuggestion toModel(
      ConnectionSuggestion domain) {
    com.pallas.communityservice.model.ConnectionSuggestion model =
        new com.pallas.communityservice.model.ConnectionSuggestion();
    model.setId(domain.getId());
    model.setInitiatorId(domain.getInitiatorId());
    model.setTargetMemberId(domain.getTargetId());
    model.setTargetCircle(toModelCircleType(domain.getTargetCircle()));
    model.setStatus(toModelStatus(domain.getStatus()));
    model.setCreatedAt(domain.getCreatedAt());
    model.setRespondedAt(domain.getRespondedAt());
    return model;
  }

  private CircleType toCircleType(com.pallas.communityservice.model.CircleType model) {
    return switch (model) {
      case TRUSTED -> CircleType.TRUSTED;
      case CONNECTED -> CircleType.CONNECTED;
    };
  }

  private com.pallas.communityservice.model.CircleType toModelCircleType(CircleType domain) {
    return switch (domain) {
      case TRUSTED -> com.pallas.communityservice.model.CircleType.TRUSTED;
      case CONNECTED -> com.pallas.communityservice.model.CircleType.CONNECTED;
    };
  }

  private com.pallas.communityservice.model.ConnectionSuggestion.StatusEnum toModelStatus(
      com.pallas.communityservice.domain.SuggestionStatus domain) {
    return switch (domain) {
      case PENDING -> com.pallas.communityservice.model.ConnectionSuggestion.StatusEnum.PENDING;
      case ACCEPTED -> com.pallas.communityservice.model.ConnectionSuggestion.StatusEnum.ACCEPTED;
      case REJECTED -> com.pallas.communityservice.model.ConnectionSuggestion.StatusEnum.REJECTED;
    };
  }

  private SuggestionDecision toDecision(
      com.pallas.communityservice.model.SuggestionDecision model) {
    return switch (model) {
      case ACCEPTED -> SuggestionDecision.ACCEPTED;
      case REJECTED -> SuggestionDecision.REJECTED;
    };
  }
}
