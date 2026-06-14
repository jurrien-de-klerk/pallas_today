package com.pallas.storyservice.api;

import com.pallas.storyservice.model.StoriesNearYouPage;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link StoriesNearYouApi} interface. */
@RestController
public class StoriesNearYouController implements StoriesNearYouApi {

  @Override
  public ResponseEntity<StoriesNearYouPage> getStoriesNearYou(
      UUID memberId, Integer limit, OffsetDateTime before) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Endpoint not yet implemented");
  }
}
