/*
  Pantalla Recuperar Contraseña
  Descripción: Solicita correo y envía petición a `/auth/forgot-password`.
  Incluye validación básica, mensajes de estado y accesibilidad.
*/
import { useState } from 'react'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import { Link } from 'react-router-dom'
import AuthLayout from '../components/layout/AuthLayout'
import api from '../lib/api'

export default function ForgotPassword(){
  const [email, setEmail] = useState('')
  const [errors, setErrors] = useState({})
  const [sent, setSent] = useState(false)

  const validate = () => {
    const e = {}
    if (!email) e.email = 'Ingresa tu correo'
    setErrors(e)
    return Object.keys(e).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!validate()) return
    try {
      await api.post('/auth/forgot-password', { email })
      setSent(true)
    } catch (err) {
      alert('No se pudo enviar el enlace')
    }
  }

  return (
    <AuthLayout title="Recuperar contraseña" subtitle="Te enviaremos un enlace de recuperación">
      <form className="space-y-6" onSubmit={handleSubmit}>
        <Input id="email" label="Correo" type="email" placeholder="correo@ejemplo.com" value={email} onChange={(e)=>setEmail(e.target.value)} autoComplete="email" error={errors.email} />
        <div className="flex items-center gap-4 justify-between">
          <Button type="submit">Enviar enlace</Button>
          <Link to="/login" className="text-neon font-semibold">Volver a iniciar sesión</Link>
        </div>
        {sent && <p className="text-white/80 text-sm">Si el correo existe, te enviaremos instrucciones.</p>}
      </form>
    </AuthLayout>
  )
}