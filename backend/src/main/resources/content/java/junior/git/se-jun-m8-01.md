---
id: se-jun-m8-01
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m8
moduleTitle: "Module 8: Professional Practices"
moduleGlyph: "⚙️"
moduleSortOrder: 8
topicSlug: git
topicTitle: "Git"
topicSortOrder: 1
lesson: git
title: "Git"
sortOrder: 1
difficulty: 2
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [history, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly explains the three areas: working tree, staging area, and repository"
    - "Describes the add → commit → push workflow with purpose of each step"
    - "Writes an example of a meaningful commit message and explains why it is better than 'fix stuff'"
    - "Explains the purpose of .gitignore with at least two examples of files that should be ignored"
    - "Uses git log and git diff correctly in context"
  keywords: [commit, stage, push, pull, repository, working, tree, message, log, diff, ignore, history, version]
  modelAnswer: |
    # The three areas:
    # Working tree — files you are editing right now
    # Staging area — files selected for the next commit (git add)
    # Repository — permanent history of commits (.git directory)
    
    # Workflow:
    git add src/main/java/QuestService.java    # stage specific file
    git commit -m "feat: add quest completion XP calculation"  # create snapshot
    git push origin feature/quest-completion   # share with remote
    
    # Good commit message format:
    # <type>: <what changed and why>
    # feat: add quest completion XP calculation
    # fix: correct off-by-one in score normalisation
    # refactor: extract grade threshold to named constant
    
    # .gitignore examples:
    # *.class         — compiled bytecode
    # target/         — Maven build output
    # .env            — secrets and environment variables
    # *.iml           — IntelliJ module files
guidedSteps:
  - id: git-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      After modifying a file, what must you do before creating a commit that includes those changes?
    inputConfig:
      options:
        - "Run git commit directly — it includes all modified files automatically"
        - "Run git push — commits happen automatically on push"
        - "Run git add to stage the file, then git commit to create the snapshot"
        - "Run git save — this is an alias for commit"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Run git add to stage the file, then git commit to create the snapshot"]
      rejectedFeedback: "Git has a two-step commit process: first 'git add <file>' moves changes from the working tree to the staging area (index). Then 'git commit' takes a snapshot of everything in the staging area. This design lets you commit some changes while leaving others unready — useful for keeping commits focused."
    hint: "Git has a staging area between your working files and the commit history."
    reflectionPrompt: "Why is having a staging area (rather than committing all changes at once) useful when you are working on multiple things simultaneously?"
  - id: git-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A .gitignore file tells Git which files ___ track. Files matched by .gitignore patterns will not appear in 'git status' and cannot be accidentally committed.
    inputConfig:
      placeholder: "one word (what Git should do)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["not to", "never to", "to not", "to ignore", "not"]
      rejectedFeedback: "A .gitignore file tells Git which files NOT to track. Patterns in .gitignore are matched against file paths; matched files are excluded from status, add, and commit. Common entries: compiled output (*.class, target/), IDE metadata (.idea/, *.iml), and secrets (.env, credentials.json)."
    hint: "The file is called .git*ignore* — the name tells you what it does."
    reflectionPrompt: "What could go wrong if you accidentally committed a .env file containing database passwords to a public repository?"
  - id: git-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what makes a commit message meaningful. Give one example of a poor commit message and one good alternative for the same change, and explain the difference.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [why, what, context, clear, meaningful, describe, purpose, understand, vague, fix, message]
      rejectedFeedback: "A meaningful commit message explains WHAT changed and WHY. 'fix stuff' tells future you (or your team) nothing. 'fix: correct off-by-one error in quest score normalisation causing scores above 100' tells exactly what changed and why it mattered. Good messages make git log a useful project history, not a cryptic list of noise."
    hint: "Imagine reading 'git log' six months later. Would you understand what happened and why from the message alone?"
    reflectionPrompt: "How would you feel reading a project's git log full of messages like 'update', 'wip', and 'fix'? What does that communicate about the team's discipline?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does 'git pull' do?"
    options:
      - "Uploads local commits to the remote repository"
      - "Creates a new branch"
      - "Fetches remote changes and merges them into the current branch"
      - "Discards all local changes"
    correctIndex: 2
    feedback: "git pull = git fetch (download remote changes) + git merge (integrate into current branch). It keeps your local branch up to date with the remote. Use it regularly to avoid large, painful merge conflicts."
  - type: MULTIPLE_CHOICE
    question: "What does 'git diff' show you by default (without arguments)?"
    options:
      - "Differences between local and remote branches"
      - "Changes in the working tree that have NOT yet been staged"
      - "Changes between the last two commits"
      - "All untracked files"
    correctIndex: 1
    feedback: "git diff (no args) shows unstaged changes in the working tree — what has changed since the last git add. Use 'git diff --staged' to see what is staged (ready to commit). Use 'git diff HEAD' to see all changes since the last commit."
retrieval:
  recall: "Describe the journey a file change takes from editing to appearing in the remote repository (working tree → staging → local repository → remote)."
  explain: "Why does Git use a staging area? What specific workflow benefit does it provide over committing all changes at once?"
  mistakeId:
    code: |
      # Developer's commit history:
      git commit -m "stuff"
      git commit -m "more stuff"
      git commit -m "fix"
      git commit -m "actually fix"
      git commit -m "wtf"
      git commit -m "finally works"
    answer: "These commit messages are meaningless. Anyone reading git log cannot understand what changed or why. Fix: write messages that describe the intent — 'feat: add user login endpoint', 'fix: handle null token in auth filter', 'refactor: extract token parsing to TokenValidator'. Each commit should represent one focused change with a message that explains it to a future reader."
---

# Hook

You have just spent three days refactoring a complex service. On day four, a critical bug is discovered in production that requires rolling back to the previous state. Without version control, you are staring at a single file that represents three days of mixed changes. With Git, you can pinpoint exactly when the bug was introduced, revert to a specific good state, and work on the fix in isolation.

Git is the single most universal tool in professional software development. Whether you work solo or in a team of hundreds, version control is the foundation everything else is built on. But Git is more than a backup system — it is a communication tool. Your commit history tells the story of how software evolved, why decisions were made, and what problems were solved.

> Reflection: Have you ever lost code, overwrote a working version, or been unable to reproduce a previous state? What would having a full commit history have changed about that situation?

# Lore Introduction

The Academy's Scroll Archives contain every spell ever written — including every draft, revision, and correction. Before the Archive existed, Runesmiths kept their latest working copy and discarded earlier drafts. When a new version introduced a catastrophic error, the previous version was gone. Knowledge was permanently lost.

Archmage Veylan established the Archive Protocol: every time a Runesmith completed a meaningful unit of work, they deposited a sealed copy in the Archive, labelled with the date, the purpose, and the changes made. "A sealed copy costs almost nothing," Veylan wrote. "The loss of a working version costs everything."

Git is the Archive Protocol for code.

# Core Learning

## Concept Introduction

**Git** is a distributed version control system. It tracks changes to files over time, enabling you to recall specific versions, collaborate with others, and recover from mistakes.

**The three areas:**

| Area | What it is | Commands |
|---|---|---|
| **Working tree** | Files on disk — what you are editing | (your editor) |
| **Staging area (index)** | Changes selected for the next commit | `git add` |
| **Repository** | Permanent history of commits | `git commit`, `git log` |

**Core commands:**

```bash
# Stage a specific file
git add src/main/java/QuestService.java

# Stage all changes
git add .

# Create a commit with a message
git commit -m "feat: add quest completion XP calculation"

# Upload local commits to remote
git push origin main

# Download remote changes and merge
git pull origin main

# See uncommitted changes
git diff

# See staged changes (ready to commit)
git diff --staged

# View commit history
git log --oneline

# View the repository's current state
git status
```

## Why It Matters

**History.** `git log` is the story of your codebase. Good commit messages make it a readable narrative; poor ones make it useless noise.

**Recovery.** Every commit is a save point. You can always return to a working state.

**Collaboration.** Git's merge and conflict resolution mechanisms allow multiple developers to work on the same codebase simultaneously.

**Accountability.** `git blame` shows who changed each line and when — not for punishment, but for context when investigating unexpected behaviour.

## Worked Examples

**A meaningful workflow:**

```bash
# Check what has changed
git status

# Stage specific files (not git add . blindly)
git add src/main/java/academy/quest/QuestService.java
git add src/test/java/academy/quest/QuestServiceTest.java

# Review what will be committed
git diff --staged

# Commit with a meaningful message
git commit -m "feat: calculate and award XP on quest completion

XP is now calculated based on quest difficulty and time taken.
Added unit tests for all three difficulty tiers."

# Push to remote
git push origin feature/quest-xp
```

**A good .gitignore for a Java/Maven project:**

```gitignore
# Compiled output
target/
*.class

# IDE metadata
.idea/
*.iml
.vscode/

# Secrets — never commit these
.env
application-local.properties

# OS files
.DS_Store
Thumbs.db
```

## Common Mistakes

**`git add .` without reviewing.** Staging all files at once risks accidentally committing secrets, build artifacts, or unrelated changes. Stage specific files or directories.

**Poor commit messages.** "fix", "update", "wip" tell future readers nothing. Use the format `type: short description` where type is feat, fix, refactor, test, docs, or chore.

**Committing directly to main.** On team projects, always work on branches. Committing directly to main bypasses code review and risks breaking the shared baseline.

**Committing secrets.** A `.env` file committed to a public repository is a security incident. Once in history, secrets are retrievable even after deletion. Use `.gitignore` and secret management tools.

## Mental Model

Think of Git as a timeline of sealed envelopes. Each commit is an envelope containing a snapshot of your project at a moment in time, labelled with who sealed it, when, and why. You can open any envelope from the past. You can branch the timeline, work on a divergent path, and merge it back. The envelopes never disappear unless you explicitly destroy them.

## Mini Summary

- Git has three areas: working tree (editing), staging area (selected for commit), repository (permanent history).
- `git add` stages, `git commit` records, `git push` shares, `git pull` receives.
- Commit messages should explain what changed and why — they are the project's narrative.
- `.gitignore` prevents secrets, build artifacts, and IDE files from being committed.
- `git diff` shows unstaged changes; `git log` shows commit history.

# Guided Practice Quest

**Quest: The Archive Protocol**

The Academy demands that every Runesmith master the Archive Protocol before advancing to team projects. You must demonstrate understanding of the Git workflow, staging area, commit messages, and .gitignore.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Describe (in writing — no need to run commands) the complete Git workflow for the following scenario:

You are adding a new `BadgeService` class to the Academy project. You have modified `BadgeService.java`, `BadgeServiceTest.java`, `application.properties`, and your IDE added a `.idea/` directory.

Write the exact sequence of Git commands you would run (with real, meaningful messages) and explain each step. Then write a reflection (minimum 80 words) covering:
1. Why you would not use `git add .`
2. What your commit message would be and why you chose that format
3. What your `.gitignore` additions would be for this project

# Integration

**Connecting to History — The Problem of Historical Record**

Before modern archiving practices, history was fragile. Oral traditions degraded; physical records burned, flooded, or decayed. The historian's central challenge: how do you preserve an accurate, navigable record of what happened and why? Medieval chroniclers grappled with the same problem software teams face with poor commit histories — they recorded *what* happened without recording *why*, leaving future scholars unable to understand causality.

The discipline of **historiography** — the study of how history is recorded — distinguishes between primary sources (original documents), secondary sources (interpretations), and the meta-question of whose perspective is represented. In software, your commit history is a primary source. A commit saying "refactor: extract authentication logic into AuthService to isolate security concerns from controller layer" is a rich primary source. A commit saying "refactor" is opaque.

Archaeologists have learned to read geological strata — layers of earth that record what happened over millennia. A well-maintained git log is your codebase's stratigraphy. Future maintainers can excavate the history, find the layer where a bug was introduced, and understand the conditions that led to it.

> Reflection: If your current project's git history were the only record future developers had of why the code looks the way it does, what story would it tell? What would be missing?

# Lore Conclusion

The Archive Protocol had been in place for five years when Apprentice Lena opened the wrong scroll and accidentally destroyed three months of experimental enchantment work. She stood frozen, staring at the blank parchment.

Senior Archivist Doran touched her shoulder gently. "What was your last Archive entry?" Lena thought. "Yesterday afternoon — the stability test worked and I deposited a sealed copy." Doran smiled. "Then you have lost half a day, not three months." He walked to the shelf and retrieved the sealed copy, intact and precisely labelled. Lena took it with shaking hands, then looked at the Archive with a new and permanent respect.

---
