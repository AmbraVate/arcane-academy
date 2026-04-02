import type { StoryBeat } from '../../types'
import styles from './StoryPanel.module.css'

export default function StoryPanel({ beats, fullPage = false }: { beats: StoryBeat[]; fullPage?: boolean }) {
  return (
    <div className={styles.story}>
      {beats.map((beat, i) => {
        if (beat.type === 'narration') return <Narration key={i} text={beat.text} fullPage={fullPage} />
        if (beat.type === 'example')  return <Example key={i} beat={beat} fullPage={fullPage} />
        return <Dialogue key={i} beat={beat} fullPage={fullPage} />
      })}
    </div>
  )
}

function Narration({ text, fullPage }: { text: string; fullPage: boolean }) {
  return (
    <div
      className={`${styles.narration} ${fullPage ? styles.narrationFull : ''}`}
      dangerouslySetInnerHTML={{ __html: text }}
    />
  )
}

function Example({ beat, fullPage }: { beat: StoryBeat; fullPage: boolean }) {
  return (
    <div className={`${styles.exampleBlock} ${fullPage ? styles.exampleBlockFull : ''}`}>
      {beat.speaker && (
        <div className={`${styles.exampleLabel} ${fullPage ? styles.exampleLabelFull : ''}`}>
          ✦ {beat.speaker}
        </div>
      )}
      <pre className={`${styles.exampleCode} ${fullPage ? styles.exampleCodeFull : ''}`}>
        <code dangerouslySetInnerHTML={{ __html: beat.text }} />
      </pre>
    </div>
  )
}

function Dialogue({ beat, fullPage }: { beat: StoryBeat; fullPage: boolean }) {
  const speakerClass =
    beat.sCls === 's-mentor' ? styles.speakerMentor :
    beat.sCls === 's-enemy'  ? styles.speakerEnemy  :
    styles.speakerNpc

  const avatarClass =
    beat.cls === 'mentor' ? styles.avatarMentor :
    beat.cls === 'enemy'  ? styles.avatarEnemy  :
    styles.avatarNpc

  return (
    <div className={styles.dialogue}>
      <div className={`${styles.avatar} ${avatarClass} ${fullPage ? styles.avatarFull : ''}`}>
        {beat.av}
      </div>
      <div className={`${styles.bubble} ${fullPage ? styles.bubbleFull : ''}`}>
        <div className={`${styles.speaker} ${speakerClass} ${fullPage ? styles.speakerFull : ''}`}>
          {beat.speaker}
        </div>
        <div
          className={`${styles.speech} ${fullPage ? styles.speechFull : ''}`}
          dangerouslySetInnerHTML={{ __html: beat.text ?? '' }}
        />
      </div>
    </div>
  )
}
