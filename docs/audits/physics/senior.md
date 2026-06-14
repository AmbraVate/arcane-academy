# Physics Curriculum Audit — Senior Tier

**Auditor:** Research Physicist / Professor of Physics (PhD, 20+ years research and higher education)
**Date:** 2026-06-14
**Tier mandate:** Second / third-year undergraduate — mathematical rigour expected throughout; students should be able to derive key results, critically evaluate assumptions, and connect multiple physical frameworks.

---

## 1. Verdict at a Glance

The Senior tier is **university-grade**. The five sampled lessons — rotational mechanics, simple harmonic motion and resonance, electric fields and potential, special relativity, and quantum physics (phy-sen-m3-02, the established quality benchmark) — all meet or exceed the standard of a well-written second-year undergraduate text. Mathematical rigour is consistent: every formula is derived or stated with clear conditions; worked examples are numerical with realistic values; the Common Mistakes sections address errors specific to the level rather than recycling apprentice-level confusions.

What elevates the Senior tier above the lower tiers is vertical integration. Each lesson explicitly references earlier platform content (the pendulum Trials from Apprentice, the gravitation inverse-square from Junior, the wave curves from Junior wave lessons) and projects forward to later lessons or tiers. This scaffolding is rare in online platforms and directly serves the platform's "university-equivalent depth" promise.

The primary concern is coverage of the unsampled topics (atomic_physics, complex_motion, data_analysis, electromagnetic_waves, magnetic_fields, maxwells_theory, modelling, nuclear_physics, numerical_methods, scientific_computing, wave_mechanics, mini_project). Topics sampled are clustered in m1 (dynamics) and m2/m3 (electromagnetism, modern physics); the data analysis, modelling, scientific computing, and numerical methods cluster may vary in quality since it requires a different kind of pedagogy (computational rather than pen-and-paper). The quantum physics lesson (phy-sen-m3-02) is confirmed as benchmark quality; the relativity lesson (phy-sen-m3-01) is of equivalent standard.

**Overall rating: 5 / 5 for sampled content — the benchmark is met throughout. Audit the computational and wave-mechanics cluster before full publication.**

---

## 2. KEEP

- **phy-sen-m3-02** ("Quantum Physics"): the established benchmark. E = hf, λ = h/p, Δx·Δp ≥ ℏ/2 — all derived in the lesson with correct historical context (Planck 1900, Einstein 1905, de Broglie 1924, Heisenberg 1927). The photoelectric effect treatment (threshold frequency, work function, Einstein's photon explanation over Lenard's waves objection) is the correct pedagogical sequence. The guided step with the numerical uncertainty calculation is precisely set. Keep verbatim and use as the internal quality standard for all Senior lessons.
- **phy-sen-m3-01** ("Special Relativity: Space and Time Rewritten"): the two postulates are stated with appropriate precision (laws of physics identical in all inertial frames; c is invariant). The light-clock derivation of time dilation is the correct approach — it derives γ rather than asserting it. The muon example (γ ≈ 15, proper lifetime 2.2 μs → 33 μs in lab frame, 15 km penetration) is the standard experimental confirmation and is handled correctly. The E = mc² application (Sun losing 4.2 × 10⁹ kg/s) gives a viscerally useful scale. Keep.
- **phy-sen-m1-01** ("Rotational Mechanics"): τ = Iα, L = Iω, KE = ½Iω² are all present; the table of moments of inertia (hoop mr², disc ½mr², sphere ⅖mr²) is correct; the skater spin-up calculation (I₁ω₁ = I₂ω₂ → ω₂ = 8 rad/s) is correct and the numbers are well-chosen. Gyroscopic precession (Ω_precession = τ/L) is included — many second-year courses omit this. Keep.
- **phy-sen-m1-02** ("SHM and Resonance"): the amplitude-independence of period (deriving the cancellation from F = −kx, not just asserting it) is the correct treatment. The Millennium Bridge / Tacoma Narrows distinction (aeroelastic flutter vs true resonance — the lesson correctly distinguishes them) is the kind of precision that separates a good course from a great one. The worked example on prescribing critical damping (c = 2√(km) = 160 kg/s) is quantitative and applicable. Keep verbatim.
- **phy-sen-m2-01** ("Electric Fields and Potential"): E = F/q, E = kQ/r², E = V/d — all three forms are present, clearly motivated, and connected. The potential-as-landscape analogy (charge as a marble on a rubber sheet, field as gradient of potential) is accurate and memorable. The electron-volt derivation (W = qΔV → naming a unit after it) is elegant. The "field exists whether or not a test charge is present" correction is exactly the misconception that needs addressing at this level. Keep.

---

## 3. CHANGE

- **phy-sen-m1-01** (Rotational Mechanics): the lesson correctly states L = Iω and derives the skater example, but the angular momentum vector and the right-hand rule for its direction are absent. At second-year level, the direction of L is essential — it determines the direction of precession, the stability of spinning tops, and the physics of gyroscopes. Add a sub-section: "Angular momentum is a vector, pointing along the rotation axis by the right-hand rule: curl the fingers of the right hand in the direction of rotation, and the thumb points in the direction of L." Follow with the precession formula's vector form (dL/dt = τ, where both are vectors).
- **phy-sen-m2-01** (Electric Fields): Gauss's law is the natural second-year generalization of E = kQ/r² and underlies the parallel-plate result (E = σ/ε₀). The lesson derives E = V/d empirically but does not connect it to Gauss's law. At second-year level, add a boxed note: "Gauss's law — the rigorous foundation. The flux of E through any closed surface equals Q_enclosed/ε₀. For a parallel-plate capacitor this immediately gives E = σ/ε₀ = Q/(ε₀A), which equals V/d." This is one paragraph; it does not require a new lesson.
- **phy-sen-m3-01** (Special Relativity): the lesson covers time dilation and length contraction but omits the relativity of simultaneity, which is logically prior to both. The order of exposition (time dilation first, then length contraction) is the standard A-level approach but at second-year level the thought-experiment derivation of simultaneity failure should come first, because it motivates why the Lorentz transformation is needed. Add a sub-section before the time-dilation derivation: "Simultaneity is relative: two lightning strikes simultaneous in the platform frame are not simultaneous in the train frame — and vice versa. This is not a clock error; it is a feature of spacetime itself."
- **phy-sen-m1-02** (SHM): the lesson correctly covers the energy shuttle (KE ↔ PE) but does not write the explicit energy equations x(t) = A cos(ωt), v(t) = −Aω sin(ωt), and KE + PE = ½mA²ω² = constant. The time-domain solutions are required at second-year level for any work with coupled oscillators, Fourier analysis, or the quantum harmonic oscillator. Add these equations with derivation from F = ma and x = A cos(ωt).

---

## 4. UPDATE

- **phy-sen-m3-01** (Special Relativity): the lesson states the second postulate as "the speed of light is the same for all inertial observers." This is correct but incomplete: the full statement is "the speed of light *in vacuum* is c regardless of the motion of the source or the observer." The "in vacuum" clause matters — light travels slower in glass, which confused many people in 1905 and still confuses students today. Add "in vacuum" to the postulate statement.
- **phy-sen-m3-02** (Quantum Physics): the lesson correctly states the Heisenberg uncertainty principle as Δx·Δp ≥ ℏ/2. Some sources write ΔxΔp ≥ ℏ (Robertson's formulation with different prefactor convention). Add a footnote-equivalent note: "The exact prefactor ℏ/2 applies to Gaussian wave packets; some texts write ℏ as a lower bound — both are correct depending on the definition of standard deviation used. The order-of-magnitude estimate ΔxΔp ~ ℏ is always sufficient for estimation problems."
- **phy-sen-m2-01** (Electric Fields): Coulomb's constant k is given as 9 × 10⁹ N·m²/C². This is the correct approximation but the exact value is k = 1/(4πε₀) = 8.988 × 10⁹ N·m²/C². The ε₀ = 8.854 × 10⁻¹² F/m formulation is universal in university physics. Add: "k = 1/(4πε₀) where ε₀ = 8.854 × 10⁻¹² F/m is the permittivity of free space — this form connects directly to Maxwell's equations in the next lessons."

---

## 5. REMOVE

- No entire lessons or sections identified for removal in the sampled content.
- **phy-sen-m1-02** (SHM): the worked example "Example 3: The swing and the tower" (Taipei 101 tuned mass damper) is excellent but the parenthetical "(the tuned mass damper you met in the Trials' integration)" appears to assume prior platform content that may not exist for all users. Verify the cross-reference is valid; if not, add one sentence of independent context.

---

## 6. GAPS

- **Maxwell's equations in integral form**: the `maxwells_theory` directory is unsampled, but this is the most important topic in the Senior EM module. Maxwell's equations are the culmination of the Junior induction lesson (Faraday) and the Senior fields sequence (Gauss). Confirm that the module presents all four equations in integral form, derives the wave equation, and obtains c = 1/√(ε₀μ₀). This is a non-negotiable second-year outcome.
- **Wave mechanics and Schrödinger equation**: `wave_mechanics` is unsampled. At second/third-year level, the time-independent Schrödinger equation (TISE) must appear, solved for at least the infinite square well and the harmonic oscillator. The quantum physics lesson (phy-sen-m3-02) correctly introduces wave-particle duality and the uncertainty principle, but without the TISE and its solutions the tier does not reach second-year quantum mechanics standard.
- **Nuclear physics**: `nuclear_physics` is unsampled. Radioactive decay (N = N₀e^{−λt}), half-life, binding energy per nucleon, and Q-values for fission and fusion are all second-year requirements. Confirm these are covered.
- **Data analysis and numerical methods**: `data_analysis`, `numerical_methods`, and `scientific_computing` are unsampled. At second-year level, students should be able to perform linear regression, propagate uncertainties through multi-variable functions (∂f/∂x style), and understand basic numerical integration (Euler, Runge-Kutta). This cluster requires a different kind of lesson design (computational notebooks rather than formula derivations); confirm the format is appropriate.
- **Relativistic momentum and energy**: phy-sen-m3-01 correctly derives time dilation and mentions E = mc², but the full relativistic energy-momentum relation E² = (pc)² + (mc²)² is absent. This is needed for any particle-physics calculation and is the natural sequel. Confirm whether it appears in a later lesson in the relativity module.

---

## 7. Practice & Assessment

- The guided-step design in the Senior tier is the strongest on the platform. The light-clock derivation (guided as a sequence of algebraic steps) and the muon calculation in phy-sen-m3-01, the uncertainty-principle estimation in phy-sen-m3-02, and the damping coefficient calculation in phy-sen-m1-02 are all examples of assessment that requires genuine physical reasoning rather than formula recall.
- The RUBRIC_REFLECTION solo assessments at this level are well-calibrated: the model answers are concise but mathematically complete; the rubric items decompose the learning objective into independently assessable components.
- **Concern — phy-sen-m1-01** (Rotational Mechanics): the solo practice quest asks students to calculate the moment of inertia of a compound object (e.g., a bicycle wheel with spokes). This requires the parallel-axis theorem (I = I_cm + Md²), which is mentioned in passing in the lesson but not derived. Students will need it to complete the assessment. Either add the derivation to the lesson or narrow the solo quest to avoid parallel-axis problems.
- The micro-checkpoints across all sampled Senior lessons discriminate correctly between superficially plausible distractors: the SHM checkpoint on critical damping (which correctly identifies it as fastest-return-without-overshoot, not maximum energy dissipation) and the relativity checkpoint on the muon are particularly well-constructed.

---

## 8. Prioritized Action List

| Priority | Action | Lesson(s) |
|----------|--------|-----------|
| 1 (Critical) | Audit maxwells_theory module — confirm all 4 equations in integral form, wave equation derivation, c = 1/√(ε₀μ₀) | maxwells_theory module |
| 2 (Critical) | Audit wave_mechanics module — confirm TISE, infinite square well, harmonic oscillator solutions | wave_mechanics module |
| 3 (Critical) | Audit nuclear_physics module — confirm decay law, half-life, binding energy, fission/fusion Q-values | nuclear_physics module |
| 4 (High) | Add relativity of simultaneity sub-section before time-dilation derivation | phy-sen-m3-01 |
| 5 (High) | Add angular momentum vector direction (right-hand rule) and vector form of precession | phy-sen-m1-01 |
| 6 (High) | Add explicit time-domain SHM solutions x(t), v(t), energy expression | phy-sen-m1-02 |
| 7 (High) | Add parallel-axis theorem derivation or narrow solo quest scope | phy-sen-m1-01 |
| 8 (Medium) | Add Gauss's law boxed note connecting to parallel-plate result | phy-sen-m2-01 |
| 9 (Medium) | Add "in vacuum" to second postulate of special relativity | phy-sen-m3-01 |
| 10 (Medium) | Add k = 1/(4πε₀) with ε₀ value; connect to Maxwell's equations | phy-sen-m2-01 |
| 11 (Medium) | Audit data_analysis, numerical_methods, scientific_computing — confirm computational lesson format | Unsampled cluster |
| 12 (Low) | Add uncertainty-principle prefactor note (ℏ/2 vs ℏ convention) | phy-sen-m3-02 |
