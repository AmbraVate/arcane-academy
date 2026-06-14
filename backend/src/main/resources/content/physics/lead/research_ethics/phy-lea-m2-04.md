---
id: phy-lea-m2-04
domainId: physics
tier: LEAD
moduleId: phy-lea-m2
moduleTitle: "Module 2: Research Physics"
moduleGlyph: "🧪"
moduleSortOrder: 2
topicSlug: research_ethics
topicTitle: "Research Ethics"
topicSortOrder: 4
title: "Research Ethics: Integrity, Credit, and Consequence"
sortOrder: 4
xpReward: 150
practiceType: NONE
questType: MASTERY
feynmanPrompt: "Explain to a senior student the three cardinal sins of research misconduct, why authorship and credit disputes corrode science, and what responsibilities a physicist carries for the uses of their discoveries."
learningObjectives:
  - Define fabrication, falsification, and plagiarism, and explain how incentive structures and rationalisation produce misconduct in otherwise able scientists
  - Apply fair-credit principles: authorship criteria, acknowledgement of contributions, and the treatment of students and rivals
  - Reason about consequence responsibilities: dual-use research, the physicist's role in weapons history, and the duty of honest public counsel
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines the three cardinal sins precisely — fabrication (inventing data), falsification (manipulating data/results), plagiarism (taking words or ideas without credit) — and distinguishes them from honest error"
    - "Analyses how misconduct happens: incentive pressure (publish-or-perish, priority races) plus incremental rationalisation, and why early small corrections prevent late catastrophes"
    - "Applies credit principles: authorship reflecting genuine intellectual contribution, the Matilda/Matthew problems (credit flowing to the famous, away from juniors), and the handling of the Franklin case or similar"
    - "Reasons soundly about consequences: dual-use dilemmas, the Manhattan Project's legacy and physicists' responses, and the working duties — disclosure, honest counsel, refusal where lines are crossed"
  keywords: [fabrication, falsification, plagiarism, authorship, credit, dual-use, responsibility, integrity]
  modelAnswer: |
    Research misconduct has three cardinal forms. Fabrication: inventing data that was
    never measured. Falsification: manipulating real data — trimming inconvenient
    points, doctoring images, adjusting until significance appears — so the record no
    longer reflects what happened. Plagiarism: presenting others' words, data, or ideas
    as one's own. All three differ from honest error in one respect: intent to deceive.
    Honest error corrected promptly is science working; deception is science's
    immune-system disease, because the entire edifice — peer review, replication,
    citation — runs on the presumption that the record reflects reality.

    The disturbing lesson of the famous cases (Schön's fabricated transistors among
    them) is that perpetrators are usually able scientists under ordinary pressures:
    priority races, funding deadlines, the publish-or-perish ratchet. The descent is
    incremental — smooth one outlier, then one curve, then one dataset — each step
    rationalised as temporary, until retreat costs more than continuation. The
    practical defences are therefore boring and early: keep raw data immutable and
    shared, welcome the lab habits (notebooks, witnesses, blind analysis) that make
    small dishonesties impossible, and normalise prompt correction so that error never
    needs to become deception.

    Credit is ethics' second front. Authorship should reflect genuine intellectual
    contribution — design, execution, analysis, writing — not seniority, courtesy, or
    leverage; everyone who meets the bar belongs on the paper, and no one who doesn't.
    The known pathologies: credit flows toward the already-famous and away from
    juniors and the overlooked — Rosalind Franklin's diffraction data underpinning a
    structure announced by others remains the canonical case. The leader's duties are
    concrete: state authorship rules before work begins, acknowledge specifically,
    and spend one's own standing to make juniors' contributions visible.

    The third front has no clean instrument: consequences. Physics built the bomb;
    the generation that built it split lastingly over what they owed the world after —
    from Oppenheimer's anguish to the Russell-Einstein manifesto to Pugwash. The
    working duties that emerged: think ahead of the work (dual-use review before, not
    after), disclose honestly to those who must decide, refuse participation where
    one's own line is crossed, and give the public counsel that is true rather than
    comfortable. The knowledge, once made, cannot be unmade; the physicist's
    responsibility is to ensure it arrives accompanied by honest understanding.
guidedSteps:
  - id: phy-lea-m2-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Classify each act. (A) A researcher deletes three data points that spoil her fit,
      with no documented cause, and publishes the rest. (B) A researcher publishes a
      result that later proves wrong because of a subtle calibration error he had no
      way to detect; on discovery, he promptly publishes a correction.
    inputConfig:
      options:
        - "A is falsification (manipulating the record); B is honest error handled correctly — not misconduct at all"
        - "Both are misconduct"
        - "Neither is misconduct"
        - "A is acceptable data cleaning; B is misconduct because the result was wrong"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A is falsification (manipulating the record); B is honest error handled correctly — not misconduct at all"]
      rejectedFeedback: "The line is intent and transparency, not correctness. Undocumented deletion of inconvenient points makes the record lie about what was measured — falsification (the Senior outlier rule, now with its ethical teeth showing). Being wrong with honest method and prompt correction is science operating normally; some of the field's most valuable papers are corrections."
    hint: "Recall the Senior outlier procedure: investigate, remove only with documented cause. Which case violates it? And is 'turned out wrong' the same as 'lied'?"
    reflectionPrompt: "Why would a culture that punishes honest error as harshly as fraud end up producing MORE fraud?"
  - id: phy-lea-m2-04-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A doctoral student conceives a key insight, builds the apparatus, takes the data,
      and drafts the paper. The laboratory's famous director, who secured the funding
      and gave occasional advice, expects first authorship by custom, and the student
      fears objecting.

      In one or two sentences: what does fair credit require here, and what duty does
      the senior scientist specifically bear?
    inputConfig:
      placeholder: "What does fair credit require, and whose duty is it?"
    markingRule:
      matchMode: CONTAINS
      accepted: ["student", "contribut", "first author", "credit"]
      rejectedFeedback: "Authorship follows intellectual contribution: the student conceived, executed, analysed, and drafted — first authorship is theirs; funding and occasional advice merit acknowledgement or, at most, junior co-authorship by the field's standards. The specific duty is the director's: power decides whether fair rules are real, so the senior scientist must enforce the rule against their own interest — and the best ones state the rules before the work begins."
    hint: "List who did what against the criteria for authorship: conception, execution, analysis, writing. Then ask: in a power imbalance, who is able to make fairness actually happen?"
    reflectionPrompt: "Why is 'agree authorship rules before the work starts' so much more effective than adjudicating afterwards?"
  - id: phy-lea-m2-04-g3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Your laboratory's new technique for isotope separation would make medical isotopes
      dramatically cheaper — and would also lower a barrier to weapons proliferation.
      Publication is being decided. Which response reflects the working ethics this
      lesson teaches?
    inputConfig:
      options:
        - "Publish everything immediately — knowledge is always neutral and consequences are someone else's department"
        - "Engage the dual-use question before acting: assess the risk honestly, consult the structures that exist for this, consider what to publish at what detail, and accept that the decision is part of the science"
        - "Suppress the work entirely and tell no one, forever"
        - "Publish, but privately hope no one misuses it"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Engage the dual-use question before acting: assess the risk honestly, consult the structures that exist for this, consider what to publish at what detail, and accept that the decision is part of the science"]
      rejectedFeedback: "Both extremes abdicate. 'Knowledge is neutral' ignores that the physicist is often the only person positioned to foresee the risk in time; total suppression is usually futile (others will find it) and forfeits the benefit. The working duty is engagement: honest risk assessment, use of review structures, calibrated disclosure — and ownership of the decision as part of the work, not an externality."
    hint: "The Manhattan generation's hard-won conclusion was neither 'never build' nor 'just build'. What did they actually institute — review, counsel, restraint structures — and when must such thinking happen relative to the work?"
    reflectionPrompt: "Who is better placed than the discovering physicist to raise a dual-use alarm early — and what follows from the answer?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The Schön affair — a celebrated physicist fabricating dozens of breakthrough results before exposure — most clearly teaches that..."
    options:
      - "Fraud is committed only by incompetents who cannot do real science"
      - "Brilliance is no vaccine: incentive pressure plus incremental rationalisation can corrupt able scientists, so integrity must be built into systems (raw-data norms, replication, notebooks), not just character"
      - "Peer review reliably catches fabrication"
      - "Physics is uniquely fraudulent among the sciences"
    correctIndex: 1
    feedback: "Schön was talented and productive; review praised the work; exposure came when outside researchers noticed duplicated noise across 'different' experiments — the raw record finally consulted. The lesson the field institutionalised: design systems where small dishonesties are impossible and raw data is inspectable, because pressure is universal and rationalisation is incremental."
  - type: MULTIPLE_CHOICE
    question: "After Hiroshima, many Manhattan Project physicists devoted themselves to arms control, science advice, and public counsel (Pugwash, the Bulletin of the Atomic Scientists). Their position was that..."
    options:
      - "Physicists bear no responsibility once equations leave the blackboard"
      - "Having made the knowledge, they owed the world honest counsel about it: consequence-responsibility continues after discovery"
      - "The bomb should have been kept secret from elected governments"
      - "Physics should have stopped studying the nucleus"
    correctIndex: 1
    feedback: "The builders' own conclusion: the knowledge could not be unmade, so the duty became lifelong honest counsel — informing publics and governments, building restraint institutions, refusing comfortable lies. It is the founding precedent for today's dual-use review in every field from nuclear physics to AI."
---

# Hook

Jan Hendrik Schön published a breakthrough roughly every eight days at the world's most storied physics laboratory. Organic lasers, molecular transistors, superconductivity records — Nature and Science could not print him fast enough, and prize committees circled. In 2002 a physicist comparing two of his papers noticed something impossible: *identical noise*. Different experiments, different materials, the same random wiggles, point for point. Noise — as your whole Senior tier taught you — is the one thing that never repeats.

He had been drawing the data. Twenty-eight papers retracted; doctorate revoked; collaborators' careers scarred; years of other people's lives spent failing to replicate fictions. And the question that keeps the case in every Guild file is not *how could he* — it is that he was able, trained, and under exactly the pressures every researcher knows. Integrity, it turns out, is not a personality trait. It is a *system* — built from boring habits, fair rules, and the willingness to think about consequences before they arrive. Today's tablet is that system.

# Lore Introduction

The fourth tablet's column of names runs floor-high: the Guild's roll of researchers, generations deep. Most bear the small sigil of honourable completion. A few — Vael does not hurry past them — are struck through in red.

"Read the red ones' files someday," she says. "You will find no monsters. You will find able people, under deadline, in priority races, who moved one data point with a good rationalisation — and then were the kind of person who had moved one, which made the second easier." She unlocks the case beneath the tablet. Inside, three exhibits: a laboratory notebook with a page razored out, its stub still in the binding; a letter, never sent, in which — she tells you — a junior researcher drafted and re-drafted an objection to her own name's absence from a famous result; and a small lead casket you recognise from Selka's nuclear lessons, empty now, its sample long decayed.

"Three fronts, Lead. The lies told to the record. The credit taken from its earners. And the consequences that arrive after the discovery is loose in the world — the front with no clean instrument, where the Guild's oath was written." She sets a fresh slate before you, and for the first time since you entered the Frontier Hall, she sits. "This lesson I do not lecture. We reason together. Begin with the simplest question that has ever ended a career: *what actually happened at the bench?*"

# Core Learning

## Concept Introduction

**Front one — the record: fabrication, falsification, plagiarism.** Research misconduct has three cardinal forms, and one bright line separates all three from honest error: **intent to deceive**.

- **Fabrication:** inventing data never measured — Schön's drawn curves.
- **Falsification:** manipulating real data until the record lies — undocumented deletion of inconvenient points (the Senior outlier rule, with its ethical teeth showing), doctored images, analyses tuned until significance appears and only the winning tune reported (the forking-path garden, *walked knowingly*).
- **Plagiarism:** taking words, data, or ideas without credit — theft of the one currency science mints.

Honest error — being wrong with sound method, correcting promptly on discovery — is *science working*; a culture that punishes it like fraud manufactures concealment, and concealment is fraud's nursery. The mechanism of real misconduct is rarely a single monstrous decision: it is **incentive pressure** (publish-or-perish, priority races, funding cliffs) compounding with **incremental rationalisation** — smooth one outlier "temporarily," then a curve, then a dataset, each step smaller than the cost of confessing the last. The defences, accordingly, are boring, early, and systemic: immutable raw data, lab notebooks that cannot lose pages quietly, blind analysis (yesterday's envelope — bias-proofing doubles as fraud-proofing), replication norms, and a leader's culture where the words *"I was wrong"* are demonstrably survivable.

**Front two — credit: whose name, and who enforces it.** Science's salary is credit, and its distribution is an ethical act. The sound rule: **authorship reflects genuine intellectual contribution** — conception, execution, analysis, writing — not seniority, courtesy, funding leverage, or fear. Everyone meeting the bar belongs on the paper; no one else does. The known pathologies have names: the *Matthew effect* (credit flows to the already-famous — the celebrated director "first-authoring" a student's insight) and its mirror, the *Matilda effect* (contributions of the overlooked — historically, conspicuously, women — written out): Rosalind Franklin's Photo 51, load-bearing in the structure of DNA and acknowledged mostly posthumously, is the canonical file. Because credit disputes are decided by power, the binding duties fall on the powerful: **state authorship rules before work begins** (afterwards, every party is biased), acknowledge specifically rather than generically, and spend personal standing to make juniors' contributions visible. How a leader divides credit is the fastest assay of their character the field possesses.

**Front three — consequence: the front with no clean instrument.** Physics carries a unique historical weight: its twentieth-century triumph built the weapon that ended one war and shadowed every year since. The Manhattan generation split over what they owed the world afterward — Oppenheimer's anguish, the Franck Report's unheeded plea, the Russell–Einstein manifesto, Pugwash, the Bulletin's clock — but converged on a working consensus that became the field's inheritance: *knowledge, once made, cannot be unmade; the maker's responsibility therefore continues after discovery.* Its working duties:

- **Think ahead of the work:** dual-use assessment *before* the capability exists, because the discovering physicist is usually the only person positioned to foresee the risk in time (your nuclear lesson's closing reflection, now operational).
- **Calibrated disclosure:** between the abdications of "knowledge is neutral, publish everything" and "suppress forever" lies the real decision space — what to publish, at what detail, through what review — owned as *part of the science*, not an externality.
- **Honest counsel:** to publics and governments, the truth with its uncertainties (the communication lesson's policymaker craft, at its highest stakes) rather than the comfortable or the fundable.
- **The personal line:** know in advance what work you will refuse, because the moment of refusal is the worst moment to start deciding.

## Why It Matters

Integrity failures are not rare curiosities: surveys across the sciences find a small percentage admitting data manipulation and a large fraction reporting questionable practices, and the replication crisis is partly this lesson's first front at statistical scale. The institutional response — research-integrity offices, data-availability mandates, pre-registration, authorship taxonomies (CRediT) — is the system you will work inside and, soon, *administer*: Module 4's leadership lessons inherit every duty named today, because labs acquire their ethics from their leaders' visible behaviour under pressure, not from posted codes. The consequence front is the era's live one: AI capability research, gain-of-function biology, autonomous weapons, and climate intervention all replay the dual-use structure, and physics' century of hard-bought precedent — early review, calibrated disclosure, standing counsel institutions — is the most developed playbook any field possesses. You will be asked, in your career, to apply it: the only question is whether you will have rehearsed.

## Worked Examples

**Example 1 — Anatomy of a descent.** A postdoc's third dataset shows the effect weakening. Grant renewal is in eight weeks. Step one: she excludes a "clearly anomalous" run — undocumented, but the cause *felt* obvious. Step two, a month later: an image panel is "cleaned" for the paper. Step three: a fourth dataset is thin, and the second's points are "representative" enough to stand in. No single step felt like the crime; the first created a record that step two had to protect, and the second a record step three had to protect. The systemic counterfactual: a lab where raw data is deposited immutably on collection (step one impossible quietly), where the PI's own corrections are public lab lore (confession cheap), and where the renewal pressure is discussed openly (the rationalisation named before it is needed). Same person, same pressure, no descent — *integrity as architecture*.

**Example 2 — Credit, decided in advance.** A four-person collaboration — student (insight, apparatus, data), postdoc (analysis pipeline), director (funding, weekly steering), and a visiting theorist (one crucial calculation) — agrees in week one: the student leads and first-authors; the postdoc and theorist co-author with contributions itemised; the director, by her own ruling, takes last-author position and writes the contributions statement herself. When the result becomes famous and a journalist calls *the director*, she redirects the interview to the student by name. Nothing in the physics required any of this; everything in the field's health did. The expensive version of this example — rules unstated, fame arriving first — is in the Guild's case under a letter that was never sent.

**Example 3 — A dual-use decision, walked through.** A simulation group discovers their plasma-instability code, built for fusion energy, sharply improves a weapons-relevant calculation. Abdication one: publish all, "we just do physics." Abdication two: bury it, telling no one — forfeiting the fusion benefit while rivals reach the same result within two years. The engaged path actually taken (a composite of real cases): the group flags the issue *before* submission; institutional and national review structures are consulted; the method is published with the energy-relevant validation cases while the weapons-relevant regime's parameters are withheld pending review; and the group's lead accepts a standing advisory role — counsel continuing after discovery, the Manhattan generation's precedent at working scale. Imperfect, debatable, *owned* — which is the standard the front permits.

## Common Mistakes

- Equating "wrong" with "dishonest" — honest error promptly corrected is science working; the bright line is intent to deceive, and cultures that blur it breed concealment
- Believing fraud is for the incompetent — Schön was able and praised; pressure plus incremental rationalisation is the actual mechanism, and brilliance is no vaccine
- Trusting character instead of architecture — notebooks, immutable raw data, blind analysis, and replication norms make small dishonesties impossible; posted ethics codes alone make them merely awkward
- Settling authorship after the result — once stakes are visible every party is biased; rules stated before work begins are the only fair court
- Generic acknowledgement as credit's small change — "we thank X for assistance" can bury a load-bearing contribution; specificity is the honest denomination
- "Knowledge is neutral" as a thought-terminator — the discoverer is usually the only early-warning system; neutrality talk outsources a duty that cannot in fact be outsourced
- Total suppression as the safe harbour — usually futile, always benefit-forfeiting; calibrated disclosure through review is the working alternative
- Deciding your refusal line during the crisis — the worst possible moment; the line is drawn in advance or it is drawn for you

## Mental Model

Think of science's integrity as a currency system — the extension of the communication lesson's credibility-coin. *Fabrication and falsification* are counterfeiting: each fake note spends once, and its discovery devalues every note from that mint — collaborators, journal, field. *Plagiarism* is pickpocketing the mint's workers: the coin is real, the pocket is wrong. *Credit practice* is the payroll: a mint whose smiths are paid for others' work loses its smiths, then its coin. And *consequence ethics* is the question no currency metaphor survives without: what the money buys. A counterfeit-free, fairly-paid mint that funds catastrophes has failed at the only level that finally matters — which is why the Guild's oath is not "never err" (impossible) nor "never be wrong" (undesirable) but the three workable clauses: *keep the record true, pay credit where it was earned, and stay in the room where the consequences are decided.*

## Mini Summary

- The cardinal sins — fabrication, falsification, plagiarism — share intent to deceive; honest error promptly corrected is science working, and punishing it like fraud manufactures fraud
- Misconduct's mechanism is pressure plus incremental rationalisation in able people; the defences are architectural — immutable raw data, notebooks, blind analysis, replication, survivable confession
- Credit follows genuine intellectual contribution, with rules set before work begins; the powerful bear the enforcement duty, against their own interest (Matthew and Matilda are the named failure modes)
- Consequence responsibility continues after discovery: dual-use review before the capability, calibrated disclosure over both abdications, honest counsel for life, and a refusal line drawn in advance

# Guided Practice Quest

Vael remains seated, the three exhibits between you. "We reason together, as promised. First: two researchers — one deletes what spoils her fit, one publishes a wrong result and corrects it — separate them with the only line that matters, and tell me which one science can survive. Second: the famous director and the student who did the work — assign the names fairly, then assign the *duty*, and notice they land on different people. Third: the isotope technique that heals and proliferates — chart the path between the two abdications, and own every step aloud. Then read the red names once more, Lead. Not one of them planned to be there. That is the entire lesson."

# Solo Practice Quest

Write your own integrity protocol (350–500 words) — the document you will actually run a research group by. Cover the three fronts. The record: your lab's concrete architecture against fabrication and falsification — data handling, notebooks, correction culture — and how you will make "I was wrong" demonstrably survivable. Credit: your authorship rules, stated as you would state them on a student's first day, including how disputes are settled and what you, as the senior name, commit to enforcing against your own interest. Consequence: identify the most plausible dual-use or downstream risk in your own intended field, the review you would seek before publication, and — in one honest sentence each — the counsel you would owe the public, and the line you would refuse to cross. Close with your answer to the question the red names pose: which incremental first step would be *your* most likely one, and what architecture will you build so it is never available?

# Integration

**Mathematics:** Statistics is integrity's forensic arm — Benford's law and digit-pattern analysis catch fabricated numbers, duplicated-noise detection unmade Schön, and the multiple-comparisons mathematics defines exactly where exploratory analysis ends and falsification-by-tuning begins. Mathematics also supplies ethics' cleanest credit norm: the Hardy–Littlewood rules and the alphabetical-authorship tradition, an existence proof that fields can choose fairness by convention.

**Engineering:** Engineering ethics arrived earlier and harder — bridges fall on the public, so the profession built licensure, codes with legal force, and the duty-to-report that makes an engineer personally liable for silence (Challenger's inquiry is taught exactly as this lesson teaches Schön). Dual-use governance is now a working engineering discipline too: export controls, responsible-disclosure norms in security research, and safety review boards are the consequence front, proceduralised.

# Lore Conclusion

Vael rises at last and takes up the slate where your protocol is drafted. She reads it once, and sets it — not in the case with the exhibits — but on the tablet's ledge, beneath the column of names.

"Module 2 is complete," she says. "You can choose a question that deserves years, make it understood at every altitude, measure what barely exists without deceiving yourself, and hold the record, the credit, and the consequences honestly. The Guild calls that a *researcher*. It is the second of the three things this tier makes." She walks to the third ring of tablets and draws the dust-sheet from the first: on it, chalked, a sun — and beneath the sun, a hearth, a forge, and a city's worth of small fires.

"The third thing needs the other two, which is why it waits until now. Knowledge that stays knowledge is a library, Lead. The frontier's other duty is to *build* — and the building most demanded of physics in your lifetime has one name on every council's lips." She taps the chalked sun. "Module 3: *Physics Innovation.* First lesson — *Energy Technologies*: what the laws you mastered actually permit for powering a civilisation, and how a physicist tells the possible from the merely promised. Bring your thermodynamics. The accountants are waiting."
