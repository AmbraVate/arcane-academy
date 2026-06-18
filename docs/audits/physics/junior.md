# Physics Curriculum Audit — Junior Tier

**Auditor:** Research Physicist / Professor of Physics (PhD, 20+ years research and higher education)
**Date:** 2026-06-14
**Tier mandate:** A-level / first-year undergraduate — rigorous quantitative treatment of all major classical topics; students should leave able to solve multi-step problems and explain the physical reasoning behind each step.

---

## 1. Verdict at a Glance

The Junior tier is **genuinely first-year undergraduate in standard**. The sampled lessons — momentum/impulse, universal gravitation, resistance/Ohm's law, electromagnetic induction, gas laws, entropy, elasticity, and fluid pressure — all deliver the required mathematics, historical context, and worked numerical examples. Several lessons are outstanding: phy-jun-m3-07 ("The One-Way Arrow") on entropy is the best statistical-mechanics introduction for this level this auditor has encountered in any platform or textbook, correctly grounding irreversibility in combinatorics rather than invoking the second law by fiat. The hydraulic-jack calculation in phy-jun-m4-04 and the Newton-Moon test in phy-jun-m1-07 are similarly strong anchors.

Structural coherence is good: the gravitation lesson builds explicitly on the Junior inverse-square law; the gas laws lesson builds on the apprentice particle model; the entropy lesson invokes the statistical counting that underpins it. Cross-tier continuity is visible and deliberate.

Primary concerns: the unsampled topics (circular_motion, electric_charge, energy_systems, engineering_physics, magnetism, measurement_systems, mechanical_systems, thermal_applications, mini_project) require audit — the sampled cluster is biased toward mechanics and thermodynamics, and the electromagnetic and measurement clusters may diverge in quality. More critically, the tier contains no vector treatment of forces beyond magnitude; A-level specifications require component resolution and the parallelogram rule. Uncertainty propagation is mentioned in Junior mandate but not evidenced in the sampled lessons.

**Overall rating: 4.5 / 5 — outstanding in sampled content; complete the audit of unsampled topics before publishing the full tier.**

---

## 2. KEEP

- **phy-jun-m3-07** ("The One-Way Arrow — Entropy"): the statistical-mechanical derivation of irreversibility through combinatorics (counting microstates: 252 arrangements for 5-and-5 vs 1 for all-heads) is precisely right. This is the correct order of exposition — entropy as statistics, second law as consequence — and it is rare at any level, let alone first-year. The worked-out arithmetic is clean and the dough-kneading analogy is accurate (it maps directly to the stretch-and-fold picture later used in chaos theory in the Lead tier, providing a pleasant vertical continuity). Keep verbatim.
- **phy-jun-m1-07** ("Universal Gravitation"): Newton's Moon test (centripetal acceleration computed from orbit radius and period, compared with g/r²) is the correct way to verify the inverse-square law empirically. G = 6.67 × 10⁻¹¹ N·m²/kg² is given with correct units. "Orbit as perpetual fall" framing is conceptually exact. Keep.
- **phy-jun-m1-01** ("Momentum and Impulse"): p = mv and FΔt = Δp are stated in that order (momentum defined before impulse), which is the correct logical dependency. The cricket-ball worked example (Δp = 11.2 kg·m·s⁻¹, F = 11,200 N) is well-scaled for intuition (striking that 1 ms contact produces a force equal to lifting a tonne). Keep.
- **phy-jun-m4-01** ("Elasticity and Hooke's Law"): the archer-bow energy calculation (E = ½ × 400 × 0.25² = 12.5 J — correction noted below — arrow gets 40 J of 50 J available, v ≈ 52 m/s via ½mv² = 40) is a well-chosen worked example. The distinction between elastic limit and yield point is correctly stated. Keep structure; correct one numerical error (see CHANGE section).
- **phy-jun-m4-04** ("Pressure in Fluids"): Pascal's principle with the hydraulic jack (50 N on 2 cm² → 2500 N on 100 cm²) is correct and the calculation is explicit. The depth-pressure formula P = ρgh is given with correct SI units. The "pressure is direction-blind" statement is accurate and addresses a common student misconception. Keep.
- **phy-jun-m2-04** ("Resistance and Ohm's Law"): the ohmic/non-ohmic distinction using the filament lamp I-V curve is correctly placed — students who only see V = IR think everything is ohmic. The four factors (length, cross-sectional area, material, temperature) are all physically motivated. Keep.
- **phy-jun-m2-10** ("Inducing a Voltage"): the "only CHANGE induces" principle is the essential insight and it is correctly stated and motivated. Lenz's law framed as energy conservation (not just a rule about direction) is the right physical motivation. Keep framing.
- **phy-jun-m3-01** ("Gas Laws — Pressure, Volume, Temperature"): the motorway tyre example (290 K → 320 K, ⇒ 353 kPa) uses all three variables and makes the kelvin requirement viscerally obvious (the lesson demonstrates the non-sense that results if you try it in Celsius). Keep.

---

## 3. CHANGE

- **phy-jun-m4-01** ("Elasticity and Hooke's Law"), worked example energy figure: the lesson states E = ½ × 400 × 0.25 = 50 J. The correct formula is E = ½kx² = ½ × 400 × 0.25² = ½ × 400 × 0.0625 = 12.5 J, not 50 J. This is a significant numerical error — the archer's bow drawn 25 cm storing 50 J would require the calculation ½ × 400 × 0.5² = 50 J (i.e., a 50 cm draw), which contradicts the stated value of 0.25 m. Either correct x to 0.5 m or correct the energy to 12.5 J and adjust the downstream arrow-velocity calculation accordingly (with 12.5 J total and 80% efficiency → 10 J to arrow → v = √(2 × 10 / 0.05) ≈ 20 m/s). This error propagates into the velocity answer and will mislead students.
- **phy-jun-m2-10** ("Inducing a Voltage"): Faraday's law is correctly stated qualitatively but the quantitative form (EMF = −dΦ/dt, where Φ = BA cos θ) is absent. At first-year undergraduate level this formula is expected. Add the quantitative statement with a simple calculation: a coil of 100 turns, area 0.01 m², field changing from 0 to 0.5 T in 0.1 s produces EMF = N × ΔΦ/Δt = 100 × (0.5 × 0.01)/0.1 = 5 V. This does not exceed the tier mandate and is required for the electromagnetic induction topic to be complete at A-level.
- **phy-jun-m3-07** ("Entropy"): the Boltzmann equation S = k_B ln Ω is the natural quantitative summary of the counting argument the lesson makes so well, and it is absent. Add it as the concluding formula with k_B = 1.38 × 10⁻²³ J/K stated. A numerical example is not required (the numbers become unmanageable for macroscopic systems), but the equation should appear so students can connect the lesson's counting to the formula they will encounter in any undergraduate thermodynamics text.

---

## 4. UPDATE

- **phy-jun-m1-07** (gravitation): the lesson correctly performs Newton's Moon test but uses g = 9.8 m/s² without noting that this is the surface value of g_Earth and varies with altitude and location (relevant since the lesson has just introduced the inverse-square law). Add one sentence: "The surface value g = 9.8 m/s² is itself the field of the Earth at its surface radius; the inverse-square law predicts g varies across the planet and with altitude — a fact exploited in gravimetric surveys."
- **phy-jun-m2-04** (Ohm's law): the lesson lists temperature as a factor affecting resistance but does not distinguish between materials with positive temperature coefficients (metals: resistance increases with temperature) and negative (semiconductors, NTCs: resistance decreases). Both appear in A-level specifications and in the filament-lamp curve already shown. Add one sentence distinguishing the two behaviours.
- **phy-jun-m4-04** (fluid pressure): Archimedes' principle (buoyancy = weight of fluid displaced) is the natural sequel to P = ρgh and appears in all A-level specifications, but it is absent from the sampled lesson. Confirm whether it appears later in the fluid_physics module; if not, add it with a single worked example (a steel cube in water).

---

## 5. REMOVE

- No entire lessons or sections identified for removal.
- **phy-jun-m3-01** (Gas Laws): the "three dials" analogy (P, V, T as dials on a dashboard) is vivid and useful. However, the lesson uses it in the Mini Summary and again in the Core Learning conceptual section, which creates redundancy. The Mental Model section can subsume the dial analogy; remove the second instance in Mini Summary.

---

## 6. GAPS

- **Vector resolution and component forces**: the Junior tier mandate requires students to resolve forces into components and apply equilibrium conditions. This is central to inclined-plane problems, tension calculations, and the entire engineering_physics module. The sampled lessons treat forces as scalars (magnitudes only). If the `forces` and `mechanical_systems` directories (unsampled) do not cover this, it is a critical gap requiring new lessons.
- **Uncertainty propagation**: the tier mandate specifies this skill. No sampled lesson demonstrates propagation of absolute uncertainties (addition rule) or percentage uncertainties (multiplication rule). Confirm whether `measurement_systems` covers this; if not, add it. Students who cannot propagate uncertainties cannot correctly interpret any of the worked examples in the tier.
- **Conservation of momentum in 2D**: phy-jun-m1-01 covers 1D impulse-momentum. Two-dimensional collisions (glancing, using x- and y-components) are an A-level requirement and depend on the vector treatment noted above.
- **Electric potential and capacitance**: the Junior tier covers Ohm's law and electromagnetic induction (sampled) and presumably electric charge (unsampled), but capacitance (Q = CV, energy = ½CV²) is a required A-level topic and was absent from the sampled content. Confirm its location.
- **Gravitational potential energy and escape velocity**: phy-jun-m1-07 correctly covers the inverse-square force law but the gravitational potential (V = −GM/r) and escape velocity (v_esc = √(2GM/r)) are natural sequels required at first-year undergraduate level. Confirm whether they appear in a later lesson in the gravitation module.

---

## 7. Practice & Assessment

- Assessment quality is uniformly high in all sampled lessons. The momentum cricket-ball example, the Moon-test calculation, the hydraulic-jack force calculation, and the gas-law tyre example are all carefully calibrated: numbers are realistic, intermediate steps are shown, and the final answers are physically interpretable ("a force equal to lifting a tonne").
- The **SHORT_TEXT** guided steps in phy-jun-m3-07 (entropy) and phy-jun-m2-10 (induction) are the strongest assessments in the sampled tier: they require causal reasoning, not formula recall, which is exactly right for conceptually difficult topics.
- **Concern — phy-jun-m4-01** (elasticity): as noted, the energy calculation contains a numerical error (50 J for x = 0.25 m with k = 400 N/m should be 12.5 J). This will cause students who check with the guided step to distrust the lesson, or worse, to distrust their own correct calculation.
- The RUBRIC_REFLECTION solo assessments are well-written; the model answers are concise and accurate. The keyword lists are appropriate for fuzzy matching — they focus on physical concepts, not on exact phrasing.

---

## 8. Prioritized Action List

| Priority | Action | Lesson(s) |
|----------|--------|-----------|
| 1 (Critical) | Fix energy calculation error: E = ½ × 400 × 0.25² = 12.5 J (not 50 J); propagate correction to velocity | phy-jun-m4-01 |
| 2 (Critical) | Audit unsampled topics (circular_motion, electric_charge, energy_systems, engineering_physics, magnetism, measurement_systems, mechanical_systems, thermal_applications) for completeness | All unsampled |
| 3 (High) | Add quantitative Faraday's law (EMF = N·ΔΦ/Δt) with worked example | phy-jun-m2-10 |
| 4 (High) | Add Boltzmann equation S = k_B ln Ω as conclusion to entropy lesson | phy-jun-m3-07 |
| 5 (High) | Confirm vector resolution / force components covered; add if absent | mechanical_systems or new |
| 6 (High) | Confirm uncertainty propagation covered; add if absent | measurement_systems or new |
| 7 (Medium) | Add positive/negative temperature coefficient distinction | phy-jun-m2-04 |
| 8 (Medium) | Confirm Archimedes' principle present in fluid_physics module | fluid_physics module |
| 9 (Medium) | Confirm gravitational potential V = −GM/r and escape velocity present in gravitation module | gravitation module |
| 10 (Low) | Remove redundant dial-analogy restatement from Mini Summary | phy-jun-m3-01 |
