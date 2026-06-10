-- Convert PostgreSQL native enum columns to text + CHECK constraints.
-- Normalize stored enum values to uppercase so JPA can use @Enumerated(EnumType.STRING)
-- without custom converters.

-- Remove enum-typed dependencies from V1 before altering column types.
DROP INDEX IF EXISTS uq_pending_suggestion_pending;
ALTER TABLE connection_suggestions
ALTER COLUMN status DROP DEFAULT;

-- connection_suggestions.target_circle
ALTER TABLE connection_suggestions
ALTER COLUMN target_circle TYPE text USING target_circle::text;

UPDATE connection_suggestions
SET target_circle = UPPER(target_circle::text)
WHERE target_circle::text IN ('trusted', 'connected');

ALTER TABLE connection_suggestions
ADD CONSTRAINT chk_connection_suggestions_target_circle
CHECK (target_circle IN ('TRUSTED', 'CONNECTED'));

-- connection_suggestions.status
ALTER TABLE connection_suggestions
ALTER COLUMN status TYPE text USING status::text;

UPDATE connection_suggestions
SET status = UPPER(status::text)
WHERE status::text IN ('pending', 'accepted', 'rejected');

ALTER TABLE connection_suggestions
ADD CONSTRAINT chk_connection_suggestions_status
CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED'));

ALTER TABLE connection_suggestions
ALTER COLUMN status SET DEFAULT 'PENDING';

-- circle_membership.circle_type
ALTER TABLE circle_membership
ALTER COLUMN circle_type TYPE text USING circle_type::text;

UPDATE circle_membership
SET circle_type = UPPER(circle_type::text)
WHERE circle_type::text IN ('trusted', 'connected');

ALTER TABLE circle_membership
ADD CONSTRAINT chk_circle_membership_circle_type
CHECK (circle_type IN ('TRUSTED', 'CONNECTED'));

-- Drop now-unused enum types from V1.
DROP TYPE circle_type;
DROP TYPE suggestion_status;

-- Recreate the partial unique index with text semantics and uppercase status.
CREATE UNIQUE INDEX uq_pending_suggestion_pending
ON connection_suggestions (initiator_id, target_id, target_circle)
WHERE status = 'PENDING';
