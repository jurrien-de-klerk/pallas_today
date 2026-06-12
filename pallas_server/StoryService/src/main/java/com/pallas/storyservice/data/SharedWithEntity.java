package com.pallas.storyservice.data;

/**
 * JPA-layer enum for the audience permitted to read a story.
 *
 * <p>Persisted as text via {@code @Enumerated(EnumType.STRING)}; the constant names match the
 * uppercase values enforced by the {@code chk_stories_shared_with} database constraint.
 */
enum SharedWithEntity {
  TRUSTED,
  CONNECTED,
  COMMUNITY,
  PUBLIC
}
