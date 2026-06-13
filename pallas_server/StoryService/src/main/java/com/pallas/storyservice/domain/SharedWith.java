package com.pallas.storyservice.domain;

/**
 * The audience permitted to read a story.
 *
 * <ul>
 *   <li>{@code TRUSTED} — members in the author's trusted circle (default)
 *   <li>{@code CONNECTED} — members in the author's connected circle
 *   <li>{@code COMMUNITY} — all registered members of the Pallas Community
 *   <li>{@code PUBLIC} — everyone, including guests
 * </ul>
 */
public enum SharedWith {
  TRUSTED,
  CONNECTED,
  COMMUNITY,
  PUBLIC
}
