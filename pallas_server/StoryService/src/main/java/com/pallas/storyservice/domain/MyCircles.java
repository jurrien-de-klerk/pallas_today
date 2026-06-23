package com.pallas.storyservice.domain;

import java.util.List;
import java.util.UUID;

/** The authenticated member's circles containing trusted and connected members. */
public record MyCircles(List<UUID> trustedMembers, List<UUID> connectedMembers) {

  public MyCircles {
    trustedMembers = List.copyOf(trustedMembers);
    connectedMembers = List.copyOf(connectedMembers);
  }
}
