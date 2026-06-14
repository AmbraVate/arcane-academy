---
id: phy-jun-m3-06
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m3
moduleTitle: "Module 3: Thermodynamics"
moduleGlyph: "♨️"
moduleSortOrder: 3
topicSlug: energy_systems
topicTitle: "Energy Systems"
topicSortOrder: 2
title: "Energy Systems at Scale"
sortOrder: 6
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Compare energy sources by power density, dispatchability, and efficiency chain
  - Explain why grids need storage and what physics each storage type uses
  - Analyse a national energy system as one chained Sankey
integrationDomains: [engineering, earth_science]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Compares at least three energy sources on output profile and controllability
    - Explains storage need — supply and demand must balance second by second
    - Matches storage technologies to their physics (mgh, ½mv², chemical, thermal)
    - Reads a national-scale energy chain including its major losses
  keywords: [grid, dispatchable, storage, pumped, battery, baseload, balance, capacity]
  modelAnswer: |
    A grid must balance generation and demand second by second, which makes sources' profiles
    matter as much as their totals: dispatchable stations (gas, hydro) follow demand on
    command, nuclear runs steady, and wind and solar deliver weather-shaped output that
    storage must reshape. Storage technologies are the energy stores of earlier modules,
    industrialised: pumped hydro banks mgh by the lake-full, batteries bank chemistry,
    flywheels bank ½mv², thermal stores bank mcΔT. A national energy Sankey shows the
    sobering structure: roughly a third of primary energy reaches useful work, the rest
    drooping away in conversion chains — which is why efficiency and electrification of
    heat and transport dominate energy policy.
guidedSteps:
  - id: phy-jun-m3-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Grid operators' core constraint, every second of every day, is:
    inputConfig:
      options:
        - "Keeping all stations at full power"
        - "Generation must match demand moment by moment — the grid stores almost nothing itself"
        - "Using each fuel equally"
        - "Keeping voltage at zero overnight"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Generation must match demand moment by moment — the grid stores almost nothing itself"]
      rejectedFeedback: "Wires store nothing useful: every kettle switched on must be matched by extra generation within seconds (frequency sags otherwise — the spinning chorus from the Tower slows). This balancing act is why dispatchability and storage are worth fortunes."
    hint: "What does the grid itself hold in reserve? (Almost...)"
    reflectionPrompt: "What physically happens to grid frequency when demand suddenly outruns generation — and why? (The Tower's flywheels remember.)"
  - id: phy-jun-m3-06-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Pumped-storage hydro stations buy cheap night electricity to pump water uphill, and sell it back at peak. The physics of their 'battery' is:
    inputConfig:
      options:
        - "Chemical energy in the water"
        - "Gravitational potential energy — mgh banked by the reservoir-full, released through turbines on demand"
        - "Nuclear energy"
        - "The water's temperature"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Gravitational potential energy — mgh banked by the reservoir-full, released through turbines on demand"]
      rejectedFeedback: "Apprentice physics, utility scale: 10 million tonnes raised 350 m banks mgh ≈ 3.5×10¹³ J ≈ 10 GWh — a city's evening, stored as altitude. Round-trip efficiency ~75–80%; response time seconds. The oldest energy store, still the world's largest."
    hint: "Water, raised. Which account from Module Two of the Apprentice tier?"
    reflectionPrompt: "Why are pumped-storage sites geographically scarce, and what does that scarcity do to alternatives' value?"
  - id: phy-jun-m3-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Wind and solar produce the cheapest electricity in history, yet grids cannot run on them alone without help. Explain the problem and name two distinct physics-backed solutions. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [intermittent, weather, storage, batteries, pumped, interconnect, dispatchable, demand]
      rejectedFeedback: "Their output follows weather and daylight, not demand — calm winter evenings deliver near-zero just as demand peaks. Solutions: storage (pumped hydro's mgh, grid batteries' chemistry, thermal stores) to shift energy in time; interconnectors to shift it in space (somewhere is always windy); dispatchable backup (hydro, gas) and flexible demand to bridge the rest. The physics of the whole module, deployed as policy."
    hint: "The problem is WHEN, not HOW MUCH. Solutions move energy through time or space."
    reflectionPrompt: "Which storage physics — mgh, chemistry, mcΔT — best suits storing summer heat for winter, and why is that one so hard?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A 'dispatchable' power source is one that:"
    options:
      - "Is renewable"
      - "Can be turned up or down on command to follow demand"
      - "Runs only at night"
      - "Needs no fuel"
    correctIndex: 1
    feedback: "Dispatchability = controllability: hydro and gas excel (seconds to minutes), nuclear prefers steady running, wind and solar deliver what weather grants. Grids need a controllable core or storage to shape the rest."
  - type: MULTIPLE_CHOICE
    question: "In national energy ledgers, roughly what fraction of primary energy typically ends as useful work/services?"
    options:
      - "Nearly all of it"
      - "Around a third — most droops away in conversion losses, dominated by heat engines"
      - "About 90%"
      - "Under 5%"
    correctIndex: 1
    feedback: "The national Sankey's sobering shape: thermal-plant chimneys, engine exhausts, and transmission shave roughly two-thirds. Hence policy's twin levers: efficiency (narrow the marshes) and electrification (replace heat-engine chains with shorter electric ones)."
---

# Hook

Tonight, somewhere in the hills, a power company is pumping an entire lake uphill. Ten million tonnes of water, hoisted three hundred metres — your Apprentice mgh formula deployed with a mountain for a battery casing — so that tomorrow evening, when a nation switches on its kettles in the same five minutes, the lake can fall back through turbines and hold the grid's frequency steady.

This is energy at civilisation's scale, and it runs on everything you've learned: generators spinning in synchrony (the Tower), efficiency chains and their fat losses (the Bursar's rivers), heat capacity, gravity, chemistry, and the merciless second-by-second balance between every lamp switched on and some shaft, somewhere, leaning harder. Today we draw the largest Sankey of all — a nation's — and learn why the cheapest electricity in history still needs lakes on hills, chemistry in containers, and cables under the sea.

# Lore Introduction

The realm-wide sheet covers the drawing office wall, and the Bursar walks you along it like a general at a map table. Headwaters at the left: coal-fields, gas-wells, the great dams, the new wind-coasts and sun-farms, each river ruled to scale. Mid-sheet: the conversion weirs — power stations with their broad marsh-branches drooping, refineries, the grid's arteries. Right edge: the destinations — cities' light and heat, the wagon-roads' motion, the foundries' fire. "Note the shape before the detail," she says. Your eye finds it at once: of all the headwater width entering at the left, barely a third arrives anywhere useful. "Two-thirds to the marshes," the Bursar confirms. "Mostly the old story — fire made to push pistons. Now—" she pins a smaller chart beside the great sheet: a single day's demand, a restless line peaking at breakfast and supper, "—the harder truth. The rivers must match THIS, hour by hour, breath by breath. The sun does not dine when we do. Today, junior: the storehouses, the controllable rivers, and the arithmetic of keeping a realm lit through a calm winter evening."

# Core Learning

## Concept Introduction

**The balance constraint.** A grid stores essentially nothing: generation must equal demand continuously (mismatch shows instantly as frequency drift — the synchronised flywheels of the Tower's lesson speeding or sagging). Demand swings daily (breakfast/evening peaks) and seasonally; therefore the *shape* of each source's output matters as much as its total:

| Source | Profile | Dispatchable? |
|--------|---------|---------------|
| Gas turbines | On command, minutes | ★★★ — the grid's throttle |
| Hydro (dammed) | On command, seconds | ★★★ — and cheap to hold back |
| Nuclear | Steady "baseload" | ★ — prefers constant running |
| Wind | Weather-shaped | ✗ — take what blows |
| Solar | Daylight-shaped | ✗ — and winter-weak |

**Storage: the module's stores, industrialised.** To reshape weather-shaped supply into demand's profile, bank energy with physics you already own:

- **Pumped hydro** — mgh by the reservoir (10 GWh class; ~75–80% round trip; the world's dominant storage)
- **Grid batteries** — electrochemistry (millisecond response; hours of depth; falling costs reshaping grids)
- **Flywheels** — ½mv² in spinning steel/carbon (seconds-scale smoothing)
- **Thermal stores** — mcΔT in molten salt, hot water, hot rock (cheap bulk; pairs with solar-thermal and heat networks)
- (Hydrogen and synthetic fuels: chemistry for weeks-to-seasons — promising, lossy, the frontier.)

**The national Sankey's shape.** Roughly a third of primary energy becomes useful service; the marshes are dominated by **heat-engine conversion losses** (station chimneys, vehicle exhausts). Hence the two great policy levers, readable straight off the sheet: **efficiency** (narrow the marshes: insulation, LEDs, heat pumps) and **electrification** (replace long fire-driven chains with short electric ones — an EV's chain beats a petrol car's even on today's grids, and improves as the headwaters green).

## Why It Matters

- Energy systems are the century's defining engineering problem; this lesson is the physics literacy the debate assumes.
- Every storage technology is an earlier lesson wearing a hard hat — recognising mgh, ½mv², and mcΔT at utility scale converts your toolkit into judgement.
- Reading national Sankeys and demand curves inoculates against slogans from every direction: the numbers have shapes, and the shapes argue.

## Worked Examples

**Example 1: Sizing the evening kettle-surge**
A TV finale ends; 2 million kettles (2.3 kW each) switch on within minutes: +4.6 GW of demand — several large power stations' worth. Grid answer, in order: frequency sags fractionally → spinning reserve leans in (seconds) → pumped storage opens its gates (tens of seconds; one big station ≈ 1.8 GW) → fast gas turbines spool (minutes). The whole choreography is rehearsed daily around scheduled surges — demand forecasting includes television listings. (Truly.)

**Example 2: A calm winter week, audited**
Demand: 40 GW average. Wind fleet (capacity 30 GW) delivers 3 GW in the anticyclone's calm; solar offers winter's whisper. Gap ≈ 30+ GW for days — beyond any battery fleet's depth (current national batteries: ~hours). The bridge: dispatchable hydro and gas, interconnectors importing from windier neighbours, demand flexibility — and the engineering frontier (long-duration storage, hydrogen) sized by exactly this worst-week arithmetic. The calm week, not the average year, is what systems are designed against.

**Example 3: The heat-pump lever, computed**
Heating a home with: (a) a gas boiler — fuel→heat at ~90%, one stage; (b) resistance heating — power-station chain ≈ 35–40% fuel-to-heat; (c) a **heat pump** (COP ~3) on the same chain: 0.38 × 3 ≈ 115% fuel-to-heat — *better than burning the fuel at home*, because the device spends electricity moving ambient heat rather than making heat (the hidden-river diagram from yesterday). On a renewables-heavy grid the comparison turns rout. One Sankey chain, one policy revolution.

## Common Mistakes

- **Comparing sources by capacity alone** — 30 GW of wind is not 30 GW on a calm evening; profiles and capacity factors carry the real information.
- **"Batteries will simply store summer for winter"** — current grid batteries bank hours; seasonal storage needs different physics (thermal bulk, chemical fuels) and remains genuinely hard.
- **Treating baseload and dispatchable as synonyms** — nuclear runs steady (baseload) but throttles poorly; hydro is the true throttle.
- **Forgetting the grid stores nothing** — every imbalance is seconds from visible; storage and dispatch exist because wires can't wait.
- **Reading the national Sankey fatalistically** — the two-thirds marsh is not physics' final word: heat-engine losses are *chain choices*, and electrification literally redraws the sheet.

## Mental Model

A national grid is **an orchestra that must match its audience's applause in real time**. The audience (demand) claps in daily rhythms with sudden crescendos; the orchestra (generation) must swell and hush in perfect sympathy, because the concert hall (the grid) has no echo — silence any section and the harmony sags within seconds (frequency). Nuclear is the organ: magnificent, steady, slow to vary. Gas and hydro are the strings: expressive, instantly responsive. Wind and solar are brilliant guest soloists who play only when inspired. And storage is the recording crew: capturing the soloists' midnight brilliance — onto lakes raised high (mgh), into chemical vaults, into spinning steel — to replay it, on cue, into tomorrow evening's crescendo.

## Mini Summary

- ✔ Grids balance generation and demand second by second; wires store nothing
- ✔ Profiles matter: dispatchable throttles (hydro, gas), steady baseload (nuclear), weather-shaped renewables
- ✔ Storage = old physics at scale: pumped mgh, battery chemistry, flywheel ½mv², thermal mcΔT
- ✔ National Sankeys: ~⅓ useful; heat-engine marshes dominate — efficiency and electrification are the levers
- ✔ Design for the calm winter week, not the sunny average

# Guided Practice Quest

Work through the guided steps to hold a grid's breath steady, bank a city's evening as altitude, and prescribe for the cheapest-yet-calmest electricity in history.

# Solo Practice Quest

Three commissions at realm scale: (1) *Find your nation's sheet*: locate your country's actual energy-flow (Sankey) diagram (most governments publish one); identify the three widest marshes and the useful-fraction, and write a five-sentence briefing on its shape. (2) *Storage arithmetic*: compute the energy banked by a pumped-storage station of your invention (choose reservoir mass and height; mgh, then convert to GWh), and how many hours it could carry a 1 GW city; then size the battery fleet (at 50 kWh per home battery) equalling it. (3) *The kettle surge*: model your own country's version (population, synchronised event, kettle penetration — honest estimates) and propose the grid's response sequence. Close with your household's electrification audit: which fire-driven chain in your life (heating? cooking? travel?) would redraw your personal Sankey most, and by roughly how much.

# Integration

**Engineering**: Grid engineering adds the layers this lesson sketches: frequency-response markets, HVDC interconnectors (the Tower's transformer lesson at continental scale), inertia services as spinning mass retires, and reliability standards written against the worst calm week in decades. Energy-systems modelling is now a profession sitting exactly on this lesson's foundations.

**Earth Science**: The headwaters are geophysics: fossil rivers are banked Carboniferous sunlight; hydro is the water cycle harnessed (solar energy, evaporated uphill); wind is differential solar heating (your weather lessons, monetised). Climate constraints close the loop — the marshes' CO₂ is why the sheet must be redrawn within your working lifetime, making this the rare physics lesson with a deadline.

# Lore Conclusion

Your realm-briefing, storage arithmetic, and kettle-surge choreography earn the Bursar's longest sentence yet: "You read the sheet like a junior; you argued it like a minister's auditor. The drawing office releases you." Calde collects you at the door with evident pride and walks you back through the Foundry, past the new engine on its trial stand, its chimney-branch drawn fresh on the wall in your own ruling. At the vault stair she stops. "You can now draw where every joule of the realm goes, junior. Which makes tomorrow's question yours to feel in full." She lays her scarred hand on the engine's warm flank. "The First Law permits this engine to keep every joule of its fire as work. The Bursar's sheets show no engine ever has. Two-thirds to the marshes — always, everywhere, every design, nine centuries. That is not engineering failure. That is a LAW, hiding in the failures." Her grin is gone; what's in its place is older. "Tomorrow: the one-way arrow. Entropy. The law that even the Foundry obeys."
