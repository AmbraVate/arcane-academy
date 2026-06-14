---
id: fe-jun-m4-06
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m4
moduleTitle: "Module 4: Routing and Navigation"
moduleGlyph: "🗺️"
moduleSortOrder: 4
topicSlug: react_router
topicTitle: "React Router"
topicSortOrder: 2
lesson: route_parameters
title: "Route Parameters"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines URL parameters with :paramName"
    - "Reads parameters with useParams()"
    - "Uses query strings (useSearchParams) for optional filters"
    - "Understands the difference between URL params and query params"
  keywords: [useParams, useSearchParams, ":id", dynamic, query-string, URLSearchParams, filter, optional]
  modelAnswer: |
    URL parameters (:id in the route path) are required segments that identify a resource.
    Read them with useParams() — returns { id: 'value' }. Query strings (?sort=asc&page=2)
    are optional key-value pairs for filtering and pagination. Read with useSearchParams()
    which returns a URLSearchParams object and a setter. Update with setSearchParams()
    without navigating away.
guidedSteps:
  - id: fe-jun-m4-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      URL: /users/42/posts?sort=newest. What does useParams() return?
    inputConfig:
      options:
        - "{ id: '42', sort: 'newest' }"
        - "{ userId: '42' } (assuming Route path='/users/:userId/posts')"
        - "{ path: '/users/42/posts', query: 'sort=newest' }"
        - "['42', 'newest']"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["{ userId: '42' } (assuming Route path='/users/:userId/posts')"]
      rejectedFeedback: "useParams() returns only URL params from the route path — { userId: '42' }. Query strings (?sort=newest) are NOT in useParams. Read query strings with useSearchParams(). The parameter name in useParams matches the :name in the route definition."
    hint: "useParams only returns :param values from the route path, not query string values."
    reflectionPrompt: "URL params identify a resource (/users/42). Query params filter or modify the display (?sort=newest). This semantic distinction matters for REST API design too — the same convention applies."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you update a query parameter without navigating to a new page?"
    options:
      - "Use navigate() with the new URL"
      - "Use setSearchParams() from useSearchParams()"
      - "Manually change window.location.search"
      - "Query params cannot be changed without navigation"
    correctIndex: 1
    feedback: "const [searchParams, setSearchParams] = useSearchParams(). setSearchParams({ sort: 'newest', page: '2' }) updates the URL query string without a full navigation — the component re-renders with the new params."

retrieval:
  recall: "Write a UserProfile component that reads :userId from the URL and fetches that user's data."
  explain: "When would you use a URL parameter vs a query parameter?"
  mistakeId:
    code: "const params = useParams(); const sort = params.sort; // always undefined"
    answer: "Query params come from useSearchParams, not useParams. const [searchParams] = useSearchParams(); const sort = searchParams.get('sort');"
---

# Hook

URLs are a data structure. The path identifies the resource; query parameters filter it. Knowing which tool to use — useParams vs useSearchParams — makes URL-driven state clean and shareable.

# Lore Introduction

*"The archive reference system,"* says Master Aelindra, *"uses location identifiers (wing, shelf, position) and optional modifiers (language preference, edition). Required identifiers are part of the path; optional modifiers are annotations."*

# Core Learning

## Concept Introduction

```jsx
// Route with URL parameter
<Route path="/users/:userId/posts/:postId" element={<Post />} />

// Read URL params
function Post() {
  const { userId, postId } = useParams();
  // for /users/42/posts/7: { userId: '42', postId: '7' }
}

// Query string: /posts?sort=newest&page=2
function PostList() {
  const [searchParams, setSearchParams] = useSearchParams();
  const sort = searchParams.get('sort') ?? 'date';
  const page = parseInt(searchParams.get('page') ?? '1');

  return (
    <>
      <select value={sort} onChange={e => setSearchParams({ sort: e.target.value })}>
        <option value="date">Newest</option>
        <option value="title">Title</option>
      </select>
    </>
  );
}
```

## Common Mistakes

- **Reading query parameters from `useParams()`**: `useParams()` only returns path parameters (`:id`). Query string values (`?sort=asc`) must be read with `useSearchParams()` — they are never in `useParams()`.
- **Forgetting that all URL parameter values are strings**: `useParams()` returns `{ id: '42' }` — the string `"42"`, not the number `42`. Always parse numeric params before using them in comparisons or arithmetic.
- **Using a URL parameter where a query parameter is semantically correct**: URL params identify a resource (`/users/42`). Optional filters and sort orders are query params (`?sort=name&page=2`). Putting filters in the URL path makes URLs impossible to bookmark correctly.
- **Calling `setSearchParams` with only the new key**: `setSearchParams({ page: '2' })` replaces the entire query string, discarding other existing params like `?sort=name`. Merge existing params: `setSearchParams(prev => { prev.set('page', '2'); return prev; })`.

## Mini Summary
- ✔ :param in route path → useParams() for required resource IDs
- ✔ ?key=value → useSearchParams() for optional filters/pagination
- ✔ setSearchParams() updates query without navigation
- ✔ URL params identify; query params filter

# Solo Practice Quest

Build a product listing with URL param /category/:slug and query params ?sort=price&page=1. The route renders products for that category, sorted and paginated by the query params. The sort dropdown updates the URL without reloading.

# Integration

**Mathematics — Dimensional Coordinates:** URL params identify position in a resource hierarchy (users/42/posts/7 = coordinates in a 2D resource space). Query params add optional dimensions (sort, filter, page). Together they form a coordinate system for navigating the application's data space — analogous to geographic coordinates with optional altitude.

# Lore Conclusion

*"Every URL is an address. The path is the street; the query is the apartment number. Know which to use."*

---
