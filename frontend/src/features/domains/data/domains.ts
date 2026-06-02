export type School = 'engineering-systems' | 'mathematical-scientific' | 'human-systems' | 'creative-cultural' | 'heritage'

export interface Domain {
  id: string
  name: string
  glyph: string
  tagline: string
  status: 'active' | 'coming_soon'
  modules: number
  accentStroke: string
  school: School
  trackGroup?: string
  guildName?: string
}

export interface SchoolMeta {
  name: string
  glyph: string
  color: string
  description: string
}

export const SCHOOL_META: Record<School, SchoolMeta> = {
  'engineering-systems':     { name: 'School of Engineering',                  glyph: '⚡',  color: '#a78bfa', description: 'Software Engineering, Frontend Engineering, Data Engineering, Cyber Engineering' },
  'mathematical-scientific': { name: 'School of Mathematics',                  glyph: '🔬', color: '#4ade80', description: 'Mathematics, Natural Sciences, Botany' },
  'human-systems':           { name: 'School of History & Civilisation',       glyph: '🧠', color: '#c4b5fd', description: 'Psychology, Philosophy, History, Economics' },
  'creative-cultural':       { name: 'School of Creative Arts',                glyph: '🎵', color: '#f472b6', description: 'Music, Communication & Writing' },
  'heritage':                { name: 'School of Heritage',                     glyph: '🌳', color: '#c9a227', description: 'Genealogy and family history' },
}

export interface TrackGroup {
  id: string
  name: string
  glyph: string
  color: string
  description: string
  school: School
}

export const TRACK_GROUPS: TrackGroup[] = [
  { id: 'software-engineering', name: 'Software Engineering',        glyph: '⚡',  color: '#a78bfa', description: 'Language-agnostic engineering fundamentals, Java, and Python', school: 'engineering-systems' },
  { id: 'frontend-engineering', name: 'Frontend Engineering',        glyph: '🎨', color: '#38bdf8', description: 'React, Tailwind CSS, HTML, CSS, JavaScript, and TypeScript',      school: 'engineering-systems' },
  { id: 'data-databases',       name: 'Data Engineering & Databases', glyph: '🗃️', color: '#7dd3fc', description: 'SQL, relational database design, and data pipelines',           school: 'engineering-systems' },
  { id: 'cyber-engineering',    name: 'Cyber Engineering',           glyph: '🔐', color: '#f87171', description: 'Cybersecurity, network fundamentals, and ethical hacking',         school: 'engineering-systems' },
]

/** Schools that have track-group sub-navigation (all others go straight to domains). */
export function schoolHasTrackGroups(school: School): boolean {
  return TRACK_GROUPS.some(tg => tg.school === school)
}

export function trackGroupsForSchool(school: School): TrackGroup[] {
  return TRACK_GROUPS.filter(tg => tg.school === school)
}


export const DOMAINS: Domain[] = [
  // Engineering & Systems — Software Engineering
  { id: 'software-engineering', name: 'Software Engineering', glyph: '⚙️',  tagline: 'Build reliable systems — computational thinking, design, architecture, taught through Java.',        status: 'active',      modules: 71, accentStroke: 'var(--teal)',   school: 'engineering-systems', trackGroup: 'software-engineering', guildName: 'Guild of Systems Architects' },
  { id: 'python',     name: 'Python',           glyph: '🐍', tagline: 'Versatile, readable, powerful. Data, scripts, and automation.',                                         status: 'coming_soon', modules: 12, accentStroke: 'var(--teal)',   school: 'engineering-systems', trackGroup: 'software-engineering', guildName: 'Guild of Systems Architects' },
  // Engineering & Systems — Frontend Engineering
  { id: 'react',      name: 'React',            glyph: '⚛️', tagline: 'Component-driven UIs. Hooks, state, and the modern frontend — all the way to deployment.',              status: 'active',      modules: 71, accentStroke: 'var(--teal)',   school: 'engineering-systems', trackGroup: 'frontend-engineering', guildName: 'Guild of Systems Architects' },
  { id: 'tailwind',   name: 'Tailwind CSS',     glyph: '🎨',  tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',                         status: 'active',      modules: 71, accentStroke: 'var(--purple)', school: 'engineering-systems', trackGroup: 'frontend-engineering', guildName: 'Guild of Artisan Interfaces' },
  { id: 'javascript', name: 'JavaScript',       glyph: '⚡', tagline: 'Bring the web to life. Logic, events, async, and the DOM.',                                             status: 'coming_soon', modules: 14, accentStroke: 'var(--gold)',   school: 'engineering-systems', trackGroup: 'frontend-engineering', guildName: 'Guild of Systems Architects' },
  { id: 'typescript', name: 'TypeScript',       glyph: '🔷', tagline: 'JavaScript with discipline. Types, interfaces, and confidence at scale.',                               status: 'coming_soon', modules: 10, accentStroke: 'var(--gold)',   school: 'engineering-systems', trackGroup: 'frontend-engineering', guildName: 'Guild of Systems Architects' },
  { id: 'html',       name: 'HTML',             glyph: '📄', tagline: 'The structure of the web. Learn to author the skeleton of every page.',                                 status: 'coming_soon', modules: 8,  accentStroke: 'var(--orange)', school: 'engineering-systems', trackGroup: 'frontend-engineering', guildName: 'Guild of Artisan Interfaces' },
  { id: 'css',        name: 'CSS',              glyph: '🖌️', tagline: 'Craft beautiful, responsive interfaces from the ground up.',                                            status: 'coming_soon', modules: 10, accentStroke: 'var(--purple)', school: 'engineering-systems', trackGroup: 'frontend-engineering', guildName: 'Guild of Artisan Interfaces' },
  // Engineering & Systems — Data & Databases
  { id: 'sql',              name: 'SQL',              glyph: '🗃️', tagline: 'The language of data. SELECT to window functions — the queries every backend dev writes daily.',          status: 'coming_soon', modules: 8,  accentStroke: 'var(--teal)',   school: 'engineering-systems', trackGroup: 'data-databases',       guildName: 'Vault of Records' },
  // Engineering & Systems — Cyber Engineering
  { id: 'cybersecurity',    name: 'Cybersecurity',    glyph: '🔐', tagline: 'Defend, detect, and respond — understanding threats from both sides of the firewall.',                    status: 'coming_soon', modules: 0,  accentStroke: '#f87171',       school: 'engineering-systems', trackGroup: 'cyber-engineering',    guildName: 'Order of the Cipher' },
  { id: 'networking',       name: 'Networking',       glyph: '🌐', tagline: 'Protocols, packets, and routing — the physical and logical backbone of every connected system.',          status: 'coming_soon', modules: 0,  accentStroke: '#f87171',       school: 'engineering-systems', trackGroup: 'cyber-engineering',    guildName: 'Order of the Cipher' },
  // Mathematical & Scientific Foundations
  { id: 'sciences',   name: 'Natural Sciences', glyph: '🔬',  tagline: 'From scientific method to frontier research — physics, chemistry, biology, and earth science.',        status: 'active',      modules: 71, accentStroke: 'var(--teal)',   school: 'mathematical-scientific', guildName: 'Observatory of Nature' },
  { id: 'mathematics', name: 'Mathematics',     glyph: '∑',  tagline: 'From algebra to probability — the universal language of pattern and structure.',                        status: 'coming_soon', modules: 71, accentStroke: 'var(--gold)',   school: 'mathematical-scientific', guildName: 'Lodge of Theorems' },
  { id: 'botany',      name: 'Botany',          glyph: '🌿', tagline: 'Plant biology, ecology, and the living architecture of ecosystems.',                                    status: 'coming_soon', modules: 71, accentStroke: 'var(--teal)',   school: 'mathematical-scientific', guildName: 'Verdant Archive' },
  // Human Systems
  { id: 'psychology', name: 'Psychology',       glyph: '🧠',  tagline: 'From foundations to frontier — the complete undergraduate-to-graduate psychology pathway.',             status: 'active',      modules: 71, accentStroke: 'var(--purple)', school: 'human-systems',           guildName: 'Order of Minds' },
  { id: 'philosophy',  name: 'Philosophy',      glyph: '⚖️', tagline: 'Logic, epistemology, ethics — the tools of rigorous thought across every domain.',                     status: 'coming_soon', modules: 71, accentStroke: 'var(--purple)', school: 'human-systems',           guildName: 'Sanctum of Dialectics' },
  { id: 'history',     name: 'History',         glyph: '📜', tagline: 'Civilisations, revolutions, and the forces that shaped the modern world.',                              status: 'coming_soon', modules: 71, accentStroke: 'var(--gold)',   school: 'human-systems',           guildName: 'Chronicle Vaults' },
  { id: 'economics',   name: 'Economics',       glyph: '📈', tagline: 'Incentives, markets, and game theory — understanding why the world organises itself the way it does.',  status: 'coming_soon', modules: 71, accentStroke: 'var(--teal)',   school: 'human-systems',           guildName: 'Exchange of Incentives' },
  // Heritage Systems
  { id: 'genealogy',  name: 'Genealogy',        glyph: '🌳',  tagline: 'From vital records to professional proof — become a skilled genealogical researcher.',                 status: 'active',      modules: 71, accentStroke: 'var(--gold)',   school: 'heritage',                guildName: 'Keepers of Lineage' },
  // Creative & Cultural Systems
  { id: 'music',       name: 'Music',           glyph: '🎵', tagline: 'Rhythm, harmony, and composition — the mathematics of sound and emotion.',                              status: 'coming_soon', modules: 71, accentStroke: 'var(--purple)', school: 'creative-cultural',       guildName: 'Conservatory of Harmonics' },
]

export const ACTIVE_DOMAINS = DOMAINS.filter(d => d.status === 'active')
export const ACTIVE_DOMAIN_IDS = ACTIVE_DOMAINS.map(d => d.id)

export const COMING_SOON_DOMAINS = DOMAINS.filter(d => d.status === 'coming_soon')
