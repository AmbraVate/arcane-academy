/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{ts,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        bg:             '#0e0c1a',
        surface:        '#16132b',
        card:           '#1e1a35',
        border:         '#2e2850',
        gold:           '#c9a227',
        'gold-dim':     '#4a3a0a',
        purple:         '#8b5cf6',
        'purple-dim':   '#2e1a5e',
        'purple-light': '#c4b5fd',
        teal:           '#2dd4bf',
        'teal-dim':     '#0d3a34',
        text:           '#e8e0f0',
        muted:          '#8b7fa0',
        red:            '#f87171',
        green:          '#4ade80',
        orange:         '#fb923c',
      },
      fontFamily: {
        cinzel:  ['"Cinzel"', 'serif'],
        crimson: ['"Crimson Pro"', 'serif'],
      },
      keyframes: {
        'toast-in': {
          from: { opacity: '0', transform: 'translateY(10px)' },
          to:   { opacity: '1', transform: 'translateY(0)' },
        },
        shimmer: {
          '0%, 100%': { boxShadow: '0 0 16px rgba(201,162,39,0.2)' },
          '50%':      { boxShadow: '0 0 28px rgba(201,162,39,0.4)' },
        },
        'fade-up': {
          from: { opacity: '0', transform: 'translateY(24px)' },
          to:   { opacity: '1', transform: 'translateY(0)' },
        },
        'flame-pulse': {
          '0%, 100%': { transform: 'scale(1)' },
          '50%':      { transform: 'scale(1.15)' },
        },
        'level-pop': {
          '0%':   { transform: 'scale(0.85) translateY(20px)', opacity: '0' },
          '60%':  { transform: 'scale(1.05) translateY(-4px)', opacity: '1' },
          '100%': { transform: 'scale(1) translateY(0)', opacity: '1' },
        },
      },
      animation: {
        'toast-in':    'toast-in 0.3s ease',
        shimmer:       'shimmer 2s ease-in-out infinite',
        'fade-up':     'fade-up 0.7s ease both',
        'flame-pulse': 'flame-pulse 2s ease-in-out infinite',
        'level-pop':   'level-pop 0.35s cubic-bezier(0.34,1.56,0.64,1) both',
      },
    },
  },
  plugins: [],
}
