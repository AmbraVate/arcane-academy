import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

const PILLARS = [
  { icon: '🧩', name: 'Chunking',          desc: 'Knowledge broken into tight, meaningful units — each one a stepping stone in a larger graph.' },
  { icon: '⚡', name: 'Encoding',           desc: "Multi-phase delivery: hook → story → guided practice. You don't just read — you do." },
  { icon: '🎯', name: 'Active Recall',      desc: 'Tiered questions force your brain to retrieve, not recognise. Retrieval beats re-reading by 3×.' },
  { icon: '🔀', name: 'Interleaving',       desc: 'Mixed practice across topics trains transfer. Blocked practice creates illusions of competence.' },
  { icon: '📅', name: 'Spaced Repetition',  desc: 'SM-2 algorithm schedules every concept at the optimal moment — right before you forget.' },
  { icon: '🪶', name: 'Feynman Technique',  desc: "Explain it in plain language. If you can't teach it, you don't know it yet." },
]

export default function LandingPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const ctaLabel = user ? 'Dashboard' : 'Start Learning Free'
  const ctaDest  = user ? '/topics' : '/register'

  return (
    <div className="min-h-screen bg-[#05030A] text-[#E8E0F0] font-sans selection:bg-purple-light/30 selection:text-white overflow-x-hidden relative">
      
      {/* ── Ambient Background Glows ── */}
      <div className="fixed inset-0 z-0 pointer-events-none overflow-hidden">
        <div className="absolute top-[-10%] left-[-10%] w-[50vw] h-[50vw] bg-purple-light/10 blur-[150px] rounded-full mix-blend-screen" />
        <div className="absolute top-[30%] right-[-10%] w-[40vw] h-[40vw] bg-teal/10 blur-[150px] rounded-full mix-blend-screen" />
      </div>

      {/* ── Step 1: Floating Capsule Navbar ──────────────────────────────── */}
      <div className="fixed top-6 left-0 right-0 z-50 flex justify-center px-4 pointer-events-none">
        <header className="pointer-events-auto flex items-center justify-between w-full max-w-5xl h-16 px-6 bg-black/40 backdrop-blur-2xl rounded-full border border-white/10 shadow-[0_8px_32px_rgba(0,0,0,0.5)]">
          
          <div className="text-lg font-bold tracking-widest text-white flex items-center gap-3 cursor-pointer hover:text-gold transition-colors font-cinzel" onClick={() => navigate('/')}>
            <div className="w-7 h-7 rounded-full bg-gradient-to-tr from-purple to-teal flex items-center justify-center shadow-[0_0_15px_rgba(45,212,191,0.3)]">
              <span className="text-white font-black text-xs">A</span>
            </div>
            ARCANE
          </div>
          
          <nav className="hidden md:flex items-center gap-8 text-sm font-medium text-[#A1A1A1]">
            <span className="hover:text-white cursor-pointer transition-colors">Curriculum</span>
            <span className="hover:text-white cursor-pointer transition-colors">Methodology</span>
          </nav>
          
          <div className="flex items-center gap-4">
            {!user && (
              <button className="text-sm font-medium text-[#A1A1A1] hover:text-white transition-colors" onClick={() => navigate('/login')}>
                Log in
              </button>
            )}
            <button 
              onClick={() => navigate(ctaDest)}
              className="text-sm font-bold bg-white text-black px-6 py-2 rounded-full hover:scale-105 hover:bg-[#E8E0F0] transition-all duration-300 shadow-[0_0_20px_rgba(255,255,255,0.1)]"
            >
              {user ? 'Dashboard' : 'Sign Up Free'}
            </button>
          </div>
          
        </header>
      </div>

      <main className="relative z-10 pt-40 pb-20">
        
        {/* ── Step 2: Hero Section ───────────────────────────────────────── */}
        <section className="max-w-7xl mx-auto px-6 flex flex-col lg:flex-row items-center gap-16 min-h-[75vh]">
          
          {/* Left Text */}
          <div className="flex-1 text-left relative z-10">
            <div className="inline-flex items-center gap-3 px-4 py-2 rounded-full border border-white/5 bg-white/5 backdrop-blur-md mb-8 shadow-inner">
              <div className="w-2 h-2 rounded-full bg-purple-light shadow-[0_0_10px_rgba(196,181,253,1)] animate-pulse"></div>
              <span className="text-xs font-semibold tracking-widest text-[#E8E0F0] uppercase">Cognitive Engine v2.0 Live</span>
            </div>
            
            <h1 className="text-6xl md:text-7xl lg:text-[84px] font-bold tracking-tighter leading-[1.05] text-white mb-8 font-cinzel">
              Master complex <br className="hidden lg:block"/> skills like a <br className="hidden lg:block"/>
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-purple-light to-teal drop-shadow-[0_0_15px_rgba(45,212,191,0.3)]">Polymath.</span>
            </h1>
            
            <p className="text-xl text-[#A1A1A1] max-w-xl leading-relaxed mb-10 font-light">
              Replace passive tutorials with a high-fidelity cognitive engine. Arcane Academy combines gamification, interactive AI, and spaced repetition to build true expertise.
            </p>
            
            <div className="flex flex-col sm:flex-row items-center gap-4">
              <button 
                onClick={() => navigate(ctaDest)}
                className="w-full sm:w-auto px-10 py-4 bg-gradient-to-r from-purple to-purple-light text-white font-bold rounded-full hover:scale-105 hover:shadow-[0_0_40px_rgba(139,92,246,0.5)] transition-all duration-300 text-lg shadow-[0_0_20px_rgba(139,92,246,0.3)]"
              >
                {ctaLabel}
              </button>
              {!user && (
                <button 
                  onClick={() => navigate('/login')}
                  className="w-full sm:w-auto px-10 py-4 bg-transparent border border-white/10 text-white font-bold rounded-full hover:bg-white/5 transition-all duration-300 text-lg backdrop-blur-md"
                >
                  View Curriculum
                </button>
              )}
            </div>
          </div>

          {/* Right Visual (Floating 3D Glass Element) */}
          <div className="flex-1 w-full relative group perspective-1000">
            {/* Custom slow-float animation wrapper */}
            <div className="relative w-full h-[500px] md:h-[600px] animate-slow-float">
              
              {/* Outer Gradient Glass Border (1px) */}
              <div className="absolute inset-0 bg-gradient-to-br from-purple/40 via-white/5 to-teal/40 rounded-[40px] p-[1px] shadow-[0_0_80px_rgba(139,92,246,0.15)] transform rotate-[-2deg] group-hover:rotate-0 transition-transform duration-700 ease-out">
                {/* Inner Dark Card */}
                <div className="w-full h-full bg-[#0A0710]/90 backdrop-blur-3xl rounded-[39px] overflow-hidden flex flex-col relative">
                  
                  {/* Subtle inner top glow */}
                  <div className="absolute top-0 inset-x-0 h-px bg-gradient-to-r from-transparent via-white/30 to-transparent z-10" />
                  
                  {/* Fake Window Header */}
                  <div className="h-14 border-b border-white/5 bg-white/5 flex items-center px-6 gap-3">
                    <div className="flex gap-2.5">
                      <div className="w-3.5 h-3.5 rounded-full bg-red-500/50"></div>
                      <div className="w-3.5 h-3.5 rounded-full bg-yellow-500/50"></div>
                      <div className="w-3.5 h-3.5 rounded-full bg-green-500/50"></div>
                    </div>
                    <div className="ml-4 text-xs font-bold text-[#A1A1A1] uppercase tracking-widest font-mono">Master_Velan.exe</div>
                  </div>
                  
                  {/* Code / Chat Interface */}
                  <div className="p-8 flex flex-col gap-6 font-mono text-sm md:text-base relative z-10">
                    <div className="bg-white/5 p-5 rounded-2xl border border-white/5 backdrop-blur-md max-w-[90%] shadow-[0_4px_24px_rgba(0,0,0,0.5)] transform transition-transform hover:-translate-y-1">
                      <span className="text-purple-light font-bold tracking-wide text-shadow-[0_0_10px_rgba(196,181,253,0.5)]">Master Velan:</span>
                      <p className="text-white/90 mt-3 leading-relaxed">
                        "Your spell collapsed because you are dividing by zero. Look closely at your 'mana_drain' calculation."
                      </p>
                    </div>
                    
                    <div className="self-end bg-gradient-to-br from-purple/20 to-teal/10 p-5 rounded-2xl border border-white/10 backdrop-blur-md max-w-[85%] shadow-[0_4px_24px_rgba(0,0,0,0.5)] transform transition-transform hover:-translate-y-1">
                      <p className="text-white leading-relaxed">
                        &gt; Ah, so I need to check if the shield is greater than zero first?
                      </p>
                    </div>
                    
                    <div className="bg-white/5 p-5 rounded-2xl border border-white/5 backdrop-blur-md max-w-[90%] shadow-[0_4px_24px_rgba(0,0,0,0.5)] transform transition-transform hover:-translate-y-1">
                      <span className="text-purple-light font-bold tracking-wide text-shadow-[0_0_10px_rgba(196,181,253,0.5)]">Master Velan:</span>
                      <p className="text-white/90 mt-3 leading-relaxed">
                        "Precisely. A wise mage never casts into the void. Implement the check and run the test again."
                      </p>
                    </div>
                  </div>

                  {/* Overlapping glowing element breaking the frame */}
                  <div className="absolute -right-10 -bottom-10 w-64 h-64 bg-teal/20 blur-[80px] rounded-full pointer-events-none" />
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* ── Trust / Stats Strip ────────────────────────────────────────── */}
        <section className="py-12 mt-12 relative z-10">
          <div className="max-w-7xl mx-auto px-6">
            <div className="flex flex-col md:flex-row justify-center gap-12 md:gap-32 text-center">
              <div className="flex flex-col items-center group">
                <div className="text-5xl md:text-6xl font-cinzel font-bold text-white mb-3 tracking-tighter drop-shadow-[0_0_15px_rgba(201,162,39,0.3)] group-hover:text-gold transition-colors">6×</div>
                <div className="text-[#A1A1A1] text-xs uppercase tracking-[0.2em] font-bold">Faster Retention</div>
              </div>
              <div className="flex flex-col items-center group">
                <div className="text-5xl md:text-6xl font-cinzel font-bold text-white mb-3 tracking-tighter drop-shadow-[0_0_15px_rgba(139,92,246,0.3)] group-hover:text-purple-light transition-colors">AI</div>
                <div className="text-[#A1A1A1] text-xs uppercase tracking-[0.2em] font-bold">Socratic Guidance</div>
              </div>
              <div className="flex flex-col items-center group">
                <div className="text-5xl md:text-6xl font-cinzel font-bold text-white mb-3 tracking-tighter drop-shadow-[0_0_15px_rgba(45,212,191,0.3)] group-hover:text-teal transition-colors">SM-2</div>
                <div className="text-[#A1A1A1] text-xs uppercase tracking-[0.2em] font-bold">Spaced Repetition</div>
              </div>
            </div>
          </div>
        </section>

        {/* ── Features / Pillars Bento Box (Preview for Step 3) ──────────── */}
        <section className="py-32 relative z-10">
          <div className="max-w-7xl mx-auto px-6">
            <div className="text-center max-w-3xl mx-auto mb-20">
              <h2 className="text-4xl md:text-5xl font-bold text-white tracking-tight mb-6 font-cinzel">Built on Cognitive Science</h2>
              <p className="text-xl text-[#A1A1A1] max-w-xl mx-auto font-light">We replaced the standard methodology with six scientifically proven pillars of deep learning.</p>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {PILLARS.map((pillar, idx) => (
                <div key={idx} className="group relative bg-[#0A0710]/80 backdrop-blur-xl rounded-[32px] p-10 shadow-2xl transition-all duration-500 hover:-translate-y-2">
                  {/* Outer Gradient Glass Border (1px) */}
                  <div className="absolute inset-0 bg-gradient-to-br from-white/10 to-transparent rounded-[32px] p-[1px] -z-10 group-hover:from-purple/40 group-hover:to-teal/10 transition-colors duration-500">
                    <div className="w-full h-full bg-[#0A0710] rounded-[31px]"></div>
                  </div>
                  
                  <div className="relative z-10 text-3xl mb-8 bg-white/5 w-16 h-16 flex items-center justify-center rounded-2xl border border-white/10 shadow-inner group-hover:scale-110 transition-transform duration-500">
                    {pillar.icon}
                  </div>
                  <h3 className="relative z-10 text-2xl font-cinzel font-bold text-white mb-4">{pillar.name}</h3>
                  <p className="relative z-10 text-[#A1A1A1] leading-relaxed text-[17px] font-light">{pillar.desc}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* ── Final CTA ────────────────────────────────────────────────── */}
        <section className="py-48 relative overflow-hidden">
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[1000px] h-[1000px] bg-purple/10 blur-[150px] rounded-full pointer-events-none" />
          <div className="max-w-4xl mx-auto px-6 relative z-10 text-center">
            <h2 className="text-5xl md:text-7xl font-bold text-white tracking-tighter mb-8 leading-tight font-cinzel">Ready to forge <br/> your skills?</h2>
            <p className="text-2xl text-muted mb-16 max-w-2xl mx-auto font-light">
              Join Arcane Academy today. The first track is completely free for early scholars.
            </p>
            <button 
              onClick={() => navigate(ctaDest)}
              className="px-14 py-6 bg-gradient-to-r from-purple to-purple-light text-white font-bold rounded-full hover:scale-105 transition-all text-xl shadow-[0_0_60px_rgba(139,92,246,0.4)] hover:shadow-[0_0_80px_rgba(139,92,246,0.6)]"
            >
              {ctaLabel}
            </button>
          </div>
        </section>

      </main>

      {/* ── Footer ───────────────────────────────────────────────────────── */}
      <footer className="border-t border-white/5 bg-[#05030A] py-12 relative z-10">
        <div className="max-w-7xl mx-auto px-6 flex flex-col md:flex-row justify-between items-center gap-6">
          <div className="flex items-center gap-3 text-gold font-bold tracking-widest text-lg font-cinzel">
            <div className="w-6 h-6 rounded-md bg-gradient-to-tr from-purple to-purple-light flex items-center justify-center">
              <span className="text-white font-black text-[10px]">A</span>
            </div>
            ARCANE ACADEMY
          </div>
          <div className="text-[#8B7FA0] font-light text-sm tracking-wide">© {new Date().getFullYear()} Arcane Academy. Built on Science.</div>
        </div>
      </footer>
    </div>
  )
}
