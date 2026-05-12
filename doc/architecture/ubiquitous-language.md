# Ubiquitous Language

This glossary defines the domain terms used across **Pallas Today**. All team members and all parts of the codebase
should use these terms consistently.

______________________________________________________________________

## Core domain

### Story

A piece of user-authored rich text published to the platform and visible to other users.

### Story content

The textual body of a story, authored by a user.

### Publish a story

The action a user takes to create a new story and make it available on the platform.

______________________________________________________________________

## Member network

### Member

A registered participant of Pallas Today. The primary identity within the domain. Identified across all services by an
opaque `userId` (the OIDC `sub` claim). Detailed profile data is owned by the User Service.

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
