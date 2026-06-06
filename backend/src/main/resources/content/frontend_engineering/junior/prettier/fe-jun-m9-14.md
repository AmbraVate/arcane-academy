---
id: fe-jun-m9-14
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: prettier
topicTitle: "Prettier"
topicSortOrder: 5
lesson: prettier_configuration
title: "Prettier Configuration"
sortOrder: 2
difficulty: 4
estimatedMinutes: 20
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-13]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names the Prettier config file and key options"
    - "Explains why eslint-config-prettier is needed"
    - "Explains what printWidth does (and its limitation)"
    - "Describes how to ignore specific files from Prettier"
  keywords: [.prettierrc, printWidth, tabWidth, singleQuote, trailingComma, eslint-config-prettier, .prettierignore]
  modelAnswer: |
    Prettier is configured in .prettierrc (JSON, YAML, or JS). Key options: printWidth (target line length — not a hard limit), tabWidth (spaces per indent level, typically 2), singleQuote (true/false), semi (semicolons yes/no), trailingComma ('all', 'es5', 'none'). eslint-config-prettier disables ESLint rules that conflict with Prettier — you add it last in the ESLint extends array so it overrides all formatting rules. .prettierignore lists files/directories to skip (like dist/, auto-generated code). The config should be committed so all developers and CI use identical formatting.
guidedSteps:
  - id: fe-jun-m9-14-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You have ESLint configured with `quotes: ['error', 'double']` and Prettier configured with `singleQuote: true`. What happens when you run both?"
    inputConfig:
      options:
        - "ESLint wins — it overrides Prettier's formatting"
        - "They conflict — Prettier formats to single quotes, ESLint errors on single quotes"
        - "Prettier wins automatically"
        - "The two tools never check the same thing"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["They conflict — Prettier formats to single quotes, ESLint errors on single quotes"]
      rejectedFeedback: "Without eslint-config-prettier, ESLint and Prettier conflict. Prettier formats to single quotes; ESLint reports an error on single quotes. The fix: install eslint-config-prettier and add 'prettier' to the end of ESLint's extends array. This disables all ESLint rules that Prettier handles, letting Prettier own formatting and ESLint own code quality."
    hint: "What happens when two tools enforce conflicting rules on the same code?"
    reflectionPrompt: "The solution to tool conflicts is separation of concerns: Prettier owns formatting, ESLint owns code quality. eslint-config-prettier enforces this separation by disabling ESLint's formatting rules."
  - id: fe-jun-m9-14-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Why is `printWidth: 80` not a hard line length limit? What does Prettier actually do when a line would exceed it?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [break, wrap, guide, target, exceed, fit, cannot, string, long]
      rejectedFeedback: "printWidth is a guide, not a hard limit. Prettier tries to fit content within printWidth, but some constructs can't be broken — a long string literal, a deeply nested JSX attribute, or a function with many parameters. Prettier breaks where it can (function arguments to separate lines, object properties to separate lines) but won't break mid-string or force artificial breaks that would change semantics."
    hint: "Can Prettier always keep every line under 80 characters? What if a string itself is longer?"
    reflectionPrompt: "printWidth as a guide rather than a limit is the pragmatic middle ground. It avoids the rigidity of hard limits while still pushing toward readable line lengths."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why should `eslint-config-prettier` be listed last in ESLint's extends array?"
    options:
      - "It must load after all plugins for performance"
      - "It needs to override any formatting rules added by preceding configs"
      - "Prettier requires it to be last to function"
      - "Earlier position would disable too many rules"
    correctIndex: 1
    feedback: "ESLint extends are applied in order — later entries override earlier ones. eslint-config-prettier must come last to disable all formatting rules added by any preceding config (typescript-eslint/recommended, plugin:react/recommended, etc.). If it came first, a later config could re-enable conflicting rules."
retrieval:
  recall: "Name five Prettier configuration options and what each controls."
  explain: "What is eslint-config-prettier and why is it needed when using both ESLint and Prettier?"
  mistakeId:
    code: |
      // .prettierrc
      {
        "printWidth": 200,
        "tabWidth": 4,
        "singleQuote": false,
        "trailingComma": "none",
        "bracketSpacing": false
      }
    answer: "These settings work against readability: 200-character lines require horizontal scrolling and are harder to read; 4-space indentation is fine but less common in JS/TS projects; 'none' trailing commas means every multi-line object/array addition creates a two-line diff (the new item + the comma on the previous line). Better defaults: printWidth 80-100, tabWidth 2, singleQuote true, trailingComma 'all'. Accept Prettier's defaults where possible — they were chosen by people who thought carefully about readability."
---

# Hook

You install Prettier. You install ESLint. You open a file, save it. Prettier formats to single quotes. ESLint immediately errors on single quotes. You save again. Cycle repeats indefinitely.

Two tools, conflicting rules, fighting over the same code. The fix takes two lines of configuration — but you need to know which two.

# Lore Introduction

*"Two enchanters casting conflicting spells on the same object produce neither result — only chaos,"* Master Verdane explains. *"The solution is not to pick one enchanter and dismiss the other. It is to divide the domains: one enchanter handles form, the other handles function. They do not conflict because they do not overlap."*

She gestures at the eslint.config.js. *"ESLint handles function. Prettier handles form. One line in the config separates their domains."*

# Core Learning

## Concept Introduction

**Prettier config (.prettierrc):**
```json
{
  "printWidth": 100,
  "tabWidth": 2,
  "singleQuote": true,
  "semi": true,
  "trailingComma": "all",
  "bracketSpacing": true,
  "arrowParens": "always"
}
```

| Option | Values | What it controls |
|---|---|---|
| `printWidth` | number | Target line length (guide, not hard limit) |
| `tabWidth` | number | Spaces per indent level |
| `singleQuote` | boolean | `'single'` vs `"double"` quotes |
| `semi` | boolean | Trailing semicolons |
| `trailingComma` | `"all"` / `"es5"` / `"none"` | Trailing commas in multi-line structures |
| `bracketSpacing` | boolean | `{ foo: bar }` vs `{foo: bar}` |

**Avoiding conflicts with ESLint:**
```bash
npm install --save-dev eslint-config-prettier
```
```js
// eslint.config.js — 'prettier' must be last
import eslintConfigPrettier from 'eslint-config-prettier';

export default tseslint.config(
  js.configs.recommended,
  ...tseslint.configs.recommended,
  reactHooks.configs.recommended,
  eslintConfigPrettier, // ← Disables all ESLint formatting rules
);
```

**.prettierignore** (like .gitignore for Prettier):
```
dist/
node_modules/
*.generated.ts
coverage/
```

## Common Mistakes

- **Not committing .prettierrc.** Without it, different developers use different Prettier defaults. Commit it.
- **Setting printWidth too high.** 200+ character lines are hard to read. 80–120 is the common range.
- **Not adding eslint-config-prettier.** The two tools conflict without it.
- **Formatting generated code.** Auto-generated files (API types, GraphQL schemas) should be in .prettierignore.

## Mini Summary

- Prettier is configured in `.prettierrc` — commit it so everyone uses identical settings
- Key options: printWidth, tabWidth, singleQuote, semi, trailingComma
- Use `eslint-config-prettier` to disable conflicting ESLint formatting rules
- `.prettierignore` skips generated or third-party files

# Guided Practice Quest

Work through the two guided steps to understand the ESLint/Prettier conflict and printWidth behaviour.

# Solo Practice Quest

Write a `.prettierrc` for a React TypeScript project. Choose your settings, then justify each choice: why that printWidth? Why single vs double quotes? Why trailing commas or not? (Note: there are no objectively wrong answers here — what matters is that choices are deliberate and documented.)

# Integration

**Design — Constraints as Creative Enablers**

Designers and typographers have long known that constraints improve creative work. A fixed column width (printWidth) forces the formatter to make choices about how to break up complex expressions — often producing more readable code than the developer's original free-form layout. The constraint of fitting within 100 characters surfaces complexity: when a function call is so deeply nested that it can't fit on one line, Prettier's reformatting makes that complexity visible. This mirrors a principle in graphic design: constraints that appear restrictive often produce cleaner, more deliberate compositions. Prettier's options are fewer than ESLint's by design — the team consciously chose opinionated defaults over infinite flexibility, based on the insight that too much choice produces inconsistency, not quality.

# Lore Conclusion

*"The domains are separated,"* Master Verdane says. *"Prettier claims form. ESLint claims function. The eslint-config-prettier is the treaty between them — each enchanter cedes the other's territory. No more chaos. Only complementary effects."*

---
