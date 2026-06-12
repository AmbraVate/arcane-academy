---
id: phy-jun-m1-06
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: circular_motion
topicTitle: "Circular Motion"
topicSortOrder: 2
title: "Spin in the Real World"
sortOrder: 6
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Explain banking as redirecting the normal force to pay the centripetal bill
  - Analyse vertical circles (buckets, loops) and find minimum speeds
  - Describe everyday spin applications from centrifuges to gyroscopic stability
integrationDomains: [engineering, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains banking — tilted normal force gains an inward component, reducing reliance on friction
    - Analyses the top of a vertical circle, deriving the minimum-speed condition (gravity alone pays the bill)
    - Explains one separation application (centrifuge, spin-dryer) without invoking outward force
    - Describes qualitatively why spinning things resist toppling
  keywords: [banking, normal force, vertical circle, minimum speed, loop, centrifuge, gyroscope]
  modelAnswer: |
    Banking tilts the road so the normal force leans inward: its horizontal component pays
    part (at design speed, all) of the mv²/r bill, sparing friction — why velodromes and
    racetrack turns are steep. In vertical circles the bill at the top can be paid by gravity
    itself: at the minimum speed, mg = mv²/r exactly, so v = √(gr) — slower and the water
    leaves the bucket, the coaster leaves the track. Centrifuges and spin-dryers separate by
    making contents 'want' to travel straight while the drum bends inward — denser matter
    migrates outward through lighter. Spinning wheels resist toppling because their angular
    momentum takes torque to redirect: the gyroscopic stiffness that keeps bicycles and
    spinning tops upright.
guidedSteps:
  - id: phy-jun-m1-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Racetrack corners and velodrome bends are steeply banked so that:
    inputConfig:
      options:
        - "Rain drains off quickly"
        - "The tilted normal force gains an inward component that pays much of the centripetal bill, reducing dependence on tyre grip"
        - "Drivers can see further round the bend"
        - "Cars are pushed outward more gently"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The tilted normal force gains an inward component that pays much of the centripetal bill, reducing dependence on tyre grip"]
      rejectedFeedback: "Tilt the surface and its push (always perpendicular to itself) leans inward — a friction-free contribution to mv²/r. At the design speed, banking pays the whole bill; cyclists on velodromes corner at 60 km/h on wooden boards with no sideways grip to spare."
    hint: "Which direction does a tilted surface's normal force point?"
    reflectionPrompt: "Why do banked bends have a DESIGN speed — what changes above or below it?"
  - id: phy-jun-m1-06-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Swinging a bucket of water in a vertical circle of radius 1 m: the minimum speed at the TOP for the water to stay put is v = √(gr) ≈ ________ m/s (take g = 10, one decimal place).
    inputConfig:
      placeholder: "3.2"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3.2", "3.16"]
      rejectedFeedback: "At the top, gravity supplies the centripetal demand: mg = mv²/r → v = √(10 × 1) ≈ 3.2 m/s. Faster is fine (the bucket's base adds force); slower, and gravity exceeds the demand — the water leaves the circle and visits your head."
    hint: "Set mg = mv²/r and solve for v."
    reflectionPrompt: "At exactly this speed, what force does the bucket bottom exert on the water at the top?"
  - id: phy-jun-m1-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A spin-dryer drum whirls clothes at high speed; water streams out through the perforations. Explain the separation WITHOUT using the words 'centrifugal' or 'flung' — strictly first-law and centripetal language. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [straight, tangent, drum, inward, wall, holes, first law, no inward]
      rejectedFeedback: "The drum wall bends the CLOTHES into a circle by pushing them inward. The water at a perforation gets no inward push — so it continues straight (first law), exits through the hole on its tangent, and leaves the circle. The clothes were forced to turn; the water simply wasn't."
    hint: "Who receives an inward force, and who doesn't? What does the first law do with the latter?"
    reflectionPrompt: "Re-tell a cream separator or salad spinner the same way. Does the denser or lighter component end up at the wall, and why?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "At the top of a rollercoaster loop taken at exactly minimum speed, the track's force on the car is:"
    options:
      - "Maximum"
      - "Zero — gravity alone supplies the entire centripetal demand"
      - "Equal to the car's weight"
      - "Directed outward"
    correctIndex: 1
    feedback: "Minimum speed is DEFINED by gravity paying the whole bill: mg = mv²/r. Passengers feel momentarily weightless — the track could vanish for that instant unnoticed."
  - type: MULTIPLE_CHOICE
    question: "A moving bicycle is far easier to balance than a stationary one chiefly because:"
    options:
      - "Air resistance steadies it"
      - "Spinning wheels resist having their axis tilted (gyroscopic stiffness), and steering lets the rider place the wheels back under the lean"
      - "The rider becomes heavier"
      - "Friction disappears at speed"
    correctIndex: 1
    feedback: "Both effects are real: angular momentum makes the wheels' axes stubborn, and — the larger share — a moving bike can steer its contact points back beneath the centre of gravity. Stationary, neither rescue is available."
---

# Hook

You can swing a bucket of water over your head and stay dry — *if* you keep one number on your side. A velodrome cyclist corners at 60 km/h on boards as slick as a dance floor, leaning so far over that her tyres barely need to grip at all. A rollercoaster hurls you upside down and the engineers can tell you, to the decimal, the speed below which you'd leave the seat.

All of yesterday's centripetal accounting comes alive when circles tilt, climb, and invert. Banking turns the ground itself into the inward payer. Vertical circles hire *gravity* onto the payroll — but gravity won't be told what to contribute, so the circle must be ridden at its terms. And the strangest spin of all pays in stability: wheels that refuse to fall over, tops that defy the floor, and the gyroscopic stubbornness inside every bicycle you've ever trusted. Today: spin, deployed.

# Lore Introduction

The Mechanica's yard at dawn: Vex stands beside the salvaged velodrome section — a great curved wall of timber, banked like a wave about to break — flanked by the cartwheel gyroscope and the famous bucket. "Three trials," he announces, in the tone of a man who has been looking forward to this all term. "First: the wall. You will run its curve at walking pace and slide off; then at a sprint, and the wall will hold you like a hand. Second: the bucket — over the head, around, dry or drenched by arithmetic alone. Third —" he spins the cartwheel gyroscope to a blurred hum and balances it, impossibly, on one fingertip of its axle, where it stands leaning but unfalling, precessing in slow defiance, "— the wheel that argues with gravity and *wins on points*. None of this is new law, junior. It is yesterday's invoice, paying for breakfast. Boots tight? The wall first."

# Core Learning

## Concept Introduction

**Banking: tilting the payer.** A surface's normal force points perpendicular to itself. Tilt the road by angle θ and that push leans inward: its horizontal component N sin θ contributes to mv²/r *without any friction*. At the **design speed**, banking pays the entire bill — tyres do no sideways work (the condition: tan θ = v²/gr). Velodromes (banked to 45°), TGV rail curves, and motorway slip-roads are all built to this equation; below design speed you slip subtly down-slope, above it up-slope, with friction making up small differences.

**Vertical circles: gravity joins the payroll — on its own terms.** Around a vertical loop the suppliers change by position:

- **Bottom**: track/rope must pay mv²/r *plus* fight gravity — total N = mv²/r + mg. (Biggest forces, biggest g-load: coaster seats press hardest here.)
- **Top**: gravity already points centripetally (inward = downward); the track supplies only the *remainder*: N = mv²/r − mg. The **minimum speed** is where the remainder hits zero:

```
mg = mv²/r   →   v_min = √(g r)
```

Slower than √(gr) at the top: gravity exceeds demand, the circle can't hold its contents — water exits the bucket, cars need shoulder harnesses and clothoid loops (tighter at the top: smaller r reduces v_min). At exactly v_min: momentary weightlessness, the track touching nothing.

**Separation by spin.** Drums and centrifuges (last lesson's 4,000g) exploit who *isn't* paid inward: unattached water exits spin-dryer perforations along tangents; in a sealed spinning tube, denser components out-stubborn lighter ones and migrate to the outer wall — cream separators, blood fractionation, uranium enrichment. All first-law physics; the word "flung" is banned on the premises.

**Gyroscopic stubbornness (a respectful sketch).** A spinning wheel carries *angular momentum* along its axis; tilting the axis means *changing* that momentum, which demands torque and time. Result: spinning things resist toppling and respond to pushes by **precessing** (turning the push 90° round). It steadies bicycles (alongside the bigger effect: steering the wheels back under you), rifle bullets, satellites, and ships' compasses. The full algebra is Senior-tier rotational mechanics; the stubbornness is yours to feel today.

## Why It Matters

- Banked design is everywhere transport turns: rail safety, road drainage-vs-grip compromises, aircraft banking (lift's horizontal component is the payer at altitude — no road required).
- Vertical-circle minimums govern rollercoaster law, aerobatics licensing, and the wall-of-death's insurance premiums.
- Spin separation is industrial civilisation's quiet workhorse: dairy, medicine, biotech, nuclear fuel — all centrifuge bills, paid at thousands of g.

## Worked Examples

**Example 1: The velodrome's design speed**
Track banked at 42°, bend radius 25 m: tan 42° ≈ 0.9 = v²/gr → v² = 0.9 × 10 × 25 = 225 → v = **15 m/s** (54 km/h). At that speed a cyclist could (in principle) corner on ice — the boards push exactly where the bill requires. Sprinters exceeding it press *up-slope* into extra grip; that's why finishing bursts climb the wall.

**Example 2: Sizing a loop**
A coaster crests a loop of top-radius 8 m. Minimum crest speed: √(10 × 8) ≈ 9 m/s. Designers run it at ~11 m/s: N = m(v²/r − g) = m(15 − 10) = 0.5mg — riders feel half their weight at the top (floaty, not free-falling), and at the bottom (r = 12 m, v ≈ 17 m/s after the descent) about 3.4g. The whole ride profile is this lesson's two formulas, tuned for thrill-with-margin.

**Example 3: The honey and the dross**
Calde's Foundry (she'd want this noted) clarifies wax by spinning melted comb in a perforated drum: wax and honey, forced into circles by the drum wall, separate as the denser honey out-muscles the wax outward through it... no — *check the physics*: both are bent inward by the wall; at the perforations, whatever sits there receives no inward push and exits on the tangent — and within the sealed annulus, the denser honey migrates outward through the lighter wax (it "wins" the contest of going-straight). The corrected telling matters: get the direction-language right and every separator in industry reads itself.

## Common Mistakes

- **"Banking pushes the car round"** — banking *redirects the normal force*; the car still needs speed appropriate to the design, or friction covers the difference.
- **Computing loop minimums at the bottom** — the critical point is the **top**, where gravity is the (potentially over-generous) inward payer.
- **"At the top you need force to hold you UP"** — at the top of a loop the demand is *downward* (centripetal = toward centre = down); gravity helps, the track adds the rest. Seats push down on you there.
- **Letting "centrifugal/flung" back into explanations** — spin separation is unpaid inward bills and straight-line exits; the banned words smuggle the phantom force back in.
- **Crediting gyroscopes with all bicycle balance** — steering-under-the-lean is the larger effect; the gyroscopic share is real but junior. (Experimenters have built rideable counter-rotating-wheel bikes to prove it.)

## Mental Model

Think of every exotic circle as **a payroll meeting for the same old invoice, mv²/r, with new staff**. Banking hires the *ground itself*: tilt the floor and its push — previously useless, straight up — leans in and starts contributing. Vertical circles hire *gravity*: a magnificent worker at the top of the loop (full wage, every time, no fatigue), dead weight at the bottom (working against you), and utterly non-negotiable about hours — ride slower than the wage it insists on paying, and the surplus shows up as your water, your car, your stomach leaving the circle. And the gyroscope? That's the employee with seniority: so much angular momentum invested along one axis that every demand to change direction is met with paperwork, delay, and a sideways compromise called precession.

## Mini Summary

- ✔ Banking: tilted normal force pays inward — tan θ = v²/gr at design speed, friction spared
- ✔ Vertical circles: top is critical; v_min = √(gr), where gravity alone pays (momentary weightlessness)
- ✔ Bottom of loops: track pays demand PLUS weight — maximum g-load
- ✔ Spin separation: the unpushed go straight; the denser out-stubborn the lighter outward
- ✔ Spinning axes resist tilting (gyroscopic stiffness) — bicycles, bullets, satellites

# Guided Practice Quest

Work through the guided steps to lean a velodrome's push inward, ride a bucket past √(gr), and write a spin-dryer's confession with the banned words left out.

# Solo Practice Quest

Three yard-trials of your own: (1) *The bucket* (outdoors, water you can afford): compute v_min for your arm-plus-bucket radius, convert to revolutions per second, practise above it — then report the physics of the one moment that worried you (the top), including what the bucket base was doing. (2) *The coin on a hanger* (classic): balance a coin on the hook-end of a coat hanger and swing it in a vertical circle; explain who pays the coin's bill at the top, and estimate your v_min. (3) *Gyroscopic interview*: spin a bicycle wheel (lifted off the ground) and try to tilt its axle quickly; describe the resistance and the strange sideways response in your own words, then check a video of gyroscopic precession and refine your description. Close with one paragraph: choose any fairground ride you know and identify, position by position around its circuit, who is paying the centripetal bill.

# Integration

**Engineering**: Banking, loop-shaping (clothoids — loops tightened at the crest to lower v_min where speed is lowest), and g-load profiles are the daily mathematics of ride engineers and aerobatic instructors; rail and road codes specify banking (super-elevation) per curve radius and speed class. Centrifuge engineering at 100,000g (ultracentrifuges) separates molecules themselves.

**Biology**: Your inner ear's semicircular canals are fluid rings that report rotation — and they're fooled by sustained spin exactly as the physics predicts (the fluid catches up; sensation fades; stopping then feels like counter-spin: dizziness decoded). Fighter pilots' g-suits fight the bottom-of-loop blood-pooling this lesson prices; black-out altitude-bills are mv²/r in arteries.

# Lore Conclusion

You end the day breathless and dry: the wall ridden at its design sprint, the bucket's full circle survived (one rehearsal puddle notwithstanding), the great gyroscope's slow, dignified refusals felt through both wrists. Vex racks the apparatus with unusual care. "Spin, ridden," he grants. "Which leaves the payroll's one unlimited account." He nods upward — past the yard, past the bell tower, to where the early stars are out. "Every circle we built today needed walls, wires, boards, or buckets. *Those* circles—" the stars wheel imperceptibly above the Observatory as they have all your life, "—run on no apparatus at all. Tomorrow, junior: the banker himself. Universal gravitation — the pull in your bones, audited from the apple to the Moon. Sleep early. We start before the stars go in."
