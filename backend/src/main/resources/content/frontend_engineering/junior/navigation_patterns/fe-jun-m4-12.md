---
id: fe-jun-m4-12
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m4
moduleTitle: "Module 4: Routing and Navigation"
moduleGlyph: "🗺️"
moduleSortOrder: 4
topicSlug: navigation_patterns
topicTitle: "Navigation Patterns"
topicSortOrder: 4
lesson: four_o_four_handling
title: "404 Handling"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Catches unmatched routes with path='*'"
    - "Designs a helpful 404 page with navigation options"
    - "Distinguishes between client-side 404 (wrong route) and server 404 (missing resource)"
    - "Uses search functionality on 404 to help users recover"
  keywords: [404, wildcard, NotFound, catch-all, recovery, search, helpful, navigation]
  modelAnswer: |
    A wildcard Route path="*" catches all unmatched URLs and renders a 404 page.
    A good 404 page is not a dead end: it offers a search, links to popular pages,
    and a clear home link. Client-side 404 (wrong route) is handled by React Router;
    server-side 404 (missing API resource) is handled in the component. Both should
    give the user a path forward — never a dead end.
guidedSteps:
  - id: fe-jun-m4-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What should a well-designed 404 page include?
    inputConfig:
      options:
        - "Just the text '404 Not Found' — minimal and clean"
        - "A clear message, link to home, search, and links to popular sections"
        - "An auto-redirect to home after 3 seconds"
        - "A form to report the broken link"
      
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A clear message, link to home, search, and links to popular sections"]
      rejectedFeedback: "A 404 page should be a recovery point, not a dead end. Include: clear explanation (what went wrong), link to home, search to find what they were looking for, links to popular/useful pages. Auto-redirect is disorienting — give the user control."
    hint: "Think about what the user needs to do after landing on a 404."
    reflectionPrompt: "The quality of a 404 page reveals a team's attention to UX. A bare '404 Not Found' says 'we didn't think about you landing here.' A helpful 404 says 'we expected this could happen and prepared for you.'"

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When fetching a user by ID returns a 404 from the API, what should the UI show?"
    options:
      - "The application's global 404 route"
      - "An inline 'User not found' message with navigation options"
      - "A blank page"
      - "A redirect to /users list"
    correctIndex: 1
    feedback: "API 404 (resource not found) is different from route 404 (wrong URL). Show an inline message specific to the context: 'User not found — they may have deleted their account.' Offer relevant next steps (back to users list) rather than the generic site 404."

retrieval:
  recall: "Write the Route configuration for a 404 page using a wildcard."
  explain: "What is the difference between a client-side route 404 and a server-side API 404?"
  mistakeId:
    code: "No wildcard route — unmatched URLs render a blank page with the layout"
    answer: "Always add <Route path='*' element={<NotFoundPage />} /> as the last route inside Routes. Without it, unmatched URLs show the shell with a blank Outlet — confusing and unhelpful."
---

# Hook

Every application has 404 scenarios. The question is not whether users will hit dead ends — it is whether those dead ends are recoverable. A good 404 is a navigation aid, not a dead end.

# Lore Introduction

*"The Academy's map,"* says Master Aelindra, *"marks unknown territories with 'here be dragons' — but also with the nearest known landmark and a compass bearing back to the entrance. The unknown need not be a dead end."*

# Core Learning

## Concept Introduction

```jsx
// Catch-all route
<Routes>
  <Route path="/"        element={<Home />} />
  <Route path="/about"   element={<About />} />
  <Route path="*"        element={<NotFoundPage />} />  {/* last! */}
</Routes>

// Helpful 404 page
function NotFoundPage() {
  const [search, setSearch] = useState('');
  return (
    <div className="not-found">
      <h1>Page Not Found</h1>
      <p>The page you're looking for doesn't exist or has moved.</p>
      <Link to="/">← Go home</Link>
      <input
        placeholder="Search the site..."
        value={search}
        onChange={e => setSearch(e.target.value)}
      />
      <nav>
        <h2>Popular pages</h2>
        <Link to="/docs">Documentation</Link>
        <Link to="/blog">Blog</Link>
        <Link to="/support">Support</Link>
      </nav>
    </div>
  );
}
```

## Mini Summary
- ✔ path="*" wildcard catches all unmatched routes — must be last
- ✔ Good 404: explanation + home link + search + popular pages
- ✔ Never a dead end — always give a path forward
- ✔ API 404 ≠ route 404 — handle each contextually

# Solo Practice Quest

Build a helpful 404 page with: animated illustration, clear explanation, home link, search input (just logs query for now), and 4 popular page links. Make it responsive.

# Integration

**Psychology — Learned Helplessness Prevention:** Users who encounter 404 pages experience frustration and, without recovery options, may develop learned helplessness — the belief that their actions cannot produce success. A well-designed 404 provides immediate recovery pathways, restoring agency. Studies show that helpful 404 pages significantly reduce bounce rates compared to bare error messages.

# Lore Conclusion

*"A dead end is only a dead end if it offers no other direction. Every dead end in the Academy has a sign pointing back to the entrance and a list of nearby destinations."*

---
