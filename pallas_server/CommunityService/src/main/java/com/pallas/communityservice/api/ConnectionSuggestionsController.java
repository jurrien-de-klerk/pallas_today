package com.pallas.communityservice.api;

import com.pallas.communityservice.model.ConnectionSuggestion;
import com.pallas.communityservice.model.ConnectionSuggestionInput;
import com.pallas.communityservice.model.ConnectionSuggestionResponse;
import com.pallas.communityservice.model.ListConnectionSuggestions200Response;
import java.util.UUID;
import lombok.CustomLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link ConnectionSuggestionsApi} interface. */
@CustomLog
@RestController
public class ConnectionSuggestionsController implements ConnectionSuggestionsApi {

  @Override
  public ResponseEntity<ConnectionSuggestion> createConnectionSuggestion(
      ConnectionSuggestionInput connectionSuggestionInput) {
    log.info("POST /connection-suggestions");
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<ListConnectionSuggestions200Response> listConnectionSuggestions() {
    log.info("GET /connection-suggestions");
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<ConnectionSuggestion> respondToConnectionSuggestion(
      UUID id, ConnectionSuggestionResponse connectionSuggestionResponse) {
    log.info("PUT /connection-suggestions/{id}/response");
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }
}
