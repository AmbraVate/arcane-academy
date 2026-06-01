// â”€â”€ Auth â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export type SubscriptionStatus = 'FREE' | 'MONTHLY' | 'ANNUAL' | 'LIFETIME' | 'CANCELLED'

export interface User {
  userId: string; username: string; totalXp: number; rank: string; streakDays: number; token: string
  role?: 'USER' | 'ADMIN'
  /** Admin-granted flag allowing enrolment in multiple topics without a subscription. */
  bypassPaywall?: boolean
  /** Current billing status. FREE = one topic allowed; MONTHLY/ANNUAL/LIFETIME = full access. */
  subscriptionStatus?: SubscriptionStatus
  /** False until the user completes or skips the onboarding walkthrough. */
  onboardingCompleted?: boolean
}

/** Returns true when the user has full, active access to all topics. */
export function hasActiveSubscription(user?: User | null): boolean {
  if (!user) return false
  if (user.role === 'ADMIN' || user.bypassPaywall) return true
  return (
    user.subscriptionStatus === 'MONTHLY' ||
    user.subscriptionStatus === 'ANNUAL' ||
    user.subscriptionStatus === 'LIFETIME'
  )
}

// â”€â”€ Shared (kept from old system) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface StoryBeat {
  type: 'narration' | 'dialogue' | 'example'; text: string
  av?: string; cls?: string; speaker?: string; sCls?: string
}
export interface TestCaseLabel { label: string }
export interface TestResult { label: string; passed: boolean; actualOutput: string; expectedOutput: string }
export interface Badge { id: string; displayName: string; description: string; glyph: string; category: string; earned: boolean; earnedAt: string | null }
export interface CodeRunResponse { output: string | null; error: string | null; status: 'SUCCESS'|'COMPILE_ERROR'|'RUNTIME_ERROR'|'TIMEOUT'|'ERROR' }

// â”€â”€ Topic (Module → Topic → Lesson cluster level) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface Topic {
  id: string
  title: string
  purposeHtml: string | null
  sortOrder: number
}

// â”€â”€ Chunks â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface ModuleSummary {
  id: string; title: string; glyph: string; status: string
  totalLessons: number; completedLessons: number
  memoryStrength: number; healthColor: string; prerequisiteIds: string[]
}

export interface LessonSummary {
  id: string; title: string; sortOrder: number; status: string
  currentPhase: string; memoryStrength: number; healthColor: string
  feynmanCompleted: boolean; xpReward: number
  // Sprint 1 — chip metadata
  practiceType: string
  learningObjectiveCount: number
  hasChallenge: boolean
  hasMiniProject: boolean
  // Phase 1 — topic grouping
  topicId: string | null
  topicTitle: string | null
}

export interface ModuleDetail {
  id: string; domainId: string; title: string; glyph: string; status: string
  /** Ordered topic clusters — used to group lessons in the module map. */
  topics: Topic[]
  lessons: LessonSummary[]
}

// â”€â”€ Encoding â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export type EncodingPhase = 'HOOK' | 'EXPLANATION' | 'GUIDED_PRACTICE' | 'SOLO_PRACTICE' | 'RETRIEVAL_CHECK' | 'INTEGRATION' | 'COMPLETE'

export type QuestType = 'KNOWLEDGE' | 'GUIDED' | 'PRACTICE' | 'INVESTIGATION' | 'SYNTHESIS' | 'MASTERY'

export interface StoryRabbitHoleTerm { term: string; description: string }

export interface LessonEncoding {
  lessonId: string; moduleId: string; domainId: string; title: string
  phase: EncodingPhase; status: string
  hookHtml: string | null; explanationHtml: string | null
  storyBeats: StoryBeat[] | null
  guidedPracticeHtml: string | null; starterCode: string | null
  soloPracticeHtml: string | null
  testCaseLabels: TestCaseLabel[] | null; filename: string | null
  retrievalQuestions: QuestionDto[] | null
  feynmanPrompt: string | null; xpReward: number
  /** "JAVA" | "TAILWIND" | "NONE" */
  practiceType: string
  rabbitHoleTerms: StoryRabbitHoleTerm[] | null
  /** Exemplar answer revealed after solo practice is complete (written-response sub-chunks only). */
  modelAnswer: string | null
  /** Exemplar answer revealed after guided practice is passed (written-response sub-chunks only). */
  guidedPracticeModelAnswer: string | null
  // Sprint 1 — structured lesson metadata
  learningObjectives: string[] | null
  challenge: { html: string; starterCode: string | null; tests: Record<string, unknown>[] | null } | null
  miniProject: string | null
  commonMistakes: string[] | null
  assessmentCriteria: string[] | null
  // Sprint 7 — downloadable resources
  downloadables: Downloadable[] | null
  integrationPrompt: string | null
  questType: QuestType | null
  // Phase 2 — Markdown section fields (null for JSON-seeded lessons)
  loreIntroHtml: string | null
  whyItMattersHtml: string | null
  workedExamplesHtml: string | null
  mentalModelHtml: string | null
  miniSummaryHtml: string | null
  loreConclusionHtml: string | null
  // Phase 3 — guided step engine
  /** True when this lesson has guided steps; frontend switches to GuidedStepper. */
  hasGuidedSteps: boolean
  // Phase 4 — solo assessment types
  /** DETERMINISTIC | RUBRIC_REFLECTION | PATTERN_MATCH | AI_REVIEW — null means DETERMINISTIC */
  soloAssessmentType: string | null
  /** Rubric checklist items shown for RUBRIC_REFLECTION solo practice */
  rubricItems: string[] | null
  /** Remaining AI review quota for the current month — only meaningful for AI_REVIEW */
  aiReviewsRemaining: number
}

// ── Phase 3 — Guided steps ────────────────────────────────────────────────────

export type GuidedStepInputType =
  | 'FILL_BLANK' | 'MULTIPLE_CHOICE' | 'SHORT_TEXT' | 'CODE' | 'DRAG_DROP' | 'SEQUENCE'

export interface GuidedStepDto {
  id: string
  sortOrder: number
  instructionHtml: string
  inputType: GuidedStepInputType
  /** Parsed input config — structure depends on inputType */
  inputConfig: Record<string, unknown>
  hintHtml: string | null
  completed: boolean
}

export interface GuidedStepCheckResponse {
  passed: boolean
  feedback: string
  reflectionPrompt: string | null
  nextStepId: string | null
}

// ── Phase 4 — Solo assessment ─────────────────────────────────────────────

export type SoloAssessmentType =
  | 'DETERMINISTIC' | 'RUBRIC_REFLECTION' | 'PATTERN_MATCH' | 'AI_REVIEW'

export type KeywordBand = 'WEAK' | 'GOOD' | 'EXCELLENT'

/** Response from POST /api/encoding/{lessonId}/solo-practice/submit (all types) */
export interface SoloAssessmentResult {
  passed: boolean
  /** Keyword scoring band — only set for PATTERN_MATCH */
  band: KeywordBand | null
  /** Feedback / mentor text */
  feedback: string | null
  /** Model-answer HTML revealed after submission — null for DETERMINISTIC */
  modelAnswerHtml: string | null
  /** Keywords matched in the answer — only for PATTERN_MATCH */
  matchedKeywords: string[] | null
  errorType: string | null
  xpEarned: number
  newBadges: Badge[]
  /** Remaining AI-review uses for the month — only meaningful for AI_REVIEW */
  aiReviewsRemaining: number
  usedAi: boolean
  /** Test-case breakdown — only for DETERMINISTIC code paths */
  testResults: TestResult[]
}

export interface Downloadable {
  title: string
  type: 'pdf' | 'md' | 'txt' | string
  url: string
}

export interface PracticeResult {
  allPassed: boolean; testResults: TestResult[]
  xpEarned: number; mentorFeedback: string | null
  errorType?: string | null; newBadges?: Badge[]
}

// â”€â”€ Questions â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface QuestionDto {
  id: string; tier: string; type: string
  questionHtml: string; codeSnippet: string | null
  options: string[] | null
}

export interface AnswerEntry { questionId: string; answer: string }

export interface QuestionResultDto {
  questionId: string; correct: boolean
  userAnswer: string; correctAnswer: string; explanationHtml: string
}

// â”€â”€ Retrieval â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface RetrievalResultDto {
  score: number; correct: number; total: number
  results: QuestionResultDto[]; passed: boolean
  xpEarned: number; newBadges: Badge[]
  recommendation: string
}

// â”€â”€ Reviews â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface ReviewSessionDto {
  sessionId: string; sessionType: string
  questions: QuestionDto[]; estimatedMinutes: number
}

export interface ReviewResultDto {
  score: number; correct: number; total: number
  results: QuestionResultDto[]; newBadges: Badge[]
}

// â”€â”€ Diagnostic â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface DiagnosticResultDto {
  recommendedPath: string
  chunkRecommendations: Record<string, string>
  overallScore: number
}

// â”€â”€ Feynman â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface FeynmanResultDto {
  accuracy: number; completeness: number; simplicity: number; connection: number
  overallScore: number; feedback: string; xpEarned: number
}

// â”€â”€ Dashboard â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface ModuleHealthDto {
  moduleId: string; title: string; glyph: string
  status: string; memoryStrength: number; healthColor: string
  totalLessons: number; completedLessons: number
  tier: string
}

export interface DashboardDto {
  totalXp: number; rank: string; streakDays: number; streakAtRisk: boolean
  currentPath: string; diagnosticCompleted: boolean; diagnosticCompletedAt: string | null
  reviewsDue: number; dailyGoalMinutes: number
  overallProgress: number; chunkHealth: ModuleHealthDto[]
}

// â”€â”€ Rabbit Holes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface RabbitHoleModule {
  id: string; moduleId: string; title: string
  contentHtml: string; storyBeats: StoryBeat[]
  starterCode: string; testCaseLabels: { label: string }[]; filename: string; sortOrder: number
}

// â”€â”€ Curiosity Queue â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface CuriosityQueueItem {
  id: string; userId: string; lessonId: string; savedAt: string
}

// â”€â”€ Rabbit Hole Terms â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface RabbitHoleTerm {
  id: string; term: string; description: string | null
  lessonId: string | null; domainId: string | null; savedAt: string
}

// ── Phase 7 — Knowledge Graph ─────────────────────────────────────────────────

/** A single node in the curriculum knowledge graph. */
export interface GraphNode {
  /** Unique identifier matching the entity id (domain slug, module id, lesson id). */
  id: string
  /** {@code DOMAIN | MODULE | LESSON} */
  type: 'DOMAIN' | 'MODULE' | 'LESSON'
  /** Display label. */
  label: string
  /** Emoji glyph — may be undefined for lessons. */
  glyph?: string
  /** Domain this node belongs to. */
  domainId?: string
  /** {@code APPRENTICE | JUNIOR | SENIOR | LEAD} — undefined for DOMAIN nodes. */
  tier?: string
  /** Progress status — undefined for DOMAIN nodes. */
  status?: string
  /** FSRS decayed memory strength in [0, 1]. */
  memoryStrength: number
}

/** A directed edge in the curriculum knowledge graph. */
export interface GraphEdge {
  id: string
  source: string
  target: string
  /** {@code HIERARCHY | PREREQUISITE | INTEGRATION} */
  edgeType: 'HIERARCHY' | 'PREREQUISITE' | 'INTEGRATION'
}

/** Full graph payload returned by {@code GET /api/graph}. */
export interface GraphResponse {
  nodes: GraphNode[]
  edges: GraphEdge[]
}
