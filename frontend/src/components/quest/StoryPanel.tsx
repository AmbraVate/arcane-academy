import type { StoryBeat } from '../../types'
import styles from './StoryPanel.module.css'

export default function StoryPanel({ beats }: { beats: StoryBeat[] }) {
  return (
    <div className={styles.story}>
      {beats.map((beat, i) => {
        if (beat.type === 'narration') return <Narration key={i} text={beat.text} />
        if (beat.type === 'example')  return <Example key={i} beat={beat} />
        return <Dialogue key={i} beat={beat} />
      })}
    </div>
  )
}

function Narration({ text }: { text: string }) {
  return <div className={styles.narration} dangerouslySetInnerHTML={{ __html: text }} />
}

function Example({ beat }: { beat: StoryBeat }) {
  return (
    <div className={styles.exampleBlock}>
      {beat.speaker && <div className={styles.exampleLabel}>✦ {beat.speaker}</div>}
      <pre className={styles.exampleCode}><code dangerouslySetInnerHTML={{ __html: beat.text }} /></pre>
    </div>
  )
}

function Dialogue({ beat }: { beat: StoryBeat }) {
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
      <div className={`${styles.avatar} ${avatarClass}`}>{beat.av}</div>
      <div className={styles.bubble}>
        <div className={`${styles.speaker} ${speakerClass}`}>{beat.speaker}</div>
        <div className={styles.speech} dangerouslySetInnerHTML={{ __html: beat.text ?? '' }} />
      </div>
    </div>
  )
}
