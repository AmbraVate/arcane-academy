---
id: fe-jun-m5-09
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: loading_states
topicTitle: "Loading States"
topicSortOrder: 3
lesson: loading_ui_patterns
title: "Loading UI Patterns"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-07, fe-jun-m5-08]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between skeleton screens and spinners"
    - "Describes when each loading pattern is most appropriate"
    - "Explains what conditional rendering means in the context of loading states"
    - "Identifies a scenario where progressive loading improves UX"
  keywords: [skeleton, spinner, loading, conditional, render, progressive, UX, placeholder]
  modelAnswer: |
    Spinners (activity indicators) are simple and generic — they work well for brief, unknown-duration operations like button actions. Skeleton screens display placeholder shapes matching the content layout — they're better for page-level loads because they reduce layout shift and help users orient. Progressive loading renders content as it arrives rather than waiting for all data. Conditional rendering in React means checking state variables to decide what to show: if isLoading, show skeleton; if error, show error message; else show content. The choice of pattern affects perceived performance and user trust.
guidedSteps:
  - id: fe-jun-m5-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You're building a social media feed that loads many posts. Which loading pattern is most appropriate?"
    inputConfig:
      options:
        - "A full-page spinner — simple and universal"
        - "Skeleton screens — placeholder cards matching the post layout"
        - "No loading state — just show the feed when it's ready"
        - "A progress bar — users like to see exact percentages"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Skeleton screens — placeholder cards matching the post layout"]
      rejectedFeedback: "Skeleton screens work best for content-heavy layouts. They prevent layout shift, help users understand the shape of incoming content, and feel faster than spinners."
    hint: "What does the user expect to see when the feed loads? How can the loading state mirror that shape?"
    reflectionPrompt: "Why do skeleton screens feel faster than spinners, even if the actual load time is identical?"
  - id: fe-jun-m5-09-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "What is 'conditional rendering' in the context of loading states?"
    inputConfig:
      options:
        - "Rendering the component only on certain devices"
        - "Showing different JSX based on the current state (loading/error/success)"
        - "Using CSS to hide elements that haven't loaded"
        - "Lazily loading components with React.lazy"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Showing different JSX based on the current state (loading/error/success)"]
      rejectedFeedback: "Conditional rendering means returning different JSX depending on state: if isLoading return a spinner, if error return an error message, else return the actual content."
    hint: "Think about if/else statements, but for what you render."
    reflectionPrompt: "How is conditional rendering different from CSS visibility — which approach is better for loading states and why?"
  - id: fe-jun-m5-09-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Explain progressive loading and describe a real-world example where it improves user experience compared to waiting for all data before rendering anything."
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [progressive, partial, arrive, render, first, content]
      rejectedFeedback: "Progressive loading means rendering content as it becomes available rather than waiting for everything. Example: a news site that shows the article text while images are still loading — users can start reading immediately."
    hint: "Think about a page that has fast data and slow data — should you wait for the slow data before showing anything?"
    reflectionPrompt: "What is the risk of using progressive loading — could partial data confuse users?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A skeleton screen is better than a spinner for long-form content pages because:"
    options:
      - "Spinners use more CPU"
      - "Skeletons match the content layout, reducing layout shift and helping users anticipate the shape of the content"
      - "Spinners are only suitable for button actions"
      - "Skeletons load the content faster"
    correctIndex: 1
    feedback: "Skeleton screens mirror the expected layout, reducing the jarring shift when content arrives and giving users a sense of the page structure while they wait."
retrieval:
  recall: "Name three loading UI patterns and a use case for each."
  explain: "Explain why skeleton screens are considered to reduce perceived load time even when actual load time is identical to using a spinner."
  mistakeId:
    code: |
      function ArticlePage() {
        const [article, setArticle] = React.useState(null);
        React.useEffect(() => {
          fetch('/api/article/1').then(r => r.json()).then(setArticle);
        }, []);
        return (
          <div style={{ display: article ? 'block' : 'none' }}>
            <h1>{article?.title}</h1>
          </div>
        );
      }
    answer: "Using display:none hides the component but still renders it — the h1 still attempts to read article.title. A better pattern is conditional rendering: if (!article) return <SkeletonArticle />; — this returns different JSX for the loading state rather than hiding it with CSS."
---

# Hook

Two apps load the same data in 800ms. App A shows a spinning circle. App B shows a page-shaped skeleton — grey rectangles where the headline will be, where the text will be, where the image will be — then fills in smoothly. Users in user tests consistently rate App B as "faster" even though it isn't. They rate App A as "frozen" and "broken-feeling." The data is identical. The loading pattern is everything. This lesson is about making your users feel like your app is fast, even when the network isn't.

# Lore Introduction

*The Academy's Hall of Records once had a single rule: no student could enter until every scroll was shelved. The hall stood empty for hours each morning while scribes worked. Then the Librarian Magistra Evren proposed a change: students may enter as the work proceeds — they can see the shelves taking shape, see the categories being filled, and begin orienting themselves before all is complete. The experience of waiting transformed from stasis to anticipation. The Hall grew beloved. This is the art of loading UI: turning an absence into a presence.*

# Core Learning

## Concept Introduction

There are three primary loading UI patterns:

**1. Spinner (Activity Indicator)**
A rotating icon that indicates something is happening. Simple, low-effort. Works well for brief, transactional operations like form submissions or button actions.

**2. Skeleton Screen**
Placeholder shapes that mimic the layout of incoming content. Grey or animated rectangles where text will appear, rectangles where images will appear. Works best for page-level or list-level content loads.

**3. Progressive Loading**
Render content as it arrives rather than waiting for all data. If an article text loads in 200ms but images take 1000ms, show the text immediately and let images fill in.

**Conditional rendering** is the React mechanism for all of these: checking state variables and returning different JSX based on current state.

## Why It Matters

Perceived performance is as important as actual performance. Studies consistently show that skeleton screens feel faster than spinners even at equal load times. The reason is psychological: uncertainty is more stressful than defined waiting, and familiarity with the expected layout reduces cognitive work.

## Worked Example

```jsx
// Skeleton screen component
function ArticleSkeleton() {
  return (
    <div className="animate-pulse p-6">
      <div className="h-8 bg-gray-200 rounded w-3/4 mb-4" />
      <div className="h-4 bg-gray-200 rounded w-1/4 mb-8" />
      <div className="space-y-3">
        <div className="h-4 bg-gray-200 rounded" />
        <div className="h-4 bg-gray-200 rounded" />
        <div className="h-4 bg-gray-200 rounded w-5/6" />
      </div>
    </div>
  );
}

// The main component using conditional rendering
function ArticlePage({ articleId }) {
  const [isLoading, setIsLoading] = React.useState(true);
  const [article, setArticle] = React.useState(null);
  const [error, setError] = React.useState(null);

  React.useEffect(() => {
    async function load() {
      try {
        const res = await fetch(`/api/articles/${articleId}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        setArticle(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    }
    load();
  }, [articleId]);

  // Conditional rendering: different JSX per state
  if (isLoading) return <ArticleSkeleton />;

  if (error) return (
    <div className="p-6 bg-red-50 text-red-700 rounded">
      Could not load article. Please try again.
    </div>
  );

  return (
    <article className="max-w-2xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-2">{article.title}</h1>
      <p className="text-gray-500 text-sm mb-6">{article.author} · {article.date}</p>
      <div className="prose">{article.content}</div>
    </article>
  );
}
```

Progressive loading pattern — load fast and slow data separately:
```jsx
function Dashboard({ userId }) {
  const [profile, setProfile] = React.useState(null);
  const [stats, setStats] = React.useState(null); // slower

  React.useEffect(() => {
    // Fast request
    fetch(`/api/users/${userId}`)
      .then(r => r.json())
      .then(setProfile);

    // Slow request — doesn't block profile from rendering
    fetch(`/api/users/${userId}/stats`)
      .then(r => r.json())
      .then(setStats);
  }, [userId]);

  return (
    <div className="p-6 space-y-4">
      {/* Profile renders as soon as it arrives */}
      {profile ? (
        <div className="bg-white rounded shadow p-4">
          <h2 className="font-bold">{profile.name}</h2>
        </div>
      ) : (
        <div className="h-16 bg-gray-200 animate-pulse rounded" />
      )}

      {/* Stats render when they arrive — independently */}
      {stats ? (
        <div className="bg-white rounded shadow p-4">
          <p>{stats.totalXp} XP earned</p>
        </div>
      ) : (
        <div className="h-16 bg-gray-200 animate-pulse rounded" />
      )}
    </div>
  );
}
```

## Common Mistakes

**Mistake 1: Using CSS visibility/display instead of conditional rendering**
```js
// AVOID — still renders children, still evaluates expressions
<div style={{ visibility: isLoading ? 'hidden' : 'visible' }}>
  {data.map(...)} // crashes when data is null during load
</div>
```

**Mistake 2: Only loading state, no error state in UI**
```js
if (isLoading) return <Spinner />;
// No error check — user sees blank page on failure
return <DataView data={data} />;
```

**Mistake 3: Over-engineering loaders for simple interactions**
```jsx
// Skeleton screen for a single button label update? Overkill.
// Use spinners for transactional actions, skeletons for content pages.
```

## Mini Summary

- **Spinner** — best for brief transactional operations (button submit, form action)
- **Skeleton screen** — best for content-heavy page loads; mirrors expected layout
- **Progressive loading** — render content as it arrives; don't block fast content on slow content
- All three rely on **conditional rendering** — returning different JSX based on current state
- Perceived performance matters: skeletons feel faster than spinners at equal load times

# Guided Practice Quest

Work through the guided steps to practise choosing loading patterns and implementing conditional rendering correctly.

# Solo Practice Quest

Compare spinner and skeleton screen loading patterns. When would you use each? Describe the UX benefit of skeleton screens specifically and explain how conditional rendering enables all loading UI patterns. Include a sketch of a skeleton screen component using Tailwind.

# Integration

**Design — The Psychology of Perceived Performance:** Skeleton screens exploit a well-documented UX phenomenon: the mere appearance of progress signals intent. Research by Google (on their mobile search results) showed skeleton loading patterns consistently outperformed spinners on user satisfaction surveys. This is because skeleton screens set spatial expectations — the user's brain begins planning where to look, reducing the effort required when content appears.

**Psychology — Attentional Capture and Saliency:** Spinners are designed to attract attention — their motion is salient. But constant attentional capture during loading creates visual fatigue. Skeleton screens are low-saliency, occupying space without demanding focus. This allows users to maintain a relaxed, anticipatory state rather than an anxious, attention-vigilant one. Good loading UI design is partly neuroscience: managing where and how strongly attention is directed.

# Lore Conclusion

*The Hall of Records is full of students, even before all scrolls are shelved. They see the shapes of the categories, understand the layout, orient themselves. When the scribes finish, the students are already in position, ready to read. No scrambling, no disorientation, no frustrated departures. The Academy is alive with productive anticipation. That is what great loading UI does: it turns waiting into readiness.*

---
