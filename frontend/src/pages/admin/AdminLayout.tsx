import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import {
  LayoutDashboard, Library, Users, ArrowUpDown, LogOut, ChevronLeft,
} from 'lucide-react'

const NAV_ITEMS = [
  { path: '/admin',               label: 'Dashboard',      Icon: LayoutDashboard, exact: true },
  { path: '/admin/chunks',        label: 'Content',        Icon: Library },
  { path: '/admin/users',         label: 'Users',          Icon: Users },
  { path: '/admin/import-export', label: 'Import / Export',Icon: ArrowUpDown },
]

export default function AdminLayout() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  return (
    <div style={{ display: 'flex', height: '100%', background: '#0a0916' }}>

      {/* ── Sidebar ──────────────────────────────────────────────────────── */}
      <aside style={{
        width: 220, flexShrink: 0,
        background: '#100e1f',
        borderRight: '1px solid #1e1a35',
        display: 'flex', flexDirection: 'column',
      }}>

        {/* Logo */}
        <div
          style={{ padding: '18px 20px 14px', borderBottom: '1px solid #1e1a35', cursor: 'pointer' }}
          onClick={() => navigate('/topics')}
          title="Back to Academy"
        >
          <div style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: '#c9a227', letterSpacing: 2 }}>
            ✦ ARCANE ACADEMY
          </div>
          <div style={{
            marginTop: 4, fontSize: 10, fontFamily: 'Cinzel, serif',
            color: '#8b5cf6', letterSpacing: 1,
            background: 'rgba(139,92,246,.12)', border: '1px solid rgba(139,92,246,.3)',
            borderRadius: 4, padding: '2px 7px', display: 'inline-block',
          }}>
            ADMIN CONSOLE
          </div>
        </div>

        {/* Back to academy link */}
        <button
          onClick={() => navigate('/topics')}
          style={{
            display: 'flex', alignItems: 'center', gap: 7,
            margin: '10px 10px 4px',
            padding: '7px 12px',
            background: 'transparent',
            border: '1px solid #1e1a35',
            borderRadius: 7,
            color: '#8b7fa0',
            fontSize: 11,
            fontFamily: 'Cinzel, serif',
            cursor: 'pointer',
            transition: 'all .15s',
          }}
          onMouseEnter={e => {
            (e.currentTarget as HTMLButtonElement).style.borderColor = '#2e2850'
            ;(e.currentTarget as HTMLButtonElement).style.color = '#c4b5fd'
          }}
          onMouseLeave={e => {
            (e.currentTarget as HTMLButtonElement).style.borderColor = '#1e1a35'
            ;(e.currentTarget as HTMLButtonElement).style.color = '#8b7fa0'
          }}
        >
          <ChevronLeft size={12} />
          Back to Academy
        </button>

        {/* Nav */}
        <nav style={{ flex: 1, padding: '8px 8px' }}>
          {NAV_ITEMS.map(({ path, label, Icon, exact }) => (
            <NavLink
              key={path}
              to={path}
              end={exact}
              style={({ isActive }) => ({
                display: 'flex', alignItems: 'center', gap: 10,
                padding: '9px 12px', borderRadius: 7, marginBottom: 2,
                fontSize: 13, fontFamily: 'Cinzel, serif',
                textDecoration: 'none',
                transition: 'all .15s ease',
                background: isActive ? 'rgba(139,92,246,.15)' : 'transparent',
                color: isActive ? '#c4b5fd' : '#8b7fa0',
                borderLeft: isActive ? '2px solid #8b5cf6' : '2px solid transparent',
              })}
            >
              <Icon size={15} strokeWidth={1.75} />
              {label}
            </NavLink>
          ))}
        </nav>

        {/* User footer */}
        <div style={{
          padding: '12px 16px', borderTop: '1px solid #1e1a35',
          display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8,
        }}>
          <div style={{ overflow: 'hidden' }}>
            <div style={{ color: '#c4b5fd', fontWeight: 600, fontSize: 12, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
              {user?.username}
            </div>
            <div style={{ fontSize: 10, marginTop: 1, color: '#8b7fa0' }}>Administrator</div>
          </div>
          <button
            onClick={logout}
            title="Logout"
            style={{
              background: 'transparent', border: '1px solid #2e2850',
              borderRadius: 6, color: '#8b7fa0', cursor: 'pointer',
              padding: '5px 7px', display: 'flex', alignItems: 'center',
              transition: 'all .15s',
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
            <LogOut size={14} strokeWidth={1.75} />
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
