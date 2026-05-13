import api from './client'

// ── Types ─────────────────────────────────────────────────────────────────────

export interface AdminStats {
  totalUsers: number; activeUsers7d: number; totalTopics: number
  totalChunks: number; totalSubChunks: number; totalQuestions: number
  recentSignups: AdminUser[]; contentHealth: ContentHealthItem[]
}
export interface ContentHealthItem { subChunkId: string; title: string; chunkTitle: string; issues: string[] }

export interface AdminTopic {
  id: string; name: string; glyph: string; tagline: string
  accentColor: string; sortOrder: number; active: boolean
}

export interface AdminChunk {
  id: string; title: string; glyph: string; sortOrder: number
  tier: string; topicId: string; prerequisiteIds: string[]; subChunkCount: number
}

export interface AdminSubChunk {
  id: string; chunkId: string; title: string; sortOrder: number
  xpReward: number; practiceType: string; filename: string
  hookHtml: string | null; explanationHtml: string | null
  storyBeats: StoryBeat[] | null
  guidedPracticeHtml: string | null; guidedPracticeStarterCode: string | null
  guidedPracticeTests: TestCase[] | null
  soloPracticeHtml: string | null; feynmanPrompt: string | null
  questionCount: number
}

export interface StoryBeat {
  type: 'narration' | 'dialogue' | 'example'
  text?: string; av?: string; cls?: string; speaker?: string; sCls?: string
  [key: string]: unknown
}

export interface TestCase { [key: string]: string }

export interface AdminQuestion {
  id: string; subChunkId: string; type: string; tier: string
  questionHtml: string; codeSnippet: string | null
  options: string[] | null; correctAnswer: string; explanationHtml: string
}

export interface AdminUser {
  id: string; username: string; email: string; rank: string
  totalXp: number; streakDays: number; authProvider: string; role: string
  createdAt: string; lastLoginAt: string | null; completedSubChunks: number
}

export interface PagedResponse<T> {
  content: T[]; totalElements: number; totalPages: number; page: number
}

// ── Stats ─────────────────────────────────────────────────────────────────────

export const adminStatsApi = {
  get: async (): Promise<AdminStats> => {
    const { data } = await api.get('/api/admin/stats')
    return data
  },
}

// ── Topics ────────────────────────────────────────────────────────────────────

export const adminTopicApi = {
  list: async (): Promise<AdminTopic[]> => {
    const { data } = await api.get('/api/admin/topics')
    return data
  },
  create: async (topic: Partial<AdminTopic>): Promise<AdminTopic> => {
    const { data } = await api.post('/api/admin/topics', topic)
    return data
  },
  update: async (id: string, topic: Partial<AdminTopic>): Promise<AdminTopic> => {
    const { data } = await api.put(`/api/admin/topics/${id}`, topic)
    return data
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/topics/${id}`)
  },
}

// ── Chunks ────────────────────────────────────────────────────────────────────

export const adminChunkApi = {
  list: async (topicId?: string): Promise<AdminChunk[]> => {
    const { data } = await api.get('/api/admin/chunks', { params: topicId ? { topicId } : {} })
    return data
  },
  get: async (id: string): Promise<AdminChunk> => {
    const { data } = await api.get(`/api/admin/chunks/${id}`)
    return data
  },
  create: async (chunk: Partial<AdminChunk>): Promise<AdminChunk> => {
    const { data } = await api.post('/api/admin/chunks', chunk)
    return data
  },
  update: async (id: string, chunk: Partial<AdminChunk>): Promise<AdminChunk> => {
    const { data } = await api.put(`/api/admin/chunks/${id}`, chunk)
    return data
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/chunks/${id}`)
  },
}

// ── SubChunks ─────────────────────────────────────────────────────────────────

export const adminSubChunkApi = {
  list: async (chunkId: string): Promise<AdminSubChunk[]> => {
    const { data } = await api.get('/api/admin/subchunks', { params: { chunkId } })
    return data
  },
  get: async (id: string): Promise<AdminSubChunk> => {
    const { data } = await api.get(`/api/admin/subchunks/${id}`)
    return data
  },
  create: async (sc: Partial<AdminSubChunk>): Promise<AdminSubChunk> => {
    const { data } = await api.post('/api/admin/subchunks', sc)
    return data
  },
  update: async (id: string, sc: Partial<AdminSubChunk>): Promise<AdminSubChunk> => {
    const { data } = await api.put(`/api/admin/subchunks/${id}`, sc)
    return data
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/subchunks/${id}`)
  },
}

// ── Questions ─────────────────────────────────────────────────────────────────

export const adminQuestionApi = {
  list: async (subChunkId: string): Promise<AdminQuestion[]> => {
    const { data } = await api.get('/api/admin/questions', { params: { subChunkId } })
    return data
  },
  create: async (q: Partial<AdminQuestion>): Promise<AdminQuestion> => {
    const { data } = await api.post('/api/admin/questions', q)
    return data
  },
  update: async (id: string, q: Partial<AdminQuestion>): Promise<AdminQuestion> => {
    const { data } = await api.put(`/api/admin/questions/${id}`, q)
    return data
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/questions/${id}`)
  },
}

// ── Users ─────────────────────────────────────────────────────────────────────

export const adminUserApi = {
  list: async (page = 0, size = 20, search?: string): Promise<PagedResponse<AdminUser>> => {
    const { data } = await api.get('/api/admin/users', { params: { page, size, search } })
    return data
  },
  get: async (id: string): Promise<AdminUser> => {
    const { data } = await api.get(`/api/admin/users/${id}`)
    return data
  },
  resetProgress: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/users/${id}/progress`)
  },
}

// ── Import / Export ───────────────────────────────────────────────────────────

export const adminContentApi = {
  exportChunk: (chunkId: string) => `/api/admin/content/export/chunk/${chunkId}`,
  importChunk: async (file: File): Promise<{ status: string; chunkId: string; subChunks: number }> => {
    const form = new FormData()
    form.append('file', file)
    const { data } = await api.post('/api/admin/content/import', form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    return data
  },
}
