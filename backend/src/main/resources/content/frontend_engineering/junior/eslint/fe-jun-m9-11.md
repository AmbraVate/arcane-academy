---
id: fe-jun-m9-11
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: eslint
topicTitle: "ESLint"
topicSortOrder: 4
lesson: configuring_eslint
title: "Configuring ESLint"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-10]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the role of eslint.config.js (flat config)"
    - "Explains what extending a shared config provides"
    - "Names the key plugins for React TypeScript projects"
    - "Describes how to configure a rule as error vs warn"
  keywords: [eslint.config.js, plugin, extends, rules, error, warn, typescript-eslint, react-hooks, flat config]
  modelAnswer: |
    ESLint configuration lives in eslint.config.js (the modern flat config format). Extending shared configs (like typescript-eslint/recommended, plugin:react-hooks/recommended) gives you a baseline set of well-chosen rules without configuring each one manually. Key plugins for React TypeScript: @typescript-eslint/eslint-plugin (TypeScript-aware rules), eslint-plugin-react-hooks (enforces hook rules), eslint-plugin-react (React-specific rules). Rules are configured as: 0 (off), 1 (warn), or 2 (error). Start from a recommended config, then override specific rules for your team's needs.
guidedSteps:
  - id: fe-jun-m9-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why use `extends: ['plugin:@typescript-eslint/recommended']` instead of configuring every TypeScript rule manually?"
    inputConfig:
      options:
        - "Manual configuration is impossible without the plugin"
        - "Recommended configs provide a curated baseline of well-tested rules, saving hours of configuration"
        - "Extended configs override all your custom rules"
        - "The plugin only works when extended, not when rules are specified manually"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Recommended configs provide a curated baseline of well-tested rules, saving hours of configuration"]
      rejectedFeedback: "Recommended configs are curated by experts who have tested which rules catch real bugs without too many false positives. Starting from recommended means you get a solid baseline immediately — without needing to know every available rule. You then selectively override rules that don't fit your team's conventions."
    hint: "How many TypeScript ESLint rules exist? Would you want to review and configure each one individually?"
    reflectionPrompt: "Extending recommended configs is 'convention over configuration'. You accept sensible defaults and customise only what differs. This is faster and usually produces better results than building from scratch."
  - id: fe-jun-m9-11-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "The `react-hooks/exhaustive-deps` rule warns when useEffect dependency arrays are incorrect. Why would you set this to 'error' rather than 'warn'?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [bug, stale, closure, real, missed, dependency, infinite, render, effect, serious]
      rejectedFeedback: "Missing useEffect dependencies cause stale closure bugs: the effect runs with an old version of a value because React doesn't know to re-run it. This is a real, production-affecting bug — not just a style issue. Setting it to 'error' ensures it's never ignored. Warnings are easily dismissed; errors block the PR and force resolution."
    hint: "What actually goes wrong in the browser when useEffect has the wrong dependency array?"
    reflectionPrompt: "The rule severity should match the real-world impact. exhaustive-deps violations cause stale state bugs — set it to error. Preferring single quotes over double quotes is a style preference — set it to warn or off."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In an ESLint rules config, what does `'no-console': 'warn'` mean?"
    options:
      - "console usage is forbidden and blocks CI"
      - "console usage is flagged but doesn't fail the lint check"
      - "console usage is allowed and no message is shown"
      - "console is removed from the bundle automatically"
    correctIndex: 1
    feedback: "0 = off (no message), 1/'warn' = warning (shown but doesn't fail), 2/'error' = error (fails lint, blocks CI). 'warn' for no-console means developers see the warning but can still push without fixing it. If you want to prevent console.log in production code from ever being merged, use 'error'."
retrieval:
  recall: "Name two ESLint plugins important for React TypeScript projects and what each adds."
  explain: "Why should you extend a recommended config rather than configure every rule from scratch?"
  mistakeId:
    code: |
      // eslint.config.js — all rules set to warn
      rules: {
        'no-unused-vars': 'warn',
        'react-hooks/rules-of-hooks': 'warn',
        '@typescript-eslint/no-explicit-any': 'warn',
        'no-undef': 'warn',
      }
    answer: "Setting bug-catching rules to 'warn' means they're ignored — developers see yellow squiggles and move on. react-hooks/rules-of-hooks violations cause runtime bugs; no-unused-vars indicates potential logic errors; no-undef means using undefined variables. These should be 'error'. Reserve 'warn' for genuine stylistic preferences. A lint config where everything is a warning is a lint config nobody respects."
---

# Hook

You add ESLint to the project. The default config catches nothing important. You spend an afternoon wrestling with configuration options, trying to understand which of 400 rules to enable.

There's a better path: start from a recommended config that experts have already curated, then tweak what doesn't fit.

# Lore Introduction

*"The Guild does not ask each smith to invent their own safety protocols,"* the Safety Officer explains. *"The protocols are established — tested by generations of smiths who encountered every way a forge can injure you. New smiths learn the established protocols, then propose amendments based on their specific forge."*

She hands you the ESLint config. *"This is the established protocol. Start here."*

# Core Learning

## Concept Introduction

Modern ESLint uses **flat config** (`eslint.config.js`):

```js
// eslint.config.js
import js from '@eslint/js';
import tseslint from 'typescript-eslint';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';

export default tseslint.config(
  { ignores: ['dist'] },

  // Extend recommended configs
  js.configs.recommended,
  ...tseslint.configs.recommended,

  {
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
    },
    rules: {
      // React hooks rules — both are errors (real bugs)
      ...reactHooks.configs.recommended.rules,
      'react-refresh/only-export-components': 'warn',

      // TypeScript overrides
      '@typescript-eslint/no-explicit-any': 'warn', // nudge but don't block
      '@typescript-eslint/no-unused-vars': 'error', // potential bugs

      // Code quality
      'no-console': 'warn',           // remind about console.log
      'eqeqeq': 'error',              // always use ===
    },
  },
);
```

**Key plugins for React TypeScript:**

| Plugin | What it adds |
|---|---|
| `typescript-eslint` | TypeScript-aware rules (no-explicit-any, etc.) |
| `eslint-plugin-react-hooks` | Hook rules enforcement |
| `eslint-plugin-react` | React best practices |
| `eslint-plugin-react-refresh` | Vite HMR compatibility rules |

**Rule values:**
- `'off'` or `0` — disabled
- `'warn'` or `1` — shows warning, doesn't fail
- `'error'` or `2` — fails lint (exit code 1)

## Common Mistakes

- **Ignoring the recommended config baseline.** Always extend recommended; manually curating 50 rules is error-prone.
- **No TypeScript plugin.** Without `@typescript-eslint`, ESLint applies JavaScript rules to TypeScript — missing many TS-specific issues.
- **Everything as warn.** Warnings get ignored. Use error for rules that catch real bugs.
- **Not ignoring dist/.** ESLint should not lint build output. Add `dist` to ignores.

## Mini Summary

- ESLint flat config lives in `eslint.config.js`
- Extend recommended configs for a curated baseline
- Add React hooks and TypeScript plugins for complete coverage
- Use 'error' for bug-catching rules, 'warn' for style preferences

# Guided Practice Quest

Work through the two guided steps to understand why recommended configs matter and when to use error vs warn.

# Solo Practice Quest

Write the rules section of an eslint.config.js for a React TypeScript project. Include at least 6 rules with appropriate severity levels. Justify each choice: why is it error vs warn? What bug or issue does each rule prevent?

# Integration

**Design — Shared Constraints as Design System**

ESLint configuration is a form of codified team agreement: rules are the team's collective decisions about which patterns are acceptable. A shared ESLint config (published as an npm package and used by all team repositories) is a design system for code quality — the same values applied everywhere. Just as a UI design system prevents visual inconsistency, a shared lint config prevents code pattern inconsistency. The discipline of deciding rules collaboratively and encoding them in configuration is valuable beyond the rules themselves: it forces explicit conversation about what "good code" means in your team's context, reducing the implicit disagreements that cause inconsistent code review feedback.

# Lore Conclusion

*"The protocols are established,"* the Safety Officer says, reviewing the config. *"No rule is arbitrary — each one prevents a documented injury. New smiths follow the protocols and, over time, propose amendments as they discover new patterns the protocols missed."*

---
