---
id: fe-jun-m9-09
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: build_pipelines
topicTitle: "Build Pipelines"
topicSortOrder: 3
lesson: environment_configuration
title: "Environment Configuration"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-07, fe-jun-m9-08]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what .env files are and how they work in Vite"
    - "Explains which .env files should be committed vs gitignored"
    - "Describes what the VITE_ prefix requirement means for security"
    - "Explains how to use different values for dev vs production"
  keywords: [.env, environment, VITE_, secret, gitignore, commit, dev, production, import.meta.env, mode]
  modelAnswer: |
    .env files contain key=value pairs for environment-specific configuration. Vite loads different .env files based on the mode: .env (all modes), .env.development (dev only), .env.production (production build only), .env.local (any mode, never committed). Variables must be prefixed with VITE_ to be exposed to browser code; others stay server-side only. Never commit secrets (API keys, passwords) — put them in .env.local (gitignored) or CI environment secrets. Commit .env with safe placeholder values and .env.example documenting what's needed. In code, access variables via import.meta.env.VITE_VAR_NAME.
guidedSteps:
  - id: fe-jun-m9-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A developer stores their Stripe secret key in `.env` as `VITE_STRIPE_SECRET=sk_live_...` and commits it. What is wrong?"
    inputConfig:
      options:
        - "The VITE_ prefix is incorrect for Stripe keys"
        - "VITE_ variables are bundled into the client JavaScript, exposing the secret to anyone who views the source"
        - "Stripe keys must be stored in localStorage instead"
        - "The .env file format doesn't support API keys"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["VITE_ variables are bundled into the client JavaScript, exposing the secret to anyone who views the source"]
      rejectedFeedback: "VITE_ variables are inlined into the JavaScript bundle. Anyone who opens browser DevTools, views source, or downloads the JS file can read VITE_STRIPE_SECRET. Stripe secret keys must never touch client code — they belong on the server. Additionally, committing the key to git exposes it to anyone with repository access (and git history is permanent). Use non-VITE_ variables for secrets; access them server-side only."
    hint: "Remember what VITE_ means: the variable appears in browser JavaScript. Who can see browser JavaScript?"
    reflectionPrompt: "A secret that appears in client-side JavaScript is not a secret. It's public. The browser, browser extensions, and network monitoring tools can all read it. The only safe secrets are those that never leave the server."
  - id: fe-jun-m9-09-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What is the difference between `.env` and `.env.local`, and which should be committed to git?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [commit, gitignore, shared, personal, local, secret, .env, team, default]
      rejectedFeedback: ".env contains shared configuration for the whole team — safe defaults (API URLs pointing to dev servers, feature flags) — and should be committed. .env.local overrides .env for local personal settings and should be in .gitignore. It's where each developer puts their personal secrets or local dev overrides. This pattern: team shares .env (safe, committed), individuals override with .env.local (personal, ignored)."
    hint: "Which file would be different for each developer on the team?"
    reflectionPrompt: "The .env (committed) + .env.local (ignored) pattern is the correct way to share configuration defaults without sharing secrets. Always add .env.local to .gitignore."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You want different API URLs in development and production. Which file approach is correct?"
    options:
      - "One .env file, comment out the URL you're not using"
      - ".env.development with the dev URL, .env.production with the production URL"
      - "Hardcode both URLs in the app and use an if statement"
      - "Use .env.local for both environments"
    correctIndex: 1
    feedback: "Vite automatically loads .env.development when running the dev server and .env.production when building. Define VITE_API_URL in each with the appropriate value. The code stays the same (import.meta.env.VITE_API_URL) — the value changes based on the build mode."
retrieval:
  recall: "Which .env files does Vite load, and when does it load each one?"
  explain: "Why should private API secrets never be prefixed with VITE_?"
  mistakeId:
    code: |
      // Hardcoded API URL in source code
      const API_URL = 'https://api.arcane.academy';
      
      // Different value needed in development
      // const API_URL = 'http://localhost:8080';
    answer: "Hardcoded URLs with commented-out alternatives require manual changes for each environment — a guaranteed source of 'oops, deployed with the dev URL' bugs. Use environment variables: VITE_API_URL in .env.production and VITE_API_URL=http://localhost:8080 in .env.development. In code: const API_URL = import.meta.env.VITE_API_URL. Now the same code works correctly in every environment without manual changes."
---

# Hook

Your app calls `localhost:8080` in production. You forgot to change the hardcoded URL. Your users see a connection refused error. This should never happen — but it does, and it's embarrassing.

Environment configuration exists so the same code works correctly in every environment, automatically.

# Lore Introduction

*"The Academy's portals behave differently by design,"* the Portal Keeper explains. *"The training portal allows any apprentice. The examination portal requires credentials. The grand portal is sealed except on solstice."*

She holds up three keys — identical in shape, different in which portal they open. *"Same door shape. Different permissions. Different configuration. Your .env files are the keys."*

# Core Learning

## Concept Introduction

**.env files** configure environment-specific values:

```bash
 # .env (committed — safe defaults for all environments)
VITE_APP_NAME=Arcane Academy
VITE_API_URL=https://api.arcane.academy

 # .env.development (committed — dev overrides)
VITE_API_URL=http://localhost:8080

 # .env.production (committed — production overrides)
VITE_API_URL=https://api.arcane.academy

 # .env.local (NOT committed — personal secrets and local overrides)
VITE_API_URL=http://localhost:3001  # My personal dev setup
```

**File loading priority (highest to lowest):**
1. `.env.[mode].local`
2. `.env.[mode]`
3. `.env.local`
4. `.env`

**In code:**
```ts
// Vite exposes VITE_ variables via import.meta.env
const apiUrl = import.meta.env.VITE_API_URL;
const appName = import.meta.env.VITE_APP_NAME;
const isDev = import.meta.env.DEV;       // boolean
const isProd = import.meta.env.PROD;     // boolean
const mode = import.meta.env.MODE;       // 'development' | 'production'
```

**What to commit vs ignore:**

| File | Commit? | Why |
|---|---|---|
| `.env` | ✅ Yes | Safe defaults shared by team |
| `.env.development` | ✅ Yes | Dev config shared by team |
| `.env.production` | ✅ Yes | Production config shared by team |
| `.env.local` | ❌ No | Personal overrides and secrets |
| `.env.*.local` | ❌ No | Environment-specific secrets |

## Common Mistakes

- **Putting secrets in VITE_ variables.** They appear in the browser bundle. Use non-prefixed variables for server-side secrets.
- **Not committing .env.** Without it, new developers don't know what variables are needed. Commit .env with placeholder values; add a .env.example documenting each variable.
- **Not adding .env.local to .gitignore.** Vite's template does this — but check if you set up manually.
- **Different variable names per environment.** The variable name should be the same everywhere; only the value changes.

## Mini Summary

- .env files configure environment-specific values; loaded by Vite based on mode
- VITE_ prefix exposes variables to browser code; others stay server-side only
- Commit .env, .env.development, .env.production — gitignore .env.local
- Never put secrets in VITE_ variables — they're public in the browser bundle

# Guided Practice Quest

Work through the two guided steps to confirm you understand the security implications of VITE_ variables and the commit strategy.

# Solo Practice Quest

Design the .env file strategy for a project that needs: a public API URL (different per environment), a Stripe publishable key (safe for frontend), a Stripe secret key (never in frontend), and a local developer database URL (different per developer). Describe which file each goes in and why.

# Integration

**Philosophy — The Twelve-Factor App**

The Twelve-Factor App methodology (Heroku, 2012) is a set of principles for building modern software-as-a-service applications. Factor III specifically addresses configuration: "Store config in the environment." The argument: config varies between deployments (dev, staging, production) but code does not. Code and config should be strictly separated so the same codebase can be deployed anywhere by changing only environment variables — not source files. Vite's .env system is a direct implementation of this principle in the frontend context. The VITE_ prefix adds a further refinement: only explicitly public config reaches the client; sensitive config remains server-side. This is Factor III applied with a security layer — code, public config, and private config are three distinct concerns.

# Lore Conclusion

*"Three keys, three portals, one door design,"* the Portal Keeper says, surveying the .env files. *"The same code, different configurations. In training: localhost. In examination: staging. In the grand hall: production. The code does not change — the configuration does."*

---
