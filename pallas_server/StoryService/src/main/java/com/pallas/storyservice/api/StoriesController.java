package com.pallas.storyservice.api;

import com.pallas.storyservice.model.Story;
import com.pallas.storyservice.model.StoryInput;
import java.util.UUID;
import lombok.CustomLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link StoriesApi} interface. */
@CustomLog
@RestController
public class StoriesController implements StoriesApi {

  private static final String NOT_IMPLEMENTED = "Endpoint not yet implemented";

  @Override
  public ResponseEntity<Story> createStory(StoryInput storyInput) {
    log.info("POST /stories");
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<Void> deleteStory(UUID storyId) {
    log.info("DELETE /stories/story/{}", storyId);
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<Story> getStory(UUID storyId) {
    log.info("GET /stories/story/{}", storyId);
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<Story> updateStory(UUID storyId, StoryInput storyInput) {
    log.info("PUT /stories/story/{}", storyId);
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, NOT_IMPLEMENTED);
  }
}
