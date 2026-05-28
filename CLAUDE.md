
# Must do every time
1. Read `PROJECT_REFERENCE.md` to understand what has been done thus far.
2. Update `PROJECT_REFERENCE.md` with new information as it comes across it.


## Guides
The following needs to be applied:

1. SOLID and DRY principles 
2. Clean Code and Clean Architecture
3. Keep things simple
4. Domain Driven
5. TDD

## Proposed System Design

### Frontend

#### Tech Stack
> - React, TailwindCSS and Shadcn \
> - Componentised

#### Users

> - Learner: Select Topics, learn, add to their profile, earn badges
> - Admin: Amend content, manage users and roles

#### Purpose

> UI to display the learning content

### Backend

#### Tech Stack
> - Java Spring / Spring Modulith

#### Purpose

> Handle Business logic and API between the frontend and Data persistence

#### Possible Architecture
> - Split out domains, so can be migrated to microservices at a later date
> - Gamification, Content etc all needs to be seperated.

### Persistence

#### Tech Stack
> - SQL DB
> - Caching

#### Purpose

> Handle Data persistence for the backend


 