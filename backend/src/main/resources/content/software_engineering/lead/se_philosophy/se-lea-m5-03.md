---
id: se-lea-m5-03
school: engineering
domainId: software_engineering
tier: LEAD
moduleId: se-lea-m5
moduleTitle: "Module 5: Multidisciplinary Integration"
moduleGlyph: "🌌"
moduleSortOrder: 5
topicSlug: se_philosophy
topicTitle: "SE + Philosophy"
topicSortOrder: 3
lesson: se_plus_philosophy
title: "SE + Philosophy: Ethics of Automation"
sortOrder: 3
difficulty: 5
estimatedMinutes: 42
xpReward: 80
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se_plus_economics]
integrationDomains: [philosophy, ethics]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - "Applies at least two ethical frameworks (deontological, consequentialist, virtue) to an engineering decision"
    - "Analyses algorithmic bias with a concrete example and proposes a mitigation"
    - "Discusses the concept of engineering responsibility (who is liable when code causes harm)"
    - "Takes a defensible position on when an engineer should refuse to build something"
    - "Connects GDPR Article 22 (right to explanation) to software design requirements"
  keywords: [ethics, deontological, consequentialist, virtue, bias, fairness, liability, responsibility, refuse, gdpr]
  modelAnswer: |
    Ethical frameworks applied to a hiring algorithm:
    
    Consequentialist (utilitarianism): does it produce better hiring outcomes overall?
    Measures: accuracy, fairness metrics, time-to-hire.
    Problem: optimising for majority outcomes can systematically harm minorities.
    
    Deontological (Kant): does it respect candidates as rational agents?
    Right to explanation (GDPR Article 22): automated decisions affecting people
    require human oversight and explanability. Using a black-box model violates this.
    
    Virtue ethics (Aristotle): what would a virtuous engineer do?
    Build the algorithm that you'd be comfortable defending in front of those it affects.
    
    Algorithmic bias: a loan algorithm trained on historical data inherits historical
    discrimination. Even without protected characteristics as inputs, proxies
    (zip code, employment history) can encode discrimination.
    Mitigation: fairness constraints in training, disparate impact analysis, human oversight.
    
    Engineering responsibility: "just following orders" is insufficient.
    ACM Code of Ethics: engineers have professional responsibility to avoid harm.
    When to refuse: when the harm is clear, significant, and not addressable by design changes.
    
    GDPR Article 22 design requirement: algorithmic decisions must be explainable.
    Interpretable models (decision trees, logistic regression) or post-hoc explanation
    (SHAP values) must be included in system design.
guidedSteps:
  - id: phi-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A social media platform's recommendation algorithm maximises engagement. Research
      shows it also amplifies radicalising content because radicalising content drives
      high engagement. Using a consequentialist framework, evaluate this.
    inputConfig:
      options:
        - "It's fine — the algorithm is doing exactly what it was designed to do (maximise engagement)"
        - "Consequentialism requires evaluating all consequences — including radicalisation — and the harm to social cohesion likely outweighs the engagement benefit"
        - "Engineers are not responsible for how people use the platform"
        - "This is a product decision, not an engineering decision"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Consequentialism requires evaluating all consequences — including radicalisation — and the harm to social cohesion likely outweighs the engagement benefit"]
      rejectedFeedback: "Consequentialism evaluates ALL consequences, not just intended ones. Maximising engagement while ignoring radicalisation is not consequentialist analysis — it's measuring one metric and ignoring others. True consequentialist analysis includes: social harm, erosion of democratic discourse, individual radicalisation costs, and long-term platform trust damage."
    hint: "Consequentialism evaluates total outcomes. Are 'total outcomes' just engagement metrics?"
    reflectionPrompt: "The algorithm designer's fallacy: 'the algorithm is neutral; it just optimises what we told it to.' But what you choose to optimise is itself an ethical decision. Choosing engagement as the sole objective is a value judgment with consequences."
  - id: phi-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A face recognition system has 99.5% accuracy overall but 90% accuracy for Black women (compared to 99.8% for white men). The business team says "99.5% accuracy is excellent for any use case." Analyse this from both consequentialist and deontological perspectives.
    inputConfig:
      minWords: 60
    markingRule:
      matchMode: CONTAINS
      accepted: [disparate, impact, group, harm, kant, right, treat, person, fairness, distribution, justice, consequentialist]
      rejectedFeedback: "Consequentialist: the aggregate accuracy obscures distributional harm. The system produces systematically worse outcomes for Black women specifically. Total utility maximisation that accepts higher harm to already-disadvantaged groups fails a Rawlsian fairness test. Deontological: the system treats people differently based on race/gender — violating Kant's categorical imperative (treat all persons equally). GDPR Article 22 implications: automated decisions affecting protected characteristics require human oversight and must not discriminate."
    hint: "Think about aggregate vs distributed impacts. Think about who bears the cost of the 10% error rate vs the 0.2% error rate."
    reflectionPrompt: "Algorithmic fairness has multiple formal definitions (demographic parity, equalised odds, predictive parity) that can be mathematically incompatible with each other. There is no purely technical solution — fairness is a value judgment about which groups and which errors matter."
  - id: phi-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      An engineer is asked to build a system that will be used to determine parole decisions using an algorithmic risk score. The engineer has serious concerns about algorithmic bias. Describe their ethical obligations using at least two different ethical frameworks.
    inputConfig:
      minWords: 60
    markingRule:
      matchMode: CONTAINS
      accepted: [obligation, refuse, escalate, consequentialist, deontological, virtue, harm, bias, professional, responsibility]
      rejectedFeedback: "Obligations: (1) Consequentialist: investigate whether the system will produce net benefit or harm. If bias analysis shows it discriminates against protected groups in criminal justice (where errors have severe consequences — wrongful imprisonment), the consequentialist analysis likely recommends not building or significant redesign. (2) Deontological: professional code of ethics (ACM) requires engineers to avoid harm. 'Just following orders' is insufficient; engineers have professional responsibility for what they build. (3) Virtue: would a virtuous engineer build this? What would you tell the people whose parole it affects?"
    hint: "What does each framework say about your obligations when you believe harm will result? Is 'my employer asked me to' sufficient ethical cover?"
    reflectionPrompt: "Engineers are not morally neutral tool-builders. The ACM Code of Ethics explicitly states: engineers should act in the public interest and avoid harm. Professional identity includes professional responsibility. 'I was just following requirements' is the engineering equivalent of 'I was just following orders.'"

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "GDPR Article 22 requires that automated decisions affecting individuals must provide what capability?"
    options:
      - "Human review and the ability to provide an explanation"
      - "A 99.9% accuracy guarantee"
      - "Open-source publication of the algorithm"
      - "User consent before any automated decision"
    correctIndex: 0
    feedback: "GDPR Article 22: automated decisions producing significant effects on individuals (credit, hiring, insurance) must allow: (1) human intervention/oversight, (2) the individual to contest the decision, (3) an explanation of the reasoning. This is a legal design requirement with direct architectural implications (interpretable models, audit trails)."
  - type: MULTIPLE_CHOICE
    question: "Virtue ethics asks: 'What would a virtuous person do?' In engineering ethics, what question does this translate to?"
    options:
      - "What maximises productivity?"
      - "What would you be comfortable explaining and defending to the people affected by this system?"
      - "What does your manager think is right?"
      - "What is legally required?"
    correctIndex: 1
    feedback: "Virtue ethics applied to engineering: build systems you'd be comfortable defending to those they affect. If you wouldn't be comfortable explaining the system's decision-making to someone denied a loan or wrongly flagged as a fraud risk, the system may not meet the virtue ethics standard. This 'transparency test' is a practical ethical heuristic."

retrieval:
  recall: "Describe three ethical frameworks (consequentialist, deontological, virtue). How do they produce different conclusions for the same engineering decision?"
  explain: "Explain to a business stakeholder why an algorithm with 'high overall accuracy' can still be ethically problematic if the error rate is not uniformly distributed."
  mistakeId:
    code: |
      // Engineering team discussion about content moderation system:
      "Our content moderation algorithm flags 95% of harmful content.
       It also incorrectly flags 8% of content from non-English speakers.
       Business has approved it. Legal has cleared it. We should just build it."
    answer: "Legal clearance ≠ ethical clearance. The 8% false positive rate for non-English speakers is disparate impact — systematically silencing one linguistic group more than others. Questions to ask: who bears the cost of false positives (suppressed speech)? Does this comply with EU AI Act requirements for high-risk systems? Would this withstand scrutiny if published? Building it because 'legal cleared it' ignores professional engineering responsibility for the system's impact."
---

# Hook

The algorithm has no intent. It optimises what it's told to optimise. When a loan algorithm denies loans to applicants from certain zip codes, the algorithm isn't racist — it's statistical. When a content recommendation engine amplifies extremist content, the algorithm isn't radicalist — it's engagement-maximising.

But intent is not the standard by which software is judged when it causes harm. Effect is.

Engineers who build these systems are not morally neutral tool-builders. They are participants in systems that affect real people's lives. Understanding the ethical dimensions is not optional — it's professional responsibility.

> Have you ever built something that you later questioned the ethics of? What made it questionable, and what, if anything, did you do?

# Lore Introduction

The Academy's Ethics Council convenes whenever a new enchantment class is proposed that could affect non-mages. The council applies multiple frameworks: what outcomes will it produce? Does it respect all people's inherent dignity? What would the wisest artificers in history have done?

*"There is no enchantment whose effects are confined to those who chose it,"* Archmage Veylan says. *"Every powerful system touches those who did not ask to be touched by it. That is where the ethical weight lives."*

# Core Learning

## Concept Introduction

Three major ethical frameworks applied to engineering:

**Consequentialism (Utilitarian):**
The right action maximises total welfare. Judge by outcomes, not intentions.
- Ask: what are all the consequences? Who is helped? Who is harmed? At what magnitudes?
- Critique: can justify harming minorities for majority benefit; depends on what you measure

**Deontological Ethics (Kantian):**
Some actions are right or wrong regardless of consequences. People must be treated as ends, not means.
- Categorical Imperative: act only according to rules you could universalise
- In practice: rights-based analysis; GDPR Article 22 right to explanation
- Critique: can conflict with common-sense outcomes

**Virtue Ethics (Aristotelian):**
What would a person of good character do? Focus on the agent, not just the action.
- Practical question: "Would I be comfortable explaining this to those affected?"
- Community of practice: what are the professional virtues of good engineering?

## Why It Matters

- Algorithmic decisions affect millions of people — scale amplifies both good and harm
- Engineers have professional responsibility (ACM Code of Ethics) beyond "I was told to"
- Regulatory requirements (GDPR, EU AI Act) make ethics a legal compliance concern
- Trust erosion from unethical AI is an organisational and industry-wide risk
- "This is a product decision" is insufficient — engineers shape what gets built

## Worked Examples

**Algorithmic bias case (COMPAS risk scoring):**
```
COMPAS: criminal recidivism risk scoring used in US parole decisions.
ProPublica (2016) found: Black defendants falsely flagged as high-risk 
at nearly 2× the rate of white defendants.

Consequentialist analysis: if accuracy is equal overall but error distribution
is unequal, the system systematically disadvantages one group.

Deontological analysis: automated parole decisions without adequate 
explanation or human oversight may violate rights to due process.

Virtue analysis: would you be comfortable explaining a risk score 
denial to the person whose freedom depends on it?
```

**GDPR Article 22 design requirements:**
```java
// What GDPR requires for automated decisions:
1. Human oversight mechanism (not fully automated for significant decisions)
2. Ability for individuals to contest decisions
3. Explanation capability (not just accuracy)

// Architectural implications:
- Audit trail for every automated decision
- Interpretable model or post-hoc explanation (SHAP, LIME)
- Human review interface for contested decisions
- Retention policy for decision records (contestation window)
```

## Common Mistakes

- **Consequentialism narrowly applied** — measuring only intended metrics, ignoring other consequences.
- **"Legal = ethical"** — legal clearance is necessary but insufficient; the bar for ethics is higher.
- **Algorithmic fairness as purely technical** — fairness is a value judgment; different definitions are mathematically incompatible.
- **Professional responsibility deflection** — "I just built what I was asked" doesn't eliminate professional responsibility.
- **Ignoring scale** — an ethical small decision can be unethical at scale.

## Mental Model

Engineering ethics is not a separate dimension from engineering quality — it is engineering quality. A system that discriminates, erodes trust, or harms people is a poorly engineered system, regardless of its technical specifications. The best engineers hold themselves to a standard that includes the human consequences of what they build.

## Mini Summary

- ✔ Three frameworks: Consequentialism (outcomes), Deontology (rights and duties), Virtue (character)
- ✔ Algorithmic bias: high aggregate accuracy can mask systematic harm to specific groups
- ✔ GDPR Article 22: automated decisions require explainability, human oversight, and contestation
- ✔ Engineering responsibility extends beyond "I was asked to" — professional codes require harm avoidance
- ✔ The transparency test: would you explain this system's decisions to those it affects?

# Guided Practice Quest

**The Ethics Council**

The Ethics Council reviews three proposed systems. Apply all three ethical frameworks to evaluate each and produce a recommendation.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A health insurance company wants to build an automated premium pricing system. It will use:
- Age, location, BMI, smoking status
- Social media activity patterns (inferred from public data)
- Spending patterns from partner financial institutions
- Historical claims data from zip code level (not individual)

The model will determine premiums for individual customers automatically, with no human review unless a customer contacts support.

Conduct a full ethics analysis:
1. Apply all three ethical frameworks to this system
2. Identify specific algorithmic bias risks and which groups may be harmed
3. Evaluate GDPR/EU AI Act compliance requirements
4. What design changes would make the system more ethically defensible?
5. If you were the lead engineer and this was an order from the business: what would you do? (Answer with specific actions, not vague "raise concerns")
6. Where is the line between "build with ethical mitigations" and "refuse to build"?

# Integration

**Connecting to Philosophy — The Problem of Engineering Responsibility**

Philosopher Hans Jonas' *The Imperative of Responsibility* (1979) argued that modern technology has produced a new ethical category: large-scale, long-term, irreversible effects on future people. Traditional ethics addressed person-to-person relationships in the present. Technology creates obligations to distant others — geographically distant (global software) and temporally distant (future generations affected by data practices today).

Jonas' principle: "Act so that the effects of your action are compatible with the permanence of genuine human life." This is unusually demanding for engineers: consider not just immediate users but all those affected, including the most vulnerable, over the longest relevant timeframe.

This extends the engineering responsibility frame significantly. Not: "does this meet spec?" but "does this respect the dignity and wellbeing of all who will be affected, including those who had no say in its design?"

The Responsible AI movement (Timnit Gebru, Safiya Noble, Joy Buolamwini) has made these abstract philosophical questions concrete: face recognition misidentifying Black women, algorithmic welfare decisions impoverishing vulnerable people, credit scoring encoding historical discrimination. These are real harms produced by real engineering decisions made without adequate ethical analysis.

What would it mean to apply Jonas' principle to your day-to-day engineering work?

# Lore Conclusion

The Ethics Council completes its review. Two systems approved with modifications. One rejected pending redesign.

*"The most powerful enchantments cast the longest shadows,"* Archmage Veylan says. *"A ward that protects a thousand apprentices can also harm a hundred if designed without care. Power and responsibility are the same thing. Those who build powerful systems are responsible for their shadows."*

Build with the full awareness of what you're building. The technical specification is not the full specification.
---
