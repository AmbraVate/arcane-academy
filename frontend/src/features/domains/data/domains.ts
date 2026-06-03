/* ── School identifiers ───────────────────────────────────────────────────── */

export type School =
  | 'computing-engineering'
  | 'mathematics-logic'
  | 'natural-sciences'
  | 'mind-neuroscience'
  | 'history-civilisation'
  | 'philosophy-ethics'
  | 'society-politics'
  | 'business-enterprise'
  | 'language-communication'
  | 'arts-design'
  | 'health-performance'
  | 'geography-environment'
  | 'technology-futures'
  | 'law-governance'
  | 'survival-practical'

/* ── Shared interfaces ────────────────────────────────────────────────────── */

export interface Domain {
  id: string
  name: string
  glyph: string
  tagline: string
  status: 'active' | 'coming_soon'
  modules: number
  accentStroke: string
  school: School
  /** @deprecated No longer used — kept for backward-compat with legacy code. */
  trackGroup?: string
  /** @deprecated No longer used — kept for backward-compat with legacy code. */
  guildName?: string
}

export interface SchoolMeta {
  name: string
  glyph: string
  color: string
  description: string
}

export interface TrackGroup {
  id: string
  name: string
  glyph: string
  color: string
  description: string
  school: School
}

/* ── School metadata ─────────────────────────────────────────────────────── */

export const SCHOOL_META: Record<School, SchoolMeta> = {
  'computing-engineering':   { name: 'School of Computing & Engineering',        glyph: '⚡',  color: '#7c3aed', description: 'Software, frontend, backend, cloud, cybersecurity, AI, mobile & embedded engineering' },
  'mathematics-logic':       { name: 'School of Mathematics & Logic',            glyph: '∑',  color: '#3b82f6', description: 'Pure and applied mathematics, statistics, cryptography, and formal systems' },
  'natural-sciences':        { name: 'School of Natural Sciences',               glyph: '🔬', color: '#10b981', description: 'Physics, chemistry, biology, astronomy, ecology, and the living world' },
  'mind-neuroscience':       { name: 'School of Mind, Behaviour & Neuroscience', glyph: '🧠', color: '#a855f7', description: 'Psychology, cognitive science, neuroscience, decision theory, and mental health' },
  'history-civilisation':    { name: 'School of History & Civilisation',         glyph: '📜', color: '#d97706', description: 'Ancient to modern history, classical civilisations, genealogy, and cultural heritage' },
  'philosophy-ethics':       { name: 'School of Philosophy, Ethics & Religion',  glyph: '🏛️', color: '#6366f1', description: 'Philosophy, ethics, epistemology, logic, comparative religion, and mythology' },
  'society-politics':        { name: 'School of Society and Politics',        glyph: '🌐', color: '#0ea5e9', description: 'Sociology, anthropology, political science and economics' },
  'business-enterprise':     { name: 'School of Business & Enterprise',          glyph: '💼', color: '#c9a227', description: 'Entrepreneurship, management, marketing, finance, product, and strategy' },
  'language-communication':  { name: 'School of Language & Communication',       glyph: '🗣️', color: '#f97316', description: 'Linguistics, writing, public speaking, journalism, and world languages' },
  'arts-design':             { name: 'School of Arts, Design & Creativity',      glyph: '🎨', color: '#ec4899', description: 'Visual arts, graphic design, photography, film, music, and creative writing' },
  'health-performance':      { name: 'School of Health & Human Performance',     glyph: '🏃', color: '#22c55e', description: 'Anatomy, nutrition, exercise science, public health, and longevity' },
  'geography-environment':   { name: 'School of Geography, Environment & Exploration', glyph: '🗺️', color: '#06b6d4', description: 'Physical and human geography, climate, GIS, cartography, and sustainability' },
  'technology-futures':      { name: 'School of Technology Futures & Systems',   glyph: '🚀', color: '#8b5cf6', description: 'Space science, robotics, nanotechnology, biotechnology, and emerging technologies' },
  'law-governance':          { name: 'School of Law, Governance & Justice',      glyph: '⚖️', color: '#1d4ed8', description: 'Legal foundations, constitutional, criminal, civil and international law, and governance' },
  'survival-practical':      { name: 'School of Survival, Practical & Applied Skills', glyph: '🏕️', color: '#92400e', description: 'Wilderness survival, bushcraft, navigation, homesteading, and practical self-sufficiency' },
}

/* ── School hero background images ───────────────────────────────────────────── */

export const SCHOOL_HERO_IMAGES: Partial<Record<School, string>> = {
  'computing-engineering': 'schools/school_computing_engineering.jpeg',
  'mathematics-logic':     'schools/school_mathematics_logic.jpeg',
  'natural-sciences' : 'schools/school_natural_science.jpeg',
  'mind-neuroscience' : 'schools/school_mind.jpeg',
  'history-civilisation' : 'schools/school_history.jpeg',
  'philosophy-ethics' : 'schools/school_philosophy.jpeg',
  'society-politics' : 'schools/school_society.jpeg',
  'business-enterprise' : 'schools/school_business.jpeg',
  'language-communication' : 'schools/school_lang.jpeg',
  'arts-design' : 'schools/school_art.jpeg',
  'health-performance' : 'schools/school_health.jpeg',
  'geography-environment' : 'schools/school_geo.jpeg',
  'technology-futures' : 'schools/school_future.jpeg',
  'law-governance' : 'schools/school_law.jpeg',
  'survival-practical' : 'schools/school_survival.jpeg',
}

/* ── Pathway hero background images ──────────────────────────────────────────── */

export const DOMAIN_HERO_IMAGES: Partial<Record<string, string>> = {
  // ── Computing & Engineering ──────────────────────────────────────────────
  'software-engineering':      '/software_engineer.jpeg',
  'frontend-engineering':      '/Pathways/software/frontend_engineer.jpeg',
  'backend-engineering':       '/Pathways/software/backend_engineer.jpeg',
  'data-engineering':          '/Pathways/software/data_engineer.jpeg',
  'devops-engineering':        '/Pathways/software/devOps_engineer.jpeg',
  'cybersecurity-engineering': '/Pathways/software/cyber_engineer.jpeg',
  'cloud-engineering':         '/Pathways/software/cloud_engineer.jpeg',
  'mobile-engineering':        '/Pathways/software/mobile_engineer.jpeg',
  'embedded-engineering':      '/Pathways/software/emebbed_engineer.jpeg',
  'game-engineering':          '/Pathways/software/game_engineer.jpeg',
  'ai-machine-learning':       '/Pathways/software/ai_engineer.jpeg',
  // ── Mathematics & Logic ──────────────────────────────────────────────────
  'mathematical-foundations':  '/Pathways/maths/math_foundations.jpeg',
  'algebra':                   '/Pathways/maths/algebra.jpeg',
  'calculus':                  '/Pathways/maths/calculus.jpeg',
  'discrete-mathematics':      '/Pathways/maths/discrete.jpeg',
  'probability-statistics':    '/Pathways/maths/statistics.jpeg',
  'mathematical-modelling':    '/Pathways/maths/math_modelling.jpeg',
  'applied-mathematics':       '/Pathways/maths/maths_applied.jpeg',
  'cryptography':              '/Pathways/maths/crypto.jpeg',
  'logic-formal-systems':      '/Pathways/maths/formal.jpeg',
}

/* ── Track groups (kept empty — navigation is now Schools → Pathways) ─────── */

export const TRACK_GROUPS: TrackGroup[] = []

export function schoolHasTrackGroups(school: School): boolean {
  return TRACK_GROUPS.some(tg => tg.school === school)
}

export function trackGroupsForSchool(school: School): TrackGroup[] {
  return TRACK_GROUPS.filter(tg => tg.school === school)
}

/* ── All pathways ────────────────────────────────────────────────────────── */

export const DOMAINS: Domain[] = [

  // ── School of Computing & Engineering ──────────────────────────────────
  { id: 'software-engineering',   name: 'Software Engineering',         glyph: '⚙️',  status: 'active',      modules: 71, accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Build reliable systems — computational thinking, design patterns, and architecture, taught through Java.' },
  { id: 'frontend-engineering',   name: 'Frontend Engineering',         glyph: '🎨',  status: 'active',      modules: 40, accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Component-driven UIs with React and Tailwind — from first render to production.' },
  { id: 'backend-engineering',    name: 'Backend Engineering',          glyph: '🗄️',  status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'APIs, databases, authentication, and scalable server-side architecture.' },
  { id: 'data-engineering',       name: 'Data Engineering & Databases', glyph: '🗃️',  status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'SQL, data pipelines, warehousing, and the craft of reliable data systems.' },
  { id: 'devops-engineering',     name: 'DevOps & Platform Engineering', glyph: '🔧', status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'CI/CD, containerisation, infrastructure as code, and platform reliability.' },
  { id: 'cybersecurity-engineering', name: 'Cyber Security Engineering', glyph: '🔐', status: 'coming_soon', modules: 0, accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Defend, detect, and respond — offensive and defensive security from first principles.' },
  { id: 'cloud-engineering',      name: 'Cloud Engineering',            glyph: '☁️',  status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Design and operate cloud-native systems across AWS, GCP, and Azure.' },
  { id: 'mobile-engineering',     name: 'Mobile Engineering',           glyph: '📱',  status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Build native and cross-platform mobile apps for iOS and Android.' },
  { id: 'embedded-engineering',   name: 'Embedded Systems Engineering', glyph: '🔌',  status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Program microcontrollers, real-time systems, and the hardware-software interface.' },
  { id: 'game-engineering',       name: 'Game Engineering',             glyph: '🎮',  status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Build games from scratch — game loops, physics, rendering, and engine architecture.' },
  { id: 'ai-machine-learning',    name: 'AI & Machine Learning',        glyph: '🤖',  status: 'coming_soon', modules: 0,  accentStroke: '#7c3aed', school: 'computing-engineering',
    tagline: 'Neural networks, supervised learning, NLP, and the engineering of intelligent systems.' },

  // ── School of Mathematics & Logic ──────────────────────────────────────
  { id: 'mathematical-foundations', name: 'Mathematical Foundations',   glyph: '📐',  status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Sets, functions, proofs, and the foundational language of all mathematics.' },
  { id: 'algebra',                name: 'Algebra & Number Systems',      glyph: '🔢',  status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Groups, rings, fields, and the deep structure of numbers and operations.' },
  { id: 'calculus',               name: 'Calculus & Analysis',           glyph: '∞',   status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Limits, derivatives, integrals, and real analysis — the mathematics of change.' },
  { id: 'discrete-mathematics',   name: 'Discrete Mathematics',          glyph: '🔷',  status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Combinatorics, graph theory, and the mathematics underpinning computer science.' },
  { id: 'probability-statistics', name: 'Probability & Statistics',      glyph: '📊',  status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Random variables, distributions, inference, and the science of uncertainty.' },
  { id: 'mathematical-modelling', name: 'Mathematical Modelling',        glyph: '📈',  status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Translate real-world problems into mathematical structures and solve them.' },
  { id: 'applied-mathematics',    name: 'Applied Mathematics',           glyph: '⚛️',  status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Differential equations, numerical methods, and mathematics for engineering and science.' },
  { id: 'cryptography',           name: 'Cryptography & Information Theory', glyph: '🔑', status: 'coming_soon', modules: 0, accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Encryption, hash functions, information entropy, and the mathematics of secrecy.' },
  { id: 'logic-formal-systems',   name: 'Logic & Formal Systems',        glyph: '∴',   status: 'coming_soon', modules: 0,  accentStroke: '#3b82f6', school: 'mathematics-logic',
    tagline: 'Propositional and predicate logic, formal proofs, and the foundations of reasoning.' },

  // ── School of Natural Sciences ──────────────────────────────────────────
  { id: 'physics',                name: 'Physics',                       glyph: '⚛️',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'From classical mechanics to quantum theory and relativity — the laws of the universe.' },
  { id: 'chemistry',              name: 'Chemistry',                     glyph: '⚗️',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Atomic structure, bonding, reactions, and the molecular world.' },
  { id: 'biology',                name: 'Biology',                       glyph: '🧬',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Cells, evolution, ecology, and the mechanisms of life at every scale.' },
  { id: 'botany',                 name: 'Botany',                        glyph: '🌿',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Plant biology, physiology, taxonomy, and the architecture of the living world.' },
  { id: 'zoology',                name: 'Zoology',                       glyph: '🦁',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Animal diversity, behaviour, physiology, and evolutionary biology.' },
  { id: 'genetics',               name: 'Genetics',                      glyph: '🧬',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Heredity, gene expression, molecular genetics, and the human genome.' },
  { id: 'ecology',                name: 'Ecology & Environmental Science', glyph: '🌳', status: 'coming_soon', modules: 0, accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Ecosystems, biodiversity, environmental systems, and conservation science.' },
  { id: 'earth-science',          name: 'Earth Science',                 glyph: '🌍',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Geology, meteorology, plate tectonics, and the dynamic Earth system.' },
  { id: 'astronomy',              name: 'Astronomy & Astrophysics',      glyph: '🌌',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Stars, galaxies, cosmology, and the physics of the observable universe.' },
  { id: 'oceanography',           name: 'Oceanography',                  glyph: '🌊',  status: 'coming_soon', modules: 0,  accentStroke: '#10b981', school: 'natural-sciences',
    tagline: 'Ocean physics, chemistry, biology, and the deep systems of our seas.' },

  // ── School of Mind, Behaviour & Neuroscience ───────────────────────────
  { id: 'psychology',             name: 'Psychology',                    glyph: '🧠',  status: 'active',      modules: 71, accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'From foundations to frontier — the complete undergraduate-to-graduate psychology pathway.' },
  { id: 'cognitive-science',      name: 'Cognitive Science',             glyph: '💭',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'Mind, computation, and intelligence — where psychology meets AI and linguistics.' },
  { id: 'neuroscience',           name: 'Neuroscience',                  glyph: '⚡',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'Neurons, circuits, cognition, and the biological basis of thought and emotion.' },
  { id: 'behavioural-science',    name: 'Behavioural Science',           glyph: '🔍',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'How humans actually make decisions — nudge theory, habits, and behavioural economics.' },
  { id: 'developmental-psychology', name: 'Developmental Psychology',   glyph: '🌱',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'How minds grow — from infant cognition to lifespan development.' },
  { id: 'social-psychology',      name: 'Social Psychology',             glyph: '👥',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'Influence, conformity, group dynamics, and how others shape our behaviour.' },
  { id: 'learning-science',       name: 'Learning Science',              glyph: '📚',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'How learning actually works — memory, spacing, retrieval, and cognitive load theory.' },
  { id: 'mental-health',          name: 'Mental Health Foundations',     glyph: '💙',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'Understanding psychological wellbeing, common conditions, and evidence-based approaches.' },
  { id: 'decision-science',       name: 'Decision Science',              glyph: '⚖️',  status: 'coming_soon', modules: 0,  accentStroke: '#a855f7', school: 'mind-neuroscience',
    tagline: 'Rational choice, risk, uncertainty, and the psychology of how we decide.' },

  // ── School of History & Civilisation ───────────────────────────────────
  { id: 'ancient-history',        name: 'Ancient History',               glyph: '🏺',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Mesopotamia to the fall of Rome — the civilisations that shaped everything after.' },
  { id: 'medieval-history',       name: 'Medieval History',              glyph: '🏰',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Feudalism, crusades, plague, and the transformation of the medieval world.' },
  { id: 'modern-history',         name: 'Modern History',                glyph: '🏭',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'The industrial age to the present — revolutions, world wars, and the modern order.' },
  { id: 'world-history',          name: 'World History',                 glyph: '🌍',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'A global sweep of civilisation — tracing the threads that connect every corner of the world.' },
  { id: 'classical-civilisations', name: 'Classical Civilisations',     glyph: '🏛️',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Greece, Rome, and the ancient Mediterranean — ideas that still shape the modern mind.' },
  { id: 'archaeology',            name: 'Archaeology',                   glyph: '⛏️',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Reading the past from material remains — excavation, analysis, and interpretation.' },
  { id: 'genealogy',              name: 'Genealogy',                     glyph: '🌳',  status: 'active',      modules: 71, accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'From vital records to professional proof — become a skilled genealogical researcher.' },
  { id: 'military-history',       name: 'Military History',              glyph: '⚔️',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Strategy, tactics, and the battles that changed the course of history.' },
  { id: 'cultural-history',       name: 'Cultural History',              glyph: '🎭',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Art, ideas, religion, and everyday life — history as it was lived.' },
  { id: 'egyptian-studies',       name: 'Egyptian Studies',              glyph: '🔱',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Pharaohs, hieroglyphs, religion, and three thousand years of Egyptian civilisation.' },
  { id: 'roman-studies',          name: 'Roman Studies',                 glyph: '🦅',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Republic to empire — law, engineering, culture, and the legacy of Rome.' },
  { id: 'greek-studies',          name: 'Greek Studies',                 glyph: '🏺',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Philosophy, democracy, mythology, and the roots of Western thought.' },
  { id: 'viking-studies',         name: 'Viking Studies',                glyph: '⚓',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Norse culture, exploration, mythology, and the age of the Vikings.' },
  { id: 'british-history',        name: 'British History',               glyph: '👑',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'From Albion to the present — invasions, empire, reformation, and the British story.' },
  { id: 'american-history',       name: 'American History',              glyph: '🗽',  status: 'coming_soon', modules: 0,  accentStroke: '#d97706', school: 'history-civilisation',
    tagline: 'Colonial roots to superpower — revolution, civil war, civil rights, and the American century.' },

  // ── School of Philosophy, Ethics & Religion ────────────────────────────
  { id: 'philosophy',             name: 'Philosophy',                    glyph: '💡',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'The great questions — existence, knowledge, value, and reason from Socrates to today.' },
  { id: 'logic-reasoning',        name: 'Logic & Reasoning',             glyph: '∴',   status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'Formal logic, argument analysis, and the tools of disciplined thought.' },
  { id: 'ethics',                 name: 'Ethics',                        glyph: '🤝',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'Moral philosophy, applied ethics, and how to reason about right and wrong.' },
  { id: 'political-philosophy',   name: 'Political Philosophy',          glyph: '🏛️',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'Justice, liberty, equality, and the philosophical foundations of political life.' },
  { id: 'epistemology',           name: 'Epistemology',                  glyph: '👁️',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'What can we know and how do we know it? The theory of knowledge.' },
  { id: 'metaphysics',            name: 'Metaphysics',                   glyph: '🌀',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'Reality, causation, time, and identity — the deepest questions about what exists.' },
  { id: 'philosophy-of-science',  name: 'Philosophy of Science',         glyph: '🔭',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'What is scientific knowledge? Induction, falsification, and the nature of explanation.' },
  { id: 'comparative-religion',   name: 'Comparative Religion',          glyph: '🕊️',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'The world\'s faiths compared — belief, practice, ritual, and sacred texts.' },
  { id: 'mythology',              name: 'Mythology & Folklore',          glyph: '🐉',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'Creation myths, hero cycles, and the stories every culture tells itself.' },
  { id: 'eastern-philosophy',     name: 'Eastern Philosophy',            glyph: '☯️',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'Taoism, Buddhism, Confucianism, Vedanta, and the wisdom traditions of the East.' },
  { id: 'western-philosophy',     name: 'Western Philosophy Traditions', glyph: '🏛️',  status: 'coming_soon', modules: 0,  accentStroke: '#6366f1', school: 'philosophy-ethics',
    tagline: 'From Plato to Nietzsche — the major movements and thinkers of Western thought.' },

  // ── School of Society, Politics & Law ─────────────────────────────────
  { id: 'sociology',              name: 'Sociology',                     glyph: '👥',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'Society, social structures, culture, inequality, and how we live together.' },
  { id: 'anthropology',           name: 'Anthropology',                  glyph: '🌏',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'Human diversity — cultures, societies, and what it means to be human.' },
  { id: 'political-science',      name: 'Political Science',             glyph: '🗳️',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'Power, institutions, government systems, and the theory of politics.' },
  { id: 'international-relations', name: 'International Relations',     glyph: '🌐',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'States, diplomacy, war, trade, and the global order.' },
  { id: 'economics',              name: 'Economics',                     glyph: '📈',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'Incentives, markets, game theory, and why the world organises itself the way it does.' },
  { id: 'public-policy',          name: 'Public Policy',                 glyph: '📋',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'How governments decide and act — policy design, analysis, and implementation.' },
  { id: 'criminology',            name: 'Criminology',                   glyph: '🔎',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'Crime, deviance, punishment, and the social science of justice.' },
  { id: 'human-geography',        name: 'Human Geography',               glyph: '🗺️',  status: 'coming_soon', modules: 0,  accentStroke: '#0ea5e9', school: 'society-politics',
    tagline: 'How people, places, and power interact — urbanisation, migration, and spatial inequality.' },

  // ── School of Business & Enterprise ───────────────────────────────────
  { id: 'entrepreneurship',       name: 'Entrepreneurship',              glyph: '🚀',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Build something from nothing — ideation, validation, funding, and scaling.' },
  { id: 'business-management',    name: 'Business Management',           glyph: '🏢',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Operations, people, processes, and the fundamentals of running an organisation.' },
  { id: 'product-management',     name: 'Product Management',            glyph: '🧩',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Discovery, roadmaps, metrics, and the craft of building the right product.' },
  { id: 'marketing',              name: 'Marketing',                     glyph: '📣',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Brand, positioning, digital channels, and connecting products with the people who need them.' },
  { id: 'sales',                  name: 'Sales',                         glyph: '🤝',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Prospecting, discovery, objection handling, and the psychology of closing.' },
  { id: 'finance',                name: 'Finance',                       glyph: '💰',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Valuation, capital markets, financial modelling, and the language of money.' },
  { id: 'accounting',             name: 'Accounting',                    glyph: '📊',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Financial statements, management accounts, and the rules that govern reported numbers.' },
  { id: 'operations-management',  name: 'Operations Management',         glyph: '⚙️',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Supply chains, lean principles, process design, and delivering value at scale.' },
  { id: 'strategy-leadership',    name: 'Strategy & Leadership',         glyph: '♟️',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Competitive strategy, leadership models, and building organisations that endure.' },
  { id: 'innovation-management',  name: 'Innovation Management',         glyph: '💡',  status: 'coming_soon', modules: 0,  accentStroke: '#c9a227', school: 'business-enterprise',
    tagline: 'Design thinking, disruptive innovation, and managing change in complex organisations.' },

  // ── School of Language & Communication ─────────────────────────────────
  { id: 'linguistics',            name: 'Linguistics',                   glyph: '🔤',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Phonetics, syntax, semantics, and the science of how language works.' },
  { id: 'english-language',       name: 'English Language',              glyph: '📖',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Grammar, usage, style, and the mechanics of expert English.' },
  { id: 'writing-composition',    name: 'Writing & Composition',         glyph: '✍️',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Clear, compelling writing at every level — from sentence to argument.' },
  { id: 'public-speaking',        name: 'Public Speaking',               glyph: '🎤',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Structure, delivery, and presence — speak confidently in any room.' },
  { id: 'storytelling',           name: 'Storytelling',                  glyph: '📖',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Character, conflict, and narrative arc — the craft of stories that move people.' },
  { id: 'journalism',             name: 'Journalism',                    glyph: '📰',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Reporting, interviewing, verification, and the ethics of public-interest journalism.' },
  { id: 'rhetoric',               name: 'Rhetoric & Persuasion',         glyph: '🎭',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Ethos, pathos, logos — the classical art of persuasion and argumentation.' },
  { id: 'communication-strategy', name: 'Communication Strategy',        glyph: '🗓️',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Audience analysis, messaging frameworks, and strategic communication planning.' },
  { id: 'french',                 name: 'French',                        glyph: '🇫🇷',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'From bonjour to fluency — grammar, vocabulary, and spoken French.' },
  { id: 'spanish',                name: 'Spanish',                       glyph: '🇪🇸',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'The language of 500 million — conversational Spanish from first principles.' },
  { id: 'welsh',                  name: 'Welsh',                         glyph: '🏴󠁧󠁢󠁷󠁬󠁳󠁿',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Cymraeg from the ground up — grammar, pronunciation, and the living Welsh language.' },
  { id: 'mandarin',               name: 'Mandarin',                      glyph: '🇨🇳',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Tones, characters, and conversation — Mandarin Chinese structured for serious learners.' },
  { id: 'russian',                name: 'Russian',                       glyph: '🇷🇺',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Cyrillic, cases, and culture — Russian from alphabet to confident conversation.' },
  { id: 'japanese',               name: 'Japanese',                      glyph: '🇯🇵',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Hiragana, kanji, grammar, and the art of speaking Japanese.' },
  { id: 'foreign-languages',      name: 'Foreign Language Studies',      glyph: '🌍',  status: 'coming_soon', modules: 0,  accentStroke: '#f97316', school: 'language-communication',
    tagline: 'Language acquisition science, immersion strategies, and becoming multilingual.' },

  // ── School of Arts, Design & Creativity ────────────────────────────────
  { id: 'drawing-illustration',   name: 'Drawing & Illustration',        glyph: '✏️',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Line, form, perspective, and observation — the foundational language of visual art.' },
  { id: 'painting',               name: 'Painting',                      glyph: '🖌️',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Colour theory, composition, and the techniques of oil, acrylic, and watercolour.' },
  { id: 'sculpture',              name: 'Sculpture',                     glyph: '🗿',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Three-dimensional form — materials, process, and the history of sculpture.' },
  { id: 'graphic-design',         name: 'Graphic Design',                glyph: '🎨',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Typography, layout, brand identity, and visual problem-solving.' },
  { id: 'photography',            name: 'Photography',                   glyph: '📷',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Exposure, composition, light, and the craft of making meaningful images.' },
  { id: 'animation',              name: 'Animation',                     glyph: '🎞️',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Motion, timing, and the twelve principles — from 2D frames to 3D rigs.' },
  { id: 'film-cinema',            name: 'Film & Cinema Studies',         glyph: '🎬',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Cinematography, editing, narrative theory, and the art of film.' },
  { id: 'music-theory',           name: 'Music Theory & Composition',    glyph: '🎵',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Rhythm, harmony, and composition — the mathematics of sound and emotion.' },
  { id: 'architecture',           name: 'Architecture',                  glyph: '🏛️',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Structure, space, and style — the history and theory of the built environment.' },
  { id: 'creative-writing',       name: 'Creative Writing',              glyph: '📝',  status: 'coming_soon', modules: 0,  accentStroke: '#ec4899', school: 'arts-design',
    tagline: 'Fiction, poetry, and the craft of writing with intention and originality.' },

  // ── School of Health & Human Performance ───────────────────────────────
  { id: 'human-anatomy',          name: 'Human Anatomy',                 glyph: '🫀',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Bones, organs, and systems — the architecture of the human body.' },
  { id: 'physiology',             name: 'Physiology',                    glyph: '⚡',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'How the body works — cardiovascular, respiratory, nervous, and muscular systems.' },
  { id: 'nutrition',              name: 'Nutrition',                     glyph: '🥗',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Macronutrients, micronutrients, metabolism, and evidence-based dietary science.' },
  { id: 'exercise-science',       name: 'Exercise Science',              glyph: '🏋️',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Training adaptations, programming, biomechanics, and the physiology of fitness.' },
  { id: 'sports-science',         name: 'Sports Science',                glyph: '🏅',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Athletic performance, sports psychology, injury prevention, and recovery.' },
  { id: 'public-health',          name: 'Public Health',                 glyph: '🏥',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Population health, health systems, policy, and the prevention of disease.' },
  { id: 'epidemiology',           name: 'Epidemiology',                  glyph: '📊',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Disease patterns, risk factors, and the methods of population-level health research.' },
  { id: 'sleep-science',          name: 'Sleep Science',                 glyph: '🌙',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Circadian rhythms, sleep stages, and the science of restorative rest.' },
  { id: 'longevity-science',      name: 'Longevity Science',             glyph: '⌛',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'Ageing biology, healthspan research, and evidence-based strategies for a longer life.' },
  { id: 'first-aid',              name: 'First Aid & Emergency Care',    glyph: '🚑',  status: 'coming_soon', modules: 0,  accentStroke: '#22c55e', school: 'health-performance',
    tagline: 'CPR, trauma response, and the life-saving skills every person should have.' },

  // ── School of Geography, Environment & Exploration ─────────────────────
  { id: 'physical-geography',     name: 'Physical Geography',            glyph: '⛰️',  status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'Landforms, climates, soils, and the physical processes shaping the Earth.' },
  { id: 'urban-studies',          name: 'Urban Studies',                 glyph: '🏙️',  status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'Cities, planning, housing, and the dynamics of urban space and growth.' },
  { id: 'cartography',            name: 'Cartography',                   glyph: '🗺️',  status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'Map-making, projections, and the history and science of representing the world.' },
  { id: 'gis',                    name: 'GIS (Geographic Information Systems)', glyph: '📡', status: 'coming_soon', modules: 0, accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'Spatial data, mapping tools, and geographic analysis at scale.' },
  { id: 'climate-science',        name: 'Climate Science',               glyph: '🌡️',  status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'The climate system, climate change science, and the evidence base for action.' },
  { id: 'environmental-systems',  name: 'Environmental Systems',         glyph: '♻️',  status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'Earth\'s interconnected systems — atmosphere, hydrosphere, biosphere, and geosphere.' },
  { id: 'exploration-history',    name: 'Exploration History',           glyph: '🧭',  status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'The great voyages of discovery — navigators, explorers, and the mapping of the world.' },
  { id: 'sustainability',         name: 'Sustainability Studies',        glyph: '🌱',  status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'Sustainable development, circular economy, and building a liveable future.' },
  { id: 'geo-human-geography',    name: 'Human Geography',               glyph: '👨‍👩‍👧‍👦', status: 'coming_soon', modules: 0,  accentStroke: '#06b6d4', school: 'geography-environment',
    tagline: 'How people shape and are shaped by place — culture, economy, and spatial patterns.' },

  // ── School of Technology Futures & Systems ─────────────────────────────
  { id: 'space-science',          name: 'Space Science & Exploration',   glyph: '🚀',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'Rockets, orbital mechanics, planetary science, and the future of space exploration.' },
  { id: 'astrobiology',           name: 'Astrobiology',                  glyph: '🌌',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'The origin of life, habitability, and the search for life beyond Earth.' },
  { id: 'robotics',               name: 'Robotics Systems Engineering',  glyph: '🤖',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'Mechanisms, sensors, control systems, and the engineering of autonomous machines.' },
  { id: 'autonomous-systems',     name: 'Autonomous Systems',            glyph: '🚗',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'Self-driving systems, drone navigation, and the architecture of autonomy.' },
  { id: 'nanotechnology',         name: 'Nanotechnology Foundations',    glyph: '⚗️',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'Engineering at the nanoscale — materials, devices, and molecular machines.' },
  { id: 'biotechnology',          name: 'Biotechnology Systems',         glyph: '🧬',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'CRISPR, synthetic biology, biomedical engineering, and the biotech revolution.' },
  { id: 'future-studies',         name: 'Future Studies',                glyph: '🔮',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'Scenario planning, trend analysis, and the methodology of thinking about the future.' },
  { id: 'emerging-technologies',  name: 'Emerging Technologies',         glyph: '💡',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'Quantum computing, extended reality, and the technologies rewriting the world.' },
  { id: 'complex-systems',        name: 'Complex Systems Theory',        glyph: '🕸️',  status: 'coming_soon', modules: 0,  accentStroke: '#8b5cf6', school: 'technology-futures',
    tagline: 'Emergence, networks, chaos, and how complex behaviour arises from simple rules.' },

  // ── School of Law, Governance & Justice ────────────────────────────────
  { id: 'legal-foundations',      name: 'Legal Foundations',             glyph: '📜',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Sources of law, legal reasoning, and the architecture of a legal system.' },
  { id: 'constitutional-law',     name: 'Constitutional Law',            glyph: '🏛️',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Constitutions, fundamental rights, and the limits of state power.' },
  { id: 'criminal-law',           name: 'Criminal Law',                  glyph: '🔒',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Offences, defences, mens rea, and the principles of criminal liability.' },
  { id: 'civil-law',              name: 'Civil Law',                     glyph: '🤝',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Contract, tort, property, and the private law framework of civil society.' },
  { id: 'international-law',      name: 'International Law',             glyph: '🌐',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Treaties, jurisdiction, and the rules that govern states in the international order.' },
  { id: 'human-rights-law',       name: 'Human Rights Law',              glyph: '✊',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Fundamental rights, international conventions, and their enforcement.' },
  { id: 'legal-reasoning',        name: 'Legal Reasoning',               glyph: '⚖️',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Statutory interpretation, case analysis, and the logic of legal argument.' },
  { id: 'governance-systems',     name: 'Governance Systems',            glyph: '🏛️',  status: 'coming_soon', modules: 0,  accentStroke: '#1d4ed8', school: 'law-governance',
    tagline: 'Democratic institutions, public administration, and how governance is structured.' },

  // ── School of Survival, Practical & Applied Skills ─────────────────────
  { id: 'wilderness-survival',    name: 'Wilderness Survival',           glyph: '🌲',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Shelter, fire, water, and food — core survival skills for any environment.' },
  { id: 'bushcraft',              name: 'Bushcraft',                     glyph: '🪓',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Woodcraft, tracking, plant knowledge, and the deep skills of living in nature.' },
  { id: 'navigation',             name: 'Navigation',                    glyph: '🧭',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Map reading, compass work, celestial navigation, and never being truly lost.' },
  { id: 'emergency-preparedness', name: 'Emergency Preparedness',        glyph: '🚨',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Disaster readiness, emergency plans, and building resilience for the unexpected.' },
  { id: 'homesteading',           name: 'Homesteading',                  glyph: '🏡',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Growing food, raising animals, and building a self-reliant home life.' },
  { id: 'self-sufficiency',       name: 'Self-Sufficiency Systems',      glyph: '⚙️',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Energy, water, food, and systems thinking for reducing dependence on external supply.' },
  { id: 'food-preservation',      name: 'Food Preservation',             glyph: '🫙',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Fermentation, canning, drying, and curing — the ancient art of making food last.' },
  { id: 'practical-engineering',  name: 'Practical Engineering Skills',  glyph: '🔨',  status: 'coming_soon', modules: 0,  accentStroke: '#92400e', school: 'survival-practical',
    tagline: 'Joinery, plumbing, electrical basics, and the hands-on skills every household needs.' },
]

/* ── Derived exports ─────────────────────────────────────────────────────── */

export const ACTIVE_DOMAINS     = DOMAINS.filter(d => d.status === 'active')
export const ACTIVE_DOMAIN_IDS  = ACTIVE_DOMAINS.map(d => d.id)
export const COMING_SOON_DOMAINS = DOMAINS.filter(d => d.status === 'coming_soon')
