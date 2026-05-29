import api from './client'

// ── Types ─────────────────────────────────────────────────────────────────────

export interface AdminStats {
  totalUsers: number; activeUsers7d: number; totalTopics: number
  totalChunks: number; totalSubChunks: number; totalQuestions: number
  totalNotes: number; totalCapstones: number
  openStuckReports: number; pendingCapstones: number
  recentSignups: AdminUser[]; contentHealth: ContentHealthItem[]
  subscriptionBreakdown: Record<string, number>
  topicEngagement: TopicEngagementItem[]
  signupTrend: DailyCount[]
  xpDistribution: XpBucket[]
}
export interface ContentHealthItem { subChunkId: string; title: string; chunkTitle: string; topicId?: string; tier?: string; issues: string[] }
export interface TopicEngagementItem { topicId: string; topicName: string; glyph: string; totalSubChunks: number; totalCompletions: number; uniqueLearners: number }
export interface DailyCount { date: string; count: number }
export interface XpBucket { rank: string; count: number }

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
  guidedPracticeTests: TestCase[] | null; guidedPracticeModelAnswer: string | null
  soloPracticeHtml: string | null; modelAnswer: string | null; feynmanPrompt: string | null
  questionCount: number
  // Structured lesson metadata (Sprint 1)
  learningObjectives: string[] | null
  challenge: { html: string; starterCode: string | null; tests: Record<string, unknown>[] | null } | null
  miniProject: string | null
  commonMistakes: string[] | null
  assessmentCriteria: string[] | null
  // Sprint 7 — downloadable resources
  downloadables: { title: string; type: string; url: string }[] | null
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
  blocked: boolean; bypassPaywall: boolean
  createdAt: string; lastLoginAt: string | null; completedSubChunks: number
}

export interface UserStats {
  userId: string; username: string; email: string
  totalXp: number; rank: string; streakDays: number
  subChunksCompleted: number; chunksCompleted: number
  badgesEarned: number; reviewSessionsCompleted: number
  joinedAt: string; lastLoginAt: string | null
  blocked: boolean; role: string
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
  getStats: async (id: string): Promise<UserStats> => {
    const { data } = await api.get(`/api/admin/users/${id}/stats`)
    return data
  },
  resetProgress: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/users/${id}/progress`)
  },
  setBlocked: async (id: string, blocked: boolean): Promise<AdminUser> => {
    const { data } = await api.put(`/api/admin/users/${id}/blocked`, { blocked })
    return data
  },
  setRole: async (id: string, role: string): Promise<AdminUser> => {
    const { data } = await api.put(`/api/admin/users/${id}/role`, { role })
    return data
  },
  setBypassPaywall: async (id: string, bypassPaywall: boolean): Promise<AdminUser> => {
    const { data } = await api.patch(`/api/admin/users/${id}/bypass-paywall`, { bypassPaywall })
    return data
  },
}

// ── Stuck Reports ─────────────────────────────────────────────────────────────

export interface StuckReport {
  id: string
  userId: string
  username: string
  email: string
  topicId: string | null
  subChunkId: string | null
  currentPhase: string | null
  currentUrl: string | null
  userMessage: string | null
  userAgent: string | null
  status: 'NEW' | 'REVIEWED' | 'RESOLVED'
  adminNotes: string | null
  screenshotData: string | null
  createdAt: string
  updatedAt: string | null
}

export const adminStuckReportApi = {
  list: async (page = 0, size = 25): Promise<PagedResponse<StuckReport>> => {
    const { data } = await api.get('/api/admin/stuck-reports', { params: { page, size } })
    return data
  },
  updateStatus: async (id: string, status: StuckReport['status'], adminNotes?: string): Promise<StuckReport> => {
    const { data } = await api.patch(`/api/admin/stuck-reports/${id}`, { status, adminNotes })
    return data
  },
}

// ── Admin Capstones ───────────────────────────────────────────────────────────

export interface AdminCapstone {
  id: string
  userId: string
  chunkId: string
  title: string
  description: string | null
  codeContent: string | null
  githubUrl: string | null
  adminFeedback: string | null
  reviewedAt: string | null
  createdAt: string
  updatedAt: string
}

export const adminCapstoneApi = {
  list: async (page = 0, size = 25): Promise<PagedResponse<AdminCapstone>> => {
    const { data } = await api.get('/api/admin/capstones', { params: { page, size } })
    return data
  },
  addFeedback: async (id: string, adminFeedback: string): Promise<AdminCapstone> => {
    const { data } = await api.put(`/api/admin/capstones/${id}/feedback`, { adminFeedback })
    return data
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
