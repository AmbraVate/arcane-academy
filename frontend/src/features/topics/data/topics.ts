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
  guildName?: string
}

export const SCHOOLS: Record<School, { name: string; description: string }> = {
  'engineering-systems':     { name: 'Engineering & Systems',                 description: 'Software Engineering, Frontend Engineering, Data & Databases' },
  'mathematical-scientific': { name: 'Mathematical & Scientific Foundations', description: 'Mathematics, Natural Sciences, Botany' },
  'human-systems':           { name: 'Human Systems',                         description: 'Psychology, Philosophy, History, Economics' },
  'creative-cultural':       { name: 'Creative & Cultural Systems',           description: 'Music, Communication & Writing' },
  'heritage':                { name: 'Heritage Systems',                      description: 'Genealogy and family history' },
}

export const DOMAINS: Domain[] = [
  { id: 'java',       name: 'Java',             glyph: '☕',  tagline: 'From zero to job-ready. The complete apprentice-to-archmage pathway.',                                       status: 'active',      modules: 71, accentStroke: 'var(--teal)',   school: 'engineering-systems',     guildName: 'Guild of Systems Architects' },
  { id: 'psychology', name: 'Psychology',       glyph: '🧠',  tagline: 'From foundations to frontier — the complete undergraduate-to-graduate psychology pathway.',              status: 'active',      modules: 71, accentStroke: 'var(--purple)', school: 'human-systems',           guildName: 'Order of Minds' },
  { id: 'genealogy',  name: 'Genealogy',        glyph: '🌳',  tagline: 'From vital records to professional proof — become a skilled genealogical researcher.',                  status: 'active',      modules: 71, accentStroke: 'var(--gold)',   school: 'heritage',                guildName: 'Keepers of Lineage' },
  { id: 'sciences',   name: 'Natural Sciences', glyph: '🔬',  tagline: 'From scientific method to frontier research — physics, chemistry, biology, and earth science.',         status: 'active',      modules: 71, accentStroke: 'var(--teal)',   school: 'mathematical-scientific', guildName: 'Observatory of Nature' },
  { id: 'tailwind',   name: 'Tailwind CSS',     glyph: '🎨',  tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',                          status: 'active',      modules: 71, accentStroke: 'var(--purple)', school: 'engineering-systems',     guildName: 'Guild of Artisan Interfaces' },
  { id: 'react',      name: 'React',            glyph: '⚛️', tagline: 'Component-driven UIs. Hooks, state, and the modern frontend — all the way to deployment.',              status: 'active',      modules: 71, accentStroke: 'var(--teal)',   school: 'engineering-systems',     guildName: 'Guild of Systems Architects' },
  // Engineering & Systems — coming soon
  { id: 'sql',        name: 'SQL',              glyph: '🗃️', tagline: 'The language of data. SELECT to window functions — the queries every backend dev writes daily.',        status: 'coming_soon', modules: 8,  accentStroke: 'var(--teal)',   school: 'engineering-systems',     guildName: 'Vault of Records' },
  { id: 'html',       name: 'HTML',             glyph: '📄', tagline: 'The structure of the web. Learn to author the skeleton of every page.',                                 status: 'coming_soon', modules: 8,  accentStroke: 'var(--orange)', school: 'engineering-systems',     guildName: 'Guild of Artisan Interfaces' },
  { id: 'css',        name: 'CSS',              glyph: '🖌️', tagline: 'Craft beautiful, responsive interfaces from the ground up.',                                            status: 'coming_soon', modules: 10, accentStroke: 'var(--purple)', school: 'engineering-systems',     guildName: 'Guild of Artisan Interfaces' },
  { id: 'javascript', name: 'JavaScript',       glyph: '⚡', tagline: 'Bring the web to life. Logic, events, async, and the DOM.',                                             status: 'coming_soon', modules: 14, accentStroke: 'var(--gold)',   school: 'engineering-systems',     guildName: 'Guild of Systems Architects' },
  { id: 'python',     name: 'Python',           glyph: '🐍', tagline: 'Versatile, readable, powerful. Data, scripts, and automation.',                                         status: 'coming_soon', modules: 12, accentStroke: 'var(--teal)',   school: 'engineering-systems',     guildName: 'Guild of Systems Architects' },
  { id: 'typescript', name: 'TypeScript',       glyph: '🔷', tagline: 'JavaScript with discipline. Types, interfaces, and confidence at scale.',                               status: 'coming_soon', modules: 10, accentStroke: 'var(--gold)',   school: 'engineering-systems',     guildName: 'Guild of Systems Architects' },
  // Mathematical & Scientific — coming soon (Blueprint Core 12)
  { id: 'mathematics', name: 'Mathematics',     glyph: '∑',  tagline: 'From algebra to probability — the universal language of pattern and structure.',                        status: 'coming_soon', modules: 71, accentStroke: 'var(--gold)',   school: 'mathematical-scientific', guildName: 'Lodge of Theorems' },
  { id: 'botany',      name: 'Botany',          glyph: '🌿', tagline: 'Plant biology, ecology, and the living architecture of ecosystems.',                                    status: 'coming_soon', modules: 71, accentStroke: 'var(--teal)',   school: 'mathematical-scientific', guildName: 'Verdant Archive' },
  // Human Systems — coming soon (Blueprint Core 12)
  { id: 'philosophy',  name: 'Philosophy',      glyph: '⚖️', tagline: 'Logic, epistemology, ethics — the tools of rigorous thought across every domain.',                     status: 'coming_soon', modules: 71, accentStroke: 'var(--purple)', school: 'human-systems',           guildName: 'Sanctum of Dialectics' },
  { id: 'history',     name: 'History',         glyph: '📜', tagline: 'Civilisations, revolutions, and the forces that shaped the modern world.',                              status: 'coming_soon', modules: 71, accentStroke: 'var(--gold)',   school: 'human-systems',           guildName: 'Chronicle Vaults' },
  { id: 'economics',   name: 'Economics',       glyph: '📈', tagline: 'Incentives, markets, and game theory — understanding why the world organises itself the way it does.',  status: 'coming_soon', modules: 71, accentStroke: 'var(--teal)',   school: 'human-systems',           guildName: 'Exchange of Incentives' },
  // Creative & Cultural — coming soon (Blueprint Core 12)
  { id: 'music',       name: 'Music',           glyph: '🎵', tagline: 'Rhythm, harmony, and composition — the mathematics of sound and emotion.',                              status: 'coming_soon', modules: 71, accentStroke: 'var(--purple)', school: 'creative-cultural',       guildName: 'Conservatory of Harmonics' },
]

export const ACTIVE_DOMAINS = DOMAINS.filter(d => d.status === 'active')
export const ACTIVE_DOMAIN_IDS = ACTIVE_DOMAINS.map(d => d.id)

export const COMING_SOON_DOMAINS = DOMAINS.filter(d => d.status === 'coming_soon')

// Legacy aliases for gradual migration
export type Topic = Domain
export const TOPICS = DOMAINS
export const ACTIVE_TOPICS = ACTIVE_DOMAINS
export const ACTIVE_TOPIC_IDS = ACTIVE_DOMAIN_IDS
export const COMING_SOON_TOPICS = COMING_SOON_DOMAINS
