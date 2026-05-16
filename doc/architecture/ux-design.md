# UX Design

## Design Vision

Pallas Today members are critical thinkers who seek to understand the world around them. The UX design reflects that
respect: we treat members as capable adults and are transparent about what the app is doing at all times.

### Transparency and feedback

The app communicates its state clearly at every moment:

| App state   | What the member sees                                                                 |
| ----------- | ------------------------------------------------------------------------------------ |
| **Idle**    | No activity indicators; the interface is calm and ready.                             |
| **Loading** | A visible loading indicator for any in-progress data fetch.                          |
| **Success** | Clear confirmation that the member's action completed correctly.                     |
| **Error**   | A clear description of what went wrong, without exposing security-sensitive details. |

Errors are communicated both in occurrence (something went wrong) and in intent (what it means for the member), as long
as doing so does not compromise security.

### Input correction

When the app needs to correct or reject a member's input:

- It explains clearly **why** the input is not accepted.
- It helps the member do the right thing where possible, for example by suggesting valid alternatives.

### Action frequency and interaction cost

Frequently performed actions must be fast and require as few interactions as possible. Rarely performed actions can
require more steps. To apply this principle consistently, every feature action is classified into one of three
categories:

| Category               | Interaction cost target                                 |
| ---------------------- | ------------------------------------------------------- |
| **Most frequent**      | Optimised to the minimum number of taps or clicks.      |
| **Regularly frequent** | Accessible quickly, with acceptable minor friction.     |
| **Rarely performed**   | Correct and clear, but additional steps are acceptable. |

The category assigned to an action drives how much the design optimises for speed versus correctness of intent.

______________________________________________________________________

## Personas

Personas are used to guide UX decisions — in particular for determining the frequency category of an action, how much
explanation to provide, and how to phrase guidance when correcting input.

### Dedicated family member

A member whose primary goal is to stay connected with family. They share family events and spend most of their time
reading similar stories from people close to them. They prefer to spend as little time as possible in the app and are
not primarily motivated by truth-seeking.

- **Typical actions:** publishing a short family story, browsing Stories near you.
- **Design implication:** keep core sharing and reading flows as frictionless as possible.

### Activist

A member who uses the platform to showcase good deeds for their cause and to understand the world better. They are
willing to invest more time in the app and appreciate depth of information.

- **Typical actions:** publishing detailed stories, exploring community content, discovering new members.
- **Design implication:** richer content features and exploration paths are justified for this persona.

### Older member

A member who is less comfortable with technology. They want to follow their children and grandchildren and share
highlights of their own life, such as holiday photos. They prefer to spend as little time on the app as possible and
value simplicity above all.

- **Typical actions:** viewing stories from connected members, publishing a simple story with images.
- **Design implication:** interfaces must be approachable without prior technical knowledge; avoid jargon and hidden
  gestures.

______________________________________________________________________

## Navigation and Information Architecture

The information hierarchy of the app determines how navigation is structured. Top-level destinations correspond to the
main areas of the product; secondary screens are nested beneath them.

**Stories near you is the homepage** for a logged-in member. It is the first screen shown after authentication and
serves as the primary entry point for all interactions.

```text
Pallas App
├── Stories near you  ← homepage for logged-in members
│   ├── Publish a story
│   └── My stories
└── My community
    └── Find other members
```

This hierarchy is kept shallow to minimise the number of taps required to reach any destination, supporting the
frequency-based interaction cost principle described above.

______________________________________________________________________

## Loading Experience

To make the app feel fast, a progressive loading strategy is used (see
[ADR-0013](../adr/0013-app-domain-layer-for-data-aggregation.md)):

- The app domain layer aggregates data from multiple backend services concurrently.
- Data is displayed as soon as it becomes available — the member sees partial content immediately rather than waiting
  for all data to arrive.
- Service calls are made in parallel where possible to minimise total loading time.
- Whenever data is still loading, a visible loading indicator is shown, consistent with the transparency principle
  described above.
