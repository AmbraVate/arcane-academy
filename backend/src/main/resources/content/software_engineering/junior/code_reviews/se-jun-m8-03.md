---
id: se-jun-m8-03
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m8
moduleTitle: "Module 8: Professional Practices"
moduleGlyph: "⚙️"
moduleSortOrder: 8
topicSlug: code_reviews
topicTitle: "Code Reviews"
topicSortOrder: 3
lesson: code_reviews
title: "Code Reviews"
sortOrder: 3
difficulty: 2
estimatedMinutes: 22
xpReward: 45
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [branching_strategies]
integrationDomains: [psychology, communication]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Lists at least four things a reviewer should look for (correctness, readability, tests, security, design)"
    - "Demonstrates the difference between constructive and nitpicky/unconstructive feedback"
    - "Explains how code review serves as a knowledge-sharing and learning mechanism"
    - "Distinguishes async and synchronous review and when each is appropriate"
    - "Articulates how to respond well to review feedback as the author"
  keywords: [review, feedback, constructive, correct, readable, test, security, nitpick, suggest, learn, async, synchronous, author]
  modelAnswer: |
    # Good review comment (constructive, suggests improvement):
    "The null check here will throw if 'questId' is null before reaching the validation.
    Consider using Optional or adding a guard at the top of the method:
    
    if (questId == null) throw new IllegalArgumentException('questId must not be null');
    
    Happy to discuss if there's a reason for the current approach."
    
    # Poor review comment (nitpick without value):
    "I would have named this variable 'q' instead of 'quest'."
    
    # Poor review comment (blocking without explanation):
    "This is wrong. Please fix."
    
    # What to review:
    # 1. Correctness — does it do what it claims?
    # 2. Edge cases — null, empty, boundaries
    # 3. Tests — are they present and meaningful?
    # 4. Readability — can a new developer understand this?
    # 5. Security — user input validation, auth checks
    # 6. Design — SOLID, unnecessary coupling
guidedSteps:
  - id: cr-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Beyond catching bugs, what is another significant benefit of code review?
    inputConfig:
      options:
        - "It replaces the need for automated testing"
        - "It speeds up individual development time"
        - "It distributes knowledge about the codebase across the team, reducing knowledge silos"
        - "It ensures all code follows the reviewer's personal style preferences"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It distributes knowledge about the codebase across the team, reducing knowledge silos"]
      rejectedFeedback: "Code review is a powerful knowledge-sharing mechanism. When you review a colleague's code, you learn about parts of the codebase you did not write. When your code is reviewed, you receive feedback that improves your skills. Teams with consistent code review have more evenly distributed knowledge — no single person is a critical dependency for understanding a component."
    hint: "Think about what happens when the only person who understands a module goes on holiday."
    reflectionPrompt: "Have you ever been blocked because only one person understood a particular piece of code? How could regular code review have mitigated that?"
  - id: cr-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A review comment that says "I don't like this variable name, I'd use 'x'" on a clear, readable variable name is an example of a ___pick — minor style preference that adds friction without improving the code.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["nit", "nitpick", "nit-pick"]
      rejectedFeedback: "A 'nitpick' (or 'nit') is a minor subjective preference raised as a review comment. While naming does matter, commenting on a perfectly readable variable name to impose personal style preference wastes everyone's time and creates friction. Save review energy for things that actually matter: bugs, missing tests, security issues, design problems."
    hint: "This word describes minor, often pointless criticisms. Starts with 'nit'."
    reflectionPrompt: "How do you distinguish a genuine naming issue (e.g., a misleading variable name) from a mere style preference? What makes one worth raising and the other not?"
  - id: cr-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You are reviewing a pull request and notice that there are no tests for the new method, and the method directly calls a static utility with a hardcoded external URL. Write two review comments that are constructive, specific, and suggest a path forward.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [test, mock, static, inject, url, config, suggest, consider, hard-coded, verify, coverage]
      rejectedFeedback: "Good review comments are specific (reference the exact location), explain the concern (why is this a problem), and suggest a direction (what to do instead). E.g. 'No test coverage for the error path — could we add a test for when the URL is unreachable? This would also document the expected behaviour.' And: 'The URL is hardcoded as a string literal — consider externalising it to application.properties so environments can differ without code changes.'"
    hint: "Be specific, be kind, be actionable. Tell the author where the issue is, why it matters, and what a fix might look like."
    reflectionPrompt: "How does the tone of a review comment affect the author's willingness to act on it and their relationship with the reviewer?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Asynchronous code review (reviewer reads and comments on their own schedule) is most appropriate when:"
    options:
      - "A critical production bug must be fixed immediately"
      - "The PR is complex and requires deep reading without time pressure"
      - "The author and reviewer are in the same office"
      - "The PR has only one-line changes"
    correctIndex: 1
    feedback: "Async review works best when the reviewer needs uninterrupted time to understand complex changes carefully. Synchronous (pair or real-time) review is better for urgent fixes, onboarding, or when the author needs to explain context that is hard to communicate in comments."
  - type: MULTIPLE_CHOICE
    question: "As a PR author, you receive a review comment you disagree with. What is the best response?"
    options:
      - "Ignore the comment and merge anyway"
      - "Make the change silently to end the review faster"
      - "Reply with your reasoning, explain your approach, and discuss — reaching agreement before merging"
      - "Escalate to a manager immediately"
    correctIndex: 2
    feedback: "Disagreements in code review are normal and healthy. The right response is respectful discussion: explain your reasoning, understand the reviewer's concern, and reach a shared conclusion. Silent compliance hides important context; ignoring the comment erodes trust. Professional disagreement resolved through dialogue produces better code and stronger teams."
retrieval:
  recall: "List five categories of things a code reviewer should look for."
  explain: "Explain why excessive nitpicking in code reviews is harmful to team culture and productivity."
  mistakeId:
    code: |
      // Review comment left on a PR:
      "This whole approach is wrong. I wouldn't have done it this way at all.
       Please rewrite from scratch."
    answer: "This comment is unconstructive in three ways: it does not explain what is wrong, it does not suggest how to fix it, and 'I wouldn't have done it this way' is not a technical objection. A reviewer's personal preference is not a blocker. Constructive rewrite: 'The approach here couples the service layer to the HTTP client directly. Could we introduce an interface and inject the HTTP client? This would make the service testable without a real HTTP call. Happy to discuss the pattern if helpful.'"
---

# Hook

Your code compiles. Your tests pass. You feel good about the implementation. Then a colleague opens your pull request and leaves five comments — one pointing out a potential null pointer exception you missed, one questioning a design decision, and three about personal style preferences you disagree with. How do you respond? How do you feel?

Code review is one of the most impactful — and emotionally loaded — professional practices in software development. Done well, it catches real bugs, distributes knowledge, and makes the whole team better. Done poorly, it creates friction, damages relationships, and slows development to a crawl. The difference is in how reviewers give feedback and how authors receive it.

> Reflection: Think about the best and worst piece of feedback you have ever received (on code or anything else). What made the best feedback helpful? What made the worst feedback counterproductive?

# Lore Introduction

The Academy's Spell Verification Chamber was established after a catastrophically miscalculated enchantment almost destroyed the East Wing. Before then, Runesmiths submitted spells directly to the Registry without peer review. One Runesmith's blind spot was everyone's problem.

Now every spell undergoes verification by a senior peer before registration. The peer does not rewrite the spell — they read it, check for errors, suggest improvements, and confirm it is ready. New Runesmiths initially found the process uncomfortable. Over time, they came to value it. The peer review caught errors they would never have spotted alone, and they learned as much from reviewing others' work as from having their own reviewed.

# Core Learning

## Concept Introduction

**Code review** is the practice of having one or more colleagues examine a code change before it is merged into the shared codebase. It is typically conducted through a pull request on a platform like GitHub or GitLab.

**What a reviewer looks for:**

| Category | Questions to ask |
|---|---|
| **Correctness** | Does this code do what it claims? Are there logic errors? |
| **Edge cases** | What happens with null, empty, zero, negative, large inputs? |
| **Tests** | Are tests present? Do they test the right things? Are edge cases covered? |
| **Readability** | Can a new developer understand this in five minutes? |
| **Security** | Is user input validated? Are auth checks present? Are secrets hardcoded? |
| **Design** | Does this violate SOLID? Are concerns properly separated? |

## Why It Matters

**Bug catching.** Studies consistently show that code review catches 60-90% of defects before they reach production. A second pair of eyes sees what the author's brain filters out.

**Knowledge sharing.** Reviewing code means learning about parts of the codebase you did not write. Being reviewed means receiving feedback that teaches you patterns and catches gaps in your understanding. Both sides learn.

**Collective ownership.** When the team reviews code, the team owns it. No single person is a critical bottleneck for understanding or maintaining any component.

## Worked Examples

**Constructive vs poor feedback:**

```
// Poor: vague, no direction
"This is wrong. Please fix."

// Poor: personal preference without value
"I would have named this variable 'x'."

// Poor: blocks without explanation
"I don't like this approach."

// Good: specific, explains the problem, suggests a fix
"Line 47: 'questRepository.findById(id)' will return null if not found in some
legacy implementations. Consider using 'findById(id).orElseThrow(QuestNotFoundException::new)'
to make the error explicit. Happy to discuss."

// Good: acknowledges trade-off, asks a question
"The service layer is calling the HTTP client directly — this makes it difficult
to test without a live endpoint. Could we extract a SearchClient interface and
inject it? Or is there a reason to keep it coupled here that I'm missing?"
```

**As the author, responding to review:**

```
// Disagreement handled professionally
Reviewer: "I'd move this logic to the domain layer."
Author: "Good point — I put it in the service because it depends on the HTTP client.
         If we extract a port interface for the HTTP call, it could move to domain.
         Want to discuss in the next standup? Happy to refactor if the team agrees."
```

## Common Mistakes

**Nitpick culture.** Leaving 15 comments about variable naming and formatting with no substantive observations. This creates friction, demoralises authors, and makes reviewers seem pedantic rather than valuable. Use an automated formatter (Checkstyle, Prettier) for style issues — not human review.

**Blocking PRs for personal preference.** "I would have done it differently" is not a valid blocker unless the difference affects correctness, readability, performance, or security.

**Not reviewing as a learning opportunity.** Reviewers who only look for "mistakes" miss the chance to learn from approaches that are different from their own.

**Overwhelming the author.** Twenty comments on a single PR is exhausting. If a PR is so large that 20 comments are warranted, the PR is too large — not the review.

## Mental Model

Think of code review as peer editing for writing. A good editor does not rewrite your manuscript in their own voice — they point out where the argument is unclear, where a fact might be wrong, where a reader would be confused. They ask questions more than they make demands. The writer ultimately decides what to change. The goal is a better document, not the editor's preferred document.

## Mini Summary

- Code review catches bugs, shares knowledge, and creates collective ownership.
- Review for: correctness, edge cases, tests, readability, security, design.
- Good comments are specific, explain the problem, and suggest a path forward.
- Nitpicking style preferences wastes time — automate style with linters.
- As an author, engage thoughtfully with comments; disagreement is resolved through discussion, not ignored or silently complied with.

# Guided Practice Quest

**Quest: The Spell Verification Chamber**

You are a senior peer in the Verification Chamber. You must demonstrate that you understand what to review, how to give constructive feedback, and how to avoid the nitpick trap.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Review this code snippet as if it were submitted in a pull request. Leave three review comments: one about correctness/edge cases, one about testability, and one about readability. Each comment must be constructive (specific problem + suggested fix). Then write a reflection (minimum 80 words) on what separates a helpful review from a harmful one.

```java
public class AchievementService {
    public String award(String studentName, int score) {
        String badge = "";
        if (score > 90) {
            badge = "GOLD";
        } else if (score > 70) {
            badge = "SILVER";
        }
        DatabaseUtil.saveBadge(studentName, badge);
        return badge;
    }
}
```

# Integration

**Connecting to Psychology — Feedback and the Growth Mindset**

Psychologist Carol Dweck's research on mindset distinguishes two orientations: a *fixed mindset* treats ability as innate and stable; a *growth mindset* treats ability as developable through effort and feedback. Her research found that people with a fixed mindset tend to avoid challenges (risking their self-image), ignore feedback (threatening to their self-concept), and give up after setbacks. Growth mindset individuals seek challenges, use feedback to improve, and persist.

Code review is a high-frequency feedback environment. The same code change might receive five comments from a reviewer. For someone with a fixed mindset, each comment reads as an indictment of their intelligence. For someone with a growth mindset, each comment is information for improvement. The framing determines the experience.

Reviewers influence which mindset authors activate. A comment that says "you wrote this wrong" targets identity. A comment that says "this approach has a risk we should address" targets the code. Psychological safety research shows that teams where feedback is consistently framed as impersonal and constructive produce more review activity, catch more bugs, and have lower turnover — because people feel safe being fallible.

> Reflection: Think about your own reaction to receiving critical feedback on your work. What conditions make you feel safe receiving that feedback? How can you create those same conditions when you are the reviewer?

# Lore Conclusion

Apprentice Sera submitted her first enchantment for Verification Chamber review with sweating palms. The senior reviewer, Runesmith Kael, spent twenty minutes reading it carefully before leaving three annotations.

The first: a potential instability in the containment circle she had not noticed. The second: a question about why she had chosen a binding rune over a sealing rune — not wrong, but worth discussing. The third: a single word — "elegant" — next to her recursive resonance calculation.

After the review, Sera sat for a moment. The instability would have caused a failure. She would not have caught it alone. And the question about binding vs sealing runes had taught her something she would use for the rest of her career. "Is it always this useful?" she asked. Kael looked up from his desk. "Only if both people take it seriously."

---
