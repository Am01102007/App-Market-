const KEY = 'ratings:v1'

function read() {
  try {
    const raw = localStorage.getItem(KEY)
    return raw ? JSON.parse(raw) : {}
  } catch {
    return {}
  }
}

function write(obj) {
  localStorage.setItem(KEY, JSON.stringify(obj))
}

export function getRating(productId) {
  const data = read()
  const entry = data[String(productId)]
  if (!entry) return { avg: 0, count: 0 }
  return entry
}

export function submitRating(productId, stars) {
  const s = Math.max(1, Math.min(5, Number(stars) || 5))
  const data = read()
  const entry = data[String(productId)] || { avg: 0, count: 0 }
  const nextCount = entry.count + 1
  const nextAvg = ((entry.avg * entry.count) + s) / nextCount
  data[String(productId)] = { avg: Number(nextAvg.toFixed(2)), count: nextCount }
  write(data)
  return data[String(productId)]
}
/**
 * Utilidades de calificación local (offline) por producto.
 */
