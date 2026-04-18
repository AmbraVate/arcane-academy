import { cn } from '@/lib/utils'
import type { StoryBeat } from '../../types'

export default function StoryPanel({ beats, fullPage = false }: { beats: StoryBeat[]; fullPage?: boolean }) {
  return (
    <div className="flex flex-col gap-3">
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
      className={cn(
        'font-crimson italic text-muted leading-[1.75] pl-2.5 border-l-2 border-border',
        fullPage && 'text-[16px] leading-[1.9] py-3.5 px-5 border-l-[3px] border-purple bg-[rgba(139,92,246,0.05)] rounded-r-lg mb-5 text-text text-[13px]',
        !fullPage && 'text-[13px]',
      )}
      dangerouslySetInnerHTML={{ __html: text }}
    />
  )
}

function Example({ beat, fullPage }: { beat: StoryBeat; fullPage: boolean }) {
  return (
    <div
      className={cn(
        'bg-[#09070f] border border-purple-dim border-l-[3px] border-l-purple rounded-r-lg my-1 mb-3 overflow-hidden',
        fullPage && 'border-l-4',
      )}
    >
      {beat.speaker && (
        <div
          className={cn(
            'font-cinzel text-[10px] tracking-[2px] text-purple-light px-3.5 py-[7px] pb-[5px] border-b border-border',
            fullPage && 'text-[12px]',
          )}
        >
          ✦ {beat.speaker}
        </div>
      )}
      <pre
        className={cn(
          'px-3.5 py-3 font-mono text-[12px] leading-[1.7] text-[#e2e8f0] m-0 whitespace-pre overflow-x-auto',
          fullPage && 'text-[13px] leading-[1.8]',
        )}
      >
        <code dangerouslySetInnerHTML={{ __html: beat.text }} />
      </pre>
    </div>
  )
}

function Dialogue({ beat, fullPage }: { beat: StoryBeat; fullPage: boolean }) {
  const avatarBorder =
    beat.cls === 'mentor' ? 'border-gold' :
    beat.cls === 'enemy'  ? 'border-red'  :
    'border-teal'

  const speakerColor =
    beat.sCls === 's-mentor' ? 'text-gold' :
    beat.sCls === 's-enemy'  ? 'text-red'  :
    'text-teal'

  return (
    <div className="flex gap-2.5">
      <div
        className={cn(
          'rounded-full border-2 flex items-center justify-center flex-shrink-0 bg-surface',
          'w-10 h-10 text-[19px]',
          fullPage && 'w-[52px] h-[52px] text-[24px]',
          avatarBorder,
        )}
      >
        {beat.av}
      </div>
      <div
        className={cn(
          'bg-card border border-border rounded-[0_9px_9px_9px] px-[13px] py-[9px] flex-1',
          fullPage && 'px-[22px] py-4 rounded-[0_14px_14px_14px] bg-[rgba(255,255,255,0.03)] border-[rgba(255,255,255,0.08)]',
        )}
      >
        <div className={cn('font-cinzel text-[10px] text-muted mb-1', fullPage && 'text-[11px]', speakerColor)}>
          {beat.speaker}
        </div>
        <div
          className={cn(
            'text-[13px] leading-[1.75] [&_em]:text-teal [&_em]:not-italic [&_em]:font-semibold',
            fullPage && 'text-[15px] leading-[1.8]',
          )}
          dangerouslySetInnerHTML={{ __html: beat.text ?? '' }}
        />
      </div>
    </div>
  )
}
