package com.pallas.storyservice.controller;

import com.pallas.storyservice.api.StoriesNearYouApi;
import com.pallas.storyservice.model.StoriesNearYouPage;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StoriesNearYouController implements StoriesNearYouApi {

  @Override
  public ResponseEntity<StoriesNearYouPage> getStoriesNearYou(
      UUID memberId, Integer limit, OffsetDateTime before) {
    log.info("Get stories near you request received");
    StoriesNearYouPage page = new StoriesNearYouPage();
    page.setStories(List.of());
    return ResponseEntity.ok(page);
  }
}
