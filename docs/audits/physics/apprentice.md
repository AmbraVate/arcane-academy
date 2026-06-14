# Physics Curriculum Audit — Apprentice Tier

**Auditor:** Research Physicist / Professor of Physics (PhD, 20+ years research and higher education)
**Date:** 2026-06-14
**Tier mandate:** GCSE+ / lower A-level — build physical intuition, correct everyday misconceptions, establish the measurement discipline that underpins all future tiers.

---

## 1. Verdict at a Glance

The Apprentice tier is a **strong, coherent foundation**. Measurement discipline is established early and carried consistently throughout. Newtonian mechanics, energy, waves, and heat are covered with appropriate rigour for the level: qualitative reasoning dominates but is always anchored to a single quantitative relationship, which is introduced carefully and exercised with at least one worked numerical example. Narrative framing (NASA Mars Climate Orbiter crash, Saturn V, Voyager 1, Herschel/Ritter IR/UV discoveries) is well-chosen — historically accurate and pedagogically motivated, not decorative. Common-mistake sections are notably strong: the "flat means different things on d-t and v-t graphs" and the Newton's-Third-Law "everything cancels" fallacy receive the direct treatment they need at this level.

Minor concerns are proportional, not structural. Several topics in the catalogue (everyday_physics, forces, light, particle_theory, physical_quantities, scientific_investigation, sound, states_of_matter, mini_project) were not sampled but appear in the directory; if they follow the pattern of the sampled lessons their quality is likely comparable. The chief gap is absence of uncertainty propagation beyond the introductory mention in phy-app-m1-01; this is deferred appropriately to Junior but should be cross-referenced. Ratio-of-similars dimensional analysis is absent (a technique the A-level curriculum expects). Energy conservation as an over-arching principle is implicit but never stated as a law.

**Overall rating: 4 / 5 — publish with the targeted changes below.**

---

## 2. KEEP

These elements are working well and must not be altered.

- **phy-app-m1-01** ("Why Measurement Is the Heart of Physics") — the three-component definition (value, unit, uncertainty) and the NASA Mars Climate Orbiter hook are exemplary for this level. The lesson earns the right to introduce uncertainty before teaching any physics. Keep verbatim.
- **phy-app-m1-02** ("Units and the SI System") — the modern definitions via fixed constants (exact metre, kilogram via Planck constant) are handled accurately and without over-claiming. Many A-level texts still carry the artefact-kilogram; this lesson is ahead of them. Keep.
- **phy-app-m2-07 / m2-08 / m2-09** (Newton's three laws) — the sequence is correctly ordered, each law is stated precisely, and the common-mistake treatment (inertia misunderstood as a force; "equal and opposite means cancelling"; free-body diagram errors) is the best this auditor has seen at apprentice level. Keep intact.
- **phy-app-m1-07** ("Equations as Sentences") — the balance-rule presentation of equation rearrangement, with units carried through each step, is the correct way to introduce algebra in a physics context. Keep.
- **phy-app-m3-01** ("What Is a Wave?") — transverse/longitudinal distinction introduced by mechanical analogy (seagull, crowd wave), not by abstract definition. Correct and memorable. Keep.
- **phy-app-m3-10** ("Beyond Visible Light") — historical specificity (Herschel 1800, Ritter 1801) with a reproducible experiment (remote-control camera revealing IR). Scientifically accurate and engaging. Keep.
- **phy-app-m2-04 / m2-06** (speed/velocity and motion graphs) — the vector distinction is made correctly without vector algebra. The motion-graph lesson directly addresses the most common GCSE examination error. Keep.
- **phy-app-m4-04** ("Heat versus Temperature") — the microscopic distinction (temperature = average KE, heat = energy in transit) is precisely stated and is correct thermodynamics; many introductory courses skip this distinction and create problems later. Keep.

---

## 3. CHANGE

These elements require revision to the existing lesson content.

- **phy-app-m2-10** ("Work and Power"): W = Fd is stated without the cosine factor or any acknowledgement that it applies only when force and displacement are parallel. At A-level, students immediately encounter W = Fd cos θ. Add one sentence: "This formula applies when the force acts in exactly the direction of motion — when force and displacement point in different directions, only the component of force along the motion counts." This prepares for Junior without introducing vectors prematurely.
- **phy-app-m1-07** ("Equations as Sentences"): the lesson teaches rearrangement but does not mention dimensional analysis as a checking technique (checking that both sides of a rearranged equation have the same units). Add a single worked example: rearrange v = d/t to get d = vt and verify both sides give metres. One paragraph; no new concepts required.
- **phy-app-m2-08** ("Newton's Second Law"): the Saturn V worked example uses F_net = F_thrust − W but does not explicitly label this as the *net* force or show the free-body diagram in the text before writing the equation. Students at this level conflate total force with net force. Add "Net (resultant) force = thrust − weight = 3.5 × 10⁷ − 2.85 × 10⁷ ≈ 6.5 × 10⁶ N" as an explicit step before substituting into F=ma.
- **phy-app-m3-01** ("What Is a Wave?"): the mechanical vs electromagnetic distinction is correct, but no wavelength-frequency-speed relation (v = fλ) appears anywhere in the wave fundamentals module sampled. This formula must appear no later than the wave_fundamentals module. Confirm it appears in a later lesson (phy-app-m3-xx) and add a forward reference here if so; if absent, add it.

---

## 4. UPDATE

Factual or framing updates required.

- **phy-app-m1-02** (SI units): the kilogram redefinition (2019, via fixed Planck constant) is mentioned correctly, but the metre is described as "exact" without noting that it is defined via the fixed speed of light (c = 299,792,458 m/s exactly). Adding one clause — "defined by fixing the speed of light" — turns a correct statement into a precise one, and mirrors what students will encounter in A-level physics specifications.
- **phy-app-m4-04** (thermal energy): the lesson correctly defines K = °C + 273 but the exact value is 273.15 (0 °C = 273.15 K). At apprentice level, 273 is acceptable as an approximation, but add a parenthetical "(more precisely 273.15 K)" so students do not carry the truncation into A-level calculations where it costs marks.
- **phy-app-m3-10** (electromagnetic spectrum): the lesson mentions Herschel's infrared and Ritter's ultraviolet discoveries but does not name the frequency ordering of the full spectrum. A diagram or list (radio → microwave → IR → visible → UV → X-ray → gamma) with approximate wavelength ranges should appear somewhere in the electromagnetic_spectrum module. Confirm it exists in a later lesson; if not, add it here.

---

## 5. REMOVE

Elements that should be deleted or substantially cut.

- No entire sections or lessons identified for removal. The Apprentice tier is lean and focused.
- In **phy-app-m2-06** (motion graphs): the lesson correctly distinguishes d-t and v-t gradients but contains a redundant third reminder in the Mini Summary that repeats the same point already made in Common Mistakes and the Core Learning section. Cut one of the three instances to avoid the impression that the same error must be stated three times to stick.

---

## 6. GAPS

Topics the tier mandate requires but which are absent or under-served.

- **Dimensional analysis as a technique**: all A-level specifications include checking equations by unit analysis. The apprentice tier teaches units (phy-app-m1-02) and equations (phy-app-m1-07) but never combines them into the checking skill. Add one lesson or a sub-section to phy-app-m1-07 covering: check that rearranged equations are dimensionally consistent; derive that speed must be distance/time from units alone.
- **Energy conservation as a named principle**: Work (W=Fd) and gravitational PE (implicit in Newton lessons) and KE appear separately, but no lesson names the conservation of energy as a law and shows KE + PE = constant for a falling object. At A-level this is a required outcome. Recommend adding one lesson to work_and_energy: "Energy Conservation: the Universal Accountant" between the current work-and-power lesson and any thermal lessons.
- **Uncertainty propagation (introductory)**: phy-app-m1-01 correctly introduces the concept of uncertainty and the ± notation. However, no apprentice lesson shows how to add absolute uncertainties (A + B → ΔA + ΔB) or handle percentage uncertainties. This is an A-level required practical skill. Either add it here or add a clear forward pointer to the Junior tier lesson where it appears.
- **Electrostatics / electric charge basics**: the electromagnetic_spectrum module covers EM waves but the apprentice tier appears to contain no introduction to electric charge, static electricity, or simple circuits. GCSE specifications require current, voltage, and resistance at this level. Confirm whether these topics exist in the unsampled `everyday_physics` or `forces` directories; if absent, this is a significant gap requiring new lessons.

---

## 7. Practice & Assessment

- **Guided steps** are consistently strong across all sampled lessons: multiple-choice, fill-blank, and short-text formats are mixed appropriately; marking rules use `CONTAINS` and `NORMALIZED` modes sensibly; rejected-feedback messages teach the concept, not just announce the error. The Saturn V calculation in phy-app-m2-08 and the Aisha/Ben stair-climbing comparison in phy-app-m2-10 are particularly effective assessment anchors.
- **Solo Practice Quests** are well-calibrated: they require synthesis (write an explanation; design a measurement) rather than computation alone, which is appropriate for apprentice level where the primary goal is conceptual clarity.
- **Micro-checkpoints** are uniformly high quality; incorrect-answer feedback consistently explains why the distractor is wrong rather than merely repeating the correct answer.
- **Concern**: the motion-graph lesson (phy-app-m2-06) has a solo quest that asks students to sketch graphs, but there is no mechanism described for submitting or marking a hand-drawn sketch in the platform. Either convert the sketch task to a descriptive-text task ("describe in words what the graph would look like") or note that this requires a free-draw input type not currently indicated in the YAML.

---

## 8. Prioritized Action List

| Priority | Action | Lesson(s) |
|----------|--------|-----------|
| 1 (Critical) | Audit `everyday_physics`, `forces`, `light`, `particle_theory`, `physical_quantities`, `scientific_investigation`, `sound`, `states_of_matter` directories for completeness and quality | All unsampled |
| 2 (High) | Add v = fλ to the wave_fundamentals module if absent; add forward reference from phy-app-m3-01 | phy-app-m3-01 and module |
| 3 (High) | Add energy conservation lesson (KE + GPE = constant) to work_and_energy module | New lesson |
| 4 (High) | Confirm basic electricity (charge, current, voltage, resistance) exists; add if absent | New lessons if needed |
| 5 (Medium) | Add dimensional analysis checking to phy-app-m1-07 | phy-app-m1-07 |
| 6 (Medium) | Add W = Fd cos θ note to phy-app-m2-10 | phy-app-m2-10 |
| 7 (Medium) | Clarify net force step explicitly in Saturn V worked example in phy-app-m2-08 | phy-app-m2-08 |
| 8 (Medium) | Add forward pointer from phy-app-m1-01 uncertainty introduction to Junior propagation lesson | phy-app-m1-01 |
| 9 (Low) | Add "(more precisely 273.15 K)" to phy-app-m4-04 | phy-app-m4-04 |
| 10 (Low) | Remove one of three redundant restatements of d-t/v-t mistake in phy-app-m2-06 | phy-app-m2-06 |
