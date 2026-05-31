#!/usr/bin/env python3
"""
Arcane Academy — Batch Lesson Generator
Reads all se-*.json stub files, calls Anthropic API for each lesson,
writes the output to content/java/{tier}/{topicSlug}/{lessonId}.md

Usage:
    pip install anthropic
    # Set ANTHROPIC_API_KEY in your environment (or .env file)
    python generate_all_lessons.py [--tier APPRENTICE] [--dry-run] [--force]

Flags:
    --tier APPRENTICE|JUNIOR|SENIOR|LEAD   Only generate this tier
    --id   se-app-m1-01                    Only generate this specific lesson
    --dry-run                              Print prompt but don't call API
    --force                                Overwrite existing .md files
"""

import json
import os
import sys
import time
import glob
import re
import argparse
from pathlib import Path

try:
    import anthropic
except ImportError:
    print("ERROR: anthropic package not installed. Run: pip install anthropic")
    sys.exit(1)

# Fix Windows console encoding for emoji/Unicode output
if sys.platform == "win32":
    import io
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding="utf-8", errors="replace")
    sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding="utf-8", errors="replace")

# ── Config ────────────────────────────────────────────────────────────────────

# Script lives at: content/java/generate_all_lessons.py
# JAVA_DIR = content/java/  (the script's own directory)
# CONTENT_DIR = content/    (one up)
JAVA_DIR = Path(__file__).resolve().parent
CONTENT_ROOT = JAVA_DIR.parent   # content/ root — not used directly below, kept for reference
MODEL = "claude-opus-4-5"
MAX_TOKENS = 8192
RATE_LIMIT_SLEEP = 2  # seconds between API calls

TIER_DIFF = {
    "APPRENTICE": 1,
    "JUNIOR": 2,
    "SENIOR": 3,
    "LEAD": 4,
}

TIER_EST_MINS = {
    "APPRENTICE": 25,
    "JUNIOR": 30,
    "SENIOR": 35,
    "LEAD": 40,
}

TIER_XP_BASE = {
    "APPRENTICE": 30,
    "JUNIOR": 60,
    "SENIOR": 100,
    "LEAD": 150,
}

# Integration domains by tier
TIER_INTEGRATION = {
    "APPRENTICE": ["mathematics", "psychology"],
    "JUNIOR":     ["mathematics", "psychology"],
    "SENIOR":     ["mathematics", "psychology", "philosophy"],
    "LEAD":       ["mathematics", "psychology", "philosophy", "economics"],
}

# ── System Prompt ─────────────────────────────────────────────────────────────

SYSTEM_PROMPT = """You are a senior curriculum author for Arcane Academy — a gamified software engineering
learning platform set in a fantasy world of arcane magic. Your task is to write a single,
complete Markdown lesson file conforming EXACTLY to the Arcane Academy Lesson Blueprint.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
LESSON BLUEPRINT — 9-SECTION STRUCTURE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Every lesson file = YAML frontmatter + 9 body sections.

FRONTMATTER (YAML between --- delimiters) MUST include ALL of these fields:
  id, school, domainId, tier, moduleId, moduleTitle, moduleGlyph, moduleSortOrder,
  topicSlug, topicTitle, topicSortOrder, lesson, title, sortOrder, difficulty,
  estimatedMinutes, xpReward, practiceType, questType, retrievalWeight,
  questTypes (list), prerequisites (list), integrationDomains (list),
  soloAssessment (object: type, rubricItems, keywords, modelAnswer),
  guidedSteps (list of 2-4 steps),
  microCheckpoint (list of EXACTLY 2 questions),
  retrieval (object: recall, explain, mistakeId)

GUIDED STEP SCHEMA (each step must have all fields):
  - id: string
  - sortOrder: int
  - inputType: FILL_BLANK | MULTIPLE_CHOICE | SHORT_TEXT | CODE
  - instruction: string (markdown prose + code block if needed)
  - inputConfig: { placeholder: "..." } OR { options: ["A","B","C","D"] } OR { minWords: N }
  - markingRule: { matchMode: EXACT|NORMALIZED|REGEX|CONTAINS|OUTPUT_MATCH, accepted: [...], rejectedFeedback: "..." }
  - hint: string
  - reflectionPrompt: string

SOLO ASSESSMENT SCHEMA:
  APPRENTICE/JUNIOR → type: RUBRIC_REFLECTION, rubricItems: [5 items], keywords: [3-6 keywords], modelAnswer: |
  SENIOR           → type: PATTERN_MATCH, rubricItems: [5 items], keywords: [6-10 keywords], modelAnswer: |
  LEAD             → type: AI_REVIEW, rubricItems: [5 items], keywords: [8-12 keywords], modelAnswer: |

MICRO CHECKPOINT SCHEMA (exactly 2):
  - type: MULTIPLE_CHOICE
    question: "..."
    options: ["A", "B", "C", "D"]
    correctIndex: N (0-3)
    feedback: "..."

RETRIEVAL SCHEMA:
  recall: "..."
  explain: "..."
  mistakeId:
    code: "..."
    answer: "..."

BODY SECTIONS (in this exact order, each as H1):

# Hook
  Goal: Create curiosity. 60-100 words. Scenario or problem. Ends with OPTIONAL ungraded reflection question.
  The reflection question must appear as a blockquote: > Why might...?

# Lore Introduction
  Goal: Fantasy flavour. 3-5 sentences. The Academy, Archmage Veylan, the guild context.
  Map the concept to a magical discipline (variables = rune vessels, methods = incantations, etc.)

# Core Learning
  Contains ALL of these H2 sub-sections in order:
  ## Concept Introduction     — definition, syntax table or annotated code block
  ## Why It Matters           — practical relevance, 50-80 words
  ## Worked Examples          — 2-3 annotated code examples (or non-code worked examples for NONE practiceType)
  ## Common Mistakes          — 3-5 bullet points with brief explanations
  ## Mental Model             — one strong analogy, 60-100 words
  ## Mini Summary             — 4-6 ✔ bullet checkmarks

# Guided Practice Quest
  Quest name + 1-paragraph narrative prompt. The actual steps are defined in frontmatter guidedSteps[].
  Body just introduces the quest scenario.

# Solo Practice Quest
  Independent task description. 1-2 paragraphs. What to build/write/explain.
  Assessment rules are in frontmatter soloAssessment.

# Integration
  100-200 words. Pick ONE domain from integrationDomains. Draw a genuine cross-domain connection.
  End with a free-reflection question (no answer required).

# Lore Conclusion
  3-5 sentences. Narrative wrap-up. Forward-tease next concept without spoiling.
  Archmage Veylan or the guild voice.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
LORE VOCABULARY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- Variables / state          → rune vessels / memory runes
- Methods / functions        → incantations / spells
- Classes                    → arcane blueprints / spell templates
- Objects / instances        → summoned constructs
- Arrays / collections       → rune arrays / spell arrays / ledgers
- Conditionals               → binding runes / truth crystals
- Loops                      → recursive incantations / ritual loops
- Errors / exceptions        → corruption, binding failures
- Debugging                  → dispelling corruption / tracing runes
- Design patterns            → ancient codices / proven formulae
- Architecture               → grand schema / the academy's foundation
- The mentor                 → Archmage Veylan (Socratic; does not give answers directly)
- The setting                → Arcane Academy / Guild of Systems Architects
- The learner                → apprentice / junior mage / senior architect

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
TIER CALIBRATION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
APPRENTICE: Simple language. No assumed prior knowledge. Concrete, everyday analogies.
            800-1400 total body words. 2-3 guided steps.
JUNIOR:     Intermediate. Assume comfort with basics. More code depth.
            1000-1800 total body words. 3-4 guided steps.
SENIOR:     Advanced. Systems thinking. Tradeoffs. Architecture-level reasoning.
            1200-2500 total body words. 3-4 guided steps. soloAssessment type: PATTERN_MATCH.
LEAD:       Expert. Leadership, design, synthesis. Research-level depth.
            1400-3000 total body words. 3-4 guided steps. soloAssessment type: AI_REVIEW.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
OUTPUT RULES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- Output the raw Markdown file ONLY. No preamble, no explanation.
- Start immediately with ---  (opening frontmatter delimiter).
- End the file with the Lore Conclusion section.
- ALL frontmatter fields must be present and correctly typed.
- YAML strings containing colons or special chars must be quoted.
- Code blocks inside YAML (modelAnswer) must use the | block scalar.
- guidedSteps instruction fields that contain code must use a Markdown code fence inside the YAML literal string.
"""

# ── Build user prompt ─────────────────────────────────────────────────────────

def slugify(title: str) -> str:
    return re.sub(r'[^a-z0-9]+', '_', title.lower()).strip('_')


def build_user_prompt(module: dict, lesson: dict) -> str:
    tier = module["tier"]
    diff = TIER_DIFF.get(tier, 1)
    est = TIER_EST_MINS.get(tier, 25)
    integration = TIER_INTEGRATION.get(tier, ["mathematics"])
    lesson_slug = slugify(lesson["title"])
    practice = lesson.get("practiceType", "NONE")
    quest = lesson.get("questType", "KNOWLEDGE")

    solo_type = {
        "APPRENTICE": "RUBRIC_REFLECTION",
        "JUNIOR": "RUBRIC_REFLECTION",
        "SENIOR": "PATTERN_MATCH",
        "LEAD": "AI_REVIEW",
    }.get(tier, "RUBRIC_REFLECTION")

    retrieval_weight = "high" if practice == "JAVA" else "medium"

    return f"""Generate a complete Arcane Academy Markdown lesson file.

IDENTITY:
  id:             {lesson["id"]}
  school:         engineering
  domainId:       java
  tier:           {tier}
  moduleId:       {module["id"]}
  moduleTitle:    "Module {module["sortOrder"]}: {module["title"]}"
  moduleGlyph:    "{module["glyph"]}"
  moduleSortOrder: {module["sortOrder"]}
  topicSlug:      {lesson["topicSlug"]}
  topicTitle:     "{lesson["topicTitle"]}"
  topicSortOrder: {lesson["topicSortOrder"]}
  lesson:         {lesson_slug}
  title:          "{lesson["title"]}"
  sortOrder:      {lesson["sortOrder"]}
  difficulty:     {diff}
  estimatedMinutes: {est}
  xpReward:       {lesson["xpReward"]}
  practiceType:   {practice}
  questType:      {quest}
  retrievalWeight: {retrieval_weight}
  questTypes:     [guided, solo, retrieval]
  prerequisites:  []
  integrationDomains: {json.dumps(integration)}
  soloAssessment.type: {solo_type}

CONTENT BRIEF:
  - Tier: {tier} — {['simple / no assumed prior', 'intermediate / assume basics', 'advanced / systems thinking', 'expert / leadership + synthesis'][diff - 1]}
  - Module: {module["title"]}
  - Topic cluster: {lesson["topicTitle"]}
  - Lesson concept: "{lesson["title"]}"
  - Practice type: {practice} ({
    "learner writes and runs Java code" if practice == "JAVA"
    else "written / discussion response — no code execution"
    })
  - Quest type: {quest}

{"Prior lessons in this module: " + ", ".join(f['title'] for f in module['subChunks'] if f['sortOrder'] < lesson['sortOrder']) if any(f['sortOrder'] < lesson['sortOrder'] for f in module['subChunks']) else "This is the first lesson in the module."}

Generate the full lesson file now. Remember: output raw Markdown only, starting with ---.
"""


# ── API call ──────────────────────────────────────────────────────────────────

def generate_lesson(client: anthropic.Anthropic, module: dict, lesson: dict, dry_run: bool) -> str:
    prompt = build_user_prompt(module, lesson)

    if dry_run:
        print("\n" + "=" * 60)
        print(f"DRY RUN — would generate: {lesson['id']}")
        print("=" * 60)
        print(prompt[:500] + "...")
        return ""

    message = client.messages.create(
        model=MODEL,
        max_tokens=MAX_TOKENS,
        system=SYSTEM_PROMPT,
        messages=[{"role": "user", "content": prompt}],
    )
    return message.content[0].text


# ── File paths ────────────────────────────────────────────────────────────────

def output_path(module: dict, lesson: dict) -> Path:
    tier = module["tier"].lower()
    topic = lesson["topicSlug"]
    lesson_id = lesson["id"]
    # JAVA_DIR = content/java/ so output is: content/java/{tier}/{topicSlug}/{id}.md
    return JAVA_DIR / tier / topic / f"{lesson_id}.md"


# ── Main ──────────────────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(description="Batch Arcane Academy lesson generator")
    parser.add_argument("--tier", help="Only generate this tier (APPRENTICE|JUNIOR|SENIOR|LEAD)")
    parser.add_argument("--id",   help="Only generate this specific lesson ID")
    parser.add_argument("--dry-run", action="store_true")
    parser.add_argument("--force", action="store_true", help="Overwrite existing .md files")
    args = parser.parse_args()

    api_key = os.environ.get("ANTHROPIC_API_KEY")
    if not api_key and not args.dry_run:
        # Try loading from .env (root of repo = 6 levels up from this script)
        env_file = JAVA_DIR.parents[5] / ".env"
        if env_file.exists():
            for line in env_file.read_text(encoding="utf-8").splitlines():
                if line.startswith("ANTHROPIC_API_KEY="):
                    api_key = line.split("=", 1)[1].strip().strip('"')
                    break

    if not api_key and not args.dry_run:
        print("ERROR: ANTHROPIC_API_KEY not found in environment or .env file")
        sys.exit(1)

    if not args.dry_run:
        import httpx
        client = anthropic.Anthropic(
            api_key=api_key,
            http_client=httpx.Client(verify=False),
        )
    else:
        client = None

    # Collect all stub files (they live alongside this script in content/java/)
    stub_files = sorted(glob.glob(str(JAVA_DIR / "se-*.json")))
    print(f"Found {len(stub_files)} stub files in {JAVA_DIR}")

    total = 0
    skipped = 0
    generated = 0
    errors = []

    for stub_path in stub_files:
        with open(stub_path, encoding="utf-8") as f:
            module = json.load(f)

        tier = module["tier"]
        if args.tier and tier != args.tier.upper():
            continue

        for lesson in module["subChunks"]:
            lid = lesson["id"]
            if args.id and lid != args.id:
                continue

            total += 1
            out = output_path(module, lesson)

            if out.exists() and not args.force:
                print(f"  SKIP  {lid} (exists)")
                skipped += 1
                continue

            print(f"  GEN   {lid} — {lesson['title']} [{tier}]", end="", flush=True)

            if args.dry_run:
                generate_lesson(None, module, lesson, dry_run=True)
                generated += 1
                continue

            try:
                out.parent.mkdir(parents=True, exist_ok=True)
                content = generate_lesson(client, module, lesson, dry_run=False)

                if not content.strip().startswith("---"):
                    print(f"\n  WARN: output doesn't start with ---\n{content[:100]}")

                out.write_text(content, encoding="utf-8")
                print(f"  ✓  → {out.relative_to(CONTENT_ROOT.parent.parent.parent.parent.parent)}")
                generated += 1

                time.sleep(RATE_LIMIT_SLEEP)

            except Exception as e:
                print(f"\n  ERROR: {e}")
                errors.append((lid, str(e)))
                time.sleep(5)

    print(f"\n{'='*50}")
    print(f"Done. Generated: {generated}  Skipped: {skipped}  Errors: {len(errors)}")
    if errors:
        print("Errors:")
        for lid, err in errors:
            print(f"  {lid}: {err}")


if __name__ == "__main__":
    main()
