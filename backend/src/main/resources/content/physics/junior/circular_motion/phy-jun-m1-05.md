---
id: phy-jun-m1-05
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: circular_motion
topicTitle: "Circular Motion"
topicSortOrder: 2
title: "Centripetal Force"
sortOrder: 5
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Calculate centripetal acceleration (a = v²/r) and force (F = mv²/r)
  - Identify which physical force supplies the centripetal requirement in real cases
  - Predict the consequences when the supplier reaches its limit
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Computes a = v²/r and F = mv²/r with correct units
    - Names the supplying force in at least three scenarios (tyres, tension, gravity, normal force)
    - Predicts the failure mode (tangent departure/skid) when the supplier's limit is exceeded
  keywords: [centripetal, v²/r, mv²/r, supplier, friction, tension, gravity, skid, limit]
  modelAnswer: |
    A circle of radius r at speed v demands centripetal acceleration a = v²/r, and the
    resultant inward force must be F = mv²/r. The demand is filled by ordinary forces: tyre
    friction on a flat bend, string tension for a whirled mass, gravity for an orbit, the
    normal force in a banked wall-of-death. The v² makes demand grow savagely with speed —
    doubling v quadruples the required force — so every supplier has a critical speed: exceed
    the grip of tyres and the car leaves down the tangent (a skid is unpaid centripetal
    demand). A 1000 kg car rounding r = 50 m at 20 m/s needs mv²/r = 8000 N of friction —
    feasible dry, doubtful on ice.
guidedSteps:
  - id: phy-jun-m1-05-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 1000 kg car rounds a flat bend of radius 50 m at 20 m/s. Required centripetal force: F = mv²/r = ________ N.
    inputConfig:
      placeholder: "8000"
    markingRule:
      matchMode: CONTAINS
      accepted: ["8000", "8,000"]
      rejectedFeedback: "F = 1000 × 20² / 50 = 1000 × 400/50 = 8000 N — supplied entirely by tyre friction on a flat bend. No friction, no bend."
    hint: "Square the speed first; then m × v² ÷ r."
    reflectionPrompt: "What happens to the demand if the same bend is taken at 40 m/s?"
  - id: phy-jun-m1-05-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      For each circular motion, which force SUPPLIES the centripetal demand? Match the odd one out: hammer-throw / orbiting satellite / car on a flat bend / ball whirled on a string. The satellite's supplier is:
    inputConfig:
      options:
        - "Tension"
        - "Friction"
        - "Gravity — the Earth's pull is the entire inward force"
        - "Engine thrust"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Gravity — the Earth's pull is the entire inward force"]
      rejectedFeedback: "Orbits are gravity-funded circles: the satellite needs mv²/r inward and Earth's gravity provides exactly that — no engines, no tension, nothing else. Hammer and ball: tension. Car: friction."
    hint: "What is the only force acting on a coasting satellite?"
    reflectionPrompt: "If gravity supplies EXACTLY mv²/r at one speed, what happens to a satellite moving slightly slower at that altitude?"
  - id: phy-jun-m1-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A lorry takes an icy bend too fast and slides straight on, off the road. Explain the failure in centripetal terms — what was demanded, what was available, and why the path was straight rather than 'flung outward'. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [demand, friction, ice, insufficient, tangent, straight, mv²/r, grip]
      rejectedFeedback: "The bend demanded mv²/r inward; icy tyres could supply only a fraction. With the inward force underpaid, the lorry's path failed to bend enough — it continued nearly straight (down the tangent), which from the road's curve looks like 'sliding out'. Nothing pushed it outward; the centre simply couldn't afford to pull it round."
    hint: "Compare demand (mv²/r) with supply (icy friction). Unpaid demand = unclosed circle."
    reflectionPrompt: "Why does slowing down before the bend solve the problem at the SQUARE of the rate you'd expect?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Doubling the speed around the same circle multiplies the required centripetal force by:"
    options: ["2", "4", "8", "It stays equal"]
    correctIndex: 1
    feedback: "F = mv²/r: the v² strikes again. Speed is quadratically expensive in corners — the deep reason bends carry advisory speeds."
  - type: MULTIPLE_CHOICE
    question: "Centripetal force is:"
    options:
      - "A new fundamental force of nature"
      - "A job description — the net inward force, filled by friction, tension, gravity, or normal forces"
      - "The outward force on circling objects"
      - "Always equal to weight"
    correctIndex: 1
    feedback: "Never add a 'centripetal force' arrow to a diagram as its own force: identify which REAL force (or combination) points inward and pays the mv²/r bill."
---

# Hook

Every bend in every road has a price list, and it reads: **F = mv²/r**. Take the corner at 20 m/s and your tyres must find 8,000 newtons of sideways grip. Take it at 40 — just twice the speed — and the bill *quadruples* to 32,000, more than dry rubber on tarmac can pay. The corner doesn't negotiate. Underpay, and the road's curve and your car's path part company: you travel straight, the road bends away beneath you, and the accident report will say you "slid out" — though the truth is purer: you simply went where inertia goes when nobody affords to bend you.

This lesson is the bill itemised: how much force every circle demands, who pays it in each real case — tyres, strings, walls, planets — and what happens, from skids to satellites, when the payer reaches their limit.

# Lore Introduction

Vex has converted the rotunda into what he calls the Court of Demands. At the centre, the great spring balance from yesterday — and around the walls, exhibits: a tethered cart on the circular rail, a model road-bend with adjustable surface plates (oak, glass, gritted), a sling, and a beautiful brass orrery whose moons run on radial wires. "Yesterday you read the balance: this junior, this radius, this speed — *that* pull," he says, tapping the needle. "Today, the law behind the needle. Every circle files a demand — so much inward force, payable continuously. Our business is the demand schedule: how it scales, who pays, and—" he slides the glass plate into the model bend, gives the toy cart a push, and watches it sail serenely off the curve and across the floor, "—the proceedings when a payer defaults."

# Core Learning

## Concept Introduction

**The demand schedule.** Uniform circular motion at speed v, radius r, requires inward acceleration:

```
a = v² / r          and so          F = m v² / r
```

Read its temperament: **demand grows with the square of speed** (double v, quadruple F) and **eases with radius** (wider bends are cheaper). Hence advisory speeds on tight bends, the gentle sweep of motorway curves, and the savage forces inside small fast machines.

**Who pays — the supplier audit.** Centripetal force is a *job description*, not a force of nature. In every real circle, identify which ordinary force points inward:

| Circle | Supplier |
|--------|----------|
| Car on a flat bend | Sideways tyre **friction** |
| Whirled hammer/sling | **Tension** in wire or cord |
| Satellite, Moon, planet | **Gravity** — the whole bill |
| Wall-of-death rider, banked bend | **Normal force** (wall/road pushing inward) |
| Aircraft banking | Horizontal component of wing **lift** |
| Electron in an atom (preview) | Electric attraction |

(Free-body discipline from Apprentice tier applies: never draw "centripetal force" as an extra arrow — find the real forces whose resultant points inward.)

**Default proceedings.** Every supplier has a ceiling: friction its grip limit, strings their breaking tension, banking its design speed. When demand exceeds supply, the path simply *fails to bend enough* — the object departs along (or near) the tangent. Skids, snapped slings, and clothes pressed to the drum's wall are all the same event: an unpaid circular demand, settled in straight lines.

**The "feel" decoded.** Inside the turning car you feel pressed *outward* — that is your inertia meeting the door that is paying your personal mv²/r bill inward. The harder the cornering, the bigger the bill, the firmer the door's push. Passengers are cargo on credit.

## Why It Matters

- Road design is this formula with a budget: bend radii, advisory limits, surface grip standards, and banking angles are all mv²/r engineering.
- Everything that spins fast — centrifuges, turbines, flywheels — is materials-limited by the centripetal bill its own rim must pay; failure means fragments on tangents.
- Orbital mechanics begins here: set gravity equal to mv²/r and the speed of any orbit falls out — next topic's opening move.

## Worked Examples

**Example 1: The bend's advisory speed, derived**
Flat bend, r = 50 m; tyres on dry tarmac supply at most ~0.8 × weight in sideways friction: F_max = 0.8 × m × 10 = 8m newtons. Demand mv²/r ≤ supply: v² ≤ 0.8 × 10 × 50 = 400 → v ≤ 20 m/s (~72 km/h). On wet (0.4): v ≤ 14 m/s. On ice (0.1): 7 m/s. The advisory sign is the friction coefficient, published.

**Example 2: The wall of death**
A fairground rider circles the inside of a vertical drum, r = 5 m, at 12 m/s. Demand: a = v²/r = 144/5 ≈ 29 m/s² ≈ 3g — supplied by the *wall's normal force* pressing inward on the rider (~3× their weight). Friction with the wall, scaled by that huge normal force, holds them up against gravity. Below a critical speed the normal force — and with it the friction — shrinks until gravity wins: the act's entire safety case is mv²/r.

**Example 3: Sizing a centrifuge**
A lab centrifuge spins samples at r = 0.1 m, 6,000 rpm (f = 100 Hz): v = 2πrf ≈ 63 m/s; a = v²/r ≈ 40,000 m/s² ≈ **4,000g**. A 1-gram sample "weighs" 4 kg's worth against its tube floor; dense components migrate "down" (outward) through lighter ones within minutes — sedimentation that gravity would take months to achieve. The tube material, meanwhile, pays the same schedule; cheap plastic need not apply.

## Common Mistakes

- **Adding a phantom "centripetal arrow"** — diagram the real forces; their inward resultant IS the centripetal force.
- **Believing in the outward push on the circler** — what's outward is the *reaction* on the door/wall/string (third law), and your inertia's protest; the object's own bill is inward.
- **Linear thinking on speed** — "a bit fast" into a bend is *quadratically* fast in demand; halving speed quarters the bill.
- **Forgetting the supplier's ceiling** — calculations of demand mean nothing without comparing supply; the comparison IS the safety analysis.
- **Misreading the skid** — cars exceed grip and go (nearly) straight; they are not "thrown outward". Accident analysis writes tangents, not spirals.

## Mental Model

Every circle is **a subscription service with relentless billing**. The product: continuous direction-change. The price: mv²/r newtons, invoiced every instant, no pausing, no arrears. Your suppliers are whoever holds an inward line of credit — rubber, rope, gravity, walls. The fine print everyone signs without reading: *prices scale with the square of speed*. Cornering faster isn't a slightly bigger plan; it's a luxury tier. And the cancellation policy is immediate and non-negotiable: the instant any invoice goes unpaid, service stops, and you exit — as all defaulters do — on the tangent, headed exactly where you were last pointed.

## Mini Summary

- ✔ a = v²/r; F = mv²/r — demand quadratic in speed, inverse in radius
- ✔ Centripetal is a job: friction, tension, gravity, normal force, or lift fills it
- ✔ Compare demand with the supplier's ceiling — that comparison is every skid, snap, and safety case
- ✔ Default = departure on the tangent; nothing is ever "flung outward"
- ✔ The outward press you feel is your inertia against whatever pays your bill

# Guided Practice Quest

Work through the guided steps to invoice a 50-metre bend, appoint gravity as a satellite's sole supplier, and write an icy lorry's accident report in honest tangents.

# Solo Practice Quest

Three commissions for the Court of Demands: (1) *Your own bend*: choose a real corner you walk, cycle, or drive (estimate r from a map), pick two realistic speeds, and compute the centripetal demand for your mass or vehicle's — then judge each against a grip estimate (dry ~0.8 × weight, wet ~0.4). (2) *String test*: whirl a mass on a string (outdoors!) and increase speed until you genuinely feel the tension climbing; estimate r, count revolutions per second at two paces, and compute the tension demand at each — confirm the quadratic jump. (3) *Design brief*: a playground roundabout (r = 1.5 m) must stay fun but keep a clinging child's required grip below half their weight — derive its maximum safe rim speed and rpm. Show every line of working; the Court accepts no unitless filings.

# Integration

**Engineering**: Banked curves split the bill between friction and the normal force — railways and velodromes bank so steeply that, at design speed, the rails/track pay everything and the wheels pay nothing sideways. Turbine blades, centrifuge rotors, and flywheels are stress-analysed as their own centripetal suppliers: the rim pulls the rim inward, until the day it can't.

**Mathematics**: Deriving a = v²/r needs only similar triangles of velocity vectors — a beautiful piece of geometry worth seeking out — and the formula's limiting cases train your asymptotic sense: r → ∞ recovers straight-line zero-force motion; v → 0 likewise. Dimensional check: (m/s)²/m = m/s² ✓, as Module One demands forever.

# Lore Conclusion

By session's end the Court's slate carries your judgments: the model bend re-tried at three surfaces with verdicts matching the toy cart's behaviour to the handspan; the sling's tension priced at two speeds; the orrery's smallest moon — Vex's flourish — billed for its little brass orbit. "Demands filed, suppliers audited, defaults foreseen," Vex summarises, and then unlocks the rotunda's far doors onto the Mechanica's yard, where the evening's apparatus already waits: a banked test-track section salvaged from some old velodrome, a gyroscope the size of a cartwheel, a bucket on a rope. "Tomorrow, the demands go to work. Banked roads that corner for you. Wheels that refuse to fall over. Water that declines to leave an upside-down bucket. Spin, junior — applied, ridden, and occasionally survived. Wear boots you can run in."
