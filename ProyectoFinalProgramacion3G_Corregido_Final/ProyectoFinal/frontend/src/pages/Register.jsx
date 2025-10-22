import { useState } from 'react'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import { Link } from 'react-router-dom'
import api from '../lib/api'
import AuthLayout from '../components/layout/AuthLayout'

/*
  Pantalla Registro
  Descripción: Formulario de creación de cuenta con validaciones básicas.
  Acciones: Envía datos a `/auth/register`.
*/
export default function Register() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [errors, setErrors] = useState({})

  const validate = () => {
    const e = {}
    if (!name) e.name = 'Ingresa tu nombre'
    if (!email) e.email = 'Ingresa tu correo'
    if (!password || password.length < 8) e.password = 'Mínimo 8 caracteres'
    setErrors(e)
    return Object.keys(e).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!validate()) return
    try {
      await api.post('/auth/register', { name, email, password })
      alert('Registro exitoso, ahora inicia sesión')
    } catch (err) {
      alert('No se pudo registrar')
    }
  }

  return (
    <AuthLayout title="Crear cuenta" subtitle="Configura tu perfil para empezar">
      <form className="space-y-6" onSubmit={handleSubmit}>
        <Input id="name" label="Nombre" placeholder="Tu nombre" value={name} onChange={(e)=>setName(e.target.value)} autoComplete="name" error={errors.name} />
        <Input id="email" label="Correo" type="email" placeholder="correo@ejemplo.com" value={email} onChange={(e)=>setEmail(e.target.value)} autoComplete="email" error={errors.email} />
        <Input id="password" label="Contraseña" type="password" placeholder="••••••••" value={password} onChange={(e)=>setPassword(e.target.value)} autoComplete="new-password" error={errors.password} />
        <div className="flex items-center gap-4 justify-between">
          <Button type="submit">Registrarme</Button>
          <Link to="/login" className="text-neon font-semibold">Ya tengo cuenta</Link>
        </div>
      </form>
    </AuthLayout>
  )
}