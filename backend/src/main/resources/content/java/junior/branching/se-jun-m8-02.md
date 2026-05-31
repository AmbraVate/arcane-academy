---
id: se-jun-m8-02
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m8
moduleTitle: "Module 8: Professional Practices"
moduleGlyph: "⚙️"
moduleSortOrder: 8
topicSlug: branching
topicTitle: "Branching"
topicSortOrder: 2
lesson: branching_strategies
title: "Branching Strategies"
sortOrder: 2
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [git]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the purpose of feature branches and why they protect main"
    - "Contrasts GitFlow (long-lived branches) with trunk-based development (short-lived, frequent integration)"
    - "Describes the difference between merge and rebase and when to use each"
    - "Explains what a pull request is and its purpose beyond code merging"
    - "Gives examples of good and poor branch naming conventions"
  keywords: [branch, feature, trunk, merge, rebase, pull-request, review, short-lived, naming, integration, conflict, main]
  modelAnswer: |
    # Feature branch workflow:
    git checkout -b feature/quest-xp-calculation
    # ... make commits ...
    git push origin feature/quest-xp-calculation
    # Open pull request for review, then merge to main
    
    # Good branch names:
    # feature/add-badge-service
    # fix/correct-score-normalisation
    # chore/update-spring-dependency
    
    # Poor branch names:
    # myBranch
    # test123
    # fix
    
    # Merge vs rebase:
    # Merge: preserves full history, creates a merge commit
    git checkout main && git merge feature/quest-xp
    
    # Rebase: rewrites history to appear linear, no merge commit
    git checkout feature/quest-xp && git rebase main
    # Rule: never rebase branches others have pulled
guidedSteps:
  - id: br-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the primary purpose of creating a feature branch before starting new work?
    inputConfig:
      options:
        - "To make the code run faster by isolating changes"
        - "To isolate work-in-progress from the stable main branch, allowing independent development and code review before integration"
        - "To back up the codebase before making changes"
        - "To create a separate repository for each feature"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["To isolate work-in-progress from the stable main branch, allowing independent development and code review before integration"]
      rejectedFeedback: "Feature branches isolate in-progress work from the main branch. Main remains stable and deployable while work continues on the branch. When the feature is ready, it goes through a pull request (code review) before being merged into main. This protects the team's shared baseline."
    hint: "Think about what happens to 'main' while you are working on your feature for two days."
    reflectionPrompt: "What would happen if five developers all committed directly to main simultaneously without branches?"
  - id: br-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the key difference between GitFlow and trunk-based development?
    inputConfig:
      options:
        - "GitFlow uses git; trunk-based development uses Subversion"
        - "GitFlow uses long-lived branches (develop, release, hotfix); trunk-based development keeps branches very short-lived and integrates to main frequently (daily or multiple times per day)"
        - "GitFlow is for open source; trunk-based is for enterprise"
        - "They are identical — just different names used by different companies"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["GitFlow uses long-lived branches (develop, release, hotfix); trunk-based development keeps branches very short-lived and integrates to main frequently (daily or multiple times per day)"]
      rejectedFeedback: "GitFlow defines multiple long-lived branches (main, develop, release/*, hotfix/*) with specific rules for each. It works well for products with scheduled releases. Trunk-based development (used by Google, Facebook, Amazon) keeps all branches very short-lived — hours to days, not weeks — and integrates to main constantly. This enables continuous deployment and reduces merge conflicts."
    hint: "The name 'trunk-based' refers to committing to the trunk (main branch) frequently."
    reflectionPrompt: "What are the risks of keeping a branch open for three weeks before merging? How do those risks change if you merge daily?"
  - id: br-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between 'git merge' and 'git rebase'. When would you choose one over the other?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [merge, rebase, history, linear, commit, conflict, rewrite, shared, public, clean]
      rejectedFeedback: "Merge integrates branches by creating a merge commit — the full history of both branches is preserved. Rebase rewrites your branch's commits to appear as if they were written on top of the latest main — history appears linear. Use merge for integrating long-lived branches (preserves history). Use rebase to clean up local commits before a PR. Golden rule: never rebase commits that others have already pulled."
    hint: "Think about what each command does to the commit graph — does it preserve or rewrite history?"
    reflectionPrompt: "Why is 'never rebase public branches' such an important rule? What happens if you break it?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is a pull request (PR)?"
    options:
      - "A command that pulls the latest changes from remote"
      - "A formal request to merge a branch into another, enabling code review before integration"
      - "A request for a colleague to pull your branch locally"
      - "An automated test that runs when code is pushed"
    correctIndex: 1
    feedback: "A pull request (or merge request in GitLab) is a proposal to merge one branch into another, with a built-in code review interface. Team members can comment on specific lines, request changes, and approve. PRs are both a quality gate and a knowledge-sharing mechanism — reviewers learn about changes they did not write."
  - type: MULTIPLE_CHOICE
    question: "Which branch naming convention best follows professional practice?"
    options:
      - "myNewFeature"
      - "branch2"
      - "feature/add-quest-xp-calculation"
      - "TASK-123"
    correctIndex: 2
    feedback: "Good branch names are lowercase, use hyphens (not spaces or underscores), include a type prefix (feature/, fix/, chore/), and describe the change briefly. 'feature/add-quest-xp-calculation' tells anyone reading git branch what the work is about without opening a single file."
retrieval:
  recall: "Describe the complete lifecycle of a feature branch: from creation through development, pull request, review, and merge."
  explain: "Why do high-performing engineering teams prefer short-lived branches? What specific problems do long-lived branches cause?"
  mistakeId:
    code: |
      # Developer workflow observed:
      git checkout main
      git pull
      # ... edits QuestService.java, BadgeService.java, UserProfile.java ...
      # ... two weeks of work ...
      git add .
      git commit -m "big feature"
      git push origin main
    answer: "Multiple problems: (1) Committed directly to main without a branch — bypassed code review and risked breaking the shared baseline. (2) Two weeks of work in one commit — if bugs are introduced, there's no way to isolate which change caused them. (3) 'git add .' may have staged unintended files. (4) 'big feature' is a meaningless commit message. Fix: branch early (git checkout -b feature/...), commit small focused units of work daily with meaningful messages, open a pull request for review before merging."
---

# Hook

Your team is building three features simultaneously: quest XP calculation, badge awarding, and a leaderboard API. Without branching, all three developers commit to main — their work-in-progress code constantly conflicts, partially-working features leak into production, and nobody can review changes before they affect everyone.

Branching is Git's answer to parallel development. Each developer works on their own branch, isolated from others. When work is ready, it goes through a pull request — a structured review process — before being merged back. The main branch stays stable and deployable at all times.

> Reflection: Think about a time when two people worked on the same document simultaneously and caused conflicts. What would a "branching" equivalent look like in that context?

# Lore Introduction

The Academy's Runic Research Division runs three experimental projects at once. Before branches were formalised, all researchers worked from the same master spell tome. Experimental inscriptions mixed with stable ones. A failed experiment could corrupt the entire tome. A working version was impossible to identify.

Archmage Veylan introduced the Branch Protocol: each research team worked on a separate copy of the tome, clearly labelled with the project name and date. When research was complete and validated by a peer review, the Keeper of the Index would carefully merge the approved additions into the master tome. Failures stayed in the branch copies. The master tome remained clean.

# Core Learning

## Concept Introduction

A **branch** in Git is a lightweight, moveable pointer to a commit. Creating a branch is cheap (it is just a pointer) and switching between branches is fast. The main branch (traditionally `main` or `master`) should always represent stable, deployable code.

**Feature branch workflow:**

```bash
# Start from an up-to-date main
git checkout main
git pull origin main

# Create and switch to a new branch
git checkout -b feature/add-badge-service

# ... develop, test, commit ...
git add src/main/java/academy/badge/BadgeService.java
git commit -m "feat: implement badge award on quest completion"

# Push branch to remote
git push origin feature/add-badge-service

# Open a pull request (on GitHub/GitLab)
# After review and approval — merge to main
```

**Branch naming conventions:**
| Type | Format | Example |
|---|---|---|
| Feature | `feature/<description>` | `feature/quest-xp-calculation` |
| Bug fix | `fix/<description>` | `fix/correct-score-rounding` |
| Chore | `chore/<description>` | `chore/upgrade-spring-to-3-2` |
| Hotfix | `hotfix/<description>` | `hotfix/null-pointer-in-auth` |

## Why It Matters

**GitFlow** defines multiple long-lived branches: `main` (production), `develop` (integration), `release/*` (release prep), `hotfix/*` (urgent fixes), `feature/*` (new work). Suitable for products with scheduled, versioned releases.

**Trunk-Based Development (TBD)** keeps branches very short-lived (hours to days) and integrates to `main` constantly. CI/CD runs on every push. Feature flags hide incomplete work. Used by Google, Amazon, Netflix. Enables continuous deployment.

**Merge vs Rebase:**

```bash
# Merge: creates a merge commit, preserves full history
git checkout main
git merge feature/badge-service
# Result: a merge commit connects the two branches' histories

# Rebase: rewrites branch commits to sit on top of current main (linear history)
git checkout feature/badge-service
git rebase main
# Result: feature commits appear as if they were written after the latest main commit

# Golden rule: NEVER rebase a branch that others have pulled
```

## Worked Examples

**Complete feature branch lifecycle:**

```bash
# 1. Start
git checkout main && git pull origin main
git checkout -b feature/leaderboard-api

# 2. Develop — commit small, focused units
git add LeaderboardController.java LeaderboardService.java
git commit -m "feat: add GET /leaderboard endpoint returning top 10 players"

git add LeaderboardControllerTest.java LeaderboardServiceTest.java
git commit -m "test: add leaderboard endpoint integration tests"

# 3. Keep branch up to date with main
git fetch origin
git rebase origin/main

# 4. Push and open PR
git push origin feature/leaderboard-api
# Open pull request → reviewer approves → squash merge to main

# 5. Clean up
git checkout main && git pull
git branch -d feature/leaderboard-api
```

## Common Mistakes

**Long-lived branches.** Branches open for weeks accumulate divergence from main, producing painful merge conflicts when finally integrated. Branches should live for hours or days, not weeks.

**Committing to main directly.** Bypasses code review and risks breaking the shared baseline. All work should go through a branch + pull request.

**Meaningless branch names.** `branch-2`, `myfix`, `test` tell teammates nothing. Names should communicate the purpose instantly.

**Rebasing shared branches.** If you rebase a branch that a colleague has already pulled, their local history will diverge and cause confusing conflicts. Only rebase local branches that no one else is using.

## Mental Model

Think of branching like a research library with photocopying. The original volume stays safe on the shelf. You take a photocopy, mark it up freely, experiment without risk. When your annotations are valuable and reviewed, a librarian carefully incorporates the approved additions into the master volume. Failures stay in your photocopy. The original is never at risk.

## Mini Summary

- Feature branches isolate work-in-progress from the stable main branch.
- Pull requests are the review gate before integration — not just a merge mechanism.
- GitFlow uses multiple long-lived branches; trunk-based development integrates to main frequently.
- Merge preserves history with a merge commit; rebase creates linear history by rewriting commits.
- Never rebase branches that other developers have pulled.

# Guided Practice Quest

**Quest: The Branch Protocol**

The Academy's Research Division must implement proper branching protocol. You must demonstrate understanding of feature branches, GitFlow vs trunk-based strategies, merge vs rebase, and pull request purpose.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Describe (in writing) the full branching workflow for this scenario:

The Academy has three developers: Alex working on `BadgeService`, Blair working on a `LeaderboardAPI`, and Casey fixing a critical null pointer bug on main. They are using GitFlow.

Write out the branch names each developer should use, the order of operations, and how they would handle the fact that Casey's hotfix needs to go to production immediately while Alex and Blair's features are incomplete. Then write a reflection (minimum 100 words) covering:
1. Why Casey cannot wait for Alex and Blair to finish before deploying the fix
2. How trunk-based development would change this scenario
3. What a pull request adds beyond the simple `git merge` command

# Integration

**Connecting to Psychology — Cognitive Load and Context Switching**

Psychologists studying multitasking consistently find that the brain does not actually run tasks simultaneously — it switches rapidly between them, with a "switching cost" each time context changes. Every switch requires reloading the mental model of the previous task: where you were, what decisions you had made, what the current state was.

Long-lived feature branches are a software equivalent of high cognitive-switch-cost multitasking. The longer a branch lives, the more it diverges from main, the more the developer must hold in memory, and the more mental effort is required to reconcile changes when finally merging. The accumulating divergence is a form of technical debt that compounds daily.

Short-lived branches, integrated frequently, minimise this cognitive cost. Each integration is small enough that the developer can hold the entire diff in working memory. Conflicts are small and understandable. The mental model of the codebase remains fresh and accurate. Research on "flow state" — the optimal state of focused concentration — suggests that frequent, small integrations keep developers in flow more consistently than infrequent, large, painful merges.

> Reflection: Think about the last time you dealt with a large, complex merge conflict. How much cognitive effort did it take? How much of that effort was caused by the branches having lived too long without integration?

# Lore Conclusion

The Branch Protocol had changed everything for the Research Division. Where once a failed experiment could corrupt the master tome, now failures were confined to their branch copies. The master tome — main — remained clean, stable, and ready for use at any moment.

"The real power," Senior Runesmith Tobias explained to a new arrival, "is not protection from failure. It is the review process before integration. My annotations cannot enter the master tome without a peer review. That peer — someone who understands the work — often sees something I missed. The branch is not just protection. It is the mechanism for collective ownership of the tome."

---
