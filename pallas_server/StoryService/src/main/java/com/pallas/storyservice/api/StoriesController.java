package com.pallas.storyservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.storyservice.application.StoryApplicationService;
import com.pallas.storyservice.domain.SharedWith;
import com.pallas.storyservice.domain.StoryAccessDeniedException;
import com.pallas.storyservice.model.Story;
import com.pallas.storyservice.model.StoryInput;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "ObjectMapper is thread-safe and intended to be shared as a Spring bean")
  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<Story> createStory(StoryInput storyInput) {
    @SuppressWarnings("unchecked")
    List<Object> deltaOperations = (List<Object>) storyInput.getContent();
    log.info("POST /stories — received {} Quill Delta operations", deltaOperations.size());
    String contentJson = serializeContent(deltaOperations);
    com.pallas.storyservice.domain.Story story =
        applicationService.createStory(
            contentJson, SharedWith.valueOf(storyInput.getSharedWith().toString()));
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

  /**
   * Exception handler for JSON deserialization errors.
   *
   * <p>Logs detailed information about the cause of deserialization failures to help diagnose
   * malformed requests or invalid enum values.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    log.error("Request body deserialization failed", ex);
    Throwable cause = ex.getCause();
    if (cause != null) {
      log.debug("Deserialization cause: {}", cause.getMessage());
    }
    return ResponseEntity.badRequest()
        .body(
            Map.of(
                "error",
                "Invalid request body",
                "message",
                ex.getMessage() != null ? ex.getMessage() : "Malformed JSON"));
  }

  // -------------------------------------------------------------------------
  // Private helpers
  // -------------------------------------------------------------------------

  private Story toModel(com.pallas.storyservice.domain.Story domain) {
    Story model = new Story();
    model.setId(domain.getId());
    model.setAuthorId(domain.getAuthorId());
    List<Object> deltaOperations = deserializeContent(domain.getContent());
    model.setContent(deltaOperations);
    model.setSharedWith(
        com.pallas.storyservice.model.SharedWith.valueOf(domain.getSharedWith().toString()));
    model.setPublishedAt(domain.getPublishedAt());
    return model;
  }

  private String serializeContent(List<Object> deltaOperations) {
    try {
      return objectMapper.writeValueAsString(deltaOperations);
    } catch (JsonProcessingException ex) {
      log.error("Failed to serialize Quill Delta operations", ex);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Failed to serialize story content", ex);
    }
  }

  @SuppressWarnings("unchecked")
  private List<Object> deserializeContent(String contentJson) {
    try {
      return objectMapper.readValue(contentJson, List.class);
    } catch (JsonProcessingException ex) {
      log.error("Failed to deserialize story content", ex);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deserialize story content", ex);
    }
  }
}
