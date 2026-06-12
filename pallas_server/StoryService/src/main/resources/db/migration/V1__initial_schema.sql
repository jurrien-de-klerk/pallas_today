-- Initial schema for StoryService

-- Enable pgcrypto extension for gen_random_uuid() function.
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Stores published stories.
-- All member identifiers are Member Service UUIDs; the StoryService resolves the
-- Keycloak sub claim to a memberId via the Member Service before any write.
--
-- The shared_with column is stored as text with a CHECK constraint rather than a
-- native PostgreSQL enum. Native enums require a custom JPA type or value
-- conversion to align with @Enumerated(EnumType.STRING); using text with
-- uppercase values that match the Java enum names avoids that mismatch entirely.
CREATE TABLE stories (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    author_id uuid NOT NULL,
    story_content text NOT NULL,
    shared_with text NOT NULL DEFAULT 'TRUSTED',
    published_at timestamptz NOT NULL DEFAULT now(),

    PRIMARY KEY (id),
    CONSTRAINT chk_stories_shared_with
    CHECK (shared_with IN ('TRUSTED', 'CONNECTED', 'COMMUNITY', 'PUBLIC'))
);

-- Supports the "Stories near you" feed: stories are fetched per author and
-- ordered by publication time, newest first.
CREATE INDEX idx_stories_author_published_at
ON stories (author_id, published_at DESC);
