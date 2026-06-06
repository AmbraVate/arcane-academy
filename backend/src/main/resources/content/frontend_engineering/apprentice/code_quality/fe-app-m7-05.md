---
id: fe-app-m7-05
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m7
moduleTitle: "Module 7: Frontend Engineering Habits"
moduleGlyph: "🛠️"
moduleSortOrder: 7
topicSlug: code_quality
topicTitle: "Code Quality"
topicSortOrder: 2
lesson: organising_files
title: "Organising Files"
sortOrder: 2
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the purpose of separating HTML, CSS, and JavaScript into separate files"
    - "Organises a project using a logical folder structure"
    - "Explains the difference between flat and nested file structures"
    - "Names files descriptively and consistently"
    - "Explains when co-location (keeping related files together) is better than separation"
  keywords: [structure, folder, separation, flat, nested, co-location, CSS, JavaScript, HTML, organisation]
  modelAnswer: |
    Project file organisation separates concerns and makes code findable. Separate folders
    for HTML, CSS, JavaScript, images, and fonts is a starting convention. As projects
    grow, co-location (keeping component HTML, CSS, and JS together) often scales better
    than global separation. Flat structures are simpler for small projects; nested
    structures are necessary for large ones. Consistent file naming is as important
    as structure.
guidedSteps:
  - id: fe-app-m7-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A project has grown to 30 components. Which structure scales better?
    inputConfig:
      options:
        - "All CSS in one styles.css, all JS in one app.js, all HTML in one folder"
        - "Each component in its own folder with its HTML, CSS, and JS co-located"
        - "All files in the root directory with descriptive names"
        - "Separate folders for all 30 HTML files, all 30 CSS files, all 30 JS files"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Each component in its own folder with its HTML, CSS, and JS co-located"]
      rejectedFeedback: "At 30 components, co-location scales best: UserCard/UserCard.jsx, UserCard.css, UserCard.test.js in one folder. Related files are together — changing UserCard means looking in one place. Global separation (all CSS together, all JS together) forces context-switching between folders for every change."
    hint: "Think about what you need to change when updating a single component."
    reflectionPrompt: "The organisation question is: 'What do I need to find when changing X?' If changing a component requires visiting CSS, JS, and HTML in three different folders, the structure creates friction. If all files for a component are in one folder, changes require one place. Structure should serve the workflow."

  - id: fe-app-m7-05-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A good rule for file names: names should be ___ and match what they contain, using consistent casing.
    inputConfig:
      placeholder: "descriptive"
    markingRule:
      matchMode: CONTAINS
      accepted: [descriptive, clear, meaningful, readable, consistent]
      rejectedFeedback: "Descriptive file names (user-profile.css, login-form.js) make files findable without reading their contents. Generic names (page2.html, styles2.css) require reading the file to know its purpose. The file system should tell you what each file contains."
    hint: "The same principle applies to file names as variable names."
    reflectionPrompt: "index.html is a convention (the default page browsers load), not a descriptive name. But index.css is often a code smell — what does it contain? Use main.css, global.css, or reset.css to communicate the purpose. The only truly generic file is index.js (the entry point for a module)."

  - id: fe-app-m7-05-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe the file structure you would use for a small portfolio website with 3 pages (Home, About, Projects) and shared styles and JavaScript.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [index, about, projects, css, js, folder, images, shared, global]
      rejectedFeedback: "Suggested structure: index.html, about.html, projects.html in root. css/global.css (shared styles), css/home.css (page-specific). js/main.js. images/ folder. This separates files by type (appropriate for a small site) and uses descriptive names for each."
    hint: "Think about: where do shared vs page-specific files go? How do you name them?"
    reflectionPrompt: "This structure scales to about 5-10 pages before co-location (component folders) becomes necessary. The rule: use the simplest structure that is not painful to work with. Premature organisation is over-engineering; disorganisation is debt."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which file name is most appropriate for a CSS file that applies only to the checkout page?"
    options:
      - "style3.css"
      - "page-styles.css"
      - "checkout.css"
      - "main.css"
    correctIndex: 2
    feedback: "checkout.css is self-documenting — its name tells you exactly which page it styles. style3.css is a counter (meaningless without context). page-styles.css is vague (all pages have styles). main.css typically refers to global/base styles, not page-specific ones."
  - type: MULTIPLE_CHOICE
    question: "What does 'separation of concerns' mean in the context of file organisation?"
    options:
      - "Always keeping HTML, CSS, and JavaScript in completely separate files"
      - "Organising code so each file has a single, clear responsibility"
      - "Never allowing CSS and JavaScript in the same project"
      - "Using one file per function"
    correctIndex: 1
    feedback: "Separation of concerns means each file has a clear, single responsibility — not necessarily that technologies are always separated. A component file (UserCard.jsx in React) contains HTML-like structure, styles, and JS in one file — but each has a single responsibility (render this component). The concern is the component, not the technology."

retrieval:
  recall: "Describe a good file structure for a small 3-page website. Include folder names and file names."
  explain: "Explain the trade-off between global separation (all CSS together) and co-location (component files together) for large projects."
  mistakeId:
    code: "All files in root: page1.html, page2.html, style.css, style2.css, newstyle.css, script.js, script2.js"
    answer: "Flat root with numbered/generic names creates a project no one can navigate. Organise: css/global.css + css/about.css. js/main.js. images/. Give pages descriptive names: index.html (home), about.html, projects.html. Structure makes projects findable and maintainable."
---

# Hook

A well-organised project feels like a well-organised kitchen. Everything is where you expect it. You can find the garlic without opening every drawer.

A disorganised project is the opposite. You know the garlic is somewhere. You spend five minutes looking. By the time you find it, you've forgotten why you needed it.

File organisation is the kitchen of frontend development.

# Lore Introduction

*"The Academy's archive,"* says Master Aelindra, walking between the shelves, *"organises scrolls by subject, then by date, then by author. A newcomer to the archive can find any scroll in under a minute. An archive organised by 'whatever fit on the shelf' takes an expert years to navigate. Build archives that newcomers can read."*

# Core Learning

## Concept Introduction

**Starting structure for a small website:**
```
project/
├── index.html          ← home page (root index is a convention)
├── about.html
├── projects.html
├── css/
│   ├── global.css      ← resets, variables, typography
│   ├── components.css  ← buttons, cards, nav
│   └── about.css       ← about page specific
├── js/
│   └── main.js
└── images/
    ├── hero.webp
    └── logo.svg
```

**Component co-location (for larger projects):**
```
src/
├── components/
│   ├── UserCard/
│   │   ├── UserCard.jsx
│   │   ├── UserCard.css
│   │   └── UserCard.test.js
│   └── Navigation/
│       ├── Navigation.jsx
│       └── Navigation.css
├── pages/
│   ├── Home.jsx
│   └── About.jsx
└── styles/
    └── global.css
```

**File naming rules:**
- Descriptive: `checkout.css` not `style3.css`
- Consistent casing: all kebab-case or all PascalCase — not mixed
- Match content: `user-profile.js` contains user profile logic
- No spaces: spaces in filenames cause issues across OSes and URLs

## Common Mistakes

- All files in root directory — not scalable
- Numbered files (`page2.html`, `script3.js`) — meaningless without context
- Inconsistent naming: `user-card.css` and `NavBar.css` in the same project
- Deeply nested (5+ levels) — overcomplicated for small projects

## Mini Summary

- ✔ Organise by purpose: `css/`, `js/`, `images/` for small projects
- ✔ For large projects: co-locate component files (all in one folder)
- ✔ Descriptive names: `checkout.css` not `style3.css`
- ✔ Consistent casing across all file names
- ✔ Simplest structure that doesn't cause pain — don't over-engineer

# Guided Practice Quest

**The Archive Plan** — three questions on file organisation. Steps in `guidedSteps`.

# Solo Practice Quest

Plan the file structure for a personal portfolio with: Home, About, Projects, Contact pages, shared navigation and footer components, global styles, page-specific styles, and an images folder. Draw the tree structure with all file and folder names. Explain why you organised it the way you did.

# Integration

**Connecting to Mathematics — Information Theory and Retrieval Time**

Shannon's information theory quantifies how quickly information can be retrieved from a system based on its organisation. A well-organised file tree with descriptive names acts as an index — the file path itself conveys information, reducing search time. An unorganised flat structure with generic names contains no index — every lookup requires a linear search. The mathematical equivalent: retrieval from an indexed B-tree (O log n) vs linear scan of an array (O n). Good file organisation is an index structure that reduces cognitive retrieval to O(1) — you know where to look without searching.

# Lore Conclusion

*"An archive organised for the archivist alone is not an archive — it is a personal stash. An archive organised for any reader who might need it — that is a resource. Build resources, not stashes. Your future self, your teammates, and your successor will thank you."*

---
