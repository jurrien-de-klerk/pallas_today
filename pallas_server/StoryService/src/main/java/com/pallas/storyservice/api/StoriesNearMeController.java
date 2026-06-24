package com.pallas.storyservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.storyservice.application.StoryApplicationService;
import com.pallas.storyservice.model.SharedWith;
import com.pallas.storyservice.model.StoriesPage;
import com.pallas.storyservice.model.Story;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link StoriesNearMeApi} interface. */
@CustomLog
@RestController
@RequiredArgsConstructor
public class StoriesNearMeController implements StoriesNearMeApi {

  private final StoryApplicationService storyApplicationService;

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "ObjectMapper is thread-safe and intended to be shared as a Spring bean")
  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<StoriesPage> getStoriesNearMe(Integer offset, Integer count) {
    // Delegate to application service with defaults
    var stories =
        storyApplicationService.getStoriesNearMe(
            offset != null ? offset : 0, count != null ? count : 20);

    // Map domain stories to API model
    StoriesPage page = new StoriesPage();
    page.setStories(stories.stream().map(this::toApiStory).toList());

    return ResponseEntity.ok(page);
  }

  private Story toApiStory(com.pallas.storyservice.domain.Story domainStory) {
    Story apiStory = new Story();
    apiStory.setId(domainStory.getId());
    apiStory.setAuthorId(domainStory.getAuthorId());
    List<Object> deltaOperations = deserializeContent(domainStory.getContent());
    apiStory.setContent(deltaOperations);
    apiStory.setSharedWith(SharedWith.fromValue(domainStory.getSharedWith().name()));
    apiStory.setPublishedAt(domainStory.getPublishedAt());
    return apiStory;
  }

  @SuppressWarnings("unchecked")
  private List<Object> deserializeContent(String contentJson) {
    if (contentJson == null || contentJson.isBlank()) {
      log.error("Story content is null or blank");
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deserialize story content");
    }
    try {
      return objectMapper.readValue(contentJson, List.class);
    } catch (JsonProcessingException ex) {
      log.error("Failed to deserialize story content", ex);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deserialize story content", ex);
    }
  }
}
