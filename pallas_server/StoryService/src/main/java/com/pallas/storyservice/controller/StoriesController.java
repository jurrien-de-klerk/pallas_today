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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class StoriesController implements StoriesApi {

  private final ConcurrentHashMap<UUID, Story> storiesDb = new ConcurrentHashMap<>();

  @Override
  public ResponseEntity<Story> createStory(StoryInput storyInput) {
    Story story = new Story();
    UUID id = UUID.randomUUID();
    story.setId(id);
    story.setStory(storyInput.getStory());

    storiesDb.put(id, story);

    return ResponseEntity.created(URI.create("/stories/" + id)).body(story);
  }

  @Override
  public ResponseEntity<Void> deleteStory(UUID id) {
    Story removed = storiesDb.remove(id);
    if (removed == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Story> getStory(UUID id) {
    Story story = storiesDb.get(id);
    if (story == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
    }
    return ResponseEntity.ok(story);
  }

  @Override
  public ResponseEntity<ListStories200Response> listStories(Integer limit, Integer offset) {
    List<Story> allStories = new ArrayList<>(storiesDb.values());

    int start = offset != null ? offset : 0;
    int end = limit != null ? Math.min(start + limit, allStories.size()) : allStories.size();

    if (start > allStories.size()) {
      start = allStories.size();
    }
    if (end > allStories.size()) {
      end = allStories.size();
    }

    List<Story> paginatedStories = allStories.subList(start, end);

    ListStories200Response response = new ListStories200Response();
    response.setStories(paginatedStories);
    response.setTotal(allStories.size());
    response.setLimit(limit != null ? limit : allStories.size());
    response.setOffset(start);

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Story> updateStory(UUID id, StoryInput storyInput) {
    Story story = storiesDb.get(id);
    if (story == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
    }

    story.setStory(storyInput.getStory());
    storiesDb.put(id, story);

    return ResponseEntity.ok(story);
  }
}
