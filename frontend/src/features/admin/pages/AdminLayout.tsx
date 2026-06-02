import { useState } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import { LayoutDashboard, Library, BookMarked, Users, ArrowUpDown, ChevronLeft, LogOut, Flag, Menu, X, Trophy } from 'lucide-react'
import { useIsMobile } from '@/hooks/useIsMobile'

const NAV_ITEMS = [
  { path: '/admin',                label: 'Dashboard',      Icon: LayoutDashboard, exact: true },
  { path: '/admin/domains',        label: 'Pathways',       Icon: BookMarked },
  { path: '/admin/chunks',         label: 'Content',        Icon: Library },
  { path: '/admin/users',          label: 'Users',          Icon: Users },
  { path: '/admin/stuck-reports',  label: 'Stuck Reports',  Icon: Flag },
  { path: '/admin/capstones',      label: 'Capstones',      Icon: Trophy },
  { path: '/admin/import-export',  label: 'Import / Export',Icon: ArrowUpDown },
]

function Sidebar({ onClose }: { onClose?: () => void }) {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleNav = (path: string) => {
    navigate(path)
    onClose?.()
  }

  return (
    <aside style={{
      width: 220, flexShrink: 0,
      background: '#100e1f',
      borderRight: '1px solid #1e1a35',
      display: 'flex', flexDirection: 'column',
      height: '100%',
    }}>
      {/* Logo */}
      <div
        style={{ padding: '18px 20px 14px', borderBottom: '1px solid #1e1a35', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}
        onClick={() => handleNav('/schools')}
        title="Back to Academy"
      >
        <div>
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
        {onClose && (
          <button onClick={e => { e.stopPropagation(); onClose() }} style={{ background: 'transparent', border: 'none', color: '#8b7fa0', cursor: 'pointer', padding: 4 }}>
            <X size={16} />
          </button>
        )}
      </div>

      {/* Back to academy link */}
      <button
        onClick={() => handleNav('/schools')}
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
            onClick={() => onClose?.()}
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
  )
}

export default function AdminLayout() {
  const isMobile = useIsMobile()
  const [drawerOpen, setDrawerOpen] = useState(false)

  if (isMobile) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%', background: '#0a0916' }}>

        {/* Mobile top bar */}
        <header style={{
          display: 'flex', alignItems: 'center', gap: 12,
          padding: '12px 16px',
          background: '#100e1f',
          borderBottom: '1px solid #1e1a35',
          flexShrink: 0,
        }}>
          <button
            onClick={() => setDrawerOpen(true)}
            style={{ background: 'transparent', border: 'none', color: '#c9a227', cursor: 'pointer', padding: 4, display: 'flex' }}
            aria-label="Open navigation"
          >
            <Menu size={22} />
          </button>
          <div style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: '#c9a227', letterSpacing: 2, flex: 1 }}>
            ✦ ADMIN CONSOLE
          </div>
        </header>

        {/* Drawer overlay */}
        {drawerOpen && (
          <>
            {/* Backdrop */}
            <div
              onClick={() => setDrawerOpen(false)}
              style={{
                position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.6)',
                zIndex: 40,
              }}
            />
            {/* Drawer */}
            <div style={{
              position: 'fixed', top: 0, left: 0, bottom: 0,
              width: 240,
              zIndex: 50,
              display: 'flex', flexDirection: 'column',
            }}>
              <Sidebar onClose={() => setDrawerOpen(false)} />
            </div>
          </>
        )}

        {/* Main content */}
        <main style={{ flex: 1, overflowY: 'auto', padding: '20px 16px' }}>
          <Outlet />
        </main>
      </div>
    )
  }

  return (
    <div style={{ display: 'flex', height: '100%', background: '#0a0916' }}>
      <Sidebar />
      {/* ── Main area ──────────────────────────────────────────────────────── */}
      <main style={{ flex: 1, overflowY: 'auto', padding: '32px 36px' }}>
        <Outlet />
      </main>
    </div>
  )
}
