package com.pallas.communityservice.domain;

/**
 * The relationship type between two members.
 *
 * <ul>
 *   <li>{@code TRUSTED} — each member is in the other's trusted circle
 *   <li>{@code CONNECTED} — each member is in the other's connected circle
 *   <li>{@code COMMUNITY} — Pallas members with no direct circle membership
 * </ul>
 */
public enum RelationshipType {
  TRUSTED,
  CONNECTED,
  COMMUNITY
}
