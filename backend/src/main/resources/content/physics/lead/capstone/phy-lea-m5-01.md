---
id: phy-lea-m5-01
domainId: physics
tier: LEAD
moduleId: phy-lea-m5
moduleTitle: "Module 5: Capstone Project"
moduleGlyph: "🏆"
moduleSortOrder: 5
topicSlug: capstone
topicTitle: "Capstone"
topicSortOrder: 1
title: "The Archmage's Commission"
sortOrder: 1
xpReward: 2000
practiceType: NONE
questType: MASTERY
feynmanPrompt: "Walk a council of non-physicists through your complete research programme: the question you chose and why it can fail, the team and project that will attack it, the counsel and consequences it carries, and why it deserves years of other people's money and your own life."
learningObjectives:
  - Design a complete research programme: a frontier question passing the three-way test, with study skeleton, team plan, project plan, and innovation context integrated into one defensible whole
  - Defend every layer with the tier's crafts — limits and systems thinking, research design and ethics, honest audits, leadership and policy judgement — under examination
  - Communicate the programme at three altitudes and own its consequences: dual-use review, public counsel, kill criteria, and the succession of people it will train
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Chooses and frames a frontier question that passes the full three-way test — important (the answer changes something named), tractable (a credible first attack and an opened door), falsifiable (a stated observation that would refute it) — with kill criteria and the Hamming defence"
    - "Builds the machinery honestly: study skeleton with controls and power logic, project skeleton with critical path, reference-class margins and a risk register, team design with delegation, credit rules, and the bad-news architecture"
    - "Holds the wider duties: integrity protocol and dual-use review, the policymaker briefing with the is/ought boundary kept and uncertainty made decision-shaped, the innovation context (horizon, openness/claim, soil) honestly placed"
    - "Communicates at three altitudes with claims sized to evidence throughout, and demonstrates the tier's deepest discipline: stating what the programme cannot promise, what would prove it wrong, and who it will train regardless of outcome"
  keywords: [programme, falsifiable, kill criteria, critical path, dual-use, counsel, portfolio, succession]
  modelAnswer: |
    My commission proposes a five-year programme to determine whether the stubborn
    anomaly in our muon precision measurements is new physics or unaccounted
    systematics — a question that passes the three tests: important, because either
    answer moves the frontier (new physics breaks the standard model's wall; a found
    systematic recalibrates a decade of measurements built on the same methods);
    tractable now, because the new cryogenic magnetometers open a door that did not
    exist five years ago; and falsifiable, with the refuting observation stated — if
    the anomaly shrinks below two combined uncertainties under the improved
    systematics budget, the new-physics hypothesis dies, and I will say so.

    The machinery is the tier's, assembled. Study skeleton: pre-registered analysis,
    blind offsets on the central value, a control campaign on a known-physics
    channel, and power arithmetic showing five years of statistics resolves the
    question at the stated precision — with kill criteria at year two (if the
    magnetometer noise floor misses spec by 3x, the door is not open; we stop).
    Project skeleton: the critical path runs through magnet fabrication —
    reference-class margins from the collaboration's archive, declared reserve,
    a risk register with owners and triggers. Team: nine people, outcomes delegated
    with authority, credit rules stated before work begins, and the bad-news
    architecture I will calibrate personally in my first season — my own errors
    confessed first.

    The duties are written in, not appended. Integrity: raw data immutable from
    day one, the rerun kit published with every result. Dual-use review: precision
    magnetometry has navigation applications; the review happens before
    publication decisions, not after. Counsel: the funding council receives the
    is/ought boundary kept — what we will know, with what confidence, and what
    remains theirs to weigh; the public receives the honest version — we are
    checking whether the universe's books balance, we do not know the answer, and
    that is precisely why the work is worth doing. Innovation context: this is
    middle-horizon work in the national portfolio, its instruments procured at
    specifications that will stretch three suppliers, its methods published open —
    and its deepest yield is certain regardless of outcome: nine people trained in
    the gates, the audits, and the honest verbs, who will carry the craft to teams
    of their own. That is the one return I can promise, because it is the only one
    the track record guarantees.
guidedSteps:
  - id: phy-lea-m5-01-g1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      Stake the commission. In two or three sentences: name the frontier question your
      programme will attack, the door that makes it tractable NOW (instrument, method,
      or dataset), and — non-negotiably — the observation that would prove your
      hypothesis WRONG. Phrase the question itself as an actual question.
    inputConfig:
      placeholder: "The question, the door, and what would refute you..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["?"]
      rejectedFeedback: "A commission needs all three stakes: a question precise enough to carry a question mark, a named door (Hamming's overlap — why is this attackable THIS decade?), and the falsifiability clause stated without flinching — the specific observation that kills your hypothesis. The unstruck entries on the Guild's oldest tablet all passed someone's test for deep; not one could fail. Yours must be able to."
    hint: "Run the three-way test from research design, then add Hamming's timing: importance, tractability (name the door), falsifiability (name the refuting observation). The literal question mark is the cheapest of the three — and the most often missing."
    reflectionPrompt: "If your refuting observation actually arrived in year three, what — honestly — would you do in year four?"
  - id: phy-lea-m5-01-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      The Guild's examiners audit your commission's machinery. Which assembly is
      complete enough to defend?
    inputConfig:
      options:
        - "Question + study skeleton (controls, power, pre-registration) + project skeleton (critical path, reference-class margins, risk register, gates with kill criteria) + team plan (delegation, credit rules, bad-news architecture) — each layer auditable"
        - "Question + enthusiasm + a famous co-signatory — the rest emerges during the work"
        - "Study skeleton only — leadership and schedules are administrative matters beneath the science"
        - "Project plan only — the question can be chosen once the funding arrives"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Question + study skeleton (controls, power, pre-registration) + project skeleton (critical path, reference-class margins, risk register, gates with kill criteria) + team plan (delegation, credit rules, bad-news architecture) — each layer auditable"]
      rejectedFeedback: "A programme is the tier assembled: the question carries the design lesson's tests; the study skeleton carries controls, power, and pre-registration; the project skeleton carries the path, margins, register, and gates; the team plan carries delegation, credit, and the channel that keeps bad news fast. Each layer must survive its own lesson's audit — 'the rest emerges during the work' is how the Guild's red names began, and 'choose the question after funding' is the queue-of-sure-things fallacy wearing a grant."
    hint: "Walk the tier's modules in order and ask what each contributed: a test for questions, a skeleton for studies, machinery for promises, crafts for people. The complete commission carries all of them, auditable."
    reflectionPrompt: "Which layer of your own commission is weakest right now — and which lesson do you reread tonight?"
  - id: phy-lea-m5-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      The final examination is the council chamber. A minister asks: "Professor, your
      programme wants five years and serious money, and you yourself say it may find
      nothing. Why should we fund work that cannot promise results?"

      Give your answer in two or three sentences — the honest case, with the one
      return you CAN promise.
    inputConfig:
      placeholder: "Your answer to the minister..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["track record", "train", "people", "either answer", "cannot promise", "portfolio", "kill criteria", "honest"]
      rejectedFeedback: "The honest case has three planks: either answer moves the frontier (a well-chosen falsifiable question pays on both outcomes); the track record funds the portfolio, not the prediction (Faraday, the quantum, relativity — none could promise, all repaid civilisation); and the one guaranteed return is the people — researchers trained in the gates and the honest verbs, who staff the valley's future regardless of what the instrument finds. Plus the discipline that makes it credible: kill criteria, so the council knows you will stop rather than drift. What may never be offered is the false promise — that is the almanac error at programme scale."
    hint: "You may not promise the discovery (frontier lesson's forbidden move). What CAN you promise? Check the worked examples: the both-outcomes argument, the track record, the gates that protect the council's money, and the output that compounds — people."
    reflectionPrompt: "Why is 'we will stop if X' — your kill criterion, stated unprompted — often the sentence that wins a sceptical council?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Your commission must state its dual-use and consequence review. The correct placement of this thinking is..."
    options:
      - "After publication, if someone raises concerns"
      - "Designed into the programme from the start: risks assessed before capabilities exist, disclosure calibrated through review, counsel duties named — because the discovering physicist is usually the only early-warning system there is"
      - "Delegated entirely to the funding agency's lawyers"
      - "Omitted — pure research carries no consequences"
    correctIndex: 1
    feedback: "The research-ethics lesson's working duties, now structural: think ahead of the work, because afterwards is too late and nobody else is positioned to see it coming. The Manhattan generation's hard-bought precedent is the field's inheritance — and the commission that writes the review in from day one is the one the Guild signs."
  - type: MULTIPLE_CHOICE
    question: "Twenty years on, which output of a well-run research programme does the track record say will almost certainly matter most?"
    options:
      - "The papers, which will still be heavily cited"
      - "The instrument, preserved in a museum"
      - "The people it trained — carrying the crafts, the gates, and the honest verbs into teams, fields, and councils of their own; results are superseded, judgement compounds"
      - "The grant documentation, as a model of its kind"
    correctIndex: 2
    feedback: "Rutherford's eleven laureates; Thorne's column on the lineage tablet; the postdocs of every great programme staffing the next generation's frontiers. Papers age into citations, instruments into exhibits — but judgement multiplied through people is the only output that compounds across generations. The Guild funds programmes; what it is actually buying is lineages."
---

# Hook

Every tier of this Academy ended with a trial. The Apprentice timed pendulums until measurement itself made sense. The Junior ran the Mechanist's Gauntlet. The Senior built a simulation worthy of belief and kept honest books about it. Each trial asked the same hidden question at rising stakes: *can you be trusted with the next set of tools?*

The Archmage's Commission is the last trial, and it inverts the form. No system is assigned. No apparatus waits. No examiner sets the question — because the question *is* the examination. You will choose a frontier problem worth years of other people's money and your own life; design the programme that attacks it — study, project, team, counsel, consequences, succession — and defend every layer before the assembled Guild using nothing but the crafts of four tiers. There is no model answer. There are only the tests you now know how to run on yourself: *can it fail? can it be built? can it be led? should it be done? and who will it leave behind?*

The Guild does not grade commissions against a rubric. It asks one question of the whole: *would we stake the valley's money and our students' years on this person's judgement?* Dawn is here. The hall is empty. Begin.

# Lore Introduction

The Frontier Hall at dawn is exactly as Vael promised: empty of teachers. The four rings of tablets stand in the grey light bearing four tiers of chalk — some of it yours now — and on the centre table lies a single document, heavy paper, the Guild's seal pressed deep.

*THE ARCHMAGE'S COMMISSION. To the candidate: The Guild herewith grants what it grants each generation once — a programme of the candidate's own choosing, to be designed in full and defended before the assembled Guild at the next moon. The Guild's resources, the valley's trust, and the Academy's students stand behind whatever commission survives the defence. Choose as if the blank pages were yours to spend. They are.*

Beneath the seal, in Vael's hand, a smaller note: *Every artifact of your training is in this hall. The brass rule is on the table — Thorne says you know what it measures now. The damper prototype holds the door. The ledger, the envelope, the lineage tablet: consult them as colleagues, not exhibits. And one instruction only, Lead, from all of us who taught you: do not bring us a commission that cannot fail. Bring us one worth failing at.*

You take up the chalk. The first ring's tablets ask their questions as you pass — *what may be known? what cannot be summed?* — and for the first time, they are asking *you*.

# Core Learning

## Concept Introduction

**The commission's anatomy.** A complete research programme integrates every module of this tier into one defensible document. The Guild's defence examines five layers, in order:

1. **The question** (Module 2's design lesson, at life scale). It must pass the three-way test with the answers written out: *important* — name what changes on each possible outcome, and who acts differently; *tractable* — name the door (instrument, method, dataset) that makes it attackable this decade, and the first experiment that could start this season; *falsifiable* — state the observation that would refute the hypothesis, and the kill criteria with dates. Then survive Hamming: why is this the most important problem you can tractably attack? The frontier lesson supplies the map of worthy blanks; the chaos and emergence lessons supply the humility about which questions systems actually answer.
2. **The machinery** (Modules 2 and 4 assembled). Study skeleton: hypothesis with numbered predictions, isolating controls, power arithmetic from stated noise assumptions, pre-registration and blinding commitments where expectation could steer. Project skeleton: scope as verifiable deliverables, work breakdown, the critical path identified, reference-class margins declared as visible reserve, a risk register with owners and triggers, stage gates where the programme re-earns continuation. The two skeletons must *agree* — the study's power requirements are the project's schedule drivers.
3. **The people** (Module 4's leadership lesson, made concrete). Team design with outcomes delegated and authority attached; credit rules stated before work begins; the bad-news architecture scripted — including how the leader's own errors will be confessed first; and the mentorship plan, because the programme's most certain output is its people.
4. **The duties** (Module 2's ethics, Module 4's policy). Integrity architecture: immutable raw data, the rerun kit published, correction culture. Dual-use and consequence review designed in from day one — risks assessed before capabilities exist, because the discovering physicist is the only early-warning system there is. And the counsel plan: the is/ought boundary kept in every briefing, uncertainty made decision-shaped, the table never vacated.
5. **The context** (Module 3 entire). Where the programme sits in the field's portfolio — which horizon, justified; what its instruments will demand of suppliers (the procurement dividend, counted); the openness/claim ruling for its outputs, terrain-matched; and the soil audit — what ecosystem the programme needs and builds.

**The defence's standards.** The Guild's examiners — every magus who taught you — audit each layer with its own lesson's tests, but the commission lives or dies on three integrative disciplines. **Claims sized to evidence throughout:** the proposal that promises its discovery has already failed (the frontier lesson's forbidden move); the honest verbs — *measured, predicted, unknown, not yet* — govern even your own prospectus. **The both-outcomes argument:** a well-chosen falsifiable question pays on either answer — if your programme is worthless when the hypothesis dies, the question was chosen badly. **The succession clause:** the one return the track record guarantees is people — researchers trained in the gates, the audits, and the honest verbs; a commission that cannot say who it will train, and for what, has missed the only certain output it has.

**What the trial actually tests.** Not brilliance — the Guild has shelves of brilliant failures. It tests *integrated judgement*: whether the question-chooser, the experimentalist, the project leader, the ethicist, and the counsellor in you have become one person whose layers do not contradict — whose power arithmetic matches whose schedule, whose kill criteria are dated, whose team plan funds whose succession, and who can stand before a sceptical council and say *here is what I cannot promise* without flinching. That integration has a name in the Guild's oldest files. It is what the word *Archmagus* was coined for.

## Why It Matters

The commission is not an academic exercise wearing robes: it is the literal form of the senior physicist's working life. Grant proposals, programme bids, laboratory five-year plans, collaboration white papers, and national roadmap submissions are all commissions — question, machinery, people, duties, context — judged by panels running exactly these audits, and the physicists who write fundable ones are the ones who learned to integrate the layers rather than excel at one. The integration itself is the scarce skill: fields have brilliant question-choosers whose projects collapse, flawless project managers attacking unfalsifiable questions, and inspiring leaders who never wrote a dual-use review — the complete commission is rare enough that councils remember the people who bring them. And the succession clause is the quiet engine of science itself: every craft in your four tiers reached you through someone's programme that budgeted for teaching it — the lineage tablet is the Guild's true balance sheet, and your commission is now a line on it.

## Worked Examples

**Example 1 — A commission assembled (the anomaly programme).** *Question:* is the persistent muon-precision anomaly new physics or unaccounted systematics? Both outcomes pay (new physics breaks a wall; a found systematic recalibrates a decade of measurements). Door: cryogenic magnetometers, matured last five years. Refuting observation stated; kill criterion at year two (noise floor 3× off spec → stop). *Machinery:* blind central value, control channel, power arithmetic → five years of statistics; critical path through magnet fabrication, reference-class margins from the collaboration archive. *People:* nine, outcomes delegated, credit rules day one. *Duties:* dual-use review of precision magnetometry (navigation) before publication decisions. *Context:* middle horizon, methods open, three suppliers stretched. Every layer auditable by its own lesson — and the defence's hardest question ("what if it's just systematics?") answered in the design: *then we will have found that, published it, and saved the field a decade.*

**Example 2 — A commission rejected, instructively.** A candidate of a previous generation proposed a magnificent unification programme: deep question, dazzling mathematics, a decade of theory. The Guild's audit: *falsifiability* — no observation within the programme's reach could refute it ("the unstruck entries," the examiners wrote, "all passed someone's test for deep"); *kill criteria* — none; *succession* — students would be trained in one candidate formalism, employable nowhere else if it failed. Rejected — with the Guild's standard kindness: *re-stake the question at the scale where it can fail.* The candidate returned two years later with a bounded, falsifiable fragment of the same dream, defended it, and spent a career striking entries off the tablet. The dream survived by learning to lose.

**Example 3 — The defence's final question.** Every commission defence ends the same way, by Guild custom older than the hall. The presiding Archmagus asks: *"And if everything fails — the hypothesis dead by year three, the instrument descoped, the field moved on — what will remain?"* The passing answer has never varied in two centuries, though each candidate finds it in their own words: *the people will remain* — trained in the gates, carrying the crafts, choosing their own questions by the tests I was taught and will teach. Candidates who answer with salvage plans for the apparatus are sent back to reread the lineage tablet. The Guild funds programmes; it is buying lineages.

## Common Mistakes

- The unfalsifiable masterpiece — deep, important, unable to lose, unable to conclude; the oldest tablet's unstruck entries are the warning, and "bring us one worth failing at" is the instruction
- Promising the discovery — the frontier lesson's forbidden move at programme scale; promise the question's resolution, the gates, and the people, never the answer
- Layers that contradict — power arithmetic demanding statistics the schedule never collects; a team plan with no one free to run the control campaign; kill criteria the budget narrative ignores; examiners hunt the seams first
- Kill criteria as decoration — undated, unowned, or quietly incompatible with the candidate's attachment; the start-of-programme self must bind the year-three self, in writing
- Ethics and counsel as appendices — dual-use review and the briefing plan bolted on after design are visible as bolts; the duties are structural members or they are theatre
- The missing succession clause — a programme that cannot name who it trains, and for what careers if the hypothesis dies, has missed its only guaranteed output
- Defending against the examiners instead of with them — the defence is peer review at life scale; the communication lesson's rule holds: the report that stings most is usually the one that found something
- Choosing by availability at the very last — the commission shaped like your supervisor's programme, sized for comfort; Hamming's question is the first one the Guild asks, and it stings by design

## Mental Model

The commission is a cathedral plan, and you are no longer the stonemason. The *question* is the site and the dedication — chosen knowing the ground (the frontier's honest map) and knowing it must bear weight or the whole work is folly: a cathedral to an unfalsifiable saint falls at the first storm of evidence. The *machinery* is the architecture: load paths computed (power arithmetic), the critical vault identified, margins in the buttresses sized to the unknowns of ground never built on, and inspection gates where the chapter re-approves the work. The *people* are the lodge: masters who out-carve you delegated whole transepts, apprentices indentured with their names already on the credit stones, and a lodge-rule that the first cracked vault is reported at dawn, not plastered. The *duties* are the foundations and the doors: dug before the walls (review before capability), opened honestly to the town whose tithe pays for it (counsel, boundary kept). And the *context* is the truth every cathedral teaches: the builder will not see it finished — the certain inheritance is not the spire but the lodge itself, masons trained and dispersed, raising vaults in towns the founder never visited. The Guild signs the plans it would trust across generations. That is what the seal means.

## Mini Summary

- The commission integrates the tier: a three-way-tested question with a named door and a stated refuting observation; study and project skeletons that agree; a team plan with delegation, credit, and the bad-news channel; duties (integrity, dual-use, counsel) as structure; portfolio context with the openness ruling and soil audit
- The defence's standards: claims sized to evidence throughout, the both-outcomes argument (a good question pays either way), dated kill criteria binding your future self, and the succession clause — people as the one promisable return
- The trial tests integration, not brilliance: layers that don't contradict, and a candidate who can say "here is what I cannot promise" before a sceptical council without flinching
- Bring a commission that can fail — worth failing at — because the unstruck entries on the oldest tablet are the Guild's most expensive exhibit, and the lineage tablet is its true balance sheet

# Guided Practice Quest

The hall is empty; the commission paper waits; the brass rule lies across it like a paperweight and a verdict. Three acts open the moon you have been given. First, stake the claim: question, door, and the observation that would kill it — written on the first ring's tablets, where chaos and emergence will audit your humility about what systems answer. Second, assemble the machinery and walk the seams: does the power arithmetic drive the critical path? do the kill criteria carry dates? does the team plan name who owns the control campaign and whose name goes first on the credit stones? Third, rehearse the chamber: the minister's question — *why fund what cannot promise?* — answered with both outcomes, the track record, the gates, and the people. Then sign it, Lead. The defence is at the next moon, and the Guild reads signatures the way you read data: for what they are willing to stand behind.

# Solo Practice Quest

Write the Archmage's Commission (this is the capstone deliverable; 350–500 words for the defence document itself, distilled from your full design). Required structure: the question, three-way-tested, with the door named and the refuting observation stated in one unflinching sentence; the machinery in summary — controls, power logic, critical path, reference-class margins, and kill criteria with dates; the people — team shape, delegation defaults, credit rules, bad-news architecture, and the succession clause naming what your students will carry regardless of outcome; the duties — integrity architecture, dual-use review placement, and the counsel plan with the is/ought boundary explicit; and the context — horizon, openness/claim ruling, soil audit. Close with the chamber answers: two sentences to the minister who asks why fund the unpromisable, and one sentence — your own words — answering the Guild's final question: *if everything fails, what will remain?*

# Integration

**Mathematics:** The commission is the tier's mathematics under one seal: power arithmetic and the 1/√N law sizing the campaign, critical-path and Monte Carlo logic pricing the promises, decision theory disciplining the gates and the value of waiting, and calibration statistics governing every claim's stated confidence — the candidate who cannot show the arithmetic behind each layer is asking the Guild to fund adjectives.

**Engineering:** The defence itself is engineering's oldest institution — the design review — applied to a life's work: requirements traceable to the question, margins declared, failure modes (the risk register) owned before commitment, and the review board's approval meaning shared responsibility, not transferred blame. And the succession clause is engineering's deepest tradition too: every great works trained the builders of the next one, and the procurement stretch, the supplier web, and the lodge of people are what remain when the instrument itself is a museum piece.

# Lore Conclusion

The defence runs from morning until the lamps are lit. They are all there — Vael presiding in Guild grey; Selka, who questions your blinding scheme for an hour and finally, fractionally, nods; Hale, who attacks the storm-risk entries in your register and finds the owners and triggers already named; Calde, who reads only the procurement annex and the credit rules, and says nothing, which from Calde is a verdict; and old Thorne, who asks a single question — *"Show me the sentence that would prove you wrong"* — and, when you read it aloud without flinching, closes his eyes like a man hearing an instrument finally in tune.

Then the presiding Archmagus asks the final question, the one custom has preserved for two centuries: *"And if everything fails — what will remain?"*

You answer in your own words. The hall is silent. Vael rises.

"The Guild has heard the commission and audited its layers," she says, formally, and then — not formally at all — "Four tiers ago, a first-year apprentice timed a pendulum in Thorne's tower and wrote down a number with no ± and no idea what was coming." She takes up the iron key you were given at the Senior gate — and beside it sets a second object: a small brass pendulum bob, polished with age, drilled and threaded onto a cord. "Thorne's first bob. The Guild gives it to each new Archmagus with the same instruction: *hang it somewhere students can see it, and tell them the truth — that everything begins with one honest measurement.*"

She places it in your hands. The magi rise, one by one — colleagues now.

"The commission is signed. The blank pages are yours, Archmagus — and the autumn brings new apprentices who will need what Thorne gave you, and Liora, and Selka, and all of us." She smiles, one last time, in the lamplight of the hall that is now yours to keep. "The frontier keeps dishonest books, you were told on your first morning here. Go balance them. And teach."

*— End of the Physics Pathway. The Academy's lineage tablet gains a column; the column, in time, will gain names. —*
