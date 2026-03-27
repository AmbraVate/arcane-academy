// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
import type { StoryBeat } from '../../types'
import styles from './StoryPanel.module.css'

export default function StoryPanel({ beats }: { beats: StoryBeat[] }) {
  return (
    <div className={styles.story}>
      {beats.map((beat, i) => (
        beat.type === 'narration'
          ? <Narration key={i} text={beat.text} />
          : <Dialogue key={i} beat={beat} />
      ))}
    </div>
  )
}

function Narration({ text }: { text: string }) {
  return (
    <div
      className={styles.narration}
      dangerouslySetInnerHTML={{ __html: text }}
    />
  )
}

function Dialogue({ beat }: { beat: StoryBeat }) {
  const speakerClass = beat.sCls === 's-mentor'
    ? styles.speakerMentor
    : beat.sCls === 's-enemy'
    ? styles.speakerEnemy
    : styles.speakerNpc

  const avatarClass = beat.cls === 'mentor'
    ? styles.avatarMentor
    : beat.cls === 'enemy'
    ? styles.avatarEnemy
    : styles.avatarNpc

  return (
    <div className={styles.dialogue}>
      <div className={`${styles.avatar} ${avatarClass}`}>{beat.av}</div>
      <div className={styles.bubble}>
        <div className={`${styles.speaker} ${speakerClass}`}>{beat.speaker}</div>
        <div
          className={styles.speech}
          dangerouslySetInnerHTML={{ __html: beat.text ?? '' }}
        />
      </div>
    </div>
  )
}
