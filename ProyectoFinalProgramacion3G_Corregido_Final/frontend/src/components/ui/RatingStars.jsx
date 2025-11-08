/*
  Componente RatingStars
  Muestra estrellas de valoración con soporte de medias (0.0–5.0) y texto opcional.
  Props: { rating, count, className }
*/
export default function RatingStars({ rating = 0, count = 0, className = '' }) {
  const safe = Math.max(0, Math.min(5, Number(rating) || 0))
  const full = Math.floor(safe)
  const half = safe - full >= 0.5
  const empty = 5 - full - (half ? 1 : 0)

  const Star = ({ type = 'full' }) => {
    const styles = {
      full: 'text-amber-500',
      half: 'text-amber-500',
      empty: 'text-neutral-300',
    }
    return (
      <span className={`${styles[type]} text-sm`} aria-hidden>
        {type === 'half' ? '★' : '★'}
      </span>
    )
  }

  return (
    <div className={`flex items-center gap-1 ${className}`} aria-label={`Valoración ${safe} de 5`}>
      {Array.from({ length: full }).map((_, i) => <Star key={`f-${i}`} type="full" />)}
      {half && <Star type="half" />}
      {Array.from({ length: empty }).map((_, i) => <Star key={`e-${i}`} type="empty" />)}
      <span className="ml-1 text-xs text-neutral-600">
        {count > 0 ? `${count.toLocaleString()} calificaciones` : 'Sin calificaciones'}
      </span>
    </div>
  )
}
/**
 * Estrellas de rating.
 * Reutilizable para tarjetas y detalle.
 */
