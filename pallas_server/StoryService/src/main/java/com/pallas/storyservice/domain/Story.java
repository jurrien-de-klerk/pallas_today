package com.pallas.storyservice.domain;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Value;

/** A story as known to the Pallas domain. */
@Value
public class Story {
  UUID id;
  UUID authorId;
  String content;
  SharedWith sharedWith;
  OffsetDateTime publishedAt;
}
