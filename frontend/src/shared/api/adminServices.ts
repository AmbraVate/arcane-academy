import api from './client'

// â”€â”€ Types â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export interface AdminStats {
  totalUsers: number; activeUsers7d: number; totalDomains: number
  totalChunks: number; totalLessons: number; totalQuestions: number
  totalNotes: number; totalCapstones: number
  openStuckReports: number; pendingCapstones: number
  recentSignups: AdminUser[]; contentHealth: ContentHealthItem[]
  subscriptionBreakdown: Record<string, number>
  domainEngagement: DomainEngagementItem[]
  signupTrend: DailyCount[]
  xpDistribution: XpBucket[]
}
export interface ContentHealthItem { lessonId: string; title: string; chunkTitle: string; domainId?: string; tier?: string; issues: string[] }
export interface DomainEngagementItem { domainId: string; domainName: string; glyph: string; totalLessons: number; totalCompletions: number; uniqueLearners: number }
export interface DailyCount { date: string; count: number }
export interface XpBucket { rank: string; count: number }

export interface AdminDomain {
  id: string; name: string; glyph: string; tagline: string
  accentColor: string; sortOrder: number; active: boolean
}

export interface AdminChunk {
  id: string; title: string; glyph: string; sortOrder: number
  tier: string; domainId: string; prerequisiteIds: string[]; subChunkCount: number
}

export interface AdminLesson {
  id: string; moduleId: string; title: string; sortOrder: number
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
  // Sprint 7 â€” downloadable resources
  downloadables: { title: string; type: string; url: string }[] | null
}

export interface StoryBeat {
  type: 'narration' | 'dialogue' | 'example'
  text?: string; av?: string; cls?: string; speaker?: string; sCls?: string
  [key: string]: unknown
}

export interface TestCase { [key: string]: string }

export interface AdminQuestion {
  id: string; lessonId: string; type: string; tier: string
  questionHtml: string; codeSnippet: string | null
  options: string[] | null; correctAnswer: string; explanationHtml: string
}

export interface AdminUser {
  id: string; username: string; email: string; rank: string
  totalXp: number; streakDays: number; authProvider: string; role: string
  blocked: boolean; bypassPaywall: boolean
  createdAt: string; lastLoginAt: string | null; completedLessons: number
}

export interface UserStats {
  userId: string; username: string; email: string
  totalXp: number; rank: string; streakDays: number
  lessonsCompleted: number; chunksCompleted: number
  badgesEarned: number; reviewSessionsCompleted: number
  joinedAt: string; lastLoginAt: string | null
  blocked: boolean; role: string
}

export interface PagedResponse<T> {
  content: T[]; totalElements: number; totalPages: number; page: number
}

// â”€â”€ Stats â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const adminStatsApi = {
  get: async (): Promise<AdminStats> => {
    const { data } = await api.get('/api/admin/stats')
    return data
  },
}

// â”€â”€ Domains â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const adminDomainApi = {
  list: async (): Promise<AdminDomain[]> => {
    const { data } = await api.get('/api/admin/domains')
    return data
  },
  create: async (domain: Partial<AdminDomain>): Promise<AdminDomain> => {
    const { data } = await api.post('/api/admin/domains', domain)
    return data
  },
  update: async (id: string, domain: Partial<AdminDomain>): Promise<AdminDomain> => {
    const { data } = await api.put(`/api/admin/domains/${id}`, domain)
    return data
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/domains/${id}`)
  },
}

// â”€â”€ Topics (Module → Topic → Lesson cluster) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export interface AdminTopic {
  id: string; moduleId: string; title: string
  purposeHtml: string | null; learningOutcomesJson: string | null; sortOrder: number
}

export const adminTopicApi = {
  list: async (moduleId?: string): Promise<AdminTopic[]> => {
    const { data } = await api.get('/api/admin/topics', { params: moduleId ? { moduleId } : {} })
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

// â”€â”€ Chunks â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const adminChunkApi = {
  list: async (domainId?: string): Promise<AdminChunk[]> => {
    const { data } = await api.get('/api/admin/modules', { params: domainId ? { domainId } : {} })
    return data
  },
  get: async (id: string): Promise<AdminChunk> => {
    const { data } = await api.get(`/api/admin/modules/${id}`)
    return data
  },
  create: async (chunk: Partial<AdminChunk>): Promise<AdminChunk> => {
    const { data } = await api.post('/api/admin/modules', chunk)
    return data
  },
  update: async (id: string, chunk: Partial<AdminChunk>): Promise<AdminChunk> => {
    const { data } = await api.put(`/api/admin/modules/${id}`, chunk)
    return data
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/modules/${id}`)
  },
}

// â”€â”€ Lessons â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const adminLessonApi = {
  list: async (moduleId: string): Promise<AdminLesson[]> => {
    const { data } = await api.get('/api/admin/lessons', { params: { moduleId } })
    return data
  },
  get: async (id: string): Promise<AdminLesson> => {
    const { data } = await api.get(`/api/admin/lessons/${id}`)
    return data
  },
  create: async (sc: Partial<AdminLesson>): Promise<AdminLesson> => {
    const { data } = await api.post('/api/admin/lessons', sc)
    return data
  },
  update: async (id: string, sc: Partial<AdminLesson>): Promise<AdminLesson> => {
    const { data } = await api.put(`/api/admin/lessons/${id}`, sc)
    return data
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/lessons/${id}`)
  },
}

// â”€â”€ Questions â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const adminQuestionApi = {
  list: async (lessonId: string): Promise<AdminQuestion[]> => {
    const { data } = await api.get('/api/admin/questions', { params: { lessonId } })
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

// â”€â”€ Users â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

// â”€â”€ Stuck Reports â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export interface StuckReport {
  id: string
  userId: string
  username: string
  email: string
  domainId: string | null
  lessonId: string | null
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

// â”€â”€ Admin Capstones â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export interface AdminCapstone {
  id: string
  userId: string
  moduleId: string
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

// â”€â”€ Import / Export â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const adminContentApi = {
  exportChunk: (moduleId: string) => `/api/admin/content/export/chunk/${moduleId}`,
  importChunk: async (file: File): Promise<{ status: string; moduleId: string; lessons: number }> => {
    const form = new FormData()
    form.append('file', file)
    const { data } = await api.post('/api/admin/content/import', form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    return data
  },
}
