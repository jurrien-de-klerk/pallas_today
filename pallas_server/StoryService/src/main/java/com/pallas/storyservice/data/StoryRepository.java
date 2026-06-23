package com.pallas.storyservice.data;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<StoryEntity, UUID> {}
