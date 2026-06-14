---
id: phy-jun-m2-06
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: circuits
topicTitle: "Circuits"
topicSortOrder: 2
title: "Electrical Power and Energy at Home"
sortOrder: 6
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Calculate electrical power using P = IV (and P = I²R)
  - Compute domestic energy use and cost via the kilowatt-hour
  - Choose fuse ratings and explain overload protection
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Computes P = IV and uses P = I²R for resistive heating
    - Converts between joules and kilowatt-hours and prices appliance running costs
    - Selects an appropriate fuse rating from an appliance's power and voltage
  keywords: [power, watt, P = IV, I²R, kilowatt-hour, fuse, cost, rating]
  modelAnswer: |
    Electrical power is energy delivered per second: P = IV — each coulomb carries V joules and
    I coulombs arrive each second. A 230 V kettle drawing 10 A develops 2,300 W. In resistive
    components P = I²R, the current-squared law that makes overloaded thin wires dangerous and
    transmission engineers obsessive. Homes buy energy by the kilowatt-hour — one kW for one
    hour, 3.6 million joules — so a 2 kW heater run for 3 hours costs 6 kWh times the tariff.
    Fuses are chosen just above the appliance's normal current (I = P/V): a 2,300 W kettle
    draws 10 A and takes a 13 A fuse, so a fault's surge melts the guard before the flex.
guidedSteps:
  - id: phy-jun-m2-06-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A hair dryer on a 230 V supply draws 6 A. Its power is P = IV = ________ W.
    inputConfig:
      placeholder: "1380"
    markingRule:
      matchMode: CONTAINS
      accepted: ["1380", "1,380"]
      rejectedFeedback: "P = 6 × 230 = 1,380 W ≈ 1.4 kW. Six coulombs per second, each spending 230 J — the watt count is just the multiplication."
    hint: "Multiply current by voltage."
    reflectionPrompt: "What current does a 2,300 W kettle draw on the same supply?"
  - id: phy-jun-m2-06-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A 2 kW heater runs for 5 hours on a tariff of 30p per kWh. The cost is:
    inputConfig:
      options:
        - "£3.00 — 10 kWh at 30p"
        - "30p"
        - "£10.00"
        - "£1.50"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["£3.00 — 10 kWh at 30p"]
      rejectedFeedback: "Energy = power × time = 2 kW × 5 h = 10 kWh; cost = 10 × 30p = £3.00. The kilowatt-hour is the electricity bill's native unit — energy, not power."
    hint: "kWh = kW × hours; then multiply by the price."
    reflectionPrompt: "How many JOULES is 10 kWh — and why don't bills use joules?"
  - id: phy-jun-m2-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why does doubling the current through a cable QUADRUPLE the heat generated in it — and what does this mean for an overloaded extension lead? (2–3 sentences using P = I²R.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [I², squared, quadruple, heat, overload, fire, rating]
      rejectedFeedback: "Cable heating follows P = I²R: current enters SQUARED, so 2× the current = 4× the watts dissipated in the same wire. An extension lead running double its rating doesn't get twice as warm — it gets four times the heating, which is how coiled, overloaded leads progress from warm to smoking. Ratings are the I²R law printed on a label."
    hint: "In P = I²R, what does doubling I do?"
    reflectionPrompt: "Why does the same I²R logic make GRID engineers transmit at very LOW current (and therefore very high...what)?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The kilowatt-hour is a unit of:"
    options: ["Power", "Energy — one kilowatt sustained for one hour (3.6 MJ)", "Current", "Voltage"]
    correctIndex: 1
    feedback: "kWh = power × time = energy. Bills charge for energy; 'a 3 kW shower' states the RATE at which you'll be buying it."
  - type: MULTIPLE_CHOICE
    question: "An appliance is rated 460 W at 230 V. The best fuse from {3 A, 5 A, 13 A} is:"
    options:
      - "3 A — just above the 2 A normal current"
      - "13 A — the biggest is safest"
      - "5 A"
      - "No fuse needed"
    correctIndex: 0
    feedback: "I = P/V = 2 A; choose the rating just ABOVE normal draw: 3 A. A 13 A fuse would let a serious fault simmer below its threshold — guards must be snug, not generous."
---

# Hook

Somewhere in your home, a label is quietly telling you the future: "2000 W". It means: *this appliance will take two thousand joules out of the grid every second it runs* — and at today's prices, roughly the cost of a small coffee for every two hours of use. Power ratings are prophecies, and this lesson teaches you to read them: which gadgets are sippers (a router: 10 W, pennies a week) and which are gulpers (a shower: 9,000 W — a kettle's worth of energy every *fifteen seconds*).

The same arithmetic has a dark twin. The heat developed in a wire grows with the *square* of the current — double the load on a thin extension lead and you quadruple its self-heating. Between the bill and the fire risk stands one humble component, rated in amps and costing pennies: the fuse. Power, price, and protection — today, the Tower does the money.

# Lore Introduction

Hale spreads the Tower's account-book beside the master fuse-board: a century of entries — candles bought, then gas, then the first electric tariffs — and, pasted in, the Tower's very first electricity bill, itemised in a unit you now recognise. "When the Academy electrified, the Bursar demanded to know what, precisely, she was buying," Hale says. "Not amps — the river returns every coulomb it borrows. Not volts — the push is a standing offer." She taps the old bill's column header. "*Kilowatt-hours*. Energy, junior — the same joules Calde audits in her furnaces, arriving by wire. Today you learn the three-way arithmetic of the wire-borne joule: how fast it arrives (the watt), what it costs (the bill), and—" she holds up a blackened, blown fuse from the board's relic-drawer, kept like a saint's bone, "—what stands guard when the arithmetic is ignored."

# Core Learning

## Concept Introduction

**Power — the delivery rate.** Each coulomb carries V joules; I coulombs arrive per second; so:

```
P = I × V        watts = amps × volts
```

Anchors at 230 V: LED bulb 0.03 A → 7 W; laptop ~0.3 A → 70 W; kettle 10 A → 2.3 kW; shower 39 A → 9 kW (on its own dedicated heavy cable, for exactly this reason). Combine with Ohm's law and two siblings appear: **P = I²R** (the resistive-heating form) and P = V²/R — three grips on one quantity.

**The I² warning.** P = I²R makes current *quadratically* expensive in heat: double the current through any wire, quadruple the warming of that wire. This single square rules: cable ratings and why thick cables for heavy loads; the danger curve of overloaded leads (warm → hot → fire, faster than intuition); and — gloriously inverted — **grid transmission**: send the same power at huge V and tiny I, and the line's I²R losses collapse. (The full grid story crowns this module's final lesson.)

**The kilowatt-hour — energy by the bottle.**

```
1 kWh = 1 kW × 1 hour = 3,600,000 J
energy (kWh) = power (kW) × time (h);  cost = kWh × tariff
```

Bills charge for energy. A 2 kW heater × 5 h = 10 kWh; at 30p: £3. The joule is too small for commerce (one kettle-boil ≈ 360,000 J); the kWh is the joule's retail crate.

**Fuses — the guard's arithmetic.** A fuse is a thin wire engineered to melt at its rated current — a sacrificial series guard. Choosing one: compute normal draw I = P/V, pick the standard rating *just above* (kettle: 10 A → 13 A fuse; lamp: 0.26 A → 3 A). Too-large a fuse is a sleeping guard: faults below its threshold cook the flex at leisure. Circuit breakers do the same job resettably; RCDs guard a different fault entirely (current leaking to earth — through, say, a person) by comparing live and neutral flows. Three guards, three jobs, one wall-box.

## Why It Matters

- Energy literacy is money: reading ratings and tariffs turns the bill from mystery to arithmetic — and reveals which habits actually matter (heating water and air dwarfs everything with a standby light).
- The I²R square is fire-safety mathematics: lead ratings, "fully unwind", and why sockets have current limits.
- Fuse arithmetic is the everyday safety calculation most adults were never taught; you now own it.

## Worked Examples

**Example 1: The shower versus the phone**
A 9 kW shower for 10 minutes: 9 × (1/6) = 1.5 kWh ≈ 45p. A phone charge: ~15 Wh = 0.015 kWh ≈ half a penny. One shower = a hundred phone charges. Energy economics in two lines — and why "unplug your charger" campaigns aim at the wrong appliance entirely.

**Example 2: Sizing the kitchen's guard**
Kettle (2.3 kW) + toaster (1.2 kW) on one 230 V circuit: I = (2300 + 1200)/230 ≈ 15 A. A 13 A-rated socket circuit objects — and a 32 A ring main shrugs. This is why kitchens get their own heavy circuits, and why the breaker trips when someone adds the air-fryer: the junction rule (last lesson) summing branch currents, priced in amps.

**Example 3: The blown fuse's autopsy**
A 3 A-fused lamp flickers out; the fuse wire is found vapourised. Diagnosis: a fault dropped the lamp's resistance (frayed flex shorting live to neutral: R → ~0.1 Ω), demand surged toward I = 230/0.1 = 2,300 A — and the fuse melted in milliseconds at a few tens of amps, long before the flex could. The blackened guard in Hale's drawer died precisely as designed: the cheapest component, sacrificed first, every time.

## Common Mistakes

- **Confusing power with energy** — kW is the rate, kWh the amount; "I used 3 kW today" is grammar that bills don't parse.
- **kWh ≠ kW per hour** — it's kW × hours; the name misleads half of humanity.
- **"Bigger fuse = safer"** — inverted: a generous fuse sleeps through cooking-grade faults. Snug ratings guard; oversized ones decorate.
- **Linear thinking about overload** — 20% over a cable's rating is 44% more heating (1.2²); the square forgives nothing.
- **Blaming voltage for bills** — the supply's 230 V is constant; your *appliances'* current draw (hence power, hence kWh) writes the bill.

## Mental Model

Electricity at home is **a water-mill economy**. The supply is a millrace at fixed head (230 V of "height"). Every appliance is a wheel dipped into the race: its design decides how much flow it takes (current), and height × flow is the power it mills. The bill is the *miller's ledger*: not height, not flow, but sacks ground — kilowatt-hours, energy by the sack. And the fuse? The race's entrance has a deliberately weak plank rated for the wheel's normal flow: a stuck wheel or a burst channel pulls a flood, the plank snaps at once, and the millhouse — flex, walls, sleepers upstairs — never feels the surge. Choosing the plank too strong is not strength; it is volunteering the house as the next weakest point.

## Mini Summary

- ✔ P = IV (watts): delivery rate; siblings P = I²R and V²/R
- ✔ The I² square: doubling current quadruples wire heating — ratings, overloads, and the grid's high-voltage logic
- ✔ kWh = kW × h = 3.6 MJ: bills buy energy; ratings prophesy the rate
- ✔ Fuse choice: I = P/V, then the standard rating just above; snug guards, never generous
- ✔ Heat-makers (showers, heaters, kettles) dominate every bill; electronics are rounding errors

# Guided Practice Quest

Work through the guided steps to rate a hair dryer in honest watts, price five hours of warmth, and let the I² square explain why coiled extension leads dream of becoming toasters.

# Solo Practice Quest

Three entries for the account-book: (1) *Home power census*: find the rating labels of five appliances spanning sippers to gulpers; for each compute its current draw (I = P/V) and its cost per hour at your local tariff; rank them. (2) *The week's bill, predicted*: estimate your household's weekly kWh from realistic usage times of your census items (plus heating/hot water honestly guessed), price it, and compare with a real bill if you can get one — report the discrepancy and its likeliest source. (3) *Guard duty*: assign correct fuses from {3 A, 5 A, 13 A} to: a 60 W lamp, a 700 W microwave, a 2.2 kW heater, and a 25 W games console — show I = P/V for each, and write two sentences on what the wrong (oversized) choice would risk. Close with the single cheapest behaviour change your census recommends, with its annual saving computed.

# Integration

**Engineering**: Domestic supply design is this lesson with regulations: diversity factors (not everything runs at once), ring-main ratings, dedicated shower circuits, and the coordination hierarchy (appliance fuse before circuit breaker before company fuse). Smart meters simply automate the kWh ledger you just learned to keep by hand.

**Mathematics**: P = IV, P = I²R, and P = V²/R are one relation viewed through Ohm's substitution — algebraic fluency as fire safety. The kWh ↔ joule conversion is Module One unit discipline with money attached, and the quadratic overload curve is the v² braking law's electrical twin: nature keeps reusing the square.

# Lore Conclusion

Your census, predictions, and guard assignments go into the Tower's account-book, beside a century of Bursars' entries — and Hale, checking your kettle's fuse arithmetic, pronounces the Bursar's own blessing: *"Correct to the penny and the amp."* She returns the blackened relic-fuse to its drawer. "The river is measured, architected, and billed. Which closes the Tower's first floor." She crosses instead to a cabinet you've never seen opened, and lifts out something wholly unexpected: a plain iron bar, and beside it a slender compass whose needle swings as the bar approaches. No wires anywhere. "Now for the Tower's second secret, junior. The river, it turns out, has a *shadow* — a twisting influence that needles have followed across oceans for a thousand years, and that no one connected to electricity until one lecture-hall accident two centuries ago." The needle trembles, settles, points. "Tomorrow: magnetism. Bring the compass. It already knows the way."
