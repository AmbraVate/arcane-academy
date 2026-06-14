---
id: phy-lea-m4-02
domainId: physics
tier: LEAD
moduleId: phy-lea-m4
moduleTitle: "Module 4: Scientific Leadership"
moduleGlyph: "👑"
moduleSortOrder: 4
topicSlug: project_management
topicTitle: "Project Management"
topicSortOrder: 2
title: "Managing Scientific Projects: Plans That Survive Reality"
sortOrder: 2
xpReward: 150
practiceType: NONE
questType: MASTERY
feynmanPrompt: "Explain to a senior student how scientific projects are planned under uncertainty — scope, critical path, margins, risk registers — why research projects differ from construction projects, and what honest schedule reporting looks like."
learningObjectives:
  - Build a project skeleton: scope and deliverables, work breakdown, dependencies and critical path, and margins that reflect estimated uncertainty
  - Manage research-specific uncertainty: risk registers with owners and triggers, stage gates and kill criteria, and the discipline of re-planning when reality votes
  - Report project state honestly — slipped versus lied-about milestones, early warning over late surprise — and run post-mortems that feed the next plan
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Builds the skeleton correctly: scope stated as verifiable deliverables, work broken down to ownable packages, dependencies mapped, critical path identified as the sequence that sets the end date"
    - "Treats estimates as distributions: margins sized to uncertainty (not uniform padding), schedule risk concentrated on the critical path, and the planning fallacy countered with reference-class evidence from past projects"
    - "Manages risk actively: a register with likelihood, impact, owner, trigger, and response per risk; stage gates with kill criteria; re-planning treated as the method working rather than failure"
    - "Reports honestly: early warning of slips (the leadership lesson's bad-news channel applied to schedules), no green-until-red theatre, and blameless post-mortems whose lessons price the next plan's margins"
  keywords: [scope, work breakdown, critical path, margin, risk register, stage gate, planning fallacy, post-mortem]
  modelAnswer: |
    A scientific project plan is a model — the Senior modelling cycle aimed at the
    project itself — and it earns trust the same way: stated assumptions, honest
    uncertainties, validation against reality, refinement when reality votes. The
    skeleton has four members. Scope: what will exist when you are done, stated as
    verifiable deliverables — 'a calibrated detector achieving X sensitivity' — and,
    just as loudly, what is OUT of scope, because unmanaged scope growth is how
    projects drown politely. Work breakdown: the scope decomposed into packages each
    small enough to own, estimate, and verify — delegation's unit of currency from
    the leadership lesson. Dependencies: which packages feed which, mapped before
    they ambush you. And the critical path: the longest dependent chain, which alone
    sets the end date — a week lost on it is a week lost to the project, while
    off-path packages carry slack. Leaders who don't know their critical path are
    managing the wrong work.

    Research planning differs from construction in one respect that changes
    everything: the estimates are distributions, not numbers. Nobody has built this
    cryostat before — that is what makes it research — so the honest estimate is a
    range with a confidence, margins are sized to each package's uncertainty rather
    than padded uniformly, and schedule reserve is concentrated where the critical
    path runs through novel work. The planning fallacy — humanity's measured,
    incurable optimism about its own projects — is countered with reference-class
    evidence: ask what comparable cryostats actually took, not what this one feels
    like it should. Risk is then managed as a living register: each entry with
    likelihood, impact, an owner, a trigger that says when it has fired, and a
    pre-agreed response — plus stage gates with kill criteria, the research-design
    discipline scaled up, where the project must re-earn its continuation.

    The reporting culture is the leadership lesson applied to schedules. A slipped
    milestone honestly flagged early is project management working: options remain —
    descope, re-sequence, reinforce. The same slip concealed until the review —
    green-until-red theatre — is the integrity lesson's descent wearing a Gantt
    chart, and it forecloses every option money could have bought. And when phases
    end, the blameless post-mortem harvests the only data that prices the next
    plan's margins: what did we estimate, what did it take, and which assumption was
    wrong. Plans never survive contact with reality intact; planning is what lets
    you respond to reality faster than reality destroys you.
guidedSteps:
  - id: phy-lea-m4-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Your instrument project has five work packages: A(8 weeks) → C(10) → E(6) form a
      dependent chain; B(4) and D(5) run in parallel feeding E but with weeks of slack.
      A storm of requests lands on your desk. Where does a week of delay actually move
      your delivery date — and which packages deserve your closest tracking?
    inputConfig:
      options:
        - "A delay anywhere moves the end date equally — track everything identically"
        - "Only delays on the A→C→E chain — the critical path — move the end date; B and D have slack; the critical path gets the margin, the monitoring, and the best people"
        - "B and D, because parallel work is riskier"
        - "None — delays average out across packages"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Only delays on the A→C→E chain — the critical path — move the end date; B and D have slack; the critical path gets the margin, the monitoring, and the best people"]
      rejectedFeedback: "The critical path — the longest dependent chain (A→C→E = 24 weeks) — alone sets the end date: a week lost there is a week lost to the project, while B and D can slip within their slack at zero cost to delivery. This asymmetry directs everything: schedule reserve, monitoring frequency, staffing, and your own attention concentrate on the path. Leaders who track all packages equally are managing the wrong work."
    hint: "Trace each chain's total length. Which sequence determines the earliest possible finish? What happens to the end date if a slack-carrying package slips by less than its slack?"
    reflectionPrompt: "The critical path can MOVE when a slip reorders the chains. What does that imply about how often you must recompute it?"
  - id: phy-lea-m4-02-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your postdoc estimates the novel cryostat at "about 12 weeks." The last three
      comparable first-of-kind cryostats in the collaboration's records took 19, 23,
      and 21 weeks against similar estimates.

      In one or two sentences: what is the name of the bias at work, and what does the
      honest schedule entry look like?
    inputConfig:
      placeholder: "The bias, and the honest entry..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["planning fallacy", "optimis", "reference", "20", "21", "range"]
      rejectedFeedback: "The planning fallacy: humans systematically estimate their own projects from the inside view ('the steps should take...') and are measurably, incurably optimistic. The corrective is the reference class — what comparable work ACTUALLY took: here ~20 weeks. Honest entry: a range anchored on the reference class (say 16–24 weeks, expected ~20), with the difference from the inside view carried as explicit critical-path margin, not hidden hope."
    hint: "Inside view: imagine the steps. Outside view: consult the records of what similar projects took. Which does the evidence say to trust — and what number does it give here?"
    reflectionPrompt: "Why does the planning fallacy survive even in experts who KNOW about the planning fallacy — and what does that imply about process versus willpower?"
  - id: phy-lea-m4-02-g3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two project leaders each hit the same 6-week slip on a critical-path package.
      Leader A flags it the week it becomes likely: the board descopes one secondary
      deliverable and re-sequences, delivery slips 2 weeks. Leader B reports green
      until the quarterly review, then reveals the full slip: no options remain,
      delivery slips 8 weeks and trust with it. What principle separates them?
    inputConfig:
      options:
        - "Early warning preserves options; concealment forecloses them — schedule honesty is the bad-news channel applied to projects, and green-until-red is the integrity descent in a Gantt chart"
        - "Leader B was unlucky; outcomes were equivalent in expectation"
        - "Leader A revealed weakness; B protected the team's reputation"
        - "Slips should never be reported until certain"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Early warning preserves options; concealment forecloses them — schedule honesty is the bad-news channel applied to projects, and green-until-red is the integrity descent in a Gantt chart"]
      rejectedFeedback: "A slip flagged early is a problem with options: descope, re-sequence, reinforce, renegotiate — most of them cheap if exercised in time. The same slip concealed compounds silently until no money can buy back the lost weeks, and the concealment costs what the integrity lesson priced: trust, which funds every future report. The leadership lesson's messenger rule applies to your own reporting upward: be the early messenger."
    hint: "List what the board could still DO at week one versus at the quarterly review. Then ask what Leader B's next 'green' status report is worth."
    reflectionPrompt: "What incentive structures make green-until-red rational for the reporter — and how would you, as the board, redesign them?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A useful risk-register entry contains..."
    options:
      - "A list of everything that could conceivably go wrong"
      - "For each material risk: likelihood, impact, a named owner, a trigger that says when it has fired, and the pre-agreed response — a living document reviewed on cadence"
      - "Only risks with engineering solutions"
      - "Risks recorded after they occur, for the archive"
    correctIndex: 1
    feedback: "A risk without an owner is a worry; without a trigger, it's never acted on in time; without a pre-agreed response, the response is improvised in the worst week to improvise. The register is a set of pre-made decisions waiting for their conditions — and it works only as a living document, re-priced as reality reports in."
  - type: MULTIPLE_CHOICE
    question: "Why are blameless post-mortems the planning system's most valuable data source?"
    options:
      - "They satisfy funding agencies' reporting requirements"
      - "They harvest the only calibration data that exists for YOUR team's estimates — actuals versus estimates and which assumptions broke — and blamelessness is what keeps that data honest"
      - "They assign responsibility for failures to individuals"
      - "They are quicker than status meetings"
    correctIndex: 1
    feedback: "Next year's margins can only be priced from this year's actuals — the reference class is built one honest post-mortem at a time. Hunt culprits and the data corrupts instantly (the safety lesson: fear silences exactly what you need); hunt causes and every slip becomes calibration. Aviation rebuilt itself on this loop; projects borrow it for the same reason."
---

# Hook

The James Webb Space Telescope was proposed at five hundred million dollars and fourteen years from launch. It flew at ten *billion* — twenty times over — and a decade late. The Superconducting Super Collider, by contrast, was cancelled mid-dig in 1993: two billion spent, fourteen miles of tunnel abandoned in Texas, and the centre of particle physics moved to Europe for a generation. Same era, same calibre of physicists, opposite endings — and the difference was not the quality of the science. It was the machinery of promises: estimates, margins, milestones, and what happened when reality disagreed with the plan.

Here is the uncomfortable inheritance: research projects are *systematically* late and over budget — not because scientists are careless, but because humans estimate from the inside ("the steps should take...") and novel work has no inside track record. The cure is not harder willpower; it is a craft — plans built as honest models, margins sized like error bars, risks owned before they fire, and slips reported while options still exist. Yesterday you learned to lead people. Today: the machinery that keeps your promises from breaking them.

# Lore Introduction

The second tablet's chalked decade stretches across the slate — an instrument's life from first sketch to first light, milestones like fence posts, and one entire year struck through and redrawn in a different hand.

"The Deepwell Array," Vael says. "The Guild's great ear, twelve years in the building. The struck year is mine — I ran the third phase, and I am going to teach you with my own scars." She taps the redrawn section. "The cryogenic stage. My best artificer estimated twelve weeks; it took twenty-three. I had padded nothing, because the schedule *looked* tight and councils reward tight schedules. The slip landed on the one chain that set the delivery date — I could not have told you then which chain that was — and I reported it green for two months because I believed, each fortnight, that the next fortnight would save me." She lets the silence do its work. "The Array delivered, eventually. My credibility with that council took four years longer."

Selka, from the benches, without looking up: "Tell them what the post-mortem found."

"That the records of every previous cryogenic build were in our own archive," Vael says, "and they averaged twenty-one weeks against estimates of twelve. The answer was on a shelf. We had planned from hope instead." She hands you the chalk and Thorne's brass rule together. "Today, Lead: scope, paths, margins, risks, and the reporting that keeps trust alive when — not if — the plan meets reality. We will be measuring promises. Measure them like a physicist."

# Core Learning

## Concept Introduction

**The plan is a model.** A project plan is the Senior modelling cycle aimed at the project itself: simplify (what matters), state assumptions aloud, predict (schedule, cost), validate against reality (actuals), refine (re-plan). It earns trust the same way models do — and fails the same ways: hidden assumptions, no uncertainty estimates, and refusal to update when reality votes. Hold that frame and every craft below is familiar.

**The skeleton.** Four members:

- **Scope:** what will exist when you are done, stated as *verifiable deliverables* ("a detector achieving X sensitivity, calibrated against Y") — and, just as loudly, what is **out of scope**. Unmanaged scope growth — each addition individually reasonable — is how projects drown politely; the scope statement is the document that lets you say no on the record.
- **Work breakdown:** the scope decomposed into packages each small enough to *own, estimate, and verify* — the unit of delegation from yesterday's lesson, now with a date attached.
- **Dependencies:** which packages feed which, mapped before they ambush you — the systems lesson's arrows, drawn for work instead of water.
- **Critical path:** the longest dependent chain through the map. It alone sets the end date: a week lost on it is a week lost to the project, while off-path packages carry slack and can slip within it for free. The asymmetry directs everything — margin, monitoring cadence, staffing, your own attention — and the path *moves* as slips reorder the chains, so it is recomputed, not framed.

**Estimates are distributions.** The research difference in one sentence: nobody has built this before — *that is what makes it research* — so estimates are ranges with confidences, not numbers. Three disciplines follow. **Margins sized to uncertainty:** a routine pipeline gets 10%; a first-of-kind cryostat gets 60% — uniform padding is uncertainty-illiterate, and margin concentrates where the critical path runs through novel work. **The planning fallacy countered:** humans estimate from the *inside view* (imagining the steps) and are measurably, incurably optimistic — experts who know about the fallacy included; the corrective is the **reference class** — what comparable work *actually took*, from records (Vael's answer-on-a-shelf). **Margin declared, not hidden:** schedule reserve is a line item the board can see, not hope distributed invisibly through padded tasks — hidden margin gets spent twice.

**Risk, managed as a living register.** Each material risk carries five fields: *likelihood, impact, owner, trigger, response*. An owner, or it is a worry; a trigger ("if the vendor misses the June ship date"), or it is never acted on in time; a pre-agreed response, or the response is improvised in the worst week to improvise. The register is **pre-made decisions waiting for their conditions**, reviewed on cadence and re-priced as reality reports in. At phase boundaries, **stage gates** apply the research-design lesson at project scale: the project re-earns continuation against criteria set when everyone was still objective — including the kill criteria that part with sunk costs while parting is still cheap (the Super Collider's tragedy was not cancellation; it was that no gate caught the cost trajectory while options remained).

**Reporting: the bad-news channel, applied to schedules.** A slip flagged the week it becomes *likely* is project management working: options remain — descope a secondary deliverable, re-sequence, reinforce, renegotiate — and most are cheap if exercised early. The same slip concealed compounds silently until no money buys back the weeks: **green-until-red theatre** is the integrity lesson's incremental descent wearing a Gantt chart ("next fortnight will save me" is the project manager's version of "I'll fix the data later"), and it spends the trust that funds every future report. The leadership lesson's messenger rule runs *upward* too: be your board's early messenger, and build the team where your packages' owners are yours. Finally, **blameless post-mortems** close the loop: estimates versus actuals, which assumption broke, harvested without culprit-hunting (fear corrupts this data like any other) — because next year's reference class, the only calibration your team's estimates will ever have, is built one honest post-mortem at a time.

## Why It Matters

Modern physics *is* projects: detectors, telescopes, fusion campaigns, and satellite missions running years to decades, hundreds to thousands of names, budgets that draw national attention — and physicists rise into running them with, typically, no training beyond watching their own supervisors improvise. The stakes are written in the era's ledger: Webb's twentyfold overrun nearly consumed NASA astrophysics' budget (its gates and re-baselining eventually saved it); the Super Collider's missing machinery of promises cost a nation its field; LIGO — run with margins, gates, and honest re-baselining — delivered the century's discovery roughly on its revised plan, and is the standing proof that big science *can* keep promises. The craft transfers to every scale: a doctoral thesis is a project with one staff member and the same planning fallacy, and the post-mortem loop is how groups, not just observatories, get calibrated over years. And it composes with the module: yesterday's culture is what makes today's reporting honest; tomorrow's policy lesson is where these schedules meet the councils that fund them.

## Worked Examples

**Example 1 — Finding the path, moving the margin.** Detector upgrade, five packages: sensor fab (12w) → integration (8w) → commissioning (6w) chain; DAQ software (10w) and mounting hardware (4w) feed integration in parallel. Critical path: 12+8+6 = 26 weeks; DAQ carries 2 weeks' slack, mounting 16. Now apply the uncertainty discipline: sensor fab is first-of-kind (reference class says estimates run 60% low) — its honest entry becomes 12w (+7 reserve, declared); DAQ is routine (+1). Result: a 26-week chain carrying 33 weeks of honest promise — and a board that knows *which* 7 weeks are uncertain and why. The padded-everywhere version promises the same 33 but teaches nothing and hides the path; the unpadded version promises 26 and breaks.

**Example 2 — The register in action.** Entry: *Risk — sole supplier of low-noise amplifiers in financial trouble. Likelihood: medium. Impact: 12-week critical-path slip. Owner: electronics lead. Trigger: missed June pre-shipment review. Response (pre-agreed): activate second-source qualification (already costed, 3 weeks, parts on shelf).* June review missed → trigger fires → response executes *that week*, no meeting needed, slip contained to 3 weeks. The counterfactual project discovers the bankruptcy in September, improvises a sourcing scramble in its worst month, and slips 12. Same risk, same world — the difference was a decision made early, waiting for its condition.

**Example 3 — Re-baselining versus ratcheting.** Eighteen months in, reality has voted: the novel stage is 14 weeks behind, and two risks have fired. *Ratchet response:* keep the original baseline, demand overtime, report "recovering" — the schedule becomes fiction, the team learns the plan is theatre, and the slip emerges anyway, later, larger. *Re-baseline response:* take the slip to the gate with options costed — descope the secondary band (saves 8w), re-sequence commissioning (saves 4w), accept 2w net slip — board chooses, new baseline *declared*, old one archived in the post-mortem record as calibration data. Webb survived by re-baselining honestly (eventually); the Super Collider never got the chance. A plan that cannot change is not a plan; it is a position.

## Common Mistakes

- Scope stated as aspirations, not deliverables — "world-class detector" cannot be verified or defended against growth; "X sensitivity by Y date" can
- Not knowing the critical path — attention spread evenly across packages while the one chain that sets the date slips unwatched; recompute the path, it moves
- Uniform padding — 20% on everything is uncertainty-illiterate: routine work doesn't need it, novel work needs triple; size margins like error bars
- Inside-view estimating — "the steps should take 12 weeks" loses to the archive every time; the reference class is the corrective, and it is usually on a shelf you own
- Hidden margin — hope smeared invisibly through padded tasks gets spent twice; declared reserve is a board-visible line item
- Risks without owners and triggers — a register of worries, reviewed never, actioned late; five fields per risk or it isn't managed
- Green-until-red — "next fortnight will save me" is the manager's incremental descent; flag at *likely*, while options are cheap
- Ratcheting instead of re-baselining — defending a dead baseline turns the schedule into fiction and the team into cynics; reality votes, plans update, post-mortems record
- Post-mortems that hunt culprits — corrupts the only calibration data your next plan will ever have; causes, not names

## Mental Model

A project plan is a mountain expedition's route card, and research is unmapped country. *Scope* is the summit named precisely — "that peak, by the north ridge" — because "somewhere high" gets parties killed politely. The *critical path* is the route's one chain of passes that must be crossed in order; snow lost there is summit lost, while the parallel valleys carry slack. *Margins* are the provisions: not equal rations per stage, but weighted where the map runs blank — and declared in the manifest, because hidden food gets eaten twice. The *risk register* is the pre-agreed protocol — "if the col is corniced, we traverse east; Asha decides" — settled in the valley, because no one improvises well at altitude. And the *reporting* rule is the expedition's oldest law: the scout who signals bad weather early lets the party camp, re-route, or retreat in good order; the scout who hopes the clouds will pass reports them from inside the storm. Post-mortems are the route cards archived for the next party — which is how unmapped country, expedition by expedition, becomes mapped.

## Mini Summary

- The plan is a model of the project: assumptions stated, predictions made, validated by actuals, refined when reality votes — re-baselining honestly beats ratcheting a fiction
- Skeleton: verifiable scope (and explicit out-of-scope), ownable work packages, mapped dependencies, and the critical path — the one chain that sets the date and deserves the margin, monitoring, and your best people
- Estimates are distributions: counter the planning fallacy with reference-class actuals, size margins like error bars (concentrated on novel critical-path work), and declare reserve where boards can see it
- Risk = likelihood, impact, owner, trigger, pre-agreed response, reviewed on cadence; stage gates with kill criteria re-earn continuation; slips flagged at *likely* preserve options; blameless post-mortems build the only reference class your next plan will have

# Guided Practice Quest

Vael chalks the Deepwell Array's redrawn year beside three fresh exercises. "My scars, your examination. First: five packages, two chains — find the path that owns the end date, and tell me where the margin, the monitoring, and your best artificer belong; I tracked everything equally once, and the date was set by a chain I wasn't watching. Second: the cryostat — twelve weeks by hope, twenty-one by the archive; name the fallacy, then write the schedule entry I should have written. Third: the two leaders and the six-week slip — one preserved options, one preserved appearances; state the principle, and then tell me honestly which leader I was, that struck-through year. The rule is on the table, Lead. Measure the promises."

# Solo Practice Quest

Plan a real project (350–500 words): your capstone, an instrument build, a research campaign — something you genuinely intend. Write the skeleton: scope as two or three verifiable deliverables plus one explicit out-of-scope line; a work breakdown of five to eight ownable packages with dependencies; and the critical path, identified. Estimate like a physicist: ranges with confidences, a stated reference class for the most novel package (name where the actuals would come from), and margin sized to uncertainty, declared as visible reserve. Add a three-entry risk register — likelihood, impact, owner, trigger, pre-agreed response — and one stage gate with a kill criterion in the research-design lesson's spirit. Close with your reporting covenant: the threshold at which you flag a slip, to whom, and the post-mortem you commit to running — blameless, actuals versus estimates — whatever the outcome.

# Integration

**Mathematics:** Critical-path analysis is graph theory in working dress — the longest path through a directed acyclic network — and schedule risk is probability composed along it: uncertain durations convolve, slacks truncate, and Monte Carlo over the network (your Senior marching craft, aimed at calendars) yields delivery distributions instead of dates. The planning fallacy is a measured bias with a literature; reference-class forecasting is Bayesian updating with historical priors, and margin-sizing is the error-bar craft transplanted whole.

**Engineering:** Engineering wrote this lesson's institutions: work-breakdown structures and critical-path method came from defence and construction megaprojects, stage-gate reviews from aerospace (where Webb's re-baselining and LIGO's project discipline both live), and earned-value reporting from the contracting world's need to price green-until-red theatre out of existence. The borrowing runs both ways: blameless post-mortems returned from aviation and software operations to big science, and research's uncertainty-honest margins are migrating into R&D engineering everywhere.

# Lore Conclusion

Vael sets the chalk down beside the struck-through year and, after a moment, adds one small mark you hadn't noticed was missing: her own initials against the redrawn schedule — the signature she had, she admits, left off the re-baseline for four years.

"Plans that tell the truth, margins that confess what we don't know, slips reported while mercy is still for sale," she says. "With yesterday's culture underneath, that is the machinery of kept promises — and kept promises, Lead, are the only currency the next tablet trades in." She looks down the hall, past the magi, to where the second tablet of the ring waits: chalked on it, not instruments or schedules, but a long table with chairs on both sides — and only one side's chairs filled.

"Because the Deepwell Array was not funded by physicists. The council that paid for it could not read a strain curve — and owed nothing to us except what our counsel was honestly worth. Every promise you learned to keep today was made, first, across a table like that one: to ministers weighing our decade against hospitals and harvests, to publics who hear 'uncertainty' as 'doubt', to the societies that pay for blank pages on the strength of our word." She begins to walk, gesturing you up from the chair. "Tomorrow: *Policy and Society* — how physics speaks to power without lying in either direction, and what the table costs. Bring everything, Lead. The empty chairs are ours."
