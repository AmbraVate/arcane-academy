---
id: fe-jun-m9-03
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: npm
topicTitle: "npm"
topicSortOrder: 1
lesson: scripts_and_workspaces
title: "Scripts and Workspaces"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-01, fe-jun-m9-02]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what npm scripts are and how to run them"
    - "Names at least four common script names used in frontend projects"
    - "Explains what npm run does when a script is defined"
    - "Describes what a monorepo workspace is at a basic level"
  keywords: [script, run, dev, build, test, lint, preview, workspace, monorepo, package.json]
  modelAnswer: |
    npm scripts are command shortcuts defined in package.json under "scripts". You run them with npm run <name>. Common scripts: dev (start dev server), build (production build), test (run tests), lint (run ESLint), preview (preview production build locally). The commands can be anything: Vite CLI commands, shell commands, or chained commands. npm workspaces allow a single repository to contain multiple related packages (e.g., frontend, backend, shared-types), each with its own package.json, while sharing a single node_modules at the root.
guidedSteps:
  - id: fe-jun-m9-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A package.json has `\"scripts\": { \"test\": \"vitest\" }`. What command runs the tests?"
    inputConfig:
      options:
        - "npm vitest"
        - "npm run test"
        - "node vitest"
        - "vitest --run"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["npm run test"]
      rejectedFeedback: "Scripts defined in package.json are run with `npm run <script-name>`. The exception: `test`, `start`, and `install` can be run without the word 'run' (npm test, npm start). For all others (dev, build, lint, preview), you need `npm run <name>`."
    hint: "The command to run scripts defined in package.json is npm run followed by the script name."
    reflectionPrompt: "npm scripts are wrappers around CLI commands. They let you standardise what 'run tests' means for your project — regardless of whether it's Vitest, Jest, or something else. `npm run test` always works, even if the underlying tool changes."
  - id: fe-jun-m9-03-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "You have a project with both a React frontend and a Node.js backend. Describe (in simple terms) how npm workspaces would help you manage this compared to having two completely separate repositories."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [shared, single, monorepo, root, package, types, install, together, both]
      rejectedFeedback: "With workspaces: one root npm install installs dependencies for both packages. You can share TypeScript types, utilities, or configuration between frontend and backend from a shared package. Scripts can be run across all packages. The alternative (two repos) means coordinating changes across repos, managing separate installs, and duplicating shared code."
    hint: "What would be the pain of keeping frontend and backend in separate repos that share some TypeScript types?"
    reflectionPrompt: "Workspaces are a stepping stone to understanding monorepos. The benefit: shared code and unified tooling. The cost: more complex configuration and build orchestration."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `npm run build` typically do in a Vite React project?"
    options:
      - "Downloads and updates all dependencies"
      - "Runs tests in build mode"
      - "Compiles and bundles the app for production deployment"
      - "Starts the development server"
    correctIndex: 2
    feedback: "The build script typically runs `vite build`, which transpiles TypeScript, bundles all JS/CSS into optimised files, and outputs them to a dist/ folder ready for production deployment. The dev script starts the dev server. Build and dev are separate tools for separate purposes."
retrieval:
  recall: "What are the four most common script names in a frontend project and what does each do?"
  explain: "Why do teams standardise script names (dev, build, test, lint) across projects?"
  mistakeId:
    code: |
      // A developer runs the dev server like this every day:
      ./node_modules/.bin/vite --host --port 3000
    answer: "This works but defeats the purpose of scripts. The correct approach: define a script in package.json: `\"dev\": \"vite --host --port 3000\"`, then run `npm run dev`. Now every developer and CI pipeline uses the same command. If the flags change, you update one line in package.json — not every developer's mental model. Script names are the shared vocabulary for project tasks."
---

# Hook

A new developer joins the team. They ask: "How do I start the app? How do I run the tests? How do I build for production?"

In a well-configured project: `npm run dev`, `npm run test`, `npm run build`. Three answers, one pattern, works on any project that follows the convention.

npm scripts are the common language of frontend projects.

# Lore Introduction

*"Every guild has its rituals,"* the Guild Chronicler explains. *"The morning ritual: light the forge. The afternoon ritual: inspect the work. The closing ritual: seal the archive."*

She opens the package.json. *"Your project has rituals too. Starting the development server. Running the tests. Building for deployment. Define them once, name them clearly, and every guild member knows how to perform them."*

# Core Learning

## Concept Introduction

**npm scripts** are shortcuts for CLI commands, defined in package.json:

```json
{
  "scripts": {
    "dev":     "vite",
    "build":   "vite build",
    "preview": "vite preview",
    "test":    "vitest",
    "lint":    "eslint src --ext .ts,.tsx",
    "format":  "prettier --write src"
  }
}
```

Run any script with:
```bash
npm run dev
npm run build
npm run test
npm run lint
```

**Why standardise script names?** Every React project has different tools (Vite, Webpack, Create React App). But if all projects use the same script names, developers don't need to re-learn how to run each project.

**Chaining scripts:**
```json
"check": "npm run lint && npm run test && npm run build"
```

**npm workspaces** (monorepo basics):
```json
// Root package.json
{
  "workspaces": ["packages/frontend", "packages/backend", "packages/shared"]
}
```

One `npm install` at the root installs dependencies for all packages. The `shared` package can be imported by both frontend and backend.

## Common Mistakes

- **Long, unmemorable commands.** If you find yourself typing the same complex command repeatedly, add it as a script.
- **Different script names per project.** Teams that use `npm run start`, `npm run serve`, and `npm run dev` interchangeably create cognitive friction. Pick one and standardise.
- **Not running scripts via npm.** Calling `./node_modules/.bin/vite` directly works but loses the npm scripts benefit — teammates won't know how to replicate it.
- **Ignoring the pre/post hooks.** npm automatically runs `pretest` before `test` and `postbuild` after `build` if you define them — useful for cleanup or setup steps.

## Mini Summary

- Scripts are CLI command aliases defined in package.json
- Run with `npm run <name>`; common names: dev, build, test, lint, preview
- Standardised names mean any developer knows how to work with any project
- npm workspaces let multiple packages share a single node_modules

# Guided Practice Quest

Work through the two guided steps to verify you understand how to use and design npm scripts.

# Solo Practice Quest

Design the scripts section for a React + Tailwind + Vitest project. Define at least 5 scripts, give each a clear name, and write the command it should run. Explain why each script name was chosen.

# Integration

**Psychology — Convention over Configuration**

The principle "convention over configuration" (originating in Ruby on Rails) describes how agreeing on standard names reduces cognitive load. When every project uses `npm run dev` to start the dev server, developers don't need to read documentation every time they join a project — the pattern is automatic. This maps to cognitive psychology's chunking: familiar patterns are processed as single units rather than individual steps. The standardised script names (dev, build, test, lint) are a shared chunk — a piece of professional vocabulary that, once learned, transfers to every project. Teams that invent unique naming schemes force re-learning the same concepts in every context, unnecessarily consuming working memory.

# Lore Conclusion

*"The rituals are named and recorded,"* the Chronicler says, reviewing the scripts. *"Any guild member who arrives tomorrow will know exactly how to begin. Light the forge: `npm run dev`. Inspect the work: `npm run test`. Seal the archive: `npm run build`."*

The npm tome is complete.

---
