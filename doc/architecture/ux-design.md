# UX-design

## UX-design vision

- Our member are critical thinkers who try to make sense of the world around them
  - We trust them with what the app is doing
    - Errors area clearly communicated both in occurrence and in intent (as long as security is not compromised)
    - Actions the app performs are clearly communicated, it is clear what the state of the app is:
      - Idle (app is not performing any action)
      - Loading - some data is loading
      - When an action of the user performed correctly
      - when something went wrong
  - If we feel like correcting an input of the user
    - We explain clearly why
    - We help the user to do the right thing (if possible)
- We design the app such that frequently occurring actions are fast and easy and rarely occurring actions still easy but
  require more clicks
  - We divide the actions into 3 categories
    - Most occurring actions
    - regular occurring actions
    - rarely occuring actions
  - We use the categories to determine how optimized a feature must be in terms of click to accomplish the action

## Persona design

- We use personas to help us with ux-design
  - this can help in deciding what category an action belongs to
  - how important it is to correct our member
  - how to explain if we need to explain something
- We will use the next persona:
  - Dedicated family-member:
    - wants to share family events
    - spents reading other family-like stories
    - Doesn't want to spent to much to on the app
    - is not so much interested in seeking the thruth
  - Activist:
    - Want's to show good deeds done for their cause
    - Spents time on understanding the cause and seeing other good deeds
    - Is ok spending a bit more time on the app to understand the world better
  - Older person:
    - Struggles with new technology
    - Wants to follow their childeren and grand children
    - Don't spent to much on their phone and thus not on the app
    - want to show the amazing holidays they have

## Information model

- helps us in deciding on how navigation should work
- Hierarchy diagram:
  - pallas app
    - Stories near you
      - Publish a story
      - My stories
    - My community
      - Find other members

## Loading experience

- To make the app feel fast we use a progressive loading style
  - The domain layer of the app is responsible for aggregating data coming from different services
    - data available in the domain layer should be shown as soon it becomes available
    - data should be loaded as much as possible in parallel
  - As long as the app is loading it should indicating it is doing so
