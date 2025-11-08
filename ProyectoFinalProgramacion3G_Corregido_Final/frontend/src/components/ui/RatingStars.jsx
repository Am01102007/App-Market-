/*
  Componente RatingStars
  Muestra estrellas de valoración con soporte de medias (0.0–5.0) y texto opcional.
  Props: { rating, count, className }
*/
export default function RatingStars({ rating = 0, count = 0, className = '', onRate }) {
  const safe = Math.max(0, Math.min(5, Number(rating) || 0))
  const full = Math.floor(safe)
  const half = safe - full >= 0.5
  const empty = 5 - full - (half ? 1 : 0)

  const clickable = typeof onRate === 'function'

  const Star = ({ type = 'full', idx = 1 }) => {
    const styles = {
      full: 'text-amber-500',
      half: 'text-amber-500',
      empty: 'text-neutral-300',
    }
    const base = `${styles[type]} text-sm` 
    const interactive = clickable ? ' cursor-pointer hover:scale-110 transition-transform' : ''
    return (
      <button
        type="button"
        className={base + interactive}
        aria-label={`Calificar con ${idx} estrella${idx>1?'s':''}`}
        onClick={clickable ? () => onRate(idx) : undefined}
      >
        {type === 'half' ? '★' : '★'}
      </button>
    )
  }

  return (
    <div className={`flex items-center gap-1 ${className}`} aria-label={`Valoración ${safe} de 5`}>
      {Array.from({ length: full }).map((_, i) => <Star key={`f-${i}`} type="full" idx={i+1} />)}
      {half && <Star type="half" idx={full+1} />}
      {Array.from({ length: empty }).map((_, i) => <Star key={`e-${i}`} type="empty" idx={full + (half?1:0) + i + 1} />)}
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
