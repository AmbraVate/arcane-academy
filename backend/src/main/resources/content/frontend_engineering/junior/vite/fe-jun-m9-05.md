---
id: fe-jun-m9-05
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: vite
topicTitle: "Vite"
topicSortOrder: 2
lesson: vite_configuration
title: "Vite Configuration"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-04]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains where Vite is configured"
    - "Describes how to add a plugin to Vite"
    - "Explains what path aliases are and why they help"
    - "Explains what import.meta.env is and how environment variables work in Vite"
  keywords: [vite.config.ts, plugin, alias, VITE_, import.meta.env, env, path, resolve]
  modelAnswer: |
    Vite is configured in vite.config.ts at the root of the project. Plugins extend Vite's capabilities: the React plugin adds JSX transformation and HMR support. Path aliases are shortcuts for import paths — resolve.alias allows @/ to map to src/, so you write import { Button } from '@/components/Button' instead of relative paths like '../../components/Button'. Environment variables must start with VITE_ to be exposed to client code; they are accessed via import.meta.env.VITE_API_URL. Variables without the VITE_ prefix are server-side only and not exposed to the browser.
guidedSteps:
  - id: fe-jun-m9-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want to import a component as `import { Button } from '@/components/Button'` instead of `'../../components/Button'`. What Vite config option enables this?"
    inputConfig:
      options:
        - "plugins: [alias('@', './src')]"
        - "resolve.alias: { '@': path.resolve(__dirname, './src') }"
        - "import.meta.alias['@'] = './src'"
        - "baseUrl: '@/'"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["resolve.alias: { '@': path.resolve(__dirname, './src') }"]
      rejectedFeedback: "Path aliases are configured under resolve.alias in vite.config.ts. The value must be an absolute path (use path.resolve), not a relative one. This maps the '@' prefix to your src directory, so imports using '@/' work from anywhere in the project."
    hint: "Look in the resolve section of vite.config.ts, not plugins."
    reflectionPrompt: "Relative imports like '../../../components' are fragile — they break when files move. Absolute aliases like '@/components' work from anywhere and make the import's meaning clear."
  - id: fe-jun-m9-05-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "You have an API URL that differs between development and production. How would you manage this using Vite environment variables?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [VITE_, .env, import.meta.env, development, production, prefix, expose]
      rejectedFeedback: "Create a .env file for development and a .env.production file for production. In each, define VITE_API_URL=https://... (must start with VITE_ to be exposed to browser code). In your code, access it with import.meta.env.VITE_API_URL. Vite automatically uses the right file based on the mode (dev vs build). Never put secrets in VITE_ variables — they're visible in the browser."
    hint: "What prefix must environment variable names have to be accessible in browser code?"
    reflectionPrompt: "The VITE_ prefix requirement is a deliberate security feature. It prevents accidental exposure of server secrets (database passwords, API keys) to the browser. You must explicitly opt-in to exposing each variable."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which environment variable name would be accessible in browser code via `import.meta.env`?"
    options:
      - "API_KEY=abc123"
      - "REACT_APP_API=url"
      - "VITE_API_URL=https://api.example.com"
      - "NODE_ENV=production"
    correctIndex: 2
    feedback: "Only variables prefixed with VITE_ are exposed to client-side code in Vite. NODE_ENV is accessible separately via import.meta.env.MODE. REACT_APP_ is the Create React App convention. API_KEY without a prefix stays server-side only."
retrieval:
  recall: "What is the name of Vite's configuration file and what does it export?"
  explain: "Why must environment variables start with VITE_ to be accessible in the browser?"
  mistakeId:
    code: |
      // .env
      DB_PASSWORD=supersecret123
      VITE_DB_PASSWORD=supersecret123
    answer: "DB_PASSWORD without the VITE_ prefix is server-side only — correctly kept secret. VITE_DB_PASSWORD is exposed to the browser bundle — anyone can see it in the browser's developer tools or by inspecting the built JavaScript files. Never put secrets, passwords, or private API keys in VITE_ variables. Only put public values there (API URLs, feature flags, public keys). For secrets, use server-side environment variables that never reach the client."
---

# Hook

Your app works in development but breaks in production because the API URL is hardcoded to `localhost:8080`. Your colleague's import paths are full of `../../../../utils/helpers`. 

Both problems have the same root: no proper configuration. `vite.config.ts` solves both.

# Lore Introduction

*"Every spellcasting studio has its configuration scroll,"* the Studio Master explains. *"Which reagents are available, which shortcuts are permitted, which portals connect where. Without it, every caster improvises — and improvisation breeds inconsistency."*

She unfurls a configuration scroll. *"Vite has its own. Small, powerful, and shared by everyone on the project."*

# Core Learning

## Concept Introduction

**vite.config.ts** — the central configuration file:

```ts
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  // Plugins extend Vite's capabilities
  plugins: [react()],

  // Path aliases — import from '@/' instead of relative paths
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },

  // Dev server options
  server: {
    port: 3000,
    proxy: {
      '/api': 'http://localhost:8080', // Proxy API calls to backend
    },
  },
});
```

**Environment variables (.env files):**

```bash
# .env          — used in all environments
# .env.local    — local overrides (not committed)
# .env.development — dev only
# .env.production  — production only
```

```bash
# .env
VITE_API_URL=http://localhost:8080  # Exposed to browser
DB_PASSWORD=secret                  # NOT exposed (no VITE_ prefix)
```

```ts
// In your code
const apiUrl = import.meta.env.VITE_API_URL;
const mode = import.meta.env.MODE; // 'development' | 'production'
```

## Why It Matters

Without proper configuration:
- Hardcoded URLs break between environments
- Long relative imports break when files move
- Team members use different ports or proxy settings

With vite.config.ts:
- Consistent setup for the whole team (committed to git)
- Path aliases make imports readable and refactoring-safe
- Environment variables let the same codebase work in dev and production

## Common Mistakes

- **Putting secrets in VITE_ variables.** They appear in the browser bundle. Anyone can read them. Use non-prefixed variables for secrets (they stay server-side).
- **Using relative paths in aliases.** `alias: { '@': './src' }` doesn't work — use `path.resolve(__dirname, './src')`.
- **Not adding the alias to tsconfig.json.** TypeScript needs to know about aliases too: `"paths": { "@/*": ["./src/*"] }`.
- **Committing .env.local.** Local environment files should be in .gitignore. Commit .env with safe defaults; local overrides go in .env.local.

## Mini Summary

- Vite is configured in `vite.config.ts` using `defineConfig`
- Add plugins via the `plugins` array (react(), tailwindcss(), etc.)
- Path aliases via `resolve.alias` eliminate long relative imports
- Environment variables need `VITE_` prefix to be accessible in browser code

# Guided Practice Quest

Work through the two guided steps to verify you understand path aliases and the environment variable security model.

# Solo Practice Quest

Write a `vite.config.ts` for a React + TypeScript project that: uses the React plugin, adds a `@` alias for the src folder, proxies `/api` requests to `http://localhost:8080`, and runs the dev server on port 3000. No need to run it — just write the config.

# Integration

**Design — Configuration as Convention Documentation**

A well-written config file is also documentation. `resolve.alias: { '@': './src' }` communicates: "we use @ to refer to the source root." `server.proxy: { '/api': '...' }` communicates: "our frontend calls a backend at this address." These are architectural decisions encoded in a file that every developer sees when they join the project. This aligns with the design principle of "making the right thing easy and visible." Confucius noted that good governance requires that names match realities — in modern software, the configuration file is a form of governance: it declares the intended structure of the project, helping teams converge on shared conventions rather than inventing them independently.

# Lore Conclusion

*"The scroll is complete,"* the Studio Master says, reviewing the config file. *"Portals configured, shortcuts named, secrets protected. Every member of the studio works from the same foundation."*

---
