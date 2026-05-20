-- Initial schema for CommunityService

CREATE TYPE circle_type AS ENUM ('trusted', 'connected');

CREATE TYPE suggestion_status AS ENUM ('pending', 'accepted', 'rejected');

-- Stores pending and historical connection suggestions.
-- All member identifiers are Member Service UUIDs; the CommunityService resolves
-- the Keycloak sub claim to a memberId via the Member Service before any write.
CREATE TABLE connection_suggestions (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    initiator_id uuid NOT NULL,
    target_id uuid NOT NULL,
    target_circle circle_type NOT NULL,
    status suggestion_status NOT NULL DEFAULT 'pending',
    created_at timestamptz NOT NULL DEFAULT now(),
    responded_at timestamptz,

    PRIMARY KEY (id),

    -- Only one pending suggestion may exist between a given pair for the same circle.
    CONSTRAINT uq_pending_suggestion
    UNIQUE (initiator_id, target_id, target_circle)
);

-- Stores accepted circle memberships.
-- The canonical-pair constraint (a < b) guarantees exactly one row per pair and
-- makes the bidirectional relationship implicit.
CREATE TABLE circle_membership (
    member_id_a uuid NOT NULL,
    member_id_b uuid NOT NULL,
    circle_type circle_type NOT NULL,
    member_since timestamptz NOT NULL DEFAULT now(),

    -- (a, b) as the PK enforces that a pair has exactly one relationship level.
    -- Changing circle type is an UPDATE, not an INSERT.
    PRIMARY KEY (member_id_a, member_id_b),
    CONSTRAINT canonical_order CHECK (member_id_a < member_id_b)
);
