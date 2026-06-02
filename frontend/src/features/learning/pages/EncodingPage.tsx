import { useEffect, useCallback, useReducer, useState, useRef } from 'react'
import { usePreferences } from '@/hooks/usePreferences'
import { createPortal } from 'react-dom'
import { useParams, useNavigate } from 'react-router-dom'
import { encodingApi, codeApi, tailwindApi, reactApi, sqlApi, rApi, notesApi, capstoneApi } from '@/shared/api/services'
import type { UserNote } from '@/shared/api/services'
import GuidedStepper from '@/features/learning/components/GuidedStepper'
import SoloAssessmentPanel from '@/features/learning/components/SoloAssessmentPanel'

/** Chunk IDs that are capstone lessons — show the project save form on COMPLETE. */
const CAPSTONE_CHUNK_IDS = new Set([
  // Java capstones (one per tier)
  'java-app-15', 'java-jun-20', 'java-sen-19', 'java-lea-17',
  // Tailwind capstones
  'tw-app-15',   'tw-jun-20',   'tw-sen-19',   'tw-lea-17',
  // React capstones
  'rx-app-15',   'rx-jun-20',   'rx-sen-19',   'rx-lea-17',
])
import { useAuth } from '@/shared/hooks/useAuth'
import type { LessonEncoding, PracticeResult, SoloAssessmentResult, RetrievalResultDto, FeynmanResultDto, AnswerEntry, Badge, CodeRunResponse } from '@/shared/types'
import StuckButton from '@/components/StuckButton'
import StoryPanel from '@/features/learning/components/StoryPanel'
import RabbitHoleHtml from '@/features/learning/components/RabbitHoleHtml'
import QuestionCard from '@/features/learning/components/QuestionCard'
import CodeEditor from '@/features/learning/components/CodeEditor'
import TailwindEditor from '@/features/learning/components/TailwindEditor'
import ReactEditor, { type ReactEditorHandle, type ReactTestSpec } from '@/features/learning/components/ReactEditor'
import SqlEditor, { type SqlEditorHandle, type SqlTestSpec } from '@/features/learning/components/SqlEditor'
import REditor, { type REditorHandle, type RTestSpec } from '@/features/learning/components/REditor'
import OutputPanel from '@/features/learning/components/OutputPanel'
import TestChips from '@/features/learning/components/TestChips'
import AiMentorPanel from '@/features/learning/components/AiMentorPanel'
import LevelUpModal from '@/shared/components/layout/LevelUpModal'
import BadgeToast from '@/shared/components/layout/BadgeToast'
import { cn } from '@/lib/utils'
import {
  ArrowLeft, ClipboardList, BookOpen,
  Play, Loader2, Zap, Check, Eye, EyeOff, FlaskConical,
  PenLine, Target, Download, FileText, StickyNote, X, CheckCircle2,
  Code2, Lightbulb, Sparkles,
} from 'lucide-react'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }

/** For SQL practice: pull the seed SQL out of the first test spec's `setup` field. */
function extractSqlSetup(specs: unknown): string | null {
  if (!Array.isArray(specs) || specs.length === 0) return null
  const first = specs[0] as { setup?: unknown }
  return typeof first?.setup === 'string' ? first.setup : null
}

/** For R practice: pull the seed R code out of the first test spec's `setup` field. */
function extractRSetup(specs: unknown): string | null {
  if (!Array.isArray(specs) || specs.length === 0) return null
  const first = specs[0] as { setup?: unknown }
  return typeof first?.setup === 'string' ? first.setup : null
}

// ── State machine ──────────────────────────────────────────────────────────────

type EncodingState = {
  encoding: LessonEncoding | null
  loading: boolean
  practiceView: 'brief' | 'code'
  showTaskOverlay: boolean
  code: string
  output: OutputLine[]
  testResults: Map<string, boolean>
  running: boolean
  mentorFeedback: string | null
  mentorLoading: boolean
  mentorErrorType: string | null
  practiceSolved: boolean
  answers: Record<string, string>
  retrievalResult: RetrievalResultDto | null
  submittingRetrieval: boolean
  feynmanText: string
  feynmanResult: FeynmanResultDto | null
  submittingFeynman: boolean
  showHint: boolean
  storyOpen: boolean
}

type EncodingAction =
  | { type: 'LOADED'; encoding: LessonEncoding; code: string }
  | { type: 'PHASE_ADVANCED'; encoding: LessonEncoding }
  | { type: 'PRACTICE_VIEW'; view: 'brief' | 'code' }
  | { type: 'TASK_OVERLAY'; open: boolean }
  | { type: 'CODE_CHANGED'; code: string }
  | { type: 'RUN_START'; message: string }
  | { type: 'RUN_DONE'; output: OutputLine[] }
  | { type: 'SUBMIT_START'; message: string }
  | { type: 'COMPILE_ERROR'; output: OutputLine[]; errorType: string }
  | { type: 'MENTOR_LOADING' }
  | { type: 'MENTOR_READY'; feedback: string }
  | { type: 'SUBMIT_RESULT'; output: OutputLine[]; testResults: Map<string, boolean> }
  | { type: 'PRACTICE_SOLVED' }
  | { type: 'RUN_END' }
  | { type: 'ANSWER_CHANGED'; questionId: string; answer: string }
  | { type: 'RETRIEVAL_START' }
  | { type: 'RETRIEVAL_DONE'; result: RetrievalResultDto | null }
  | { type: 'FEYNMAN_TEXT'; text: string }
  | { type: 'FEYNMAN_START' }
  | { type: 'FEYNMAN_DONE'; result: FeynmanResultDto | null }
  | { type: 'HINT_TOGGLE' }
  | { type: 'STORY_OPEN'; open: boolean }

const INITIAL_OUTPUT: OutputLine[] = [{ text: '// Cast your spell to run the code.', type: 'system' }]

const initialState: EncodingState = {
  encoding: null,
  loading: true,
  practiceView: 'brief',
  showTaskOverlay: false,
  code: '',
  output: INITIAL_OUTPUT,
  testResults: new Map(),
  running: false,
  mentorFeedback: null,
  mentorLoading: false,
  mentorErrorType: null,
  practiceSolved: false,
  answers: {},
  retrievalResult: null,
  submittingRetrieval: false,
  feynmanText: '',
  feynmanResult: null,
  submittingFeynman: false,
  showHint: false,
  storyOpen: false,
}

function encodingReducer(state: EncodingState, action: EncodingAction): EncodingState {
  switch (action.type) {
    case 'LOADED':
      return { ...state, loading: false, encoding: action.encoding, code: action.code }
    case 'PHASE_ADVANCED':
      return {
        ...state,
        encoding: action.encoding,
        code: action.encoding.starterCode ?? '',
        answers: {},
        retrievalResult: null,
        feynmanResult: null,
        feynmanText: '',
        practiceSolved: false,
        practiceView: 'brief',
        showTaskOverlay: false,
        output: INITIAL_OUTPUT,
        testResults: new Map(),
        mentorFeedback: null,
        showHint: false,
      }
    case 'PRACTICE_VIEW':   return { ...state, practiceView: action.view }
    case 'TASK_OVERLAY':    return { ...state, showTaskOverlay: action.open }
    case 'CODE_CHANGED':    return { ...state, code: action.code }
    case 'RUN_START':
      return { ...state, running: true, mentorFeedback: null, output: [{ text: action.message, type: 'system' }] }
    case 'RUN_DONE':        return { ...state, running: false, output: action.output }
    case 'SUBMIT_START':
      return { ...state, running: true, mentorFeedback: null, output: [{ text: action.message, type: 'system' }], testResults: new Map() }
    case 'COMPILE_ERROR':
      return { ...state, running: false, output: action.output, mentorErrorType: action.errorType }
    case 'MENTOR_LOADING':  return { ...state, mentorLoading: true }
    case 'MENTOR_READY':    return { ...state, mentorLoading: false, mentorFeedback: action.feedback }
    case 'SUBMIT_RESULT':   return { ...state, output: action.output, testResults: action.testResults }
    case 'PRACTICE_SOLVED': return { ...state, running: false, practiceSolved: true, showTaskOverlay: false }
    case 'RUN_END':         return { ...state, running: false }
    case 'ANSWER_CHANGED':
      return { ...state, answers: { ...state.answers, [action.questionId]: action.answer } }
    case 'RETRIEVAL_START': return { ...state, submittingRetrieval: true }
    case 'RETRIEVAL_DONE':  return { ...state, submittingRetrieval: false, retrievalResult: action.result }
    case 'FEYNMAN_TEXT':    return { ...state, feynmanText: action.text }
    case 'FEYNMAN_START':   return { ...state, submittingFeynman: true }
    case 'FEYNMAN_DONE':    return { ...state, submittingFeynman: false, feynmanResult: action.result }
    case 'HINT_TOGGLE':     return { ...state, showHint: !state.showHint }
    case 'STORY_OPEN':      return { ...state, storyOpen: action.open }
    default:                return state
  }
}

// ── Component ──────────────────────────────────────────────────────────────────

export default function EncodingPage() {
  const { lessonId } = useParams<{ lessonId: string }>()
  const navigate = useNavigate()
  const { user, updateXp } = useAuth()

  const { loreEnabled } = usePreferences()
  const [state, dispatch] = useReducer(encodingReducer, initialState)
  const {
    encoding, loading, practiceView, showTaskOverlay, code, output, testResults,
    running, mentorFeedback, mentorLoading, mentorErrorType, practiceSolved,
    answers, retrievalResult, submittingRetrieval, feynmanText, feynmanResult,
    submittingFeynman, showHint, storyOpen,
  } = state

  // Notification UI stays as useState — independent of the phase state machine
  const [toast, setToast] = useState<string | null>(null)
  const [levelUpInfo, setLevelUpInfo] = useState<{ level: number; rank: string } | null>(null)
  const [newBadges, setNewBadges] = useState<Badge[]>([])

  // Notes panel
  const [notePanelOpen, setNotePanelOpen] = useState(false)
  const [noteContent, setNoteContent] = useState('')
  const [noteSaved, setNoteSaved] = useState<UserNote | null>(null)
  const [noteSaving, setNoteSaving] = useState(false)
  const noteSaveTimer = useRef<ReturnType<typeof setTimeout>>()

  // Capstone save form (shown in COMPLETE phase for capstone sub-chunks)
  const [capstoneTitle, setCapstoneTitle] = useState('')
  const [capstoneDesc, setCapstoneDesc] = useState('')
  const [capstoneCode, setCapstoneCode] = useState('')
  const [capstoneGithub, setCapstoneGithub] = useState('')
  const [capstoneSaving, setCapstoneSaving] = useState(false)
  const [capstoneSavedId, setCapstoneSavedId] = useState<string | null>(null)

  const toastTimer = useRef<ReturnType<typeof setTimeout>>()
  const reactEditorRef = useRef<ReactEditorHandle>(null)
  const sqlEditorRef = useRef<SqlEditorHandle>(null)
  const rEditorRef = useRef<REditorHandle>(null)

  // Adapter wrappers so JSX that calls these directly doesn't need to change
  const setCode = (c: string) => dispatch({ type: 'CODE_CHANGED', code: c })
  const setPracticeView = (view: 'brief' | 'code') => dispatch({ type: 'PRACTICE_VIEW', view })
  const setShowTaskOverlay = (open: boolean) => dispatch({ type: 'TASK_OVERLAY', open })
  const setStoryOpen = (open: boolean) => dispatch({ type: 'STORY_OPEN', open })
  const setFeynmanText = (text: string) => dispatch({ type: 'FEYNMAN_TEXT', text })

  useEffect(() => {
    if (!lessonId) return
    const load = async () => {
      try {
        let enc = await encodingApi.start(lessonId)
        if (enc.phase === 'HOOK' && !enc.hookHtml?.trim()) {
          enc = await encodingApi.advance(lessonId)
        }
        dispatch({
          type: 'LOADED',
          encoding: enc,
          code: enc.starterCode ?? '',
        })
      } catch {
        navigate('/')
      }
    }
    load()
  }, [lessonId, navigate])

  const showToast = useCallback((msg: string) => {
    setToast(msg)
    clearTimeout(toastTimer.current)
    toastTimer.current = setTimeout(() => setToast(null), 2600)
  }, [])


  function handleXpEarned(xpEarned: number, earnedBadges?: Badge[]) {
    if (xpEarned <= 0) return
    const prevRank = calculateRank(user?.totalXp ?? 0)
    const newRank = calculateRank((user?.totalXp ?? 0) + xpEarned)
    updateXp(xpEarned, newRank)
    showToast(`✦ +${xpEarned} XP`)
    if (newRank !== prevRank) {
      const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
      setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
    }
    if (earnedBadges?.length) setNewBadges(earnedBadges)
  }

  async function handleAdvance() {
    if (!lessonId) return
    // Auto-save any pending note when leaving a notes-eligible phase
    if (noteContent.trim() && notePhaseVisible) {
      clearTimeout(noteSaveTimer.current)
      saveNoteNow() // fire-and-forget — don't block phase advance
    }
    const enc = await encodingApi.advance(lessonId)
    dispatch({ type: 'PHASE_ADVANCED', encoding: enc })
  }

  async function handleRun() {
    if (running) return
    dispatch({ type: 'RUN_START', message: '// Running...' })
    try {
      const result: CodeRunResponse = await codeApi.run(code)
      const lines: OutputLine[] = []
      if (result.status === 'SUCCESS' && result.output)
        result.output.split('\n').forEach(l => lines.push({ text: l, type: 'normal' }))
      else if (result.error)
        result.error.split('\n').forEach(l => lines.push({ text: l, type: 'error' }))
      else lines.push({ text: '// No output produced.', type: 'system' })
      dispatch({ type: 'RUN_DONE', output: lines })
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: 'Error connecting to server.', type: 'error' }] })
    }
  }

  async function handleSubmitPractice() {
    if (!lessonId || running) return
    const written = encoding?.practiceType === 'NONE'
    dispatch({ type: 'SUBMIT_START', message: written ? '// Checking your written response...' : '// Running all test cases...' })
    try {
      const result: PracticeResult = await encodingApi.submitPractice(lessonId, code)
      if (result.errorType === 'COMPILE_ERROR' || result.errorType === 'RUNTIME_ERROR') {
        dispatch({ type: 'COMPILE_ERROR', output: [{ text: `✗ ${result.errorType === 'COMPILE_ERROR' ? 'Spell failed to compile' : 'Spell crashed at runtime'}.`, type: 'error' }], errorType: result.errorType })
        if (result.mentorFeedback) { dispatch({ type: 'MENTOR_LOADING' }); setTimeout(() => dispatch({ type: 'MENTOR_READY', feedback: result.mentorFeedback! }), 300) }
        return
      }
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        if (written) { lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.actualOutput}`, type: t.passed ? 'success' : 'error' }); return }
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `got "${t.actualOutput}", expected "${t.expectedOutput}"`}`, type: t.passed ? 'success' : 'error' })
      })
      dispatch({ type: 'SUBMIT_RESULT', output: lines, testResults: newResults })
      if (result.allPassed) {
        lines.push({ text: '✓ All test cases passed!', type: 'success' })
        dispatch({ type: 'PRACTICE_SOLVED' })
        handleXpEarned(result.xpEarned, result.newBadges)
      } else {
        lines.push({ text: '✗ Some test cases failed.', type: 'error' })
        dispatch({ type: 'RUN_END' })
        if (result.mentorFeedback) { dispatch({ type: 'MENTOR_LOADING' }); setTimeout(() => dispatch({ type: 'MENTOR_READY', feedback: result.mentorFeedback! }), 400) }
      }
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: written ? 'Error submitting response.' : 'Error submitting code.', type: 'error' }] })
    }
  }

  async function handleSubmitTailwind() {
    if (!lessonId || running) return
    dispatch({ type: 'SUBMIT_START', message: '// Checking your Tailwind classes…' })
    try {
      const result: PracticeResult = await tailwindApi.submit(lessonId, code)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `missing class — ${t.actualOutput}`}`, type: t.passed ? 'success' : 'error' })
      })
      dispatch({ type: 'SUBMIT_RESULT', output: lines, testResults: newResults })
      if (result.allPassed) {
        lines.push({ text: '✓ All checks passed!', type: 'success' })
        dispatch({ type: 'PRACTICE_SOLVED' })
        handleXpEarned(result.xpEarned, result.newBadges)
      } else {
        lines.push({ text: '✗ Some checks failed — adjust your classes and try again.', type: 'error' })
        dispatch({ type: 'RUN_END' })
      }
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: 'Error submitting — check your connection.', type: 'error' }] })
    }
  }

  async function handleSubmitReact(solo: boolean) {
    if (!lessonId || !encoding || running) return
    dispatch({ type: 'SUBMIT_START', message: '// Rendering and running tests in the sandbox…' })
    try {
      const specs: ReactTestSpec[] = Array.isArray(encoding.testCaseLabels) ? (encoding.testCaseLabels as ReactTestSpec[]) : []
      const clientResults = (await reactEditorRef.current?.runTests(specs)) ?? []
      const submitFn = solo ? reactApi.submitSoloPractice : reactApi.submit
      const result: PracticeResult = await submitFn(lessonId, code, clientResults)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}${t.passed ? '' : ` — ${t.actualOutput}`}`, type: t.passed ? 'success' : 'error' })
      })
      dispatch({ type: 'SUBMIT_RESULT', output: lines, testResults: newResults })
      if (result.allPassed) {
        lines.push({ text: '✓ All tests passed!', type: 'success' })
        dispatch({ type: 'PRACTICE_SOLVED' })
        handleXpEarned(result.xpEarned, result.newBadges)
      } else {
        lines.push({ text: '✗ Some tests failed — adjust your code and try again.', type: 'error' })
        dispatch({ type: 'RUN_END' })
      }
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: 'Error submitting — check your connection.', type: 'error' }] })
    }
  }

  async function handleSubmitSql(solo: boolean) {
    if (!lessonId || !encoding || running) return
    dispatch({ type: 'SUBMIT_START', message: '// Running query and tests in the SQLite sandbox…' })
    try {
      const specs: SqlTestSpec[] = Array.isArray(encoding.testCaseLabels) ? (encoding.testCaseLabels as SqlTestSpec[]) : []
      const clientResults = (await sqlEditorRef.current?.runTests(specs)) ?? []
      const submitFn = solo ? sqlApi.submitSoloPractice : sqlApi.submit
      const result: PracticeResult = await submitFn(lessonId, code, clientResults)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}${t.passed ? '' : ` — ${t.actualOutput}`}`, type: t.passed ? 'success' : 'error' })
      })
      dispatch({ type: 'SUBMIT_RESULT', output: lines, testResults: newResults })
      if (result.allPassed) {
        lines.push({ text: '✓ All tests passed!', type: 'success' })
        dispatch({ type: 'PRACTICE_SOLVED' })
        handleXpEarned(result.xpEarned, result.newBadges)
      } else {
        lines.push({ text: '✗ Some tests failed — adjust your code and try again.', type: 'error' })
        dispatch({ type: 'RUN_END' })
      }
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: 'Error submitting — check your connection.', type: 'error' }] })
    }
  }

  async function handleSubmitR(solo: boolean) {
    if (!lessonId || !encoding || running) return
    dispatch({ type: 'SUBMIT_START', message: '// Running R code and tests in the WebR sandbox…' })
    try {
      const specs: RTestSpec[] = Array.isArray(encoding.testCaseLabels) ? (encoding.testCaseLabels as RTestSpec[]) : []
      const clientResults = (await rEditorRef.current?.runTests(specs)) ?? []
      const submitFn = solo ? rApi.submitSoloPractice : rApi.submit
      const result: PracticeResult = await submitFn(lessonId, code, clientResults)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}${t.passed ? '' : ` — ${t.actualOutput}`}`, type: t.passed ? 'success' : 'error' })
      })
      dispatch({ type: 'SUBMIT_RESULT', output: lines, testResults: newResults })
      if (result.allPassed) {
        lines.push({ text: '✓ All tests passed!', type: 'success' })
        dispatch({ type: 'PRACTICE_SOLVED' })
        handleXpEarned(result.xpEarned, result.newBadges)
      } else {
        lines.push({ text: '✗ Some tests failed — adjust your code and try again.', type: 'error' })
        dispatch({ type: 'RUN_END' })
      }
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: 'Error submitting — check your connection.', type: 'error' }] })
    }
  }

  async function handleSubmitSoloPractice() {
    if (!lessonId || running) return
    const written = encoding?.practiceType === 'NONE'
    dispatch({ type: 'SUBMIT_START', message: written ? '// Checking your independent response...' : '// Running all test cases...' })
    try {
      const result: SoloAssessmentResult = await encodingApi.submitSoloPractice(lessonId, code)
      if (result.errorType === 'COMPILE_ERROR' || result.errorType === 'RUNTIME_ERROR') {
        dispatch({ type: 'COMPILE_ERROR', output: [{ text: `✗ ${result.errorType === 'COMPILE_ERROR' ? 'Spell failed to compile' : 'Spell crashed at runtime'}.`, type: 'error' }], errorType: result.errorType })
        if (result.feedback) { dispatch({ type: 'MENTOR_LOADING' }); setTimeout(() => dispatch({ type: 'MENTOR_READY', feedback: result.feedback! }), 300) }
        return
      }
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults?.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `got "${t.actualOutput}", expected "${t.expectedOutput}"`}`, type: t.passed ? 'success' : 'error' })
      })
      dispatch({ type: 'SUBMIT_RESULT', output: lines, testResults: newResults })
      if (result.passed) {
        lines.push({ text: '✓ All test cases passed! You built it from scratch!', type: 'success' })
        dispatch({ type: 'PRACTICE_SOLVED' })
        handleXpEarned(result.xpEarned, result.newBadges)
      } else {
        lines.push({ text: '✗ Some test cases failed — keep going!', type: 'error' })
        dispatch({ type: 'RUN_END' })
        if (result.feedback) { dispatch({ type: 'MENTOR_LOADING' }); setTimeout(() => dispatch({ type: 'MENTOR_READY', feedback: result.feedback! }), 400) }
      }
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: 'Error submitting code.', type: 'error' }] })
    }
  }

  async function handleSubmitTailwindSolo() {
    if (!lessonId || running) return
    dispatch({ type: 'SUBMIT_START', message: '// Checking your Tailwind classes…' })
    try {
      const result: PracticeResult = await tailwindApi.submitSoloPractice(lessonId, code)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `missing class — ${t.actualOutput}`}`, type: t.passed ? 'success' : 'error' })
      })
      dispatch({ type: 'SUBMIT_RESULT', output: lines, testResults: newResults })
      if (result.allPassed) {
        lines.push({ text: '✓ All checks passed! Well done!', type: 'success' })
        dispatch({ type: 'PRACTICE_SOLVED' })
      } else {
        lines.push({ text: '✗ Some checks failed — adjust your classes and try again.', type: 'error' })
        dispatch({ type: 'RUN_END' })
      }
    } catch {
      dispatch({ type: 'RUN_DONE', output: [{ text: 'Error submitting — check your connection.', type: 'error' }] })
    }
  }

  async function handleSubmitRetrieval() {
    if (!lessonId) return
    dispatch({ type: 'RETRIEVAL_START' })
    try {
      const answerList: AnswerEntry[] = Object.entries(answers).map(([questionId, answer]) => ({ questionId, answer }))
      const result = await encodingApi.submitRetrieval(lessonId, answerList)
      dispatch({ type: 'RETRIEVAL_DONE', result })
      if (result.xpEarned > 0) {
        const prevRank = calculateRank(user?.totalXp ?? 0)
        const newRank = calculateRank((user?.totalXp ?? 0) + result.xpEarned)
        updateXp(result.xpEarned, newRank)
        showToast(`✦ +${result.xpEarned} XP — ${result.passed ? 'Passed!' : 'Keep practicing'}`)
        if (newRank !== prevRank) {
          const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
          setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
        }
      }
      if (result.newBadges?.length) setNewBadges(result.newBadges)
    } catch {
      showToast('Error submitting answers')
      dispatch({ type: 'RETRIEVAL_DONE', result: null })
    }
  }

  async function handleSubmitFeynman() {
    if (!lessonId || !feynmanText.trim()) return
    dispatch({ type: 'FEYNMAN_START' })
    try {
      const result = await encodingApi.submitFeynman(lessonId, feynmanText)
      dispatch({ type: 'FEYNMAN_DONE', result })
      if (result.xpEarned > 0) { updateXp(result.xpEarned); showToast(`✦ +${result.xpEarned} XP — Feynman complete`) }
    } catch {
      showToast('Error submitting explanation')
      dispatch({ type: 'FEYNMAN_DONE', result: null })
    }
  }

  const noteTitle = encoding
    ? `${encoding.moduleId ?? 'Lesson'} — ${encoding.title}`
    : 'Note'

  const notePhaseVisible = encoding
    ? ['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'SOLO_PRACTICE'].includes(encoding.phase)
    : false

  // Lock body scroll whenever any full-screen overlay is open (prevents background scroll on iOS)
  useEffect(() => {
    const anyOverlayOpen = storyOpen || showTaskOverlay
    document.body.style.overflow = anyOverlayOpen ? 'hidden' : ''
    return () => { document.body.style.overflow = '' }
  }, [storyOpen, showTaskOverlay])

  async function saveNoteNow() {
    if (!encoding || !lessonId || !noteContent.trim()) return
    setNoteSaving(true)
    try {
      const saved = await notesApi.save({
        lessonId,
        moduleId: encoding.moduleId ?? lessonId,
        title: noteTitle,
        content: noteContent,
      })
      setNoteSaved(saved)
    } catch { /* ignore */ } finally {
      setNoteSaving(false)
    }
  }

  function scheduleAutoSave() {
    clearTimeout(noteSaveTimer.current)
    noteSaveTimer.current = setTimeout(saveNoteNow, 2000)
  }

  const isCapstoneLesson = encoding ? CAPSTONE_CHUNK_IDS.has(encoding.moduleId) : false

  async function saveCapstone() {
    if (!encoding || !capstoneTitle.trim()) return
    setCapstoneSaving(true)
    try {
      const saved = await capstoneApi.create({
        moduleId: encoding.moduleId,
        title: capstoneTitle,
        description: capstoneDesc || undefined,
        codeContent: capstoneCode || undefined,
        githubUrl: capstoneGithub || undefined,
      })
      setCapstoneSavedId(saved.id)
    } catch { /* ignore */ } finally {
      setCapstoneSaving(false)
    }
  }

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <div className="w-10 h-10 mb-3 animate-spin rounded-full border-2 border-border border-t-purple" />
        <p>Opening the Grimoire...</p>
      </div>
    )
  }
  if (!encoding) return null

  const phase = encoding.phase

  // Shared prose HTML classes
  const proseHtml = `text-[14px] leading-[1.75] text-text
    [&_p]:mb-2.5 [&_p:last-child]:mb-0
    [&_code]:bg-surface [&_code]:px-1 [&_code]:py-px [&_code]:rounded [&_code]:text-[12px] [&_code]:text-purple-light [&_code]:border [&_code]:border-border
    [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[8px] [&_pre]:overflow-x-auto [&_pre]:my-2.5 [&_pre]:mb-3.5
    [&_pre_code]:bg-transparent [&_pre_code]:border-none [&_pre_code]:p-0 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.7] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:block [&_pre_code]:px-4 [&_pre_code]:py-4 [&_pre_code]:font-mono
    [&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic
    [&_table]:w-full [&_table]:border-collapse [&_table]:my-4 [&_table]:text-[13px]
    [&_thead]:border-b-2 [&_thead]:border-[rgba(139,92,246,0.35)]
    [&_th]:text-left [&_th]:px-3 [&_th]:py-2 [&_th]:font-cinzel [&_th]:text-[11px] [&_th]:tracking-wide [&_th]:text-gold [&_th]:font-semibold
    [&_td]:px-3 [&_td]:py-2 [&_td]:border-b [&_td]:border-border [&_td]:align-top [&_td]:text-text
    [&_tr:last-child_td]:border-b-0
    [&_tbody_tr:hover]:bg-[rgba(139,92,246,0.04)]`

  return (
    <div className="flex flex-col flex-1 overflow-hidden min-h-0">
      {/* Header */}
      <div className="flex items-center gap-3 px-4 py-2.5 border-b border-border bg-card flex-shrink-0 max-[480px]:px-2.5 max-[480px]:py-2 max-[480px]:gap-2">
        <button className="btn btn-ghost text-[12px] flex items-center gap-1" onClick={() => navigate(`/module/${encoding.moduleId}`)}><ArrowLeft size={13} strokeWidth={1.75} /> Back</button>
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2">
            <div className="text-[16px] font-bold text-text truncate max-[480px]:text-[13px]">{encoding.title}</div>
            {encoding.questType && (
              <span className="text-[10px] px-1.5 py-0.5 rounded-md bg-gold/10 text-gold border border-gold/20 whitespace-nowrap flex-shrink-0 font-medium">
                {questTypeLabel(encoding.questType)}
              </span>
            )}
          </div>
          <div className="flex gap-1.5 mt-1 overflow-x-auto flex-nowrap scrollbar-none max-[480px]:gap-1">
            {(['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'SOLO_PRACTICE', 'RETRIEVAL_CHECK', 'INTEGRATION', 'COMPLETE'] as const).map(p => (
              <span key={p} className={cn(
                'text-[10px] px-2 py-0.5 rounded-[10px] bg-surface text-muted whitespace-nowrap flex-shrink-0',
                p === phase && 'bg-purple text-white',
                (phase === 'COMPLETE' || phaseOrder(p) < phaseOrder(phase)) && p !== phase && 'bg-teal-dim text-teal',
              )}>
                {phaseLabel(p)}
              </span>
            ))}
          </div>
        </div>
      </div>

      {/* HOOK */}
      {phase === 'HOOK' && (
        <div className="flex-1 flex flex-col items-center justify-center px-6 py-10 text-center overflow-y-auto animate-[fade-up_0.5s_cubic-bezier(0.22,1,0.36,1)_both] max-[480px]:px-3 max-[480px]:py-5">
          <div
            className="relative max-w-[640px] w-full px-14 py-12 rounded-[20px] overflow-hidden
              max-[480px]:px-[18px] max-[480px]:py-7 max-[480px]:rounded-[12px]"
            style={{
              background: 'radial-gradient(ellipse at 50% 0%, rgba(139,92,246,.18) 0%, transparent 65%), var(--card)',
              border: '1px solid rgba(139,92,246,.3)',
              boxShadow: '0 0 80px rgba(139,92,246,.14), 0 16px 48px rgba(0,0,0,.45)',
            }}
          >
            {/* Top & bottom gradient lines */}
            <div className="absolute top-0 left-0 right-0 h-0.5" style={{ background: 'linear-gradient(90deg, transparent 0%, var(--purple) 25%, var(--teal) 75%, transparent 100%)' }} />
            <div className="absolute bottom-0 left-0 right-0 h-0.5" style={{ background: 'linear-gradient(90deg, transparent 0%, var(--teal) 25%, var(--purple) 75%, transparent 100%)' }} />
            {/* Chapter eyebrow */}
            <div className="text-[10px] font-semibold tracking-[0.22em] uppercase text-muted mb-3 max-[480px]:text-[9px]">
              ✦ Arcane Academy
            </div>
            {/* Lesson title — the hero element */}
            <h1
              className="font-cinzel text-[22px] font-bold leading-[1.35] mb-2 max-[480px]:text-[17px]"
              style={{ color: '#c9a227', textShadow: '0 0 28px rgba(201,162,39,.35)' }}
            >
              {encoding.title}
            </h1>
            {/* Gradient rule */}
            <div className="w-24 h-px mx-auto mb-7 max-[480px]:mb-5" style={{ background: 'linear-gradient(90deg, transparent, var(--purple) 30%, var(--teal) 70%, transparent)' }} />
            {/* Hook prose */}
            <div
              className="text-[18px] leading-[1.85] text-text max-[480px]:text-[15px] max-[480px]:leading-[1.75]
                [&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0
                [&_strong]:text-gold [&_strong]:font-bold
                [&_em]:text-purple-light [&_em]:italic"
              dangerouslySetInnerHTML={{ __html: encoding.hookHtml ?? '' }}
            />
          </div>
          <button className="btn btn-primary mt-9 px-8 py-2.5 text-[14px]" onClick={handleAdvance}>Begin →</button>
        </div>
      )}

      {/* EXPLANATION */}
      {phase === 'EXPLANATION' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          {/* Downloadable resources — shown at the very top of EXPLANATION if present */}
          {encoding.downloadables && encoding.downloadables.length > 0 && (
            <div className="mb-5 p-3 rounded-[10px] border border-[rgba(255,193,7,0.2)] bg-[rgba(255,193,7,0.04)]">
              <div className="text-[11px] font-bold text-gold uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
                <Download size={11} strokeWidth={2.5} /> Resources
              </div>
              <div className="flex flex-wrap gap-2">
                {encoding.downloadables.map((dl, i) => (
                  <a
                    key={i}
                    href={dl.url}
                    target="_blank"
                    rel="noopener noreferrer"
                    download
                    className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[12px] font-medium
                      bg-[rgba(255,193,7,0.1)] border border-[rgba(255,193,7,0.25)] text-gold
                      hover:bg-[rgba(255,193,7,0.18)] hover:border-[rgba(255,193,7,0.45)] transition-colors no-underline"
                  >
                    <FileText size={11} strokeWidth={2} />
                    {dl.title}
                    <span className="text-[10px] uppercase opacity-60 font-bold">{dl.type}</span>
                  </a>
                ))}
              </div>
            </div>
          )}

          {/* Learning objectives — shown before story if present */}
          {encoding.learningObjectives && encoding.learningObjectives.length > 0 && (
            <div className="mb-6 p-4 rounded-[10px] border border-[rgba(45,212,191,0.25)] bg-[rgba(45,212,191,0.05)]">
              <div className="text-[12px] font-bold text-teal uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
                <Target size={12} strokeWidth={2} /> Learning Objectives
              </div>
              <ul className="m-0 pl-4 space-y-1.5">
                {encoding.learningObjectives.map((obj, i) => (
                  <li key={i} className="text-[13px] text-text leading-[1.6] marker:text-teal">{obj}</li>
                ))}
              </ul>
            </div>
          )}
          {/* Lore Introduction — only when lore is enabled */}
          {loreEnabled && encoding.loreIntroHtml && (
            <div className="mb-6 px-5 py-4 rounded-[12px] border-l-[3px] border-[rgba(139,92,246,0.5)] border border-[rgba(139,92,246,0.2)] bg-[rgba(139,92,246,0.04)]">
              <div
                className="text-[14px] leading-[1.85] text-text italic [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-purple-light [&_strong]:not-italic [&_strong]:font-semibold [&_em]:text-text [&_em]:italic"
                dangerouslySetInnerHTML={{ __html: encoding.loreIntroHtml }}
              />
            </div>
          )}
          {/* Plain analogy intro shown when lore is disabled */}
          {!loreEnabled && encoding.loreIntroHtml && (
            <div className="mb-6 px-4 py-3 rounded-[10px] border border-[rgba(45,212,191,0.2)] bg-[rgba(45,212,191,0.04)]">
              <div className="text-[11px] font-bold text-teal uppercase tracking-[0.08em] mb-2">Overview</div>
              <p className="text-[13px] text-muted leading-[1.7] m-0">
                Before diving in, read through the learning objectives and explanation below.
              </p>
            </div>
          )}

          {encoding.storyBeats && <StoryPanel beats={encoding.storyBeats} fullPage lessonId={encoding.lessonId} domainId={encoding.domainId} rabbitHoleTerms={encoding.rabbitHoleTerms} />}
          {encoding.explanationHtml && (
            <RabbitHoleHtml
              html={encoding.explanationHtml}
              terms={encoding.rabbitHoleTerms}
              lessonId={encoding.lessonId}
              domainId={encoding.domainId}
              className={cn('text-[15px] leading-[1.8] text-text my-6',
                '[&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0',
                '[&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic',
                '[&_h3]:text-[17px] [&_h3]:font-bold [&_h3]:text-gold [&_h3]:mt-7 [&_h3]:mb-2.5 [&_h3]:pb-1.5 [&_h3]:border-b [&_h3]:border-[rgba(255,193,7,0.15)]',
                '[&_h4]:text-[14px] [&_h4]:font-bold [&_h4]:text-purple-light [&_h4]:mt-5 [&_h4]:mb-2',
                '[&_ul]:pl-5 [&_ul]:mb-4 [&_ol]:pl-5 [&_ol]:mb-4',
                '[&_li]:mb-2 [&_li]:leading-[1.65] [&_li::marker]:text-purple-light',
                '[&_code]:bg-[rgba(139,92,246,0.12)] [&_code]:border [&_code]:border-[rgba(139,92,246,0.2)] [&_code]:rounded [&_code]:px-1.5 [&_code]:py-px [&_code]:text-[12.5px] [&_code]:text-purple-light [&_code]:font-mono',
                '[&_pre]:relative [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[10px] [&_pre]:overflow-hidden [&_pre]:my-5 [&_pre]:mb-6 [&_pre]:shadow-[0_4px_20px_rgba(0,0,0,0.3)]',
                '[&_pre]:before:content-["â—_â—_â—"] [&_pre]:before:block [&_pre]:before:px-4 [&_pre]:before:py-[9px] [&_pre]:before:text-[11px] [&_pre]:before:tracking-[4px] [&_pre]:before:text-[rgba(139,92,246,0.5)] [&_pre]:before:bg-[rgba(139,92,246,0.06)] [&_pre]:before:border-b [&_pre]:before:border-[rgba(139,92,246,0.12)]',
                '[&_pre_code]:block [&_pre_code]:px-5 [&_pre_code]:py-4 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.75] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:font-mono [&_pre_code]:overflow-x-auto [&_pre_code]:bg-transparent [&_pre_code]:border-none',
                'max-[480px]:text-[14px] max-[480px]:[&_pre_code]:text-[12px]',
              )}
            />
          )}
          {/* Phase 2 — Why It Matters */}
          {encoding.whyItMattersHtml && (
            <div className="mb-5 p-4 rounded-[10px] border border-[rgba(45,212,191,0.2)] bg-[rgba(45,212,191,0.04)]">
              <div className="text-[12px] font-bold text-teal uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
                <Zap size={12} strokeWidth={2} /> Why It Matters
              </div>
              <div
                className="text-[14px] leading-[1.75] text-text [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-teal [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic"
                dangerouslySetInnerHTML={{ __html: encoding.whyItMattersHtml }}
              />
            </div>
          )}

          {/* Phase 2 — Worked Examples */}
          {encoding.workedExamplesHtml && (
            <div className="mb-5 p-4 rounded-[10px] border border-[rgba(139,92,246,0.2)] bg-[rgba(139,92,246,0.04)]">
              <div className="text-[12px] font-bold text-purple-light uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
                <Code2 size={12} strokeWidth={2} /> Worked Examples
              </div>
              <div
                className="text-[14px] leading-[1.75] text-text
                  [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0
                  [&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic
                  [&_code]:bg-[rgba(139,92,246,0.12)] [&_code]:border [&_code]:border-[rgba(139,92,246,0.2)] [&_code]:rounded [&_code]:px-1.5 [&_code]:py-px [&_code]:text-[12px] [&_code]:text-purple-light [&_code]:font-mono
                  [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[8px] [&_pre]:overflow-x-auto [&_pre]:my-3
                  [&_pre_code]:block [&_pre_code]:px-4 [&_pre_code]:py-3 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.7] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:font-mono [&_pre_code]:bg-transparent [&_pre_code]:border-none"
                dangerouslySetInnerHTML={{ __html: encoding.workedExamplesHtml }}
              />
            </div>
          )}

          {/* Phase 2 — Mental Model */}
          {encoding.mentalModelHtml && (
            <div className="mb-5 p-4 rounded-[10px] border border-[rgba(255,193,7,0.2)] bg-[rgba(255,193,7,0.04)]">
              <div className="text-[12px] font-bold text-gold uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
                <Lightbulb size={12} strokeWidth={2} /> Mental Model
              </div>
              <div
                className="text-[14px] leading-[1.75] text-text italic [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-gold [&_strong]:not-italic [&_strong]:font-semibold [&_em]:text-text"
                dangerouslySetInnerHTML={{ __html: encoding.mentalModelHtml }}
              />
            </div>
          )}

          {/* Phase 2 — Mini Summary */}
          {encoding.miniSummaryHtml && (
            <div className="mb-6 p-4 rounded-[10px] border border-border bg-surface">
              <div className="text-[12px] font-bold text-muted uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
                <CheckCircle2 size={12} strokeWidth={2} /> Summary
              </div>
              <div
                className="text-[14px] leading-[1.75] text-text [&_p]:m-0 [&_p]:mb-2 [&_p:last-child]:mb-0 [&_ul]:pl-4 [&_ul]:m-0 [&_li]:mb-1.5 [&_li]:leading-[1.6] [&_li::marker]:text-teal [&_strong]:text-text [&_strong]:font-semibold [&_em]:text-purple-light"
                dangerouslySetInnerHTML={{ __html: encoding.miniSummaryHtml }}
              />
            </div>
          )}

          <button className="btn btn-primary" onClick={handleAdvance}>I understand — continue →</button>
        </div>
      )}

      {/* GUIDED_PRACTICE — step engine (Phase 3) */}
      {phase === 'GUIDED_PRACTICE' && encoding.hasGuidedSteps && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[13px] font-bold text-gold mb-4 tracking-[0.06em] uppercase">
            ✦ Guided Practice Quest
          </div>
          <GuidedStepper lessonId={encoding.lessonId} onAllComplete={handleAdvance} />
        </div>
      )}

      {/* GUIDED_PRACTICE — brief */}
      {phase === 'GUIDED_PRACTICE' && !encoding.hasGuidedSteps && practiceView === 'brief' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[13px] font-bold text-gold mb-2.5 tracking-[0.06em] uppercase">
            {encoding.practiceType === 'NONE' ? 'Study Material' : '✦ Guided Practice'}
          </div>
          <RabbitHoleHtml html={encoding.guidedPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={proseHtml} />
          {encoding.practiceType === 'NONE' && encoding.starterCode && (
            <pre className="mt-5 p-4 rounded-[10px] bg-bg border border-border text-[12px] leading-[1.55] overflow-x-auto whitespace-pre">
              <code>{encoding.starterCode}</code>
            </pre>
          )}
          {encoding.testCaseLabels && encoding.practiceType !== 'NONE' && (
            <div className="mt-5"><TestChips labels={encoding.testCaseLabels} results={testResults} /></div>
          )}
          {encoding.practiceType === 'NONE' ? (
            <button className="btn btn-primary mt-6" onClick={() => setPracticeView('code')}>Start Writing →</button>
          ) : (
            <button className="btn btn-primary mt-6" onClick={() => setPracticeView('code')}>Start Coding →</button>
          )}
        </div>
      )}

      {/* GUIDED_PRACTICE — coding (legacy path: no guided steps) */}
      {phase === 'GUIDED_PRACTICE' && !encoding.hasGuidedSteps && practiceView === 'code' && (
        <div className="flex flex-1 overflow-hidden min-h-0">
          {/* Mobile task overlay */}
          {showTaskOverlay && (
            <div className="fixed inset-0 bg-black/60 z-[100] hidden max-[640px]:flex items-end" onClick={() => setShowTaskOverlay(false)}>
              <div className="bg-card border-t border-border rounded-[16px_16px_0_0] px-4 py-5 pb-[max(32px,env(safe-area-inset-bottom,32px))] max-h-[75vh] overflow-y-auto w-full" onClick={e => e.stopPropagation()}>
                <div className="flex justify-between items-center mb-3.5">
                  <span className="text-[13px] font-bold text-gold uppercase tracking-[0.06em]">✦ Task</span>
                  <button type="button" className="btn btn-ghost text-[12px] min-h-[44px] px-4" onClick={() => setShowTaskOverlay(false)}>✕ Close</button>
                </div>
                <RabbitHoleHtml html={encoding.guidedPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={proseHtml} />
                {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}
              </div>
            </div>
          )}

          {/* Left panel — desktop */}
          <div className="w-[38%] min-w-[260px] max-w-[380px] flex flex-col border-r border-border overflow-y-auto p-4 gap-3 flex-shrink-0 max-[640px]:hidden">
            <div className="text-[13px] font-bold text-gold uppercase tracking-[0.06em]">✦ Task</div>
            <RabbitHoleHtml html={encoding.guidedPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={proseHtml} />
            {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}
            {practiceSolved && (
              <div className="p-3.5 bg-[rgba(0,200,83,0.08)] border border-teal rounded-[8px]">
                <div className="text-[14px] font-bold text-teal mb-2.5">✦ Practice Complete!</div>
                <button className="btn btn-primary" onClick={handleAdvance}>Continue →</button>
              </div>
            )}
          </div>

          {/* Right panel — editor */}
          <div
            className="flex-1 flex flex-col overflow-hidden min-w-0"
          >
            <div className="flex justify-between items-center px-3 py-2 border-b border-border bg-card flex-shrink-0 gap-2 max-[480px]:px-2.5 max-[480px]:py-1.5">
              <div className="flex items-center gap-2.5 min-w-0">
                <span className="text-[12px] text-muted truncate font-medium">{encoding.title}</span>
                {/* Mobile task toggle */}
                <button
                  className="hidden max-[640px]:inline-flex items-center gap-1 text-[11px] px-2.5 py-[3px] rounded-[10px] bg-purple-dim text-purple-light border border-[rgba(139,92,246,0.3)] cursor-pointer whitespace-nowrap flex-shrink-0"
                  onClick={() => setShowTaskOverlay(true)}
                >
                  <ClipboardList size={12} strokeWidth={1.75} /> Task
                </button>
              </div>
              <div className="flex gap-2 flex-shrink-0">
                {encoding.storyBeats?.length ? (
                  <button className="btn btn-ghost text-[12px] px-3 py-[5px] flex items-center gap-1" onClick={() => setStoryOpen(true)} title="Re-read the story">
                    <BookOpen size={12} strokeWidth={1.75} /> Story
                  </button>
                ) : null}
                {encoding.practiceType === 'JAVA' && (
                  <button className={cn('btn btn-ghost text-[12px] px-3.5 py-[5px] flex items-center gap-1', running && 'opacity-70')} onClick={handleRun} disabled={running}>
                    {running ? <><Loader2 size={13} strokeWidth={1.75} className="animate-spin" /> Running…</> : <><Play size={13} strokeWidth={1.75} /> Run</>}
                  </button>
                )}
                <button
                  className={cn(practiceSolved ? 'bg-teal text-bg border-none rounded-md cursor-default' : 'btn btn-primary', 'text-[12px] px-3.5 py-[5px] flex items-center gap-1')}
                  onClick={
                    encoding.practiceType === 'TAILWIND' ? handleSubmitTailwind
                    : encoding.practiceType === 'REACT' ? () => handleSubmitReact(false)
                    : encoding.practiceType === 'SQL' ? () => handleSubmitSql(false)
                    : encoding.practiceType === 'R' ? () => handleSubmitR(false)
                    : handleSubmitPractice
                  }
                  disabled={running || practiceSolved}
                >
                  {practiceSolved ? <><Check size={13} strokeWidth={2} /> Solved</> : <><Zap size={13} strokeWidth={1.75} /> Submit</>}
                </button>
              </div>
            </div>

            {encoding.practiceType === 'NONE' ? (
              <>
                <WrittenResponseEditor
                  value={code}
                  onChange={setCode}
                  disabled={practiceSolved}
                  placeholder="Write your guided response here. Follow the steps in the task panel, explain your reasoning, and use the lesson vocabulary."
                />
                <OutputPanel lines={output} />
                {practiceSolved && encoding.guidedPracticeModelAnswer && (
                  <ModelAnswerPanel answer={encoding.guidedPracticeModelAnswer} />
                )}
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Guided Response Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            ) : encoding.practiceType === 'TAILWIND' ? (
              <>
                <TailwindEditor value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'REACT' ? (
              <>
                <ReactEditor ref={reactEditorRef} value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'SQL' ? (
              <>
                <SqlEditor
                  ref={sqlEditorRef}
                  value={code}
                  onChange={setCode}
                  setup={extractSqlSetup(encoding.testCaseLabels)}
                  disabled={practiceSolved}
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'R' ? (
              <>
                <REditor
                  ref={rEditorRef}
                  value={code}
                  onChange={setCode}
                  setup={extractRSetup(encoding.testCaseLabels)}
                  disabled={practiceSolved}
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : (
              <>
                <CodeEditor value={code} onChange={setCode} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            )}
          </div>
        </div>
      )}

      {/* SOLO_PRACTICE — non-DETERMINISTIC assessment panel (RUBRIC_REFLECTION / PATTERN_MATCH / AI_REVIEW) */}
      {phase === 'SOLO_PRACTICE' && encoding.soloAssessmentType && encoding.soloAssessmentType !== 'DETERMINISTIC' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[13px] font-bold mb-1 tracking-[0.06em] uppercase flex items-center gap-1.5" style={{ color: 'var(--teal)' }}>
            <Target size={13} strokeWidth={1.75} /> Solo Challenge
          </div>
          <p className="text-muted text-[12px] mb-5 leading-[1.6]">
            Work through this independently — without looking back at the guided practice.
          </p>
          <RabbitHoleHtml
            html={encoding.soloPracticeHtml ?? ''}
            terms={encoding.rabbitHoleTerms}
            lessonId={encoding.lessonId}
            domainId={encoding.domainId}
            className={cn(proseHtml, 'mb-6')}
          />
          <SoloAssessmentPanel
            encoding={encoding}
            onSolved={(xp, badges) => {
              handleXpEarned(xp, badges)
              dispatch({ type: 'PRACTICE_SOLVED' })
            }}
            onAdvance={handleAdvance}
          />
        </div>
      )}

      {/* SOLO_PRACTICE — brief (DETERMINISTIC or null type) */}
      {phase === 'SOLO_PRACTICE' && (!encoding.soloAssessmentType || encoding.soloAssessmentType === 'DETERMINISTIC') && practiceView === 'brief' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[13px] font-bold mb-1 tracking-[0.06em] uppercase flex items-center gap-1.5" style={{ color: 'var(--teal)' }}><Target size={13} strokeWidth={1.75} /> Solo Challenge</div>
          <p className="text-muted text-[12px] mb-4 leading-[1.6]">
            Now rebuild this from a blank slate — no starter code. If you get stuck, peek at the guided practice for a hint.
          </p>
          <RabbitHoleHtml html={encoding.soloPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={proseHtml} />
          {encoding.testCaseLabels && <div className="mt-5"><TestChips labels={encoding.testCaseLabels} results={testResults} /></div>}

          {/* Hint toggle */}
          <div className="mt-5">
            <button
              className="btn btn-ghost text-[12px] px-3 py-1.5 flex items-center gap-1.5"
              onClick={() => dispatch({ type: 'HINT_TOGGLE' })}
            >
              {showHint ? <><EyeOff size={13} strokeWidth={1.75} /> Hide hint</> : <><Eye size={13} strokeWidth={1.75} /> Peek at Guided Practice</>}
            </button>
            {showHint && (
              <div className="mt-3 p-4 rounded-[10px] border border-dashed border-[rgba(139,92,246,0.35)] bg-[rgba(139,92,246,0.05)]">
                <div className="text-[11px] font-semibold text-muted uppercase tracking-[0.1em] mb-2.5">Guided Practice reference</div>
                <RabbitHoleHtml html={encoding.guidedPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={proseHtml} />
              </div>
            )}
          </div>

          <button className="btn btn-primary mt-6" onClick={() => setPracticeView('code')}>Start Coding →</button>
        </div>
      )}

      {/* SOLO_PRACTICE — coding (DETERMINISTIC only) */}
      {phase === 'SOLO_PRACTICE' && (!encoding.soloAssessmentType || encoding.soloAssessmentType === 'DETERMINISTIC') && practiceView === 'code' && (
        <div className="flex flex-1 overflow-hidden min-h-0">
          {/* Mobile task overlay */}
          {showTaskOverlay && (
            <div className="fixed inset-0 bg-black/60 z-[100] hidden max-[640px]:flex items-end" onClick={() => setShowTaskOverlay(false)}>
              <div className="bg-card border-t border-border rounded-[16px_16px_0_0] px-4 py-5 pb-[max(32px,env(safe-area-inset-bottom,32px))] max-h-[75vh] overflow-y-auto w-full" onClick={e => e.stopPropagation()}>
                <div className="flex justify-between items-center mb-3.5">
                  <span className="text-[13px] font-bold uppercase tracking-[0.06em] flex items-center gap-1.5" style={{ color: 'var(--teal)' }}><Target size={13} strokeWidth={1.75} /> Solo Challenge</span>
                  <button type="button" className="btn btn-ghost text-[12px] min-h-[44px] px-4" onClick={() => setShowTaskOverlay(false)}>✕ Close</button>
                </div>
                <RabbitHoleHtml html={encoding.soloPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={proseHtml} />
                {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}
              </div>
            </div>
          )}

          {/* Left panel — desktop */}
          <div className="w-[38%] min-w-[260px] max-w-[380px] flex flex-col border-r border-border overflow-y-auto p-4 gap-3 flex-shrink-0 max-[640px]:hidden">
            <div className="text-[13px] font-bold uppercase tracking-[0.06em] flex items-center gap-1.5" style={{ color: 'var(--teal)' }}><Target size={13} strokeWidth={1.75} /> Solo Challenge</div>
            <p className="text-muted text-[11px] leading-[1.6] mt-[-4px]">No starter code — build it from memory.</p>
            <RabbitHoleHtml html={encoding.soloPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={proseHtml} />
            {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}

            {/* Hint toggle */}
            <div className="border-t border-border pt-2.5 mt-1">
              <button className="btn btn-ghost text-[11px] px-2.5 py-1 flex items-center gap-1.5" onClick={() => dispatch({ type: 'HINT_TOGGLE' })}>
                {showHint ? <><EyeOff size={13} strokeWidth={1.75} /> Hide hint</> : <><Eye size={13} strokeWidth={1.75} /> Peek at Guided Practice</>}
              </button>
              {showHint && (
                <div className="mt-2.5 p-3 rounded-[8px] border border-dashed border-[rgba(139,92,246,0.3)] bg-[rgba(139,92,246,0.05)]">
                  <div className="text-[10px] font-semibold text-muted uppercase tracking-[0.1em] mb-2">Guided reference</div>
                  <RabbitHoleHtml html={encoding.guidedPracticeHtml ?? ''} terms={encoding.rabbitHoleTerms} lessonId={encoding.lessonId} domainId={encoding.domainId} className={cn(proseHtml, 'text-[12px]')} />
                </div>
              )}
            </div>

            {practiceSolved && (
              <div className="p-3.5 bg-[rgba(0,200,83,0.08)] border border-teal rounded-[8px]">
                <div className="text-[14px] font-bold text-teal mb-2.5">✦ Solo Challenge Complete!</div>
                <button className="btn btn-primary" onClick={handleAdvance}>Continue to Retrieval Check →</button>
              </div>
            )}
          </div>

          {/* Right panel — editor (paste blocked: Solo is write-from-memory) */}
          <div className="flex-1 flex flex-col overflow-hidden min-w-0">
            <div className="flex justify-between items-center px-3 py-2 border-b border-border bg-card flex-shrink-0 gap-2 max-[480px]:px-2.5 max-[480px]:py-1.5">
              <div className="flex items-center gap-2.5 min-w-0">
                <span className="text-[12px] text-muted truncate font-medium">{encoding.title}</span>
                <button
                  className="hidden max-[640px]:inline-flex items-center gap-1 text-[11px] px-2.5 py-[3px] rounded-[10px] bg-purple-dim text-purple-light border border-[rgba(139,92,246,0.3)] cursor-pointer whitespace-nowrap flex-shrink-0"
                  onClick={() => setShowTaskOverlay(true)}
                >
                  <ClipboardList size={12} strokeWidth={1.75} /> Task
                </button>
              </div>
              <div className="flex gap-2 flex-shrink-0">
                {encoding.storyBeats?.length ? (
                  <button className="btn btn-ghost text-[12px] px-3 py-[5px] flex items-center gap-1" onClick={() => setStoryOpen(true)} title="Re-read the story">
                    <BookOpen size={12} strokeWidth={1.75} /> Story
                  </button>
                ) : null}
                {encoding.practiceType === 'JAVA' && (
                  <button className={cn('btn btn-ghost text-[12px] px-3.5 py-[5px] flex items-center gap-1', running && 'opacity-70')} onClick={handleRun} disabled={running}>
                    {running ? <><Loader2 size={13} strokeWidth={1.75} className="animate-spin" /> Running…</> : <><Play size={13} strokeWidth={1.75} /> Run</>}
                  </button>
                )}
                <button
                  className={cn(practiceSolved ? 'bg-teal text-bg border-none rounded-md cursor-default' : 'btn btn-primary', 'text-[12px] px-3.5 py-[5px] flex items-center gap-1')}
                  onClick={
                    encoding.practiceType === 'TAILWIND' ? handleSubmitTailwindSolo
                    : encoding.practiceType === 'REACT' ? () => handleSubmitReact(true)
                    : encoding.practiceType === 'SQL' ? () => handleSubmitSql(true)
                    : encoding.practiceType === 'R' ? () => handleSubmitR(true)
                    : handleSubmitSoloPractice
                  }
                  disabled={running || practiceSolved}
                >
                  {practiceSolved ? <><Check size={13} strokeWidth={2} /> Solved</> : <><Zap size={13} strokeWidth={1.75} /> Submit</>}
                </button>
              </div>
            </div>

            {encoding.practiceType === 'NONE' ? (
              <>
                <WrittenResponseEditor
                  value={code}
                  onChange={setCode}
                  disabled={practiceSolved}
                  placeholder="Write your solo response here. Use your own example or case, and explain what evidence would support your answer."
                />
                <OutputPanel lines={output} />
                {practiceSolved && encoding.modelAnswer && (
                  <ModelAnswerPanel answer={encoding.modelAnswer} />
                )}
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Response Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            ) : encoding.practiceType === 'TAILWIND' ? (
              <>
                <TailwindEditor value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'REACT' ? (
              <>
                <ReactEditor ref={reactEditorRef} value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'SQL' ? (
              <>
                <SqlEditor
                  ref={sqlEditorRef}
                  value={code}
                  onChange={setCode}
                  setup={extractSqlSetup(encoding.testCaseLabels)}
                  disabled={practiceSolved}
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'R' ? (
              <>
                <REditor
                  ref={rEditorRef}
                  value={code}
                  onChange={setCode}
                  setup={extractRSetup(encoding.testCaseLabels)}
                  disabled={practiceSolved}
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : (
              <>
                <CodeEditor value={code} onChange={setCode} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[640px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            )}
          </div>
        </div>
      )}

      {/* RETRIEVAL_CHECK */}
      {phase === 'RETRIEVAL_CHECK' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[20px] font-bold text-gold mb-1.5">✦ Retrieval Check</div>
          <p className="text-muted text-[13px] mb-5">Answer these questions to test your understanding.</p>

          {!retrievalResult && !encoding.retrievalQuestions?.length ? (
            // Already submitted on a prior visit — let the user advance
            <div className="p-4 bg-card border border-border rounded-[10px]">
              <p className="text-muted text-[13px] mb-3">You have already completed this retrieval check.</p>
              <button className="btn btn-primary" onClick={handleAdvance}>Continue →</button>
            </div>
          ) : !retrievalResult ? (
            <>
              {encoding.retrievalQuestions?.map((q, i) => (
                <QuestionCard key={q.id} question={q} index={i} answer={answers[q.id] ?? ''} onChange={v => dispatch({ type: 'ANSWER_CHANGED', questionId: q.id, answer: v })} />
              ))}
              <button className="btn btn-primary mt-1" onClick={handleSubmitRetrieval} disabled={submittingRetrieval}>
                {submittingRetrieval ? 'Submitting...' : 'Submit Answers'}
              </button>
            </>
          ) : (
            <div className="mt-2">
              <div className="text-[20px] font-bold text-text mb-1.5">
                Score: {Math.round(retrievalResult.score * 100)}% ({retrievalResult.correct}/{retrievalResult.total})
              </div>
              <div className={cn('text-[16px] font-semibold mb-3.5', retrievalResult.passed ? 'text-green' : 'text-red')}>
                {retrievalResult.passed ? '✓ Passed!' : '✗ Needs more practice'}
              </div>
              {encoding.retrievalQuestions?.map((q, i) => (
                <QuestionCard key={q.id} question={q} index={i}
                  answer={retrievalResult.results[i]?.userAnswer ?? ''} onChange={() => {}}
                  result={retrievalResult.results[i]} disabled />
              ))}
              <p className="text-muted text-[13px] italic mt-2.5">{retrievalResult.recommendation}</p>
              <button className="btn btn-primary mt-3" onClick={handleAdvance}>Continue →</button>
            </div>
          )}
        </div>
      )}

      {/* INTEGRATION */}
      {phase === 'INTEGRATION' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[20px] font-bold text-purple mb-1.5">⟁ Integration</div>
          <p className="text-muted text-[13px] mb-5">Connect what you've learned to the wider world of knowledge.</p>
          {encoding.integrationPrompt && (
            <div
              className="prose prose-invert max-w-none text-[15px] leading-relaxed mb-8 text-text"
              dangerouslySetInnerHTML={{ __html: encoding.integrationPrompt }}
            />
          )}
          <div className="bg-surface border border-border rounded-xl p-5 mb-6">
            <p className="text-[13px] text-muted mb-2 font-medium">Reflect before continuing:</p>
            <p className="text-[14px] text-text">Take a moment to consider the connections above. How does this concept show up in other areas of your life or studies?</p>
          </div>
          <button className="btn btn-primary" onClick={handleAdvance}>Complete Lesson →</button>
        </div>
      )}

      {/* COMPLETE */}
      {phase === 'COMPLETE' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-center mb-6">
            <div className="text-[48px] text-gold mb-3">✦</div>
            <h2 className="text-[24px] font-bold text-gold m-0 mb-2">Concept Mastered!</h2>
            <p className="text-muted text-[14px]">You've completed {encoding.title}. This concept will be reviewed via spaced repetition.</p>
          </div>

          {/* ── Feynman — prominent card ──────────────────────────────────── */}
          {encoding.feynmanPrompt && !feynmanResult && (
            <div className="mb-5 p-5 rounded-[12px] border-2 border-[rgba(139,92,246,0.45)] bg-[rgba(139,92,246,0.07)] shadow-[0_0_24px_rgba(139,92,246,0.12)]">
              <div className="flex items-center gap-2 mb-1">
                <span className="text-[20px]">✨</span>
                <span className="text-[17px] font-bold text-purple-light">Teach It Back</span>
                <span className="ml-auto text-[11px] text-muted font-cinzel uppercase tracking-[0.06em]">Optional · Earns XP</span>
              </div>
              <p className="text-[12px] text-muted mb-3 leading-[1.6]">
                The best way to confirm you understand — explain it as if teaching someone from scratch. No jargon, just clarity.
              </p>
              <p className="text-[14px] text-text italic mb-3 leading-[1.65] p-3 bg-[rgba(0,0,0,0.2)] rounded-[8px] border border-[rgba(139,92,246,0.2)]">
                "{encoding.feynmanPrompt}"
              </p>
              <textarea
                className="w-full bg-surface border border-[rgba(139,92,246,0.3)] rounded-md px-3 py-3 text-[14px] text-text font-crimson resize-y mb-3 box-border focus:outline-none focus:border-purple"
                placeholder="Write your explanation here. Imagine your reader has never heard of this concept before..."
                value={feynmanText} onChange={e => setFeynmanText(e.target.value)} rows={7}
              />
              <button
                className="btn btn-primary w-full flex items-center justify-center gap-2 text-[14px] py-2.5"
                onClick={handleSubmitFeynman}
                disabled={submittingFeynman || !feynmanText.trim()}
              >
                {submittingFeynman
                  ? <><Loader2 size={14} strokeWidth={1.75} className="animate-spin" /> Evaluating your explanation…</>
                  : <><PenLine size={14} strokeWidth={1.75} /> Submit Explanation &amp; Earn XP</>}
              </button>
            </div>
          )}

          {feynmanResult && (
            <div className="mb-5 p-5 rounded-[12px] border border-teal bg-[rgba(45,212,191,0.06)]">
              <div className="flex items-center gap-2 mb-3">
                <span className="text-[20px]">✨</span>
                <span className="text-[17px] font-bold text-teal">Feynman Result</span>
                <span className="ml-auto text-[20px] font-bold text-teal">{Math.round(feynmanResult.overallScore * 100)}%</span>
              </div>
              {/* Score breakdown bars */}
              <div className="grid grid-cols-2 gap-2 mb-3 max-[480px]:grid-cols-1">
                {([
                  ['Accuracy',     feynmanResult.accuracy],
                  ['Completeness', feynmanResult.completeness],
                  ['Simplicity',   feynmanResult.simplicity],
                  ['Connection',   feynmanResult.connection],
                ] as [string, number][]).map(([label, val]) => (
                  <div key={label} className="flex flex-col gap-1">
                    <div className="flex justify-between text-[11px] text-muted">
                      <span>{label}</span><span className="text-teal font-semibold">{Math.round(val * 100)}%</span>
                    </div>
                    <div className="h-1.5 rounded-full bg-[rgba(45,212,191,0.15)] overflow-hidden">
                      <div className="h-full rounded-full bg-teal" style={{ width: `${Math.round(val * 100)}%` }} />
                    </div>
                  </div>
                ))}
              </div>
              <p className="text-[13px] text-text leading-[1.6] italic">{feynmanResult.feedback}</p>
              {feynmanResult.xpEarned > 0 && (
                <div className="mt-2.5 text-[12px] text-gold font-semibold">✦ +{feynmanResult.xpEarned} XP earned</div>
              )}
            </div>
          )}

          {/* ── Common Mistakes ───────────────────────────────────────────── */}
          {encoding.commonMistakes && encoding.commonMistakes.length > 0 && (
            <div className="mb-5 p-4 rounded-[10px] border border-[rgba(248,113,113,0.25)] bg-[rgba(248,113,113,0.05)]">
              <div className="text-[12px] font-bold uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5" style={{ color: '#f87171' }}>
                ⚠ Common Mistakes
              </div>
              <ul className="m-0 pl-4 space-y-1.5">
                {encoding.commonMistakes.map((m, i) => (
                  <li key={i} className="text-[13px] text-text leading-[1.6]" style={{ listStyleType: '"→ "' }}>{m}</li>
                ))}
              </ul>
            </div>
          )}

          {/* ── Assessment Criteria ───────────────────────────────────────── */}
          {encoding.assessmentCriteria && encoding.assessmentCriteria.length > 0 && (
            <div className="mb-5 p-4 rounded-[10px] border border-[rgba(201,162,39,0.25)] bg-[rgba(201,162,39,0.05)]">
              <div className="text-[12px] font-bold text-gold uppercase tracking-[0.08em] mb-2.5">
                ✦ You know this when you can…
              </div>
              <ul className="m-0 pl-4 space-y-1.5">
                {encoding.assessmentCriteria.map((c, i) => (
                  <li key={i} className="text-[13px] text-text leading-[1.6] marker:text-gold">{c}</li>
                ))}
              </ul>
            </div>
          )}

          {/* ── Capstone save form ───────────────────────────────────────── */}
          {isCapstoneLesson && !capstoneSavedId && (
            <div className="mb-5 p-5 rounded-[12px] border-2 border-[rgba(201,162,39,0.35)] bg-[rgba(201,162,39,0.05)]">
              <div className="flex items-center gap-2 mb-1">
                <span className="text-[20px]">🏗️</span>
                <span className="text-[17px] font-bold text-gold">Save Your Project</span>
              </div>
              <p className="text-[12px] text-muted mb-4 leading-[1.6]">
                Document what you built for your portfolio. Your project will appear on your Profile → Projects tab.
              </p>
              <div className="flex flex-col gap-3">
                <input
                  type="text"
                  placeholder="Project title *"
                  value={capstoneTitle}
                  onChange={e => setCapstoneTitle(e.target.value)}
                  className="bg-surface border border-border rounded-[8px] px-3 py-2 text-[13px] text-text placeholder:text-muted outline-none focus:border-gold transition-[border-color]"
                />
                <textarea
                  placeholder="Brief description of what you built…"
                  value={capstoneDesc}
                  onChange={e => setCapstoneDesc(e.target.value)}
                  rows={3}
                  className="bg-surface border border-border rounded-[8px] px-3 py-2 text-[13px] text-text placeholder:text-muted outline-none focus:border-gold transition-[border-color] resize-y"
                />
                <textarea
                  placeholder="Paste your code here (optional)…"
                  value={capstoneCode}
                  onChange={e => setCapstoneCode(e.target.value)}
                  rows={5}
                  className="bg-surface border border-border rounded-[8px] px-3 py-2 text-[12px] text-text font-mono placeholder:text-muted outline-none focus:border-gold transition-[border-color] resize-y"
                />
                <input
                  type="url"
                  placeholder="GitHub repository URL (optional)"
                  value={capstoneGithub}
                  onChange={e => setCapstoneGithub(e.target.value)}
                  className="bg-surface border border-border rounded-[8px] px-3 py-2 text-[13px] text-text placeholder:text-muted outline-none focus:border-gold transition-[border-color]"
                />
                <button
                  className="btn btn-primary flex items-center justify-center gap-2"
                  onClick={saveCapstone}
                  disabled={capstoneSaving || !capstoneTitle.trim()}
                >
                  {capstoneSaving
                    ? <><Loader2 size={14} className="animate-spin" /> Saving...</>
                    : 'Save Project to Profile'}
                </button>
              </div>
            </div>
          )}
          {isCapstoneLesson && capstoneSavedId && (
            <div className="mb-5 p-4 rounded-[12px] border border-teal bg-[rgba(45,212,191,0.06)] text-center">
              <div className="text-[24px] mb-2">✓</div>
              <div className="font-cinzel text-[14px] text-teal mb-1">Project Saved!</div>
              <p className="text-[12px] text-muted">Find it in Profile → Projects. An instructor may leave feedback there.</p>
            </div>
          )}

          {/* Lore Conclusion — shown only when lore mode is enabled */}
          {loreEnabled && encoding.loreConclusionHtml && (
            <div className="mb-5 px-5 py-5 rounded-[12px] border border-[rgba(201,162,39,0.25)] bg-[rgba(201,162,39,0.04)]"
              style={{ borderLeft: '3px solid rgba(201,162,39,0.5)' }}>
              <div className="flex items-center gap-2 mb-3">
                <Sparkles size={13} strokeWidth={1.75} className="text-gold flex-shrink-0" />
                <span className="font-cinzel text-[11px] text-gold uppercase tracking-[0.12em]">Lore Conclusion</span>
              </div>
              <div
                className="text-[14px] leading-[1.85] text-text italic [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-gold [&_strong]:not-italic [&_strong]:font-semibold [&_em]:text-text"
                dangerouslySetInnerHTML={{ __html: encoding.loreConclusionHtml }}
              />
            </div>
          )}
          {/* Plain conclusion when lore is disabled */}
          {!loreEnabled && encoding.loreConclusionHtml && (
            <div className="mb-5 p-4 rounded-[10px] border border-border bg-surface">
              <div className="flex items-center gap-2 mb-2">
                <CheckCircle2 size={13} strokeWidth={1.75} className="text-teal flex-shrink-0" />
                <span className="font-cinzel text-[11px] text-teal uppercase tracking-[0.12em]">Lesson Complete</span>
              </div>
              <p className="text-[13px] text-muted leading-[1.7] m-0">
                You've completed this lesson. Review the key concepts above before moving on.
              </p>
            </div>
          )}

          {/* ── Navigation ───────────────────────────────────────────────── */}
          <div className="flex gap-2.5 justify-center mt-4 flex-wrap max-[480px]:flex-col max-[480px]:items-center">
            <button className="btn btn-success" onClick={() => navigate(`/module/${encoding.moduleId}`)}>Return to Module →</button>
            <button className="btn btn-ghost" onClick={() => navigate(`/pathway/${encoding.domainId ?? 'software-engineering'}`)}>
              Dashboard
            </button>
            {encoding.storyBeats?.length ? (
              <button className="btn btn-ghost flex items-center gap-1.5" onClick={() => setStoryOpen(true)}>
                <BookOpen size={14} strokeWidth={1.75} />
                Re-read Story
              </button>
            ) : null}
          </div>
        </div>
      )}

      {/* Story re-read modal — rendered in document.body via portal so it covers the Nav on iOS */}
      {storyOpen && encoding.storyBeats?.length ? createPortal(
        <div
          className="fixed inset-0 z-[9000] flex items-start justify-center p-4"
          style={{ background: 'rgba(0,0,0,0.80)', paddingTop: 'max(16px, env(safe-area-inset-top, 16px))' }}
          onClick={() => setStoryOpen(false)}
        >
          <div
            className="relative bg-card border border-[rgba(139,92,246,0.35)] rounded-[16px] w-full max-w-[680px] max-h-[calc(100dvh-32px)] flex flex-col shadow-[0_8px_48px_rgba(0,0,0,0.6)] mt-0"
            onClick={e => e.stopPropagation()}
          >
            <div className="flex items-center justify-between px-5 py-3.5 border-b border-border flex-shrink-0">
              <span className="text-[13px] font-bold text-gold tracking-[0.06em] uppercase">ðŸ"– Story</span>
              <button type="button" className="btn btn-ghost text-[13px] px-4 py-2 min-h-[44px]" onClick={() => setStoryOpen(false)}>✕ Close</button>
            </div>
            <div className="overflow-y-auto flex-1 px-5 py-5">
              <StoryPanel beats={encoding.storyBeats} fullPage lessonId={encoding.lessonId} domainId={encoding.domainId} rabbitHoleTerms={encoding.rabbitHoleTerms} />
            </div>
          </div>
        </div>,
        document.body
      ) : null}

      {/* StuckButton — only during active practice phases */}
      {(phase === 'GUIDED_PRACTICE' || phase === 'SOLO_PRACTICE' || phase === 'RETRIEVAL_CHECK') && (
        <StuckButton />
      )}

      {/* Notes floating button — bottom-LEFT so it never clashes with StuckButton (bottom-right) */}
      {notePhaseVisible && (
        <button
          type="button"
          onClick={() => setNotePanelOpen(v => !v)}
          className={cn(
            'fixed left-4 z-[150] flex items-center gap-2 px-4 py-2.5 rounded-full text-[12px] font-cinzel tracking-wide shadow-[0_4px_16px_rgba(0,0,0,0.4)] transition-all duration-200',
            notePanelOpen
              ? 'bg-purple text-white border border-purple'
              : 'bg-card border border-border text-muted hover:border-purple-dim hover:text-text',
          )}
          style={{ bottom: 'max(24px, env(safe-area-inset-bottom, 24px))' }}
        >
          <StickyNote size={13} strokeWidth={2} />
          {notePanelOpen ? 'Close Notes' : 'Notes'}
          {noteSaved && !notePanelOpen && (
            <CheckCircle2 size={12} className="text-teal" />
          )}
        </button>
      )}

      {/* Notes panel — slides up from bottom-left */}
      {notePhaseVisible && notePanelOpen && (
        <div
          className="fixed left-4 z-[140] w-[380px] max-w-[calc(100vw-32px)] bg-card border border-purple-dim rounded-[14px] shadow-[0_8px_32px_rgba(0,0,0,0.5)] flex flex-col overflow-hidden animate-[toast-in_0.2s_ease]"
          style={{ bottom: 'calc(max(24px, env(safe-area-inset-bottom, 24px)) + 52px)' }}
        >
          <div className="flex items-center justify-between px-4 py-3 border-b border-border">
            <div className="flex items-center gap-2">
              <StickyNote size={13} className="text-purple" />
              <span className="font-cinzel text-[12px] text-text tracking-wide">My Notes</span>
            </div>
            <div className="flex items-center gap-2">
              {noteSaving && <Loader2 size={11} className="animate-spin text-muted" />}
              {noteSaved && !noteSaving && <span className="text-[10px] text-teal font-cinzel">Saved ✓</span>}
              <button
                type="button"
                onClick={saveNoteNow}
                disabled={noteSaving || !noteContent.trim()}
                className="text-[10px] font-cinzel px-2.5 py-1 rounded-[6px] bg-purple-dim text-purple-light border border-purple disabled:opacity-40 transition-colors hover:bg-purple hover:text-white"
              >
                Save
              </button>
              <button type="button" onClick={() => setNotePanelOpen(false)} className="text-muted hover:text-text transition-colors p-0.5">
                <X size={13} />
              </button>
            </div>
          </div>
          <div className="px-4 py-2 border-b border-border">
            <div className="text-[10px] text-muted font-cinzel truncate">{noteTitle}</div>
          </div>
          <textarea
            className="flex-1 bg-transparent px-4 py-3 text-[12px] text-text leading-[1.7] placeholder:text-muted outline-none resize-none min-h-[220px] max-h-[40vh]"
            placeholder="Write your notes here… they auto-save after you stop typing."
            value={noteContent}
            onChange={e => {
              setNoteContent(e.target.value)
              scheduleAutoSave()
            }}
          />
        </div>
      )}

      {toast && (
        <div className="toast fixed bottom-6 left-1/2 -translate-x-1/2 z-[200] bg-card border border-gold rounded-lg px-4 py-2.5 text-[13px] text-gold font-cinzel shadow-[0_4px_16px_rgba(0,0,0,0.4)] pointer-events-none animate-[toast-in_0.3s_ease]">
          {toast}
        </div>
      )}
      {levelUpInfo && <LevelUpModal newLevel={levelUpInfo.level} newRank={levelUpInfo.rank} onClose={() => setLevelUpInfo(null)} />}
      {newBadges.length > 0 && <BadgeToast badges={newBadges} onDone={() => setNewBadges([])} />}
    </div>
  )
}

function questTypeLabel(q: string): string {
  return ({ KNOWLEDGE: 'Knowledge Quest', GUIDED: 'Guided Quest', PRACTICE: 'Practice Quest', INVESTIGATION: 'Investigation Quest', SYNTHESIS: 'Synthesis Quest', MASTERY: 'Mastery Quest' })[q] ?? q
}
function phaseOrder(p: string): number {
  return ['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'SOLO_PRACTICE', 'RETRIEVAL_CHECK', 'INTEGRATION', 'COMPLETE'].indexOf(p)
}
function phaseLabel(p: string): string {
  return ({ HOOK: 'Hook', EXPLANATION: 'Learn', GUIDED_PRACTICE: 'Practice', SOLO_PRACTICE: 'Solo', RETRIEVAL_CHECK: 'Check', INTEGRATION: 'Connect', COMPLETE: 'Done' })[p] ?? p
}
function WrittenResponseEditor({
  value,
  onChange,
  disabled,
  placeholder,
}: {
  value: string
  onChange: (value: string) => void
  disabled?: boolean
  placeholder: string
}) {
  const words = value.trim() ? value.trim().split(/\s+/).length : 0

  return (
    <div className="flex-1 flex flex-col min-h-0 bg-bg">
      <div className="flex items-center justify-between px-3 py-2 border-b border-border bg-card text-[12px] text-muted">
        <span>Written response</span>
        <span>{words} words</span>
      </div>
      <textarea
        className="flex-1 w-full resize-none bg-bg text-text font-crimson text-[15px] leading-[1.7] p-4 border-0 outline-none box-border disabled:opacity-70"
        value={value}
        onChange={e => onChange(e.target.value)}
        disabled={disabled}
        placeholder={placeholder}
      />
    </div>
  )
}
/**
 * Reveals a model / exemplar answer after solo practice is submitted.
 * Collapsible so students are encouraged to genuinely attempt before peeking.
 */
function ModelAnswerPanel({ answer }: { answer: string }) {
  const [open, setOpen] = useState(false)
  return (
    <div className="border-t border-border flex-shrink-0">
      <button
        className="w-full flex items-center justify-between px-4 py-2.5 text-[12px] font-semibold text-gold hover:bg-[rgba(255,215,0,0.04)] transition-colors"
        onClick={() => setOpen(v => !v)}
      >
        <span>ðŸ"‹ Model Answer</span>
        <span className="text-muted text-[11px]">{open ? 'â–² hide' : 'â–¼ reveal'}</span>
      </button>
      {open && (
        <div className="px-4 pb-4 pt-1 text-[13px] leading-[1.75] text-text font-crimson whitespace-pre-wrap border-t border-dashed border-[rgba(255,215,0,0.2)] bg-[rgba(255,215,0,0.03)]">
          {answer}
        </div>
      )}
    </div>
  )
}

function calculateRank(xp: number): string {
  if (xp >= 11000) return 'Lord Magus'
  if (xp >= 8000) return 'Magus'
  if (xp >= 6500) return 'Archmage'
  if (xp >= 4000) return 'Mage'
  if (xp >= 2000) return 'Adept'
  if (xp >= 800) return 'Apprentice'
  return 'Novice'
}
