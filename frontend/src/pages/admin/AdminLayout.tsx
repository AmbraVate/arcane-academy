import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'

const NAV_ITEMS = [
  { path: '/admin',              label: 'Dashboard',      icon: '📊', exact: true },
  { path: '/admin/chunks',       label: 'Content',        icon: '📚' },
  { path: '/admin/users',        label: 'Users',          icon: '👥' },
  { path: '/admin/import-export',label: 'Import / Export',icon: '📦' },
]

export default function AdminLayout() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  return (
    <div style={{ display: 'flex', height: '100%', background: '#0a0916' }}>

      {/* ── Sidebar ──────────────────────────────────────────────────────── */}
      <aside style={{
        width: 220,
        flexShrink: 0,
        background: '#100e1f',
        borderRight: '1px solid #1e1a35',
        display: 'flex',
        flexDirection: 'column',
        padding: '0',
      }}>

        {/* Logo */}
        <div
          style={{
            padding: '18px 20px 14px',
            borderBottom: '1px solid #1e1a35',
            cursor: 'pointer',
          }}
          onClick={() => navigate('/topics')}
        >
          <div style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: '#c9a227', letterSpacing: 2 }}>
            ✦ ARCANE ACADEMY
          </div>
          <div style={{
            marginTop: 4,
            fontSize: 10,
            fontFamily: 'Cinzel, serif',
            color: '#8b5cf6',
            letterSpacing: 1,
            background: 'rgba(139,92,246,.12)',
            border: '1px solid rgba(139,92,246,.3)',
            borderRadius: 4,
            padding: '2px 7px',
            display: 'inline-block',
          }}>
            ADMIN
          </div>
        </div>

        {/* Nav */}
        <nav style={{ flex: 1, padding: '12px 8px' }}>
          {NAV_ITEMS.map(item => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.exact}
              style={({ isActive }) => ({
                display: 'flex',
                alignItems: 'center',
                gap: 10,
                padding: '9px 12px',
                borderRadius: 7,
                marginBottom: 2,
                fontSize: 13,
                fontFamily: 'Cinzel, serif',
                textDecoration: 'none',
                transition: 'all .15s ease',
                background: isActive ? 'rgba(139,92,246,.15)' : 'transparent',
                color: isActive ? '#c4b5fd' : '#8b7fa0',
                borderLeft: isActive ? '2px solid #8b5cf6' : '2px solid transparent',
              })}
            >
              <span style={{ fontSize: 16 }}>{item.icon}</span>
              {item.label}
            </NavLink>
          ))}
        </nav>

        {/* User footer */}
        <div style={{
          padding: '12px 16px',
          borderTop: '1px solid #1e1a35',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: 8,
        }}>
          <div style={{ fontSize: 12, color: '#8b7fa0', overflow: 'hidden' }}>
            <div style={{ color: '#c4b5fd', fontWeight: 600, fontSize: 12, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
              {user?.username}
            </div>
            <div style={{ fontSize: 10, marginTop: 1 }}>Administrator</div>
          </div>
          <button
            onClick={logout}
            title="Logout"
            style={{
              background: 'transparent',
              border: '1px solid #2e2850',
              borderRadius: 6,
              color: '#8b7fa0',
              cursor: 'pointer',
              padding: '4px 8px',
              fontSize: 12,
              transition: 'all .15s ease',
            }}
            onMouseEnter={e => {
              (e.currentTarget as HTMLButtonElement).style.borderColor = '#f87171'
              ;(e.currentTarget as HTMLButtonElement).style.color = '#f87171'
            }}
            onMouseLeave={e => {
              (e.currentTarget as HTMLButtonElement).style.borderColor = '#2e2850'
              ;(e.currentTarget as HTMLButtonElement).style.color = '#8b7fa0'
            }}
          >
            ⏏
          </button>
        </div>
      </aside>

      {/* ── Main area ──────────────────────────────────────────────────────── */}
      <main style={{ flex: 1, overflowY: 'auto', padding: '32px 36px' }}>
        <Outlet />
      </main>
    </div>
  )
}
