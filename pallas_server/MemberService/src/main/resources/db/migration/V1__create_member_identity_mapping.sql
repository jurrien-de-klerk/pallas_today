-- Maps a Keycloak subject claim (sub) to a domain-owned MemberId.
-- The sub is written once on first authenticated request and never changes.
-- This table is the only place in the system that knows about Keycloak identifiers.
CREATE TABLE member_identity_mapping (
    member_id  UUID        NOT NULL,
    keycloak_sub VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT pk_member_identity_mapping PRIMARY KEY (member_id),
    CONSTRAINT uq_member_identity_mapping_sub UNIQUE (keycloak_sub)
);
