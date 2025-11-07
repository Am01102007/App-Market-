/**
 * Componente de protección de rutas.
 * Verifica token y redirige a login si no existe.
 */
import { Navigate, useLocation } from 'react-router-dom'

export default function RequireAuth({ children }) {
  const token = localStorage.getItem('token')
  const location = useLocation()

  if (!token) {
    return <Navigate to="/login" replace state={{ from: location }} />
  }

  return children
}
