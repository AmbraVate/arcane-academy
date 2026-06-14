---
id: fe-jun-m5-13
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: rest_consumption
topicTitle: "REST Consumption"
topicSortOrder: 5
lesson: consuming_rest_apis
title: "Consuming REST APIs"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-12]
integrationDomains: [sociology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Maps GET/POST/PUT/PATCH/DELETE to their CRUD equivalents"
    - "Explains what a base URL and endpoint path are"
    - "Describes how to read API documentation to construct a request"
    - "Shows the correct method for each CRUD operation with an example"
  keywords: [GET, POST, PUT, PATCH, DELETE, CRUD, endpoint, REST, resource, method]
  modelAnswer: |
    REST APIs map HTTP methods to CRUD operations: GET retrieves resources, POST creates new ones, PUT replaces a resource entirely, PATCH updates specific fields, DELETE removes. URLs represent resources: /api/users is the users collection, /api/users/1 is a specific user. Reading API docs means identifying: the endpoint URL, required method, request body shape, and expected response. A CRUD pattern for users: GET /users (list), POST /users (create), GET /users/:id (read one), PUT /users/:id (update), DELETE /users/:id (delete).
guidedSteps:
  - id: fe-jun-m5-13-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want to update only the `email` field of user 5, leaving all other fields unchanged. Which method is most appropriate?"
    inputConfig:
      options:
        - "PUT /api/users/5 — replace the entire user object"
        - "PATCH /api/users/5 — update only the specified fields"
        - "POST /api/users/5 — create a new version of the user"
        - "GET /api/users/5?email=new@example.com — use query parameters"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["PATCH /api/users/5 — update only the specified fields"]
      rejectedFeedback: "PATCH is for partial updates — you only send the fields you want to change. PUT replaces the entire resource, which would overwrite fields you didn't intend to change."
    hint: "Think about what 'partial update' vs 'full replacement' means for the other fields."
    reflectionPrompt: "When would you use PUT instead of PATCH? What's the risk of using PUT for partial updates?"
  - id: fe-jun-m5-13-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "An API's documentation says: `GET /posts/:id/comments` returns an array of comments for a post. How would you fetch comments for post 42?"
    inputConfig:
      options:
        - "POST /posts/42/comments with body: { id: 42 }"
        - "GET /posts/42/comments — substitute 42 for :id"
        - "GET /posts?id=42&type=comments"
        - "GET /comments?postId=42 — this is always the pattern"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["GET /posts/42/comments — substitute 42 for :id"]
      rejectedFeedback: ":id is a URL parameter placeholder. Replace it with the actual value: /posts/42/comments. This is standard REST routing notation."
    hint: ":id is a placeholder — substitute the real value where it appears."
    reflectionPrompt: "What does the nested path /posts/42/comments communicate about the relationship between posts and comments?"
  - id: fe-jun-m5-13-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Write the complete fetch calls for: (1) getting all products, (2) creating a new product with name 'Widget' and price 9.99, (3) deleting product with id 7."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [GET, POST, DELETE, fetch]
      rejectedFeedback: "GET /api/products (no body), POST /api/products with JSON body and Content-Type header, DELETE /api/products/7 with method: 'DELETE'."
    hint: "GET needs no second argument, POST needs method/headers/body, DELETE needs method: 'DELETE'."
    reflectionPrompt: "Does a DELETE request typically include a body? What does it use to identify the resource instead?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does the URL `/api/users/3/posts` most likely return in a REST API?"
    options:
      - "All users and all posts in one response"
      - "The posts belonging to user with id 3"
      - "A list of users filtered by posts"
      - "Post number 3 for all users"
    correctIndex: 1
    feedback: "Nested REST paths express relationships. /users/3/posts reads as 'posts that belong to user 3'. The nesting shows the ownership/relationship."
retrieval:
  recall: "Map all five HTTP methods (GET, POST, PUT, PATCH, DELETE) to their CRUD operations."
  explain: "Explain the difference between PUT and PATCH, and describe a scenario where using the wrong one would cause a data problem."
  mistakeId:
    code: |
      // Updating user's email only
      fetch('/api/users/5', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: 'new@example.com' })
      });
    answer: "PUT replaces the entire resource. Sending only the email field will erase all other user fields (name, role, etc.) on most API implementations. Use PATCH for partial updates: method: 'PATCH' sends only the fields you want to change."
---

# Hook

You've joined a new team. There's an existing API. The documentation says `/api/orders/:id` and you need to update an order's status. Do you use PUT? PATCH? POST? You try PUT with just the status field — the order's customer name disappears from the database. You try POST — a new order is created instead. REST has conventions. HTTP methods have semantics. Once you understand the CRUD-to-method mapping and how to read an API docs page, integrating with any REST API becomes systematic rather than trial-and-error.

# Lore Introduction

*The Academy's central Repository uses a standardised system that every Guild has agreed upon: the same symbols mean the same things, regardless of which Tower you visit. Retrieve (scroll and quill). Create (sun rising). Replace (full moon). Partial update (crescent). Remove (flame). Any apprentice who knows this system can interact with any Repository, anywhere in the Academy, without special instruction. REST is that system for the web: a set of shared conventions that, once known, unlock every API you will ever encounter.*

# Core Learning

## Concept Introduction

**REST** (Representational State Transfer) is an architectural style for APIs. RESTful APIs follow conventions:

- **URLs represent resources**: `/api/users` (collection), `/api/users/1` (specific item), `/api/users/1/posts` (related resources)
- **HTTP methods describe operations**:

| Method | CRUD | Typical use |
|--------|------|-------------|
| GET | Read | Retrieve a resource or list |
| POST | Create | Create a new resource |
| PUT | Update (replace) | Replace the entire resource |
| PATCH | Update (partial) | Update specific fields only |
| DELETE | Delete | Remove a resource |

**Reading API docs**: Find the endpoint URL, required method, request body format (if any), required headers, and response shape.

## Why It Matters

REST is the dominant API style for web applications. Every service you integrate with — payment providers, auth services, third-party data — uses a REST (or REST-like) API. Understanding the conventions lets you read any API documentation and integrate quickly and correctly.

## Worked Example

```js
const BASE_URL = 'https://api.example.com';

// GET — retrieve all users
async function getUsers() {
  const res = await fetch(`${BASE_URL}/users`);
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
}

// GET — retrieve one user by ID
async function getUser(id) {
  const res = await fetch(`${BASE_URL}/users/${id}`);
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
}

// POST — create a new user
async function createUser(userData) {
  const res = await fetch(`${BASE_URL}/users`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json(); // returns the created user, typically with an id
}

// PATCH — update specific fields
async function updateUserEmail(id, email) {
  const res = await fetch(`${BASE_URL}/users/${id}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email }) // only send what's changing
  });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
}

// DELETE — remove a user
async function deleteUser(id) {
  const res = await fetch(`${BASE_URL}/users/${id}`, {
    method: 'DELETE'
  });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  // DELETE often returns 204 No Content — no body to parse
}
```

A React component implementing full CRUD for a list:
```jsx
function ItemManager() {
  const [items, setItems] = React.useState([]);

  React.useEffect(() => {
    fetch('/api/items')
      .then(r => r.json())
      .then(setItems);
  }, []);

  async function handleDelete(id) {
    await fetch(`/api/items/${id}`, { method: 'DELETE' });
    setItems(prev => prev.filter(item => item.id !== id));
  }

  return (
    <ul className="divide-y">
      {items.map(item => (
        <li key={item.id} className="flex justify-between items-center py-3 px-4">
          <span>{item.name}</span>
          <button
            onClick={() => handleDelete(item.id)}
            className="text-red-500 hover:text-red-700 text-sm"
          >
            Delete
          </button>
        </li>
      ))}
    </ul>
  );
}
```

## Common Mistakes

**Mistake 1: Using PUT for partial updates**
```js
// WRONG — overwrites all other fields with undefined
fetch('/api/users/5', {
  method: 'PUT',
  body: JSON.stringify({ email: 'new@email.com' }) // other fields will be erased!
});
```

**Mistake 2: Using POST instead of DELETE**
```js
// WRONG — semantically incorrect and won't work on well-designed APIs
fetch('/api/users/delete/5', { method: 'POST' });

// CORRECT
fetch('/api/users/5', { method: 'DELETE' });
```

**Mistake 3: Hardcoding full URLs instead of using a base URL**
```js
// FRAGILE — if the base changes, every call breaks
fetch('https://staging-api.example.com/v2/users');
fetch('https://staging-api.example.com/v2/posts');

// MAINTAINABLE
const API_BASE = 'https://staging-api.example.com/v2';
fetch(`${API_BASE}/users`);
fetch(`${API_BASE}/posts`);
```

## Mini Summary

- REST maps HTTP methods to CRUD: GET (read), POST (create), PUT (replace), PATCH (partial update), DELETE (remove)
- URLs represent resources: collection `/users`, specific item `/users/:id`, nested `/users/:id/posts`
- **PUT** replaces the entire resource; **PATCH** updates specific fields
- Always define a `BASE_URL` constant rather than duplicating the full URL everywhere
- Reading API docs: identify endpoint, method, required body, and expected response shape

# Guided Practice Quest

Work through the steps above to practise choosing the right HTTP method for each operation and reading URL patterns from API documentation.

# Solo Practice Quest

Explain the five main HTTP methods used in REST APIs, their CRUD mappings, and when to use PUT vs PATCH. Write fetch calls for each of: list all products, get product 5, create a product, update product 5's price, and delete product 5.

# Integration

**Sociology — Standards and Coordination:** REST is a coordination mechanism — a shared standard that lets diverse teams and organisations communicate without prior agreement on specifics. This mirrors how social institutions like money, law, and language work: they are coordination systems that allow strangers to transact. The web works because REST (and HTTP generally) allows a JavaScript app written in Berlin to talk to a Java server in Singapore without either team knowing anything about the other's implementation. Standards are social infrastructure.

**Philosophy — Convention vs Nature:** The philosopher David Hume distinguished between natural relations (which exist in nature) and conventional ones (which exist because we agree they do). HTTP methods are conventional — there is nothing intrinsically "creating" about POST or "deleting" about DELETE. They mean what they mean because we collectively decided they do. Like traffic lights and currency, their power comes from convention, not nature. REST works because enough people follow the conventions consistently.

# Lore Conclusion

*The Repository system works because every Guild agreed: the same symbols, the same meanings, the same protocol. An apprentice from the Alchemy Ward can walk into the History Tower and navigate its shelves without a guide. REST gives you that same freedom: learn the conventions once, apply them everywhere. The web's great library is open to you.*

---
