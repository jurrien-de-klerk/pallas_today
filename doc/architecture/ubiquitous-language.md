# Ubiquitous Language

This glossary defines the domain terms used across **Pallas Today**. All team members and all parts of the codebase
should use these terms consistently.

______________________________________________________________________

## Core domain

### Story

A piece of user-authored rich-text content published to the platform. A story can describe an event in a member's life,
express an emotion, or convey anything the author wishes to share. Visibility is governed by its **Shared With**
modifier.

### Story content

The body of a story, authored by a member using a structured rich-text editor. Stored as a **JSON Delta** document.

### Shared With

The access modifier attached to a story that defines the audience permitted to read it. One of four levels must be
chosen when publishing a story:

- **Trusted** — members in the author's trusted circle (default).
- **Connected** — members in the author's connected circle.
- **Community** — all registered members of the Pallas Community.
- **Public** — everyone, including guests.

Images that belong to a story inherit the same Shared With level as their owning story.

### Stories near you

The personalised feed shown to a member, aggregating stories published by the member themselves and by members in their
trusted and connected circles. Stories are ordered chronologically, newest first. When browsing another member's Stories
near you board, only stories whose Shared With level permits the visiting member to see them are included.

### Publish a story

The action a member takes to create a new story and make it available to their selected audience on the platform.

______________________________________________________________________

## Member network

### Member

A registered participant of Pallas Today. The primary identity within the domain. Identified across all services by an
opaque `memberId` owned and issued by the Member Service. The Member Service resolves the mapping between the Keycloak
identity (OIDC `sub` claim) and the `memberId` internally, decoupling the rest of the platform from the identity
provider. Detailed profile data is owned by the Member Service.

### Pallas Community

The complete set of all members on the platform. Every member belongs to the Pallas Community. Members within the
community can discover one another, but access is limited by relationship type.

### Trusted circle

A member's set of close, intimate connections. Membership is bidirectional: if A is in B's trusted circle, B is in A's
trusted circle.

### Connected circle

A member's set of connections at a lower level of intimacy than the trusted circle. Membership is bidirectional.

### Trusted members

Two members who are in each other's trusted circle. Represents the strongest relationship type on the platform.

### Connected members

Two members who are in each other's connected circle.

### Community members

Members of the Pallas Community who share no direct circle membership with each other. They can discover one another but
have the most limited mutual access.

### Guests

People who are not members of the Pallas Community. They have the least access to platform content and features.

### Connection suggestion

A request sent by one member to another to upgrade their relationship to a closer circle. The receiving member must
explicitly accept or reject it. A connection suggestion is only created when moving to a closer relationship;
downgrading a connection requires no suggestion and no consent.

### Strengthening the bond

The act of moving a relationship with another member to a closer circle. Requires the other member's consent via a
connection suggestion.

### Easing the relationship

The act of moving a relationship with another member to a more distant circle, or removing it entirely. Applied
immediately without requiring consent from the other member.
