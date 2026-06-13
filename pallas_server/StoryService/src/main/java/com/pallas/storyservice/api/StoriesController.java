package com.pallas.storyservice.api;

import com.pallas.storyservice.application.StoryApplicationService;
import com.pallas.storyservice.domain.SharedWith;
import com.pallas.storyservice.domain.StoryAccessDeniedException;
import com.pallas.storyservice.model.Story;
import com.pallas.storyservice.model.StoryInput;
import java.net.URI;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link StoriesApi} interface. */
@CustomLog
@RestController
@RequiredArgsConstructor
public class StoriesController implements StoriesApi {

  private static final String NOT_IMPLEMENTED = "Endpoint not yet implemented";

  private final StoryApplicationService applicationService;

  @Override
  public ResponseEntity<Story> createStory(StoryInput storyInput) {
    log.info("POST /stories");
    com.pallas.storyservice.domain.Story story =
        applicationService.createStory(
            storyInput.getContent(), SharedWith.valueOf(storyInput.getSharedWith().toString()));
    Story model = toModel(story);
    URI location = URI.create(String.format("/stories/story/%s", story.getId()));
    return ResponseEntity.created(location).body(model);
  }

  @Override
  public ResponseEntity<Void> deleteStory(UUID storyId) {
    log.info("DELETE /stories/story/{}", storyId);
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<Story> getStory(UUID storyId) {
    log.info("GET /stories/story/{}", storyId);
    com.pallas.storyservice.domain.Story story = applicationService.getStory(storyId);
    return ResponseEntity.ok(toModel(story));
  }

  @Override
  public ResponseEntity<Story> updateStory(UUID storyId, StoryInput storyInput) {
    log.info("PUT /stories/story/{}", storyId);
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, NOT_IMPLEMENTED);
  }

  /**
   * Exception handler for access denied or not found errors.
   *
   * <p>Both scenarios (story not found and access denied) throw {@link StoryAccessDeniedException}
   * and should return 404 to prevent information leakage.
   */
  @ExceptionHandler(StoryAccessDeniedException.class)
  public ResponseEntity<Void> handleAccessDenied(StoryAccessDeniedException ex) {
    log.debug("Story access denied or not found: {}", ex.getMessage());
    return ResponseEntity.notFound().build();
  }

  // -------------------------------------------------------------------------
  // Private helpers
  // -------------------------------------------------------------------------

  private Story toModel(com.pallas.storyservice.domain.Story domain) {
    Story model = new Story();
    model.setId(domain.getId());
    model.setAuthorId(domain.getAuthorId());
    model.setContent(domain.getContent());
    model.setSharedWith(
        com.pallas.storyservice.model.SharedWith.valueOf(domain.getSharedWith().toString()));
    model.setPublishedAt(domain.getPublishedAt());
    return model;
  }
}
