package com.pallas.storyservice.controller;

import com.pallas.storyservice.api.StoriesApi;
import com.pallas.storyservice.model.SharedWith;
import com.pallas.storyservice.model.Story;
import com.pallas.storyservice.model.StoryInput;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
public class StoriesController implements StoriesApi {

  private final ConcurrentHashMap<UUID, Story> storiesDb = new ConcurrentHashMap<>();

  @Override
  public ResponseEntity<Story> createStory(StoryInput storyInput) {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Story story = new Story();
    UUID id = UUID.randomUUID();
    story.setId(id);
    story.setAuthorId(UUID.fromString(jwt.getSubject()));
    story.setContent(storyInput.getContent());
    story.setSharedWith(
        storyInput.getSharedWith() != null ? storyInput.getSharedWith() : SharedWith.TRUSTED);
    story.setPublishedAt(OffsetDateTime.now());

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
  public ResponseEntity<Story> updateStory(UUID id, StoryInput storyInput) {
    log.info("Update story request received");
    Story story = storiesDb.get(id);
    if (story == null) {
      log.warn("Update requested for unknown story");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
    }

    story.setContent(storyInput.getContent());
    if (storyInput.getSharedWith() != null) {
      story.setSharedWith(storyInput.getSharedWith());
    }
    storiesDb.put(id, story);
    log.info("Story updated");

    return ResponseEntity.ok(story);
  }
}
