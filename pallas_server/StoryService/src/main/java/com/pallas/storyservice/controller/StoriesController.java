package com.pallas.storyservice.controller;

import com.pallas.storyservice.api.StoriesApi;
import com.pallas.storyservice.model.ListStories200Response;
import com.pallas.storyservice.model.Story;
import com.pallas.storyservice.model.StoryInput;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
public class StoriesController implements StoriesApi {

  private static final int DEFAULT_LIMIT = 20;
  private static final int MAX_LIMIT = 100;

  private final ConcurrentHashMap<UUID, Story> storiesDb = new ConcurrentHashMap<>();

  @Override
  public ResponseEntity<Story> createStory(StoryInput storyInput) {
    Story story = new Story();
    UUID id = UUID.randomUUID();
    story.setId(id);
    story.setStory(storyInput.getStory());

    storiesDb.put(id, story);
    log.info("Story created");

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    return ResponseEntity.created(location).body(story);
  }

  @Override
  public ResponseEntity<Void> deleteStory(UUID id) {
    Story removed = storiesDb.remove(id);
    if (removed == null) {
      log.warn("Delete requested for unknown story");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
    }
    log.info("Story deleted");
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Story> getStory(UUID id) {
    log.info("Get story request received");
    Story story = storiesDb.get(id);
    if (story == null) {
      log.warn("Get requested for unknown story");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
    }
    return ResponseEntity.ok(story);
  }

  @Override
  public ResponseEntity<ListStories200Response> listStories(Integer limit, Integer offset) {
    log.info("List stories request received");
    // Validate offset and limit parameters
    int actualOffset = offset != null ? offset : 0;
    int actualLimit = limit != null ? limit : DEFAULT_LIMIT;

    if (actualOffset < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "offset must be non-negative");
    }
    if (actualLimit < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be non-negative");
    }

    // Apply max limit constraint
    actualLimit = Math.min(actualLimit, MAX_LIMIT);

    // Get all stories and sort deterministically by UUID
    List<Story> allStories = new ArrayList<>(storiesDb.values());
    allStories.sort((s1, s2) -> s1.getId().compareTo(s2.getId()));

    int start = actualOffset;
    int end = Math.min(start + actualLimit, allStories.size());

    // Clamp start to valid range
    if (start > allStories.size()) {
      start = allStories.size();
    }

    List<Story> paginatedStories = allStories.subList(start, end);
    log.debug("Returning {} of {} stories (offset={}, limit={})", paginatedStories.size(), allStories.size(), start, actualLimit);

    ListStories200Response response = new ListStories200Response();
    response.setStories(paginatedStories);
    response.setTotal(allStories.size());
    response.setLimit(actualLimit);
    response.setOffset(start);

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Story> updateStory(UUID id, StoryInput storyInput) {
    log.info("Update story request received");
    Story story = storiesDb.get(id);
    if (story == null) {
      log.warn("Update requested for unknown story");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
    }

    story.setStory(storyInput.getStory());
    storiesDb.put(id, story);
    log.info("Story updated");

    return ResponseEntity.ok(story);
  }
}
