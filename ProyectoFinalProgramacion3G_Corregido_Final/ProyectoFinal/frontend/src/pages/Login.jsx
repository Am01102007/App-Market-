import { useState } from 'react'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import { Link, useNavigate } from 'react-router-dom'
import api from '../lib/api'
import { setToken } from '../lib/auth'
import AuthLayout from '../components/layout/AuthLayout'

/*
  Pantalla Login
  Descripción: Formulario de acceso con validación mínima y accesibilidad.
  Acciones: Envía credenciales a `/auth/login` y guarda el token.
*/
export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [errors, setErrors] = useState({})
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const validate = () => {
    const e = {}
    if (!email) e.email = 'Ingresa tu correo'
    if (!password) e.password = 'Ingresa tu contraseña'
    setErrors(e)
    return Object.keys(e).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!validate()) return
    setLoading(true)
    try {
      const res = await api.post('/auth/login', { email, password })
      const token = res.data?.token || ''
      if (!token) throw new Error('Token ausente')
      setToken(token)
      navigate('/dashboard')
    } catch (err) {
      alert('Credenciales inválidas o error de conexión')
    } finally {
      setLoading(false)
    }
  }

  return (
    <AuthLayout title="Bienvenido" subtitle="Inicia sesión para continuar">
      <form className="space-y-6" onSubmit={handleSubmit}>
        <Input id="email" label="Correo" type="email" placeholder="Betty@example.com" value={email} onChange={(e)=>setEmail(e.target.value)} autoComplete="email" error={errors.email} />
        <Input id="password" label="Contraseña" type="password" placeholder="Tu contraseña" value={password} onChange={(e)=>setPassword(e.target.value)} autoComplete="current-password" error={errors.password} />
        <div className="flex items-center gap-3 justify-between">
          <Button type="submit" className="min-w-[180px]" disabled={loading}>{loading ? 'Iniciando…' : 'Iniciar sesión'}</Button>
          <Link to="/forgot" className="text-neon font-semibold">¿Olvidaste tu contraseña?</Link>
        </div>
        <p className="text-white/70 text-sm text-center">¿Aún no tienes cuenta? <Link to="/register" className="text-neon font-semibold">Regístrate</Link></p>
      </form>
    </AuthLayout>
  )
}