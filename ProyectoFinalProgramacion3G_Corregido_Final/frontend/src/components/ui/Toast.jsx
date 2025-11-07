import { useEffect } from 'react'

export default function Toast({ message, type = 'info', onClose = () => {}, autoHide = 3000 }) {
  useEffect(() => {
    if (!message || !autoHide) return
    const t = setTimeout(() => onClose(), autoHide)
    return () => clearTimeout(t)
  }, [message, autoHide, onClose])

  if (!message) return null

  const styles = {
    info: 'bg-primary text-white',
    success: 'bg-success text-white',
    error: 'bg-danger text-white',
  }

  return (
    <div role="status" className={`fixed bottom-6 right-6 rounded-lg px-4 py-3 shadow-lg border border-neutral-200 ${styles[type]}`}>
      <span>{message}</span>
    </div>
  )
}
/**
 * Componente Toast.
 * Muestra mensajes de estado temporales.
 */
