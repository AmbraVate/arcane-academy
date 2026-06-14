---
id: phy-lea-m4-03
domainId: physics
tier: LEAD
moduleId: phy-lea-m4
moduleTitle: "Module 4: Scientific Leadership"
moduleGlyph: "👑"
moduleSortOrder: 4
topicSlug: policy_and_society
topicTitle: "Policy and Society"
topicSortOrder: 3
title: "Physics, Policy, and the Public Square"
sortOrder: 3
xpReward: 150
practiceType: NONE
questType: MASTERY
feynmanPrompt: "Explain to a senior student the difference between what science can tell a policymaker and what it cannot decide for them, how to communicate uncertainty without licensing inaction, and how public trust in science is kept or lost."
learningObjectives:
  - Distinguish the science questions (what is, with what confidence) from the values questions (what to do about it) in any policy debate, and serve the boundary honestly
  - Communicate uncertainty to decision-makers without licensing paralysis — risk framing, scenario ranges, and decisions under uncertainty as the norm rather than the exception
  - Explain how public trust in science is built and lost, and the working duties of the physicist in the public square — honest counsel, calibrated claims, and respect for the audience
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Separates the questions correctly: science answers 'what is and with what confidence' (is/confidence claims); policy weighs values, costs, and trade-offs ('ought' decisions) — and the adviser who smuggles values inside science claims forfeits both authorities"
    - "Communicates uncertainty as decision-input: ranges tied to thresholds, scenarios with likelihoods, expected costs of acting versus waiting — and counters the 'uncertain means ignore it' fallacy with everyday risk reasoning (insurance, seatbelts)"
    - "Explains trust dynamics: credibility as a slow-filling, fast-draining stock; overclaiming, hidden uncertainty, and contempt for the audience as the classic drains; track-record honesty as the only refill"
    - "States the working duties: counsel that is true rather than comfortable, claims sized to evidence, transparency about interests, respect for the public's right to weigh values — and staying at the table when the advice is unwelcome"
  keywords: [is/ought, honest broker, uncertainty, threshold, risk, trust, counsel, public]
  modelAnswer: |
    Every policy debate that involves physics contains two questions wearing one coat,
    and the adviser's first craft is separating them. The science question: what is
    the case, with what confidence — the dose the reactor releases, the warming the
    emissions commit, the range of the missile. The values question: what to do about
    it — what risks a society will carry, who pays, what trade-offs are tolerable.
    Science can answer the first kind with measured confidence; it has no authority
    on the second, because 'ought' weighs goods against each other and instruments
    weigh nothing but the world. The adviser who smuggles preferences inside
    confidence intervals — declaring policy in science's voice — forfeits both
    authorities at once: the science gets doubted and the values get resented. The
    working stance is the honest broker: lay out what is known, with what
    uncertainty; map the options and their measured consequences; and leave the
    choosing to those accountable for it — while refusing, equally, the opposite
    failure of hiding behind 'more research is needed' when the evidence already
    bounds the answer.

    Uncertainty is where most counsel fails, in both directions. Decision-makers
    decide under uncertainty constantly — that is what deciding is — so the
    physicist's job is to make uncertainty decision-shaped: ranges tied to the
    decision's thresholds ('between 1.99 and 2.03 against your limit of 2.05'),
    scenarios with likelihoods rather than single forecasts, and the expected costs
    of acting versus waiting stated side by side. The fallacy to kill on sight is
    'uncertain, therefore ignore': no one refuses insurance because the house
    probably won't burn. And the seduction to refuse is false certainty — shaving
    the error bars to sound authoritative buys one persuasive afternoon and spends
    the credibility that funds every future hearing, because reality eventually
    publishes its own number.

    Trust is the account all of this draws on, and it behaves like the systems
    lesson says stocks behave: filled slowly by kept promises and honest hedges,
    drained fast by overclaims, hidden uncertainty, and contempt for the audience.
    The public is not a lecture hall that failed; people weigh evidence inside
    lives, values, and histories the adviser does not share, and respect for that
    is methodology, not manners. The duties that survive every case I have studied:
    counsel that is true rather than comfortable, claims sized exactly to evidence,
    interests declared, the values boundary kept — and staying at the table when
    the advice is unwelcome, because the chair vacated in frustration is filled by
    someone with fewer scruples about the boundary.
guidedSteps:
  - id: phy-lea-m4-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A minister asks: "Should we build the reactor?" Which response keeps the
      science/values boundary honestly?
    inputConfig:
      options:
        - "'Yes — the physics says build it.' Science should decide where it has expertise"
        - "'Here is what we can tell you: projected output, measured risk bounds, waste profile, cost ranges with uncertainties. Whether those risks and costs are acceptable against your alternatives is the decision you hold — and here is how each option performs on each axis.'"
        - "'That is a values question; physics has nothing to offer.' Decline to engage"
        - "'More research is needed before anyone can say anything'"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["'Here is what we can tell you: projected output, measured risk bounds, waste profile, cost ranges with uncertainties. Whether those risks and costs are acceptable against your alternatives is the decision you hold — and here is how each option performs on each axis.'"]
      rejectedFeedback: "The honest broker serves the boundary from both sides: deliver everything science can measure — output, risks, waste, costs, each with confidence — and map the options' consequences, while leaving the weighing of risks against values to those accountable for it. 'The physics says build it' smuggles values into science's voice; 'nothing to offer' abandons measurable facts the decision needs; 'more research' hides behind uncertainty the evidence already bounds."
    hint: "Split the minister's question: which parts are 'what is, with what confidence' (science's to answer) and which are 'what risks are acceptable' (the accountable chooser's)? The best answer serves both without crossing."
    reflectionPrompt: "Why does smuggling values inside science claims eventually damage the science's credibility too?"
  - id: phy-lea-m4-03-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A council member says: "Your flood projection runs from 0.4 to 1.2 metres — you
      scientists can't even agree, so we'll wait for certainty before spending on
      defences."

      In one or two sentences, give the physicist's reply — what is wrong with
      'uncertain, therefore wait'?
    inputConfig:
      placeholder: "Your reply to the council member..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["insur", "risk", "whole range", "both ends", "act", "cost of waiting", "threshold", "lower bound"]
      rejectedFeedback: "Uncertainty is not ignorance — the range IS the knowledge: even the best case of 0.4 m overtops the current defences, so the decision is already bounded; and waiting has its own price, since defences take a decade to build while the range narrows too slowly to matter. Everyday life prices uncertainty constantly — no one refuses insurance because the house probably won't burn. The question is never 'is it certain?' but 'what does each choice cost across the whole range?'"
    hint: "Check whether the decision actually changes anywhere inside the range — does even the LOW end demand action? And what does the waiting itself cost? An insurance analogy lands well here."
    reflectionPrompt: "When is 'wait for better data' genuinely the right call — what has to be true about the range, the thresholds, and the cost of delay?"
  - id: phy-lea-m4-03-g3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      During a public health scare, an official — wanting to project confidence —
      announces a scientific claim as certain when the honest status is 'likely, with
      open questions'. Reality later lands on the unlucky side. What is the lasting
      effect on the next emergency?
    inputConfig:
      options:
        - "None — the public forgets quickly"
        - "The credibility stock drains: the next honest 'likely' is heard as spin, hedges are read as cover-ups, and the audience that was burned shops for counsel elsewhere — trust refills only at track-record speed"
        - "Trust increases, because confidence reads as leadership"
        - "Only the individual official is affected; science's standing is separate"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The credibility stock drains: the next honest 'likely' is heard as spin, hedges are read as cover-ups, and the audience that was burned shops for counsel elsewhere — trust refills only at track-record speed"]
      rejectedFeedback: "Trust behaves like the systems lesson's stock: slow inflow (years of kept, calibrated promises), fast outflow (one confident claim that reality contradicts). And it is shared plumbing — the communication lesson's collective credibility: the official's overclaim taxes every scientist at the next podium. The defence is calibration in good times: say 'likely' when it is likely, show your hedges being honoured, and the account survives the unlucky draws."
    hint: "Model trust as a stock with flows (the systems lesson). What fills it, what drains it, and at what relative speeds? And whose account is it — the official's alone?"
    reflectionPrompt: "Recall a real public episode where calibrated honesty ('we don't know yet, here is how we'll find out') outperformed projected certainty. What made it work?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An adviser whose private values favour one energy policy is asked to brief the council on the evidence. The professional handling is..."
    options:
      - "Shade the ranges subtly toward the favoured option — everyone does it"
      - "Declare the interest, present the evidence with uncertainties at full honest width, map all options' consequences by the same standard — and let the council see the boundary being kept"
      - "Refuse the briefing — values disqualify an adviser"
      - "Present only the favoured option's evidence, since time is short"
    correctIndex: 1
    feedback: "Advisers have values — disqualification on that ground would empty every chair. The craft is transparency plus symmetry: interests declared, every option audited by the same standards, uncertainty at honest width even where it weakens the favoured case. Councils learn fastest from watching the boundary kept against the adviser's own preferences — that is what makes the next briefing believed."
  - type: MULTIPLE_CHOICE
    question: "Why does the lesson insist that respecting the public's reasoning is methodology, not manners?"
    options:
      - "Because politeness wins grants"
      - "Because people weigh evidence inside values, risks, and histories the adviser does not share — counsel that ignores this fails to inform the actual decision being made, however accurate its physics"
      - "Because the public cannot understand physics anyway"
      - "Because respect is legally required"
    correctIndex: 1
    feedback: "A farmer weighing flood defences against this year's margins, a parent weighing a vaccine inside a community's history — these are decisions under values and constraints, not failed physics exams. Counsel built for the audience's real decision (their thresholds, their stakes) informs it; the deficit-model lecture ('they'd agree if they understood') misdiagnoses disagreement about values as ignorance of facts, and loses both the argument and the trust."
---

# Hook

In 1939, the most famous physicist alive signed a letter to a president about an obscure nuclear cross-section — and the letter moved governments. In 1954, the man who had *led* the resulting project sat before a security board that stripped his clearance, in part for giving counsel his government did not want to hear. Between those two scenes lies everything this lesson teaches: physics had become a public power, and the physicist's voice — sought, feared, used, and discounted — had acquired duties no equation specifies.

You now hold the whole toolkit of those duties' *content*: audits of the possible, maps of ignorance, honest error bars, kept promises. But content is not counsel. The minister cannot read a strain curve and *should not have to*; the public weighs your projections inside lives you do not live; and the question they ask — *should we?* — is one your instruments cannot answer, because instruments weigh the world and "should" weighs goods against each other. Today: the table where science meets power — what may be said in science's voice, what must be left to the accountable, and how the trust that makes any of it matter is kept.

# Lore Introduction

The third tablet's long table stretches across the slate — chairs on both sides, one side filled. Beneath it, Vael has laid out not instruments but documents: the Deepwell funding hearing's transcript; a yellowed almanac page you recognise — the famine forecast from the communication lesson, hedges intact and fatally misread; and a single letter bearing the Guild's seal *and* a council's.

"The empty chairs are ours," she says. "They have always been ours — by invitation, suspicion, or subpoena." She lifts the letter. "The Guild's first summons to counsel, two centuries old: the river council asking whether the new locks would hold the spring floods. Our examiner answered with a number and a hedge — *they will hold to this water height, with this confidence; above it, we cannot promise.* The council built higher. The floods came to the hedge's edge and stopped. That answer's grandchildren fund this Academy."

She sets the letter beside the famine page. "And here is the other inheritance: the forecast that was honest and *useless* — hedges the council misread as confidence, with the granaries empty by spring. Honest and unheard kills as surely as dishonest and believed." From the benches, Hale speaks without being asked: "I testified three times on storm defences. The time I shaded a range to sound certain bought me one good vote and cost the tower ten years of being half-believed." Vael nods, and turns to you. "Today, Lead: the boundary between what we may say in science's voice and what we may not; uncertainty made decision-shaped; and the stock that all of it draws on. Take the chair across the table this time. We will be the council."

# Core Learning

## Concept Introduction

**The boundary: is-questions and ought-questions.** Every policy debate touching physics contains two question-kinds wearing one coat. **Science questions** — *what is the case, with what confidence*: the dose released, the warming committed, the flood height's range. These science answers with measured uncertainty, and its authority there is earned and real. **Values questions** — *what to do about it*: which risks a society accepts, who bears the costs, what trade-offs are tolerable. On these science has *no special authority* — "ought" weighs goods against each other, and instruments weigh only the world. The boundary fails in two directions, both fatal. **Smuggling:** delivering policy preferences in science's voice ("the physics says build it") — when discovered, and it is always discovered, *both* authorities are forfeited: the science becomes suspect and the values become resented. **Abdication:** hiding behind "more research is needed" when the evidence already bounds the decision — uncertainty as a place to stand instead of a thing to report. The working stance between them is the **honest broker**: deliver everything science can measure, map the options and their measured consequences symmetrically, declare your interests, and leave the choosing to those *accountable* for it — visibly, so the council learns the boundary is real.

**Uncertainty, made decision-shaped.** Decision-makers decide under uncertainty *constantly* — that is what deciding is — so counsel fails when it delivers uncertainty in laboratory form and lets the audience founder. Three crafts convert it:

- **Ranges tied to thresholds** (the communication lesson's policymaker altitude, now the core skill): not "0.4–1.2 m of flood rise" but "even our *best* case overtops the current defences — the decision is already bounded; the range only sizes the response."
- **Scenarios with likelihoods**, never single forecasts: the council can plan against a distribution; it can only gamble against a point.
- **Costs of acting versus waiting, side by side:** waiting is also a decision with a price — defences take a decade, ranges narrow slowly, and "wait for certainty" often means "decide, badly, by default."

Two fallacies to kill on sight. *"Uncertain, therefore ignore":* nobody refuses insurance because the house probably won't burn; everyday life prices uncertainty without demanding its elimination, and counsel should harness that competence, not lecture past it. *False certainty:* shaving the error bars to sound authoritative — Hale's shaded range — buys one persuasive afternoon and spends years of the account, because reality eventually publishes its own number. (And note the chaos lesson standing behind both: some uncertainties — *which* storm, *which* year — are irreducible in principle; the honest adviser says so, and redirects the decision to the attractor questions that *are* answerable.)

**Trust: the stock that funds the table.** Public and political trust behaves exactly as the systems lesson says stocks behave: **filled slowly** — years of kept promises, calibrated claims, hedges honoured by events — and **drained fast**: one confident claim that reality contradicts, one hidden uncertainty exposed, one tone of contempt. It is also **shared plumbing** (the communication lesson's collective credibility, at civilisational scale): the official who overclaims in a health scare taxes every scientist at every next podium. And the refill mechanism is the only one there is: *track record* — which is why calibration in good times ("likely" said when likely, "we don't know yet, here is how we'll find out" said plainly) is what survives the unlucky draws. Respect for the audience is part of the same machinery, and it is **methodology, not manners**: people weigh evidence inside values, risks, and histories the adviser does not share — the farmer sets flood spending against this year's margins; the parent weighs a vaccine inside a community's remembered history. The *deficit model* — "they would agree if they understood the physics" — misdiagnoses disagreement about values as ignorance of facts, and loses both the argument and the account.

**The duties, in standing order.** Counsel **true rather than comfortable** (the Oppenheimer hearing is the field's permanent reminder of the price, and of the duty's seniority to the price). Claims **sized exactly to evidence**, hedges delivered *and translated* (the almanac's lesson: honest-but-misread starves the valley too). **Interests declared**; options audited **symmetrically**; the **boundary kept** even when crossing it would win the vote. And the last duty, easiest to state and hardest to keep: **stay at the table** when the advice is unwelcome — the chair vacated in frustration is filled within the week by someone with fewer scruples about every duty above.

## Why It Matters

Physics' public questions are this century's largest: energy portfolios and grid futures (your energy-lesson audits, now before parliaments), climate response (attractor statistics briefed to treasuries), nuclear stewardship, quantum-era cryptography migration, and AI's compute governance — every one a science/values braid that someone will untangle for the council, well or badly. The trust stock is measurably under strain across nations, and the drains are precisely this lesson's list — overclaims in emergencies, hedges hidden then exposed, contempt read and returned; rebuilding runs at track-record speed, so the calibration discipline of working scientists *now* is the refill rate of the account *later*. Institutionally, science advice is a built profession — advisers, assessment bodies, national academies — staffed disproportionately by physicists because the audit-and-error-bar training transfers; your Module 2 crafts are its entry requirements, and this lesson is its operating manual. And for the capstone ahead: the commission you will write must brief power honestly to be worth anything — the boundary, the thresholds, and the stock are graded there.

## Worked Examples

**Example 1 — Splitting the minister's question.** "Should we build the reactor?" decomposes on the boundary: *science's side* — output projections, accident-risk bounds with their methodology, waste half-lives (your nuclear lessons, literally), decommissioning cost ranges, and the same audit run symmetrically on the alternatives (the gas plant's emissions, the wind portfolio's storage bill — the energy lesson's tables); *the council's side* — which risk profile their public will carry, intergenerational waste ethics, regional economics. The honest broker delivers the first side complete, maps how each option performs on each axis, and *names the second side as the council's* — out loud, in the record. The brief that decides for them is the one that ends up deciding nothing, twice.

**Example 2 — The range that already decides.** Flood counsel: "0.4 to 1.2 metres by mid-century." The council hears disagreement and reaches for "wait." The decision-shaped translation: "Note what the range *agrees* on: even 0.4 — our best case — overtops the current defences by spring tides; every scenario in the range demands raising them, and the range only sizes *how much*. Waiting costs a decade of construction lead while narrowing the range by perhaps a tenth. The choice before you is not whether but how high — and here is the cost of each increment against the residual risk it retires." Same physics, same honesty; the uncertainty has been converted from an excuse into a specification. (And where the decision *would* change across the range, say that too — that is when staged responses and monitoring triggers earn their place: the risk-register craft, lent to the council.)

**Example 3 — Two emergencies, one account.** Health scare, week one, evidence thin. *Official A:* "The risk is zero" — projecting confidence; reality lands unlucky; every subsequent "likely" from every podium is heard as spin, and the audience shops elsewhere — the stock drains in a news cycle. *Official B:* "Here is what we know, here is what we don't, here is exactly how and when we'll know more — and here is what we're doing meanwhile under that uncertainty." Clumsier on day one; *believed in month six*, because the hedges were honoured on schedule and the audience watched it happen. The systems lesson, live: B is filling the stock at inflow speed; A drained it at outflow speed; and both were drawing on plumbing shared with every scientist who speaks after them.

## Common Mistakes

- Smuggling values in science's voice — "the physics says build it" forfeits both authorities when discovered, and it is always discovered
- Abdicating behind uncertainty — "more research is needed" when the evidence already bounds the decision is a place to hide, not a finding
- Laboratory-form uncertainty — delivering ranges without thresholds, scenarios without likelihoods, and letting "you can't even agree" win by default
- Tolerating "uncertain, therefore wait" — check whether the decision changes anywhere in the range, and price the waiting; insurance reasoning is the audience's own competence — use it
- Shaving the error bars to sound certain — one persuasive afternoon against years of the account; reality publishes its own number
- The deficit model — diagnosing values disagreement as physics ignorance; the lecture loses the argument and the trust together
- Symmetry failures — auditing the disfavoured option harder than the favoured one; councils notice, and the next briefing arrives pre-discounted
- Leaving the table — counsel withdrawn in frustration is replaced, within the week, by counsel with fewer scruples; staying is the duty precisely when it is unpleasant

## Mental Model

The adviser is a *navigator on a ship the captain commands*. The navigator's authority is real and bounded: charts, soundings, weather ranges — the *is* of the sea, delivered with honest confidence ("shoals at two fathoms on this bearing; the storm track's range covers both passages"). The captain weighs what charts cannot: the cargo's worth, the crew's endurance, the owners' orders — the *ought* of the voyage. A navigator who grabs the wheel ("the charts say sail north!") is mutinying in cartography's name — and once is enough for no chart of theirs to be trusted again; a navigator who answers every heading with "the sea is uncertain" abandons the bridge while pretending to stand on it. The craft is the chart made *decision-shaped* — soundings against the keel's draught, storm ranges against each passage's lee — and the account that makes any of it matter is the log of past voyages: every sounding that proved true fills it a drop; one shoal called clear drains it by the fathom. And the navigator's last duty is the oldest: when the captain chooses the passage you warned against, you do not leave the bridge — you plot the best course through it.

## Mini Summary

- Split every policy question on the boundary: science answers *what is, with what confidence*; the accountable weigh *what to do* — smuggling values in science's voice and hiding behind "more research" are the twin failures; the honest broker serves both sides without crossing
- Make uncertainty decision-shaped: ranges tied to thresholds (check whether the decision changes anywhere in the range), scenarios with likelihoods, costs of acting versus waiting side by side — and kill "uncertain, therefore ignore" with the audience's own insurance reasoning
- Trust is a stock: filled at track-record speed by calibrated claims and honoured hedges, drained in a news cycle by overclaims and contempt — and the plumbing is shared by everyone who speaks for science after you
- The standing duties: true over comfortable, claims sized to evidence and *translated*, interests declared, options audited symmetrically, and the table never vacated in frustration

# Guided Practice Quest

Vael seats the magi along the table's far side — Selka with the budget ledger, Hale with the storm charts, old Thorne presiding — and takes the gavel herself. "The council is in session, Lead, and you hold the counsel's chair. First question — Selka, as minister: *should we build the reactor?* Serve the boundary from both sides and show us where our decision begins. Second — my own old wound: the flood range that 'can't even agree' — convert our uncertainty into our specification before Hale votes to wait. Third — the scare: certainty would calm the square tonight and cost the account for a decade; speak the sentence that is clumsy now and believed in month six. The empty chair is yours from today, Lead. Fill it so it stays worth filling."

# Solo Practice Quest

Write a counsel brief for a real policy question that physics informs (350–500 words) — an energy choice, flood or climate response, cryptography migration, research-budget allocation, or another of your choosing. Structure it on the boundary: first, the *is* — what is known, with honest uncertainties, options audited symmetrically by the same standards (cite which of your tier's audits you are deploying); then the *decision-shaping* — ranges tied to the decider's actual thresholds, scenarios with likelihoods, the cost of waiting priced beside the cost of acting, and an explicit check of whether the decision changes anywhere inside the range; then the *ought*, named and handed over — one paragraph stating precisely which values trade-offs belong to the accountable chooser, not to you. Declare your own interests where they bear. Close with the sentence you would say if the council chooses against your counsel — the one that keeps you at the table.

# Integration

**Mathematics:** Decision theory is this lesson's formal engine — expected costs across scenario distributions, value-of-information calculations that price "wait for better data" honestly, and threshold analysis that locates where, inside a range, a decision actually flips. Calibration itself is measurable: forecasters are scored on whether their "70% likely" events happen 70% of the time, and the scoring rules (proper scoring, Brier) make "trust at track-record speed" a literal statistic.

**Engineering:** Engineering meets the public square through codes, standards, and siting decisions — the flood defence's design height *is* a range-to-threshold conversion with a society's risk tolerance built in — and its regulatory practice (safety cases, environmental impact statements, public inquiries) is the honest-broker stance proceduralised. The profession's hardest-won lesson matches this one's: Challenger's engineers had the *is* right and lost the *table* — which is why engineering ethics now teaches staying in the room, escalating honestly, and putting the dissent in the record.

# Lore Conclusion

The session runs until the lamps gutter, and when Vael finally sets down the gavel, old Thorne does something the hall has not seen: he rises from the council's side, crosses to the counsel's chair — your chair — and rests his hand on its back.

"I sat here for the locks hearing, as a boy carrying my master's charts," he says. "The number and the hedge. Two centuries of this Academy stand on the council believing us that morning." He looks at you. "Keep the account."

Vael gathers the documents — the transcript, the almanac page, the sealed letter — and uncovers the ring's final tablet as the magi begin to depart. On it: a map of the valley you have never seen drawn this way — not rivers and granaries but *flows of another kind*: ideas moving from benches to forges to markets, instruments becoming industries, and at the margins, blank regions marked in the Guild's oldest hand. "One lesson remains before the Guild's last question," she says. "You can audit the possible, fund the blank pages, lead the builders, keep the promises, and counsel the powerful. Tomorrow we assemble all of it into the craft of *choosing what to build next* — how discoveries become capabilities, why some nations and ages turn knowledge into plenty while others shelve it, and what a portfolio of futures looks like when a physicist designs it honestly. *Innovation Strategy*, Lead. Then the chair, the hall, and the question are yours."
