export type Genre = 'all' | 'tech' | 'science' | 'history'

export interface Topic {
  id: string
  name: string
  glyph: string
  tagline: string
  status: 'active' | 'coming_soon'
  chunks: number
  accentStroke: string
  genre: Genre
}

export const TOPICS: Topic[] = [
  { id: 'java',       name: 'Java',             glyph: '☕',  tagline: 'From zero to job-ready. The complete apprentice-to-archmage pathway.',                                       status: 'active',      chunks: 71, accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'psychology', name: 'Psychology',       glyph: '🧠',  tagline: 'From foundations to frontier — the complete undergraduate-to-graduate psychology pathway.',              status: 'active',      chunks: 71, accentStroke: 'var(--purple)', genre: 'science' },
  { id: 'genealogy',  name: 'Genealogy',        glyph: '🌳',  tagline: 'From vital records to professional proof — become a skilled genealogical researcher.',                  status: 'active',      chunks: 71, accentStroke: 'var(--gold)',   genre: 'history' },
  { id: 'sciences',   name: 'Natural Sciences', glyph: '🔬',  tagline: 'From scientific method to frontier research — physics, chemistry, biology, and earth science.',         status: 'active',      chunks: 71, accentStroke: 'var(--teal)',   genre: 'science' },
  { id: 'tailwind',   name: 'Tailwind CSS',     glyph: '🎨',  tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',                          status: 'active',      chunks: 71, accentStroke: 'var(--purple)', genre: 'tech'    },
  { id: 'react',      name: 'React',            glyph: '⚛️', tagline: 'Component-driven UIs. Hooks, state, and the modern frontend — all the way to deployment.',              status: 'active',      chunks: 71, accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'sql',        name: 'SQL',              glyph: '🗃️', tagline: 'The language of data. SELECT to window functions — the queries every backend dev writes daily.',        status: 'coming_soon', chunks: 8,  accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'html',       name: 'HTML',             glyph: '📄', tagline: 'The structure of the web. Learn to author the skeleton of every page.',                                 status: 'coming_soon', chunks: 8,  accentStroke: 'var(--orange)', genre: 'tech'    },
  { id: 'css',        name: 'CSS',              glyph: '🖌️', tagline: 'Craft beautiful, responsive interfaces from the ground up.',                                            status: 'coming_soon', chunks: 10, accentStroke: 'var(--purple)', genre: 'tech'    },
  { id: 'javascript', name: 'JavaScript',       glyph: '⚡', tagline: 'Bring the web to life. Logic, events, async, and the DOM.',                                             status: 'coming_soon', chunks: 14, accentStroke: 'var(--gold)',   genre: 'tech'    },
  { id: 'python',     name: 'Python',           glyph: '🐍', tagline: 'Versatile, readable, powerful. Data, scripts, and automation.',                                         status: 'coming_soon', chunks: 12, accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'typescript', name: 'TypeScript',       glyph: '🔷', tagline: 'JavaScript with discipline. Types, interfaces, and confidence at scale.',                               status: 'coming_soon', chunks: 10, accentStroke: 'var(--gold)',   genre: 'tech'    },
]

export const ACTIVE_TOPICS = TOPICS.filter(t => t.status === 'active')
export const ACTIVE_TOPIC_IDS = ACTIVE_TOPICS.map(t => t.id)

export const COMING_SOON_TOPICS = TOPICS.filter(t => t.status === 'coming_soon')
