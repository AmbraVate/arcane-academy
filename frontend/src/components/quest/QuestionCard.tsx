import styles from './QuestionCard.module.css'
import type { QuestionDto, QuestionResultDto } from '../../types'

const OPTION_LETTERS = ['A', 'B', 'C', 'D', 'E']

const TIER_META: Record<string, { label: string; cls: string }> = {
  RECALL:         { label: 'Recall',         cls: styles.tierRecall },
  APPLICATION:    { label: 'Application',    cls: styles.tierApplication },
  DISCRIMINATION: { label: 'Discrimination', cls: styles.tierDiscrimination },
}

interface Props {
  question: QuestionDto
  index: number
  answer: string
  onChange: (v: string) => void
  /** Optional — show correct/wrong state after submission */
  result?: QuestionResultDto
  /** If true, inputs are disabled */
  disabled?: boolean
}

export default function QuestionCard({ question, index, answer, onChange, result, disabled }: Props) {
  const tier = TIER_META[question.tier] ?? { label: question.tier, cls: '' }
  const isChoice = question.type === 'MULTIPLE_CHOICE' || question.type === 'TRUE_FALSE'
  const options = question.type === 'TRUE_FALSE' ? ['True', 'False'] : (question.options ?? [])

  return (
    <div className={`${styles.card} ${result ? (result.correct ? styles.cardCorrect : styles.cardWrong) : ''}`}>
      {/* Card header row */}
      <div className={styles.cardHead}>
        <span className={styles.qIndex}>Q{index + 1}</span>
        <span className={`${styles.tierBadge} ${tier.cls}`}>{tier.label}</span>
        <span className={styles.typeBadge}>{formatType(question.type)}</span>
      </div>

      {/* Question text */}
      <div
        className={styles.qText}
        dangerouslySetInnerHTML={{ __html: question.questionHtml }}
      />

      {/* Code snippet */}
      {question.codeSnippet && (
        <div className={styles.codeBlock}>
          <div className={styles.codeHeader}>
            <span className={styles.codeDot} /><span className={styles.codeDot} /><span className={styles.codeDot} />
            <span className={styles.codeLang}>Java</span>
          </div>
          <pre className={styles.codePre}><code>{question.codeSnippet}</code></pre>
        </div>
      )}

      {/* Multiple choice / True-False */}
      {isChoice && (
        <div className={styles.options}>
          {options.map((opt, i) => {
            const letter = OPTION_LETTERS[i] ?? opt
            const selected = answer === opt
            const isCorrect = result && opt === result.correctAnswer
            const isWrong   = result && selected && !result.correct

            return (
              <label
                key={opt}
                className={`
                  ${styles.option}
                  ${selected ? styles.optSelected : ''}
                  ${isCorrect ? styles.optCorrect : ''}
                  ${isWrong   ? styles.optWrong   : ''}
                `}
              >
                <input
                  type="radio"
                  name={question.id}
                  value={opt}
                  checked={selected}
                  onChange={() => !disabled && onChange(opt)}
                  disabled={disabled}
                  className={styles.radioInput}
                />
                <span className={`${styles.letterBadge} ${selected ? styles.letterSelected : ''} ${isCorrect ? styles.letterCorrect : ''} ${isWrong ? styles.letterWrong : ''}`}>
                  {letter}
                </span>
                <span className={styles.optText}>{opt}</span>
                {isCorrect && <span className={styles.optIcon}>✓</span>}
                {isWrong   && <span className={styles.optIcon}>✗</span>}
              </label>
            )
          })}
        </div>
      )}

      {/* Free text / code answer */}
      {!isChoice && (
        <div className={styles.textAnswerWrap}>
          <div className={styles.textAnswerLabel}>
            {question.type === 'WHATS_THE_OUTPUT' ? '⟩ What does this output?' :
             question.type === 'DEBUGGING'        ? '⟩ What is the bug / fix?' :
             question.type === 'CODE_COMPLETION'  ? '⟩ Complete the code:' :
             '⟩ Your answer:'}
          </div>
          <textarea
            className={`${styles.textAnswer} ${result ? (result.correct ? styles.textCorrect : styles.textWrong) : ''}`}
            placeholder="Type your answer here..."
            value={answer}
            onChange={e => !disabled && onChange(e.target.value)}
            disabled={disabled}
            rows={3}
          />
        </div>
      )}

      {/* Result explanation */}
      {result && !result.correct && (
        <div className={styles.explanation}>
          <div className={styles.explHeader}>
            <span className={styles.explIcon}>✗</span> Incorrect
          </div>
          <div className={styles.explRow}>
            <span className={styles.explLabel}>Your answer:</span>
            <span className={styles.explWrong}>{result.userAnswer || '(no answer)'}</span>
          </div>
          <div className={styles.explRow}>
            <span className={styles.explLabel}>Correct answer:</span>
            <span className={styles.explRight}>{result.correctAnswer}</span>
          </div>
          {result.explanationHtml && (
            <div
              className={styles.explText}
              dangerouslySetInnerHTML={{ __html: result.explanationHtml }}
            />
          )}
        </div>
      )}

      {result && result.correct && (
        <div className={styles.explanationCorrect}>
          <span className={styles.explIcon}>✓</span> Correct
        </div>
      )}
    </div>
  )
}

function formatType(type: string): string {
  const map: Record<string, string> = {
    MULTIPLE_CHOICE:  'Multiple Choice',
    TRUE_FALSE:       'True / False',
    FILL_BLANK:       'Fill in the Blank',
    CODE_COMPLETION:  'Code Completion',
    WHATS_THE_OUTPUT: "What's the Output?",
    DEBUGGING:        'Find the Bug',
    SCENARIO:         'Scenario',
    COMPARE_CONTRAST: 'Compare & Contrast',
  }
  return map[type] ?? type
}
