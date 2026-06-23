package com.pallas.storyservice.api;

import com.pallas.storyservice.model.StoriesPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link StoriesNearYouApi} interface. */
@RestController
public class StoriesNearMeController implements StoriesNearMeApi {

  @Override
  public ResponseEntity<StoriesPage> getStoriesNearMe(Integer limit, Integer before) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Endpoint not yet implemented");
  }
}
