---
id: fe-jun-m5-06
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: async_operations
topicTitle: "Async Operations"
topicSortOrder: 2
lesson: sequential_vs_parallel
title: "Sequential vs Parallel Requests"
sortOrder: 3
difficulty: 4
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-04, fe-jun-m5-05]
integrationDomains: [mathematics, sociology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between sequential and parallel request execution"
    - "Describes when sequential is necessary vs when parallel is preferred"
    - "Explains what Promise.all() does and what it returns"
    - "Identifies the risk of Promise.all() when one request fails"
  keywords: [sequential, parallel, Promise.all, await, independent, dependent, performance]
  modelAnswer: |
    Sequential requests run one after another — the second doesn't start until the first finishes. This is necessary when requests are dependent (you need data from request 1 to make request 2). Parallel requests run simultaneously using Promise.all(), which takes an array of Promises and resolves when all of them fulfil. The total time is the slowest request, not the sum. Use parallel when requests are independent — for example, fetching user profile and notifications at the same time. Risk: Promise.all() rejects if any one Promise rejects, so consider Promise.allSettled() for better error tolerance.
guidedSteps:
  - id: fe-jun-m5-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You need to fetch a user profile, then use their `teamId` to fetch their team details. Which approach is required?"
    inputConfig:
      options:
        - "Parallel — use Promise.all() for speed"
        - "Sequential — the second request depends on data from the first"
        - "Either — it doesn't matter for dependent requests"
        - "Nested — wrap the second fetch inside the first response handler"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Sequential — the second request depends on data from the first"]
      rejectedFeedback: "When the second request needs data from the first, you must run them sequentially — you don't know the teamId until the first request returns."
    hint: "Ask yourself: can you start both requests at the same time? Do you have all the information you need?"
    reflectionPrompt: "What would happen if you tried to run both in parallel — what information would be missing?"
  - id: fe-jun-m5-06-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "Your dashboard needs to load a user's profile AND their notifications — two completely independent requests. Which approach is fastest?"
    inputConfig:
      options:
        - "Sequential await — easier to write"
        - "Promise.all() — both run simultaneously, total time is the slowest one"
        - "Promise.race() — stops as soon as one finishes"
        - "setTimeout — stagger them to avoid server load"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Promise.all() — both run simultaneously, total time is the slowest one"]
      rejectedFeedback: "Independent requests should run in parallel. Promise.all() starts both simultaneously; the total wait is only as long as the slowest request, not the sum of both."
    hint: "If you could do two things at once rather than one after another, which would be faster?"
    reflectionPrompt: "If profile takes 200ms and notifications take 300ms, what's the total time with sequential vs parallel?"
  - id: fe-jun-m5-06-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Explain what happens to Promise.all() if one of its Promises rejects. What alternative method is more fault-tolerant?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [reject, fail, allSettled, all, one]
      rejectedFeedback: "Promise.all() rejects immediately if any Promise rejects, discarding results from the others. Promise.allSettled() waits for all and gives you each result (fulfilled or rejected) individually."
    hint: "Think about what 'all' means — if one fails, does the whole group fail?"
    reflectionPrompt: "For a dashboard where some widgets are optional, would you prefer Promise.all() or Promise.allSettled()? Why?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "If you await three requests sequentially and they each take 200ms, how long does the total function take?"
    options:
      - "~200ms — they run in parallel"
      - "~400ms — only two run at once"
      - "~600ms — each waits for the previous one"
      - "It depends on the browser"
    correctIndex: 2
    feedback: "Sequential awaits are additive — each waits for the previous to complete. 3 × 200ms = ~600ms total. Promise.all() would take only ~200ms for the same three requests."
retrieval:
  recall: "What is the difference in total execution time between three 200ms sequential requests and three 200ms parallel requests via Promise.all()?"
  explain: "Describe a real-world scenario where sequential requests are necessary and one where parallel requests are the right choice."
  mistakeId:
    code: |
      async function loadDashboard() {
        const profile = await fetch('/api/profile').then(r => r.json());
        const stats = await fetch('/api/stats').then(r => r.json());
        const news = await fetch('/api/news').then(r => r.json());
        return { profile, stats, news };
      }
    answer: "These three requests are independent — none depends on the others. Running them sequentially wastes time. Use Promise.all() to run all three in parallel: const [profile, stats, news] = await Promise.all([fetch('/api/profile').then(r => r.json()), fetch('/api/stats').then(r => r.json()), fetch('/api/news').then(r => r.json())]);"
---

# Hook

Your dashboard loads a user profile, then their stats, then their notifications — one after another. Each request takes about 300ms. The page takes almost a second to load, and users are complaining. A colleague takes one look and rewrites it in five minutes: all three requests start at the same time, the page loads in 300ms. The fix isn't clever — it's just understanding when requests can run in parallel. Sequential is sometimes essential. Parallel is often faster, and knowing when to use each is a real performance skill.

# Lore Introduction

*The Academy once dispatched emissaries one at a time: send the first, wait for their return, then send the second. The Chronicler noted that the Academy's response time tripled unnecessarily — the second emissary could have departed immediately, as their mission required no knowledge of the first. Now, independent missions depart in parallel. The corridors are busier, but the Academy's work is done in a third the time. Emissaries with dependent missions — those who must know the outcome of a prior quest before setting off — still go in sequence. Wisdom is knowing which is which.*

# Core Learning

## Concept Introduction

There are two execution patterns for multiple async requests:

**Sequential** — each request waits for the previous one to finish:
```
Start → Request 1 → Request 2 → Request 3 → Done
```
Total time = sum of all requests

**Parallel** — all requests start simultaneously:
```
Start → Request 1 ─┐
      → Request 2 ─┼→ Done (when the last finishes)
      → Request 3 ─┘
```
Total time = slowest single request

Use **sequential** when requests are **dependent** — you need the result of one to make the next.
Use **parallel** when requests are **independent** — they don't need each other's data.

## Why It Matters

Unnecessary sequential requests are a common performance bottleneck. A dashboard page that makes five independent API calls sequentially can take 5x longer than it needs to. `Promise.all()` is a straightforward, built-in solution.

## Worked Example

```js
// SEQUENTIAL — necessary when requests are dependent
async function fetchUserAndTeam(userId) {
  const userRes = await fetch(`/api/users/${userId}`);
  const user = await userRes.json();

  // We NEED user.teamId before we can fetch the team
  const teamRes = await fetch(`/api/teams/${user.teamId}`);
  const team = await teamRes.json();

  return { user, team };
}

// PARALLEL — faster for independent requests
async function loadDashboard(userId) {
  // All three start simultaneously
  const [profile, notifications, settings] = await Promise.all([
    fetch(`/api/users/${userId}`).then(r => r.json()),
    fetch(`/api/notifications/${userId}`).then(r => r.json()),
    fetch(`/api/settings/${userId}`).then(r => r.json())
  ]);

  return { profile, notifications, settings };
}
```

Using `Promise.allSettled()` for fault-tolerant parallel requests:
```js
async function loadDashboardSafe(userId) {
  const results = await Promise.allSettled([
    fetch('/api/profile').then(r => r.json()),
    fetch('/api/notifications').then(r => r.json()),
    fetch('/api/ads').then(r => r.json()) // optional widget
  ]);

  // Each result has status: 'fulfilled' or 'rejected'
  const profile = results[0].status === 'fulfilled' ? results[0].value : null;
  const notifications = results[1].status === 'fulfilled' ? results[1].value : [];
  const ads = results[2].status === 'fulfilled' ? results[2].value : null;

  return { profile, notifications, ads };
}
```

In a React component:
```jsx
function Dashboard({ userId }) {
  const [data, setData] = React.useState(null);

  React.useEffect(() => {
    async function load() {
      const [profile, stats] = await Promise.all([
        fetch(`/api/users/${userId}`).then(r => r.json()),
        fetch(`/api/stats/${userId}`).then(r => r.json())
      ]);
      setData({ profile, stats });
    }
    load();
  }, [userId]);

  if (!data) return <div className="p-4 text-gray-500">Loading...</div>;

  return (
    <div className="p-6 grid grid-cols-2 gap-4">
      <div className="bg-white rounded shadow p-4">{data.profile.name}</div>
      <div className="bg-white rounded shadow p-4">{data.stats.total} items</div>
    </div>
  );
}
```

## Common Mistakes

**Mistake 1: Sequential awaits for independent requests**
```js
// SLOW — unnecessary sequential execution
const a = await fetch('/api/a').then(r => r.json());
const b = await fetch('/api/b').then(r => r.json()); // waits for 'a'

// FAST — parallel
const [a, b] = await Promise.all([
  fetch('/api/a').then(r => r.json()),
  fetch('/api/b').then(r => r.json())
]);
```

**Mistake 2: Promise.all() with one flaky request failing everything**
```js
// If /api/ads fails, the whole Promise.all rejects!
const [profile, ads] = await Promise.all([
  fetch('/api/profile').then(r => r.json()),
  fetch('/api/ads').then(r => r.json()) // ads shouldn't crash the page
]);
// Solution: use Promise.allSettled() or wrap risky ones in try/catch
```

## Mini Summary

- **Sequential**: each await waits for the previous; required for dependent requests
- **Parallel**: `Promise.all([...])` starts all requests simultaneously; total time = slowest
- `Promise.all()` rejects immediately if any Promise rejects
- `Promise.allSettled()` waits for all and reports each result individually — better for optional data
- Defaulting to sequential when parallel would work is a common, measurable performance problem

# Guided Practice Quest

Work through the steps to solidify when sequential vs parallel is appropriate and how Promise.all() behaves on failure.

# Solo Practice Quest

Explain sequential vs parallel async requests with a concrete real-world example for each. Describe `Promise.all()` and `Promise.allSettled()`, their difference in error handling, and when you'd choose one over the other. Include a code example of Promise.all().

# Integration

**Mathematics — Parallel vs Series Circuits:** In electrical engineering, components in series have combined resistance equal to the sum (R1 + R2 + R3), while components in parallel have combined resistance less than any single one. The time analogy is direct: sequential operations add up (series), parallel operations take only the maximum (parallel). Network request latency follows this exact model. Optimising web performance with parallelism is applied mathematics: minimising the critical path.

**Sociology — Division of Labour:** Adam Smith's pin factory observation showed that dividing labour — specialising tasks and running them concurrently — dramatically increases output. The same principle applies to API orchestration. A page that needs data from three microservices will load three times faster if those services are queried in parallel. Distributed systems are, in a sense, a technological implementation of Smith's insight: decompose work, specialise, run concurrently.

# Lore Conclusion

*Three missions required, three emissaries available. The Council has learned: where paths are independent, they need not walk in a queue. The Academy's great Clock Tower was restored in a single day — the stonemasons, glaziers, and bell-smiths all working simultaneously, their tasks requiring nothing of each other. Only the final assembly required them to wait. Know when to queue and when to fly in parallel, and your quests will resolve in time, not in an age.*

---
