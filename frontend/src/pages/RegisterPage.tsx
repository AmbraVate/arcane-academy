import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import styles from './AuthPage.module.css'

export default function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await register(username, email, password)
      navigate('/')
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })
        ?.response?.data?.message ?? 'Registration failed.'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.glyph}>⚗</div>
        <h1 className={styles.title}>Begin Your Journey</h1>
        <p className={styles.sub}>Create your wizard identity</p>

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.field}>
            <label className={styles.label}>Wizard Name</label>
            <input className="input" type="text" value={username}
              onChange={e => setUsername(e.target.value)} placeholder="Aldric" required minLength={3} />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Email</label>
            <input className="input" type="email" value={email}
              onChange={e => setEmail(e.target.value)} placeholder="wizard@academy.com" required />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Password</label>
            <input className="input" type="password" value={password}
              onChange={e => setPassword(e.target.value)} placeholder="Min. 8 characters" required minLength={8} />
          </div>
          {error && <div className={styles.error}>{error}</div>}
          <button className="btn btn-primary" type="submit" disabled={loading} style={{ width: '100%', padding: '10px' }}>
            {loading ? 'Enrolling...' : '✦ Enroll in the Academy'}
          </button>
        </form>

        <p className={styles.switch}>
          Already enrolled? <Link to="/login" style={{ color: 'var(--purple-light)' }}>Login here</Link>
        </p>
      </div>
    </div>
  )
}
