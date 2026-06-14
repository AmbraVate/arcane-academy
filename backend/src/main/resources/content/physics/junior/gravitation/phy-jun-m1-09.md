---
id: phy-jun-m1-09
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: gravitation
topicTitle: "Gravitation"
topicSortOrder: 3
title: "Orbits and Satellites"
sortOrder: 9
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Derive orbital speed by equating gravity with the centripetal demand
  - Explain why higher orbits are slower and longer-period
  - Distinguish low orbits, geostationary orbits, and escape
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Sets GMm/r² = mv²/r to obtain v = √(GM/r) and uses it
    - States the higher-is-slower rule and its period consequence
    - Explains geostationary orbits (24 h period over the equator) and one use
    - Explains escape velocity qualitatively as outrunning the field's energy debt
  keywords: [orbital speed, √(GM/r), geostationary, period, higher slower, escape velocity, satellite]
  modelAnswer: |
    An orbit is gravity employed as the exact centripetal supplier: GMm/r² = mv²/r gives
    v = √(GM/r) — each radius has ONE circular speed, independent of the satellite's mass. Low
    Earth orbit demands ~7.8 km/s and 90-minute laps; higher orbits are slower and longer,
    until at r ≈ 42,000 km the period reaches 24 hours — geostationary, hovering over one
    equatorial point, home of communications and weather satellites. Escape velocity
    (11.2 km/s from Earth's surface) is the speed whose kinetic energy pays off the field's
    entire energy debt — above it, you coast away forever; below it, you remain the planet's
    customer on some orbit or trajectory back.
guidedSteps:
  - id: phy-jun-m1-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Newton's cannon on a mountaintop fires horizontally, ever faster. At ~7.9 km/s (ignoring air), the ball:
    inputConfig:
      options:
        - "Flies off into space forever"
        - "Falls toward Earth continuously, but its curve matches the planet's — it circles back and orbits"
        - "Hovers motionless"
        - "Burns up"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Falls toward Earth continuously, but its curve matches the planet's — it circles back and orbits"]
      rejectedFeedback: "At orbital speed, the ball drops ~5 m for every 8 km travelled — and Earth's surface curves away ~5 m per 8 km. The fall and the curve cancel: perpetual missing. Slower, it lands; much faster, it escapes; at 7.9 km/s, it orbits."
    hint: "Recall: an orbit is a fall that perpetually misses. What must match for the miss to be perpetual?"
    reflectionPrompt: "Why was 'ignoring air' essential — what really happens at 7.9 km/s at mountaintop altitude?"
  - id: phy-jun-m1-09-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Satellite A circles at 7,000 km from Earth's centre; satellite B at 28,000 km (4× further). Using v = √(GM/r), B's speed compared with A's is:
    inputConfig:
      options:
        - "Four times slower"
        - "Half — speed falls with the square root of radius"
        - "The same"
        - "Twice as fast"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Half — speed falls with the square root of radius"]
      rejectedFeedback: "v ∝ 1/√r: four times the radius means √4 = 2 times slower. Higher orbits are SLOWER (and their laps longer twice over: more circumference at less speed)."
    hint: "r is under a square root, inverted."
    reflectionPrompt: "Counter-intuition check: to catch a satellite AHEAD of you in the same orbit, should you speed up or drop lower? Think it through with v(r)."
  - id: phy-jun-m1-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why must a geostationary satellite orbit at one specific altitude (~36,000 km up), and why over the equator? What jobs is such an orbit uniquely good for? (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [24, period, matches, rotation, equator, fixed, communications, dish, weather]
      rejectedFeedback: "Each radius fixes one period; only at r ≈ 42,000 km (≈36,000 km altitude) does the period equal 24 h. Orbiting above the equator in Earth's spin direction, the satellite then hovers over one ground point forever — so TV dishes can bolt rigidly to walls, and weather satellites stare at one hemisphere continuously."
    hint: "Match the orbital period to the planet's rotation; ask what plane keeps it over one spot."
    reflectionPrompt: "Why can't a satellite hover geostationary above London or Sydney?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Orbital speed v = √(GM/r) does not contain the satellite's mass. Therefore:"
    options:
      - "Heavier satellites need faster speeds"
      - "A pebble and a space station at the same radius orbit at exactly the same speed"
      - "Only light objects can orbit"
      - "The formula is wrong"
    correctIndex: 1
    feedback: "Gravity scales its pull to each customer's mass (the free-fall lesson's ghost), so every mass at radius r shares one orbital speed — which is why astronauts float beside their station instead of drifting off."
  - type: MULTIPLE_CHOICE
    question: "Escape velocity from Earth's surface is about:"
    options: ["7.9 km/s", "11.2 km/s", "340 m/s", "300,000 km/s"]
    correctIndex: 1
    feedback: "11.2 km/s — the speed whose kinetic energy exactly pays off Earth's gravitational energy debt. (7.9 km/s is circular LOW-orbit speed: enough to fall forever, not enough to leave.)"
---

# Hook

In 1687, Newton published a thought experiment so clean it reads like a dare: put a cannon on a high mountain and fire horizontally. Slow shot — it arcs to the ground nearby. Faster — it lands further. Faster still — further yet... and since the Earth is *round*, the ground is curving away beneath every shot. So fire fast enough, and the ball's fall matches the planet's curve exactly: it drops forever toward a surface that forever recedes, sails around the world, and — mind the back of your head — returns to the mountain.

That speed is 7.9 kilometres per second, and 270 years later humanity finally built the cannon (we call them rockets). Today some 10,000 active satellites run Newton's dare continuously — your GPS position, weather forecast, and TV feed are all falling around the Earth at this moment. This lesson is the dare made arithmetic: how fast for which orbit, why higher means slower, what makes one special altitude worth billions, and how fast you must go to leave the table entirely.

# Lore Introduction

Vex's brass model — the mountain, the cannon, the globe — stands on the Mechanica's central bench, and beside it something you've not seen before: the Academy's orbital ledger, a heavy book of charts where generations have computed the paths of moons and comets. "The thought is Newton's; the ledger is ours," says Vex. "Every entry is one bargain struck with the banker: *this* radius, *this* speed, or no deal." He spins the model globe. "Most apprentices believe orbits are found by trial. They are not. They are *priced* — one speed per altitude, no haggling, satellite's size irrelevant. By tonight you will price three orbits yourself: the courier's low lap, the watcher's high hover, and—" he taps the ledger's final, mostly empty section, marked with a comet's sigil, "—the one purchase that closes your account here forever. We call it escape."

# Core Learning

## Concept Introduction

**The orbital bargain.** A circular orbit is gravity hired as the *exact* centripetal supplier. Set the offer equal to the demand:

```
G M m / r² = m v² / r        →        v = √(G M / r)
```

Read its clauses:
- **One radius, one speed** — no negotiation. Too slow for your altitude: you spiral in (well, descend on an ellipse). Too fast: you climb away.
- **The satellite's mass cancels** — pebble or station, same speed at the same r (gravity scales its pull per customer; the free-fall universality again). This is why astronauts float *beside* their craft.
- **Higher is slower**: v ∝ 1/√r — and doubly longer per lap (more circumference at less speed): T grows as r^1.5 (Kepler's third law, met here in working clothes).

**The catalogue of orbits:**

| Orbit | Altitude | Speed | Period | Tenants |
|-------|----------|-------|--------|---------|
| Low Earth (LEO) | 200–2,000 km | ~7.8 km/s | ~90 min | ISS, imaging, Starlink |
| Mid (MEO) | ~20,000 km | ~3.9 km/s | ~12 h | GPS and friends |
| **Geostationary (GEO)** | 35,786 km | 3.07 km/s | **24 h** | Comms, broadcast, weather |

**Geostationary** is the catalogue's prize: at one radius (~42,000 km from centre) the period equals Earth's day; placed over the **equator**, orbiting with the spin, the satellite hovers over one ground point forever. Consequences: TV dishes bolt rigidly to walls; weather eyes stare unblinking at one hemisphere; and that single ring of sky is parcelled by international treaty like beachfront property. (Why equator-only? Any tilted orbit's plane must pass through Earth's centre — it would swing north and south of the target daily.)

**Leaving the table: escape velocity.** Orbiting is falling-and-missing; *escaping* is outrunning the debt. Climbing a field costs energy without limit of distance — but the inverse square means the total debt is finite. Pay it all in one kinetic lump:

```
v_escape = √(2GM/r)  =  √2 × v_orbital        (11.2 km/s from Earth's surface)
```

Above it (unpowered), you coast away forever, slowing but never stopping; below it, you remain bound — on an ellipse, a lap, or a return trajectory. The Moon's gentler 2.4 km/s is why modest ascent stages sufficed in 1969; black holes are the concept's terminus — escape velocity exceeding light speed itself (a Senior-tier appointment).

## Why It Matters

- The space economy — navigation, communications, weather, Earth observation — runs entirely on this lesson's two formulas and the catalogue they generate.
- Orbital mechanics' counter-intuitions (brake to catch up, thrust forward to climb-and-slow) govern real rendezvous, debris avoidance, and the increasingly urgent traffic management of LEO.
- Escape-velocity reasoning sizes every interplanetary mission's launch vehicle — and explains why small worlds keep no atmospheres (gas molecules exceed their escape speeds).

## Worked Examples

**Example 1: Pricing the ISS lap**
r = 6,770 km: v = √(GM/r) = √(3.99×10¹⁴ / 6.77×10⁶) ≈ **7.67 km/s**. Period: T = 2πr/v ≈ 5,540 s ≈ **92 minutes** — sixteen sunrises a day for the crew, and the reason "station passes" last only minutes for sky-watchers.

**Example 2: Deriving geostationary altitude**
Demand T = 86,164 s (the sidereal day). From T² = 4π²r³/GM: r³ = GMT²/4π² = 3.99×10¹⁴ × (86,164)²/39.5 ≈ 7.5×10²² → r ≈ **42,200 km** (35,800 km altitude). One unknown, one equation, one parking ring — every broadcast satellite in the sky sits where this arithmetic says it must.

**Example 3: The rendezvous paradox**
Your craft trails a target in the same LEO. Intuition says thrust forward; orbital mechanics says: that *raises* your orbit, where v is lower — you climb, slow, and fall further behind. The real manoeuvre: brake gently, drop to a lower, *faster* lane, overtake beneath, then re-boost. Every docking since Gemini has obeyed this inverted traffic law — v = √(GM/r) is the whole explanation.

## Common Mistakes

- **"Satellites stay up because they're beyond gravity"** — gravity is the *only* thing keeping them on the lap; cut it and they leave on the tangent.
- **"Heavier satellites orbit slower"** — mass cancels; the bargain is per-radius, not per-customer.
- **Confusing 7.9 and 11.2 km/s** — circular-orbit speed vs escape; falling-forever vs leaving-forever. (Ratio √2, always.)
- **Geostationary anywhere** — equator only, one altitude only; "hovering over Moscow" violates geometry, not engineering.
- **"Escape velocity is needed to reach space"** — rockets thrust continuously and could (with absurd fuel) leave at walking pace; v_escape is the *unpowered coasting* threshold, and the practical energy benchmark.

## Mental Model

Think of a planet's gravity as **a valley with a strange shape — infinitely wide, but of finite depth**. Every object near the planet lives somewhere on the valley's slopes. *Orbiting* is rolling around the valley's wall at exactly the speed where the slope's inward lean matches your turn — each contour line (radius) has its own posted lap-speed, slower on the high, gentler contours. *Climbing* costs energy with every metre — but because the slope keeps easing (inverse square), the total climb out is a finite bill: pay it all at once as speed — 11.2 km/s from Earth's floor — and you roll over the rim, out onto the flat plains of interplanetary space, never to roll back. The catalogue of satellites is just the valley's contour map, with tenants.

## Mini Summary

- ✔ Orbit: gravity = centripetal demand → v = √(GM/r); one speed per radius, mass irrelevant
- ✔ Higher is slower and much longer: T² ∝ r³ (Kepler III in working form)
- ✔ Geostationary: T = 24 h at r ≈ 42,000 km, equator only — the broadcast and weather ring
- ✔ Escape: v = √(2GM/r) = √2 × orbital — the finite price of leaving a bottomless-looking valley
- ✔ Orbital traffic is counter-intuitive: brake to catch up, thrust to fall behind

# Guided Practice Quest

Work through the guided steps to fire Newton's cannon into a closed loop, halve a speed by quadrupling a radius, and park a broadcast satellite where the arithmetic — and the treaty — says it belongs.

# Solo Practice Quest

Open your own orbital ledger, three entries: (1) *The Moon, audited*: from v = √(GM/r) at r = 3.84×10⁸ m, compute the Moon's orbital speed and period; compare your period with the actual month and explain any small discrepancy honestly. (2) *Design a constellation*: choose a mission (imaging? navigation? broadcast?), select an orbit class for it from the catalogue, and justify with numbers — speed, period, and one operational consequence (revisit rate, dish-pointing, latency). (3) *The escape audit*: compute escape velocity for Earth, the Moon, and Mars (look up M and r); then, given that air molecules at atmospheric temperatures move at hundreds of m/s with a fast tail, write three sentences on why the Moon is airless, Mars thin-aired, and Earth still breathing. Show working; the ledger accepts no bare assertions.

# Integration

**Engineering**: Real missions add the engineering coefficients to this lesson's skeleton: atmospheric drag (LEO satellites need periodic re-boosts; the ISS "falls" ~2 km/month), transfer orbits (the Hohmann ellipse — two burns between contour lines), station-keeping fuel budgets, and end-of-life graveyard orbits. Launch sites hug the equator to pocket Earth's 460 m/s of free rotational speed — the planet's own contribution to the bargain.

**Mathematics**: Equating two force expressions and watching a variable cancel is a master pattern — you'll reuse it constantly. T² ∝ r³ emerges from one line of algebra here, yet historically ran the other way: Kepler distilled it from decades of Mars data, and Newton's law had to *reproduce* it or die. When a one-line derivation lands on a seventeenth-century measurement, you are watching mathematics audit reality.

# Lore Conclusion

By lamplight you enter your three prices into the Academy's orbital ledger — the courier's 90-minute lap, the watcher's 24-hour hover derived to the very ring, the comet's-leave of 11.2 — and Vex countersigns each in the Mechanica's terse hand. He turns the ledger's pages backward, past centuries of entries, to show you the oldest: a comet's return, priced and *predicted* by some long-dead junior's master, with a marginal note recording its punctual arrival decades later. "The banker keeps appointments," Vex says simply. He closes the book. "Module One stands complete: motion's debts, circles' bills, and the heavens' contracts. What remains—" he leads you back into the Mechanica proper, where the great levers, gear-trains, and balanced beams of the machine-floor wait in lamplit ranks, "—is the oldest physics of all. Before the heavens were priced, junior, someone had to lift the stones. Tomorrow: the machines — and the noble art of trading force for distance."
