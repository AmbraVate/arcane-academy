---
id: fe-jun-m5-12
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: error_handling
topicTitle: "Error Handling"
topicSortOrder: 4
lesson: user_facing_errors
title: "User-Facing Error Messages"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-10, fe-jun-m5-11]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why technical error messages should not be shown to users"
    - "Describes what a good user-facing error message includes"
    - "Shows how to map error types to user-friendly messages"
    - "Explains the security risk of leaking error details"
  keywords: [user, message, friendly, technical, expose, map, status, security, clear]
  modelAnswer: |
    Technical error messages (stack traces, SQL errors, internal paths) confuse users and expose security vulnerabilities — an attacker can learn your stack from a verbose error. User-facing error messages should be clear, actionable, and not technical: 'Something went wrong. Please try again.' or 'That email is already taken.' Good messages tell the user what happened in plain language and what they can do next. Map HTTP status codes to specific messages: 401 → 'Please log in', 404 → 'Not found', 500 → 'Server error, try again later.' Keep technical detail in logs, not in the UI.
guidedSteps:
  - id: fe-jun-m5-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "The server returns a 500 error with this body: `{ error: 'NullPointerException at UserService.java:142' }`. What should you show the user?"
    inputConfig:
      options:
        - "The full error message including the Java stack trace"
        - "A generic message like 'Something went wrong. Please try again.'"
        - "The HTTP status code: 'Error 500'"
        - "Nothing — hide the error silently"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A generic message like 'Something went wrong. Please try again.'"]
      rejectedFeedback: "Stack traces and internal file paths confuse users and expose your implementation details to attackers. Show a friendly, actionable message. Log the technical detail internally."
    hint: "Would a stack trace help a user fix the problem? What might an attacker do with it?"
    reflectionPrompt: "Who is a user-facing error message for? Who is a logged technical error for?"
  - id: fe-jun-m5-12-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "A user tries to log in with wrong credentials. The server returns 401. Which message is most appropriate?"
    inputConfig:
      options:
        - "'HTTP 401 Unauthorized'"
        - "'Invalid credentials. Please check your email and password.'"
        - "'Authentication failed: user_not_found in auth_service.js:88'"
        - "'Access denied by server policy'"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["'Invalid credentials. Please check your email and password.'"]
      rejectedFeedback: "The second option is clear, actionable, and in plain language. It tells the user what happened and what to do. The others are either too technical, too vague, or expose implementation details."
    hint: "Good error messages are human-readable, explain what happened, and suggest what to do next."
    reflectionPrompt: "Note: you wouldn't say 'email not found' vs 'wrong password' separately — why not? (Think security.)"
  - id: fe-jun-m5-12-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Explain the security risk of showing users the raw error message from a server, and how you would safely map server errors to user messages in code."
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [security, expose, map, status, friendly, attacker, leak, stack]
      rejectedFeedback: "Raw errors can expose stack traces, file paths, database names, or library versions — useful information for attackers. Map HTTP status codes to specific friendly messages in a switch/if-else, and log technical details to the console or error service."
    hint: "Think about what information a 'SQL syntax error at users.controller.ts:45' gives to someone trying to break your app."
    reflectionPrompt: "What is the minimal information a user needs to know when a request fails?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which principle guides the design of user-facing error messages?"
    options:
      - "Be as detailed as possible so users can debug it themselves"
      - "Be clear and actionable in plain language; log technical detail separately"
      - "Always use the HTTP status code and status text"
      - "Never show error messages — redirect to the homepage"
    correctIndex: 1
    feedback: "User-facing messages should be plain-language, actionable, and free of technical jargon. Technical detail belongs in logs and error monitoring tools, not in the user interface."
retrieval:
  recall: "List three pieces of information that should NOT appear in a user-facing error message, and where that information should go instead."
  explain: "Explain why showing different error messages for 'email not found' vs 'wrong password' can be a security vulnerability."
  mistakeId:
    code: |
      .catch(err => {
        setError(err.message);
      });
      // err.message might be: "NetworkError when attempting to fetch resource"
      // or: "Unexpected token < in JSON at position 0"
    answer: "Setting error state to err.message exposes technical messages to the user. Map the error to a friendly message instead: setError('Unable to load data. Please check your connection and try again.');"
---

# Hook

A user's form submission fails. The error message reads: `SyntaxError: Unexpected token < in JSON at position 0`. The user doesn't know what JSON is. They don't know what position 0 means. They close the tab. Now add a second scenario: a malicious user sees `SELECT query failed: column 'email' doesn't exist in table 'users'` and learns your database schema. Error messages have two audiences: the confused user and the potential attacker. Neither should see raw technical errors. This lesson is about translating technical failures into human language.

# Lore Introduction

*When an Academy messenger returns with bad news, the protocol is clear: they deliver the message in plain, dignified language. "The Northern Ward was sealed and could not be accessed." They do not recite the magical incantation that failed, name the specific ward-lock mechanism, or describe which precise rune misfired — that is for the Master Engineers, recorded in the private repair logs. Users of the Academy's services deserve clarity, not confusion. And they certainly should not be given information that could be used to pick the locks.*

# Core Learning

## Concept Introduction

Error messages serve two different audiences with completely different needs:

**Technical audience** (developers, error monitoring tools):
- Stack traces, file paths, line numbers
- HTTP status codes, API response bodies
- Network logs and request details

**User audience**:
- Plain English explanation of what happened
- What they can do about it
- Whether it's their fault or the system's fault
- No technical jargon

Every error that reaches your UI should be translated from the technical language of the system to the human language of your user.

**Security note**: Raw error messages can expose:
- Database schema (table names, column names)
- File paths and framework internals
- Stack traces with library versions
- Authentication implementation details (e.g. distinguishing "user not found" from "wrong password")

## Why It Matters

Good error UX is measurable: users who receive clear, actionable error messages are more likely to recover successfully. Users who receive cryptic technical errors either give up or file support tickets. Clear error messages reduce support cost and improve trust.

## Worked Example

```js
// Helper: map HTTP status to user-friendly message
function getErrorMessage(status) {
  const messages = {
    400: 'The request was invalid. Please check your input and try again.',
    401: 'Please sign in to continue.',
    403: 'You don\'t have permission to do that.',
    404: 'We couldn\'t find what you were looking for.',
    409: 'A conflict occurred — this item may already exist.',
    422: 'Some fields are invalid. Please review your input.',
    429: 'Too many requests. Please wait a moment and try again.',
    500: 'Something went wrong on our end. Please try again later.',
    503: 'Service temporarily unavailable. Please try again soon.'
  };
  return messages[status] || 'An unexpected error occurred. Please try again.';
}

// Using it in an async function
async function submitForm(data) {
  try {
    const res = await fetch('/api/submit', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    if (!res.ok) {
      // Log technical detail privately
      console.error(`Form submit failed: HTTP ${res.status}`);
      // Show friendly message to user
      throw new Error(getErrorMessage(res.status));
    }

    return await res.json();
  } catch (err) {
    if (err.name === 'TypeError') {
      // Network failure
      throw new Error('Unable to connect. Please check your internet connection.');
    }
    throw err; // re-throw the friendly message we created
  }
}
```

In a React component with contextual error UI:
```jsx
function LoginForm() {
  const [error, setError] = React.useState(null);
  const [isLoading, setIsLoading] = React.useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      const res = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: e.target.email.value, password: e.target.password.value })
      });

      if (!res.ok) {
        // Consistent message for 401 — don't distinguish 'wrong email' vs 'wrong password'
        if (res.status === 401) {
          throw new Error('Invalid email or password. Please try again.');
        }
        throw new Error(getErrorMessage(res.status));
      }

      const { token } = await res.json();
      // handle success...

    } catch (err) {
      setError(err.message); // friendly message, not raw error
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="max-w-sm mx-auto p-6 space-y-4">
      {error && (
        <div
          role="alert"
          className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded"
        >
          {error}
        </div>
      )}
      <input name="email" type="email" className="w-full border rounded px-3 py-2" placeholder="Email" />
      <input name="password" type="password" className="w-full border rounded px-3 py-2" placeholder="Password" />
      <button
        type="submit"
        disabled={isLoading}
        className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 disabled:opacity-50"
      >
        {isLoading ? 'Signing in...' : 'Sign In'}
      </button>
    </form>
  );
}
```

## Common Mistakes

**Mistake 1: Showing raw error messages**
```js
setError(err.message); // "Unexpected token < in JSON at position 0"
// Users don't understand this. Map it to something human.
```

**Mistake 2: Over-specific auth messages**
```js
// SECURITY RISK — tells attackers which accounts exist
if (status === 404) setError('Email not registered');
if (status === 401) setError('Wrong password');
// BETTER — ambiguous but friendly
setError('Invalid email or password');
```

**Mistake 3: Technical codes in user-visible errors**
```jsx
// BAD — "Error 422: Unprocessable Entity" means nothing to a user
<p>Error {res.status}: {res.statusText}</p>
```

## Mini Summary

- Translate technical errors into plain, human-readable messages before displaying them
- Use a status-to-message map to handle HTTP errors consistently
- Never expose stack traces, file paths, or database details in the UI
- Log technical details to the console or error monitoring; show friendly messages to users
- For auth errors, use ambiguous messages to avoid leaking whether an account exists
- Good error messages are: **clear**, **actionable**, and **blame-free** where the fault is the system's

# Guided Practice Quest

Work through the guided steps to practise translating raw HTTP errors into user-appropriate messages and understanding the security implications of error exposure.

# Solo Practice Quest

Explain why user-facing error messages must be different from technical error logs. Describe the security risk of showing raw errors, and write a function that maps common HTTP status codes to friendly messages. Include an example of the right and wrong way to display a 401 authentication error.

# Integration

**Psychology — Attribution Theory:** Weiner's Attribution Theory describes how people assign causes to outcomes. When a user sees "NullPointerException at line 88," they attribute the failure to themselves — they must have done something wrong, and they don't know how to fix it. Anxiety rises. When they see "Something went wrong. Please try again." they attribute it correctly (to the system), feel less blame, and are more likely to retry. The language of error messages shapes the user's emotional response and behaviour.

**Design — Tone and Voice:** Error message writing is a UX copywriting discipline. Good UX writing for errors follows specific principles: be specific about what went wrong (not "error"), be actionable (tell them what to do), don't apologise excessively, don't blame. This is a design system concern: maintaining consistent tone across all error states is as important as consistent visual design. Companies like Mailchimp and Stripe are known for error messages that are both useful and distinctly human in tone.

# Lore Conclusion

*The Academy's public-facing notices are written by the Communication Guild: clear, dignified, helpful. "The Northern Ward is temporarily sealed for maintenance — please return after sundown." Not: "KeyedLock[ward=north].open() threw NullKeyException at MagicGate.java:42." The private repair logs contain the full technical detail. But visitors to the Academy see only what helps them act. That is the art of the user-facing message: translate complexity into clarity, and let the technical truth live in the logbook where it belongs.*

---
