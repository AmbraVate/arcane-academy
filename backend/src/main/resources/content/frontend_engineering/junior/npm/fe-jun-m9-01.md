---
id: fe-jun-m9-01
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
lesson: package_management
title: "Package Management"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-15]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what npm is and what problem it solves"
    - "Distinguishes between dependencies and devDependencies"
    - "Describes the role of package.json"
    - "Explains what npm install does"
  keywords: [npm, package, dependency, devDependency, install, package.json, registry, node_modules]
  modelAnswer: |
    npm is the Node Package Manager — a registry and tool for managing JavaScript libraries (packages) your project depends on. package.json lists your project's dependencies: packages needed at runtime go in dependencies (React, axios), packages only needed during development go in devDependencies (Vitest, ESLint, TypeScript). Running npm install reads package.json and downloads all listed packages into node_modules. This means you don't commit node_modules — any developer can clone the project, run npm install, and get the exact same packages.
guidedSteps:
  - id: fe-jun-m9-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You are installing Vitest, which you only use for running tests. Where should it be listed in package.json?"
    inputConfig:
      options:
        - "dependencies — all packages go here"
        - "devDependencies — it's only needed during development, not in production"
        - "peerDependencies — testing tools are always peer dependencies"
        - "optionalDependencies — tests are optional"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["devDependencies — it's only needed during development, not in production"]
      rejectedFeedback: "devDependencies are packages needed to build and test the project but not to run it in production. Vitest, ESLint, TypeScript, and bundlers belong here. React and your API libraries belong in dependencies. This distinction reduces the size of your production deployment — devDependencies aren't installed in production environments."
    hint: "Does a deployed production server need to run your tests?"
    reflectionPrompt: "Separating dev from production dependencies is a performance and security concern. Fewer packages in production means smaller attack surface and faster deployments."
  - id: fe-jun-m9-01-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A colleague clones your repository. There is no node_modules folder. What single command do they run to set up the project, and what does it do?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [npm install, install, package.json, node_modules, download, packages]
      rejectedFeedback: "They run `npm install`. This reads package.json and package-lock.json, then downloads all listed packages into a new node_modules folder. The lock file ensures they get exactly the same versions you used — not whatever the latest versions happen to be."
    hint: "What command reads package.json and downloads everything it lists?"
    reflectionPrompt: "node_modules is not committed to git (it's in .gitignore) because it's large and reproducible. package.json is the recipe; npm install is the cook."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You run `npm install react`. Where is the package installed?"
    options:
      - "Globally on your machine, available to all projects"
      - "Into the node_modules folder in the current project directory"
      - "Into the src folder alongside your source code"
      - "Into the browser's cache"
    correctIndex: 1
    feedback: "npm install (without -g flag) installs packages locally into the project's node_modules folder. Each project has its own node_modules, so different projects can use different versions of the same package. Global install (-g) is only for CLI tools you want to run from anywhere."
retrieval:
  recall: "What is the difference between dependencies and devDependencies?"
  explain: "Why is node_modules not committed to git?"
  mistakeId:
    code: |
      // Developer commits node_modules to git
      git add node_modules
      git commit -m "add dependencies"
    answer: "node_modules can contain hundreds of thousands of files and be gigabytes in size. Committing it bloats the repository, slows clones and checkouts, and creates merge conflicts on every dependency update. The correct approach: add node_modules to .gitignore, commit package.json and package-lock.json, and let every developer run npm install. The lock file guarantees they get identical package versions."
---

# Hook

You clone a colleague's project. There's a src folder, a package.json, but no node_modules. You try to run it. Errors everywhere — modules not found.

Five minutes later, you run one command. Everything works.

That command is `npm install`. Understanding why reveals the entire package management system.

# Lore Introduction

*"Every guild maintains a register of its tools,"* the Toolmaster explains. *"The register lists what is needed — not the tools themselves. When a new apprentice joins, they bring the register to the storehouse and collect precisely what it lists."*

She taps the package.json. *"This is the register. The storehouse is the npm registry. node_modules is the apprentice's tool belt — assembled fresh from the register each time."*

# Core Learning

## Concept Introduction

**npm** (Node Package Manager) has two roles:
1. A **registry** — an online database of 2+ million JavaScript packages
2. A **CLI tool** — commands to install, update, and manage packages

**package.json** — the project manifest:
```json
{
  "name": "guild-dashboard",
  "version": "1.0.0",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "test": "vitest"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0"
  },
  "devDependencies": {
    "vite": "^5.0.0",
    "vitest": "^1.0.0",
    "@testing-library/react": "^14.0.0",
    "typescript": "^5.0.0"
  }
}
```

| Section | Purpose |
|---|---|
| `dependencies` | Needed at runtime in production |
| `devDependencies` | Needed during development/testing only |
| `scripts` | Shortcuts for common commands |

**Common commands:**
```bash
npm install              # Install all dependencies from package.json
npm install react        # Add react to dependencies
npm install -D vitest    # Add vitest to devDependencies
npm uninstall lodash     # Remove a package
npm run dev             # Run the "dev" script
```

## Why It Matters

Before package managers, sharing JavaScript libraries meant manually downloading files, tracking versions by hand, and hoping your team was using the same version. npm standardised this: declare what you need, let the tool fetch it. Any developer on any machine can reproduce your exact dependency set.

## Common Mistakes

- **Committing node_modules.** It's large, slow to manage, and reproducible via `npm install`. Add it to `.gitignore`.
- **Confusing dependencies and devDependencies.** dev tools in `dependencies` bloat production deployments; production packages in `devDependencies` break production builds.
- **Using `npm install packagename` without checking the right flag.** Default: `dependencies`. Use `-D` for dev tools.
- **Not committing package-lock.json.** The lock file ensures reproducible installs — always commit it.

## Mini Summary

- npm manages third-party libraries your project depends on
- package.json lists dependencies (production) and devDependencies (dev only)
- `npm install` downloads everything listed into node_modules
- Never commit node_modules; always commit package-lock.json

# Guided Practice Quest

Work through the two guided steps to check your understanding of dependency categories and what npm install actually does.

# Solo Practice Quest

Open (or imagine) a React + Tailwind + Vitest project. Categorise these into dependencies vs devDependencies: react, react-dom, tailwindcss, vitest, @testing-library/react, axios, typescript, vite. Explain your reasoning for each.

# Integration

**Mathematics — Dependency Graphs**

npm packages depend on other packages, which depend on others — forming a directed acyclic graph (DAG). When you install React, npm resolves not just React itself but all of React's dependencies, and their dependencies. This graph can contain hundreds of packages from a single install. npm uses Dijkstra-like resolution to find compatible versions that satisfy all constraints simultaneously — a version of the constraint satisfaction problem. The lock file (package-lock.json) captures the resolved graph, ensuring every developer gets the same DAG regardless of when they install.

# Lore Conclusion

*"The register is correct,"* the Toolmaster says, reviewing the package.json. *"Every tool listed, nothing missing, nothing superfluous. Any apprentice who joins the guild can collect their tools in one visit to the storehouse."*

The npm rune lights up in your grimoire.

---
