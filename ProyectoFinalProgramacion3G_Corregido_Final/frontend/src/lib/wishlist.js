const KEY = 'wishlist'

function read() {
  try {
    const raw = localStorage.getItem(KEY)
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

function write(items) {
  localStorage.setItem(KEY, JSON.stringify(items))
}

export function getWishlist() {
  return read()
}

export function getWishlistIds() {
  return read().map(i => String(i.id))
}

export function isWishlisted(productId) {
  return read().some(i => String(i.id) === String(productId))
}

export function addToWishlist(product) {
  const items = read()
  if (!items.some(i => String(i.id) === String(product.id))) {
    items.push({ id: product.id, name: product.name, price: Number(product.price), imageUrl: product.imageUrl, category: product.category })
    write(items)
  }
  return items
}

export function removeFromWishlist(productId) {
  const items = read().filter(i => String(i.id) !== String(productId))
  write(items)
  return items
}

export function toggleWishlist(product) {
  if (isWishlisted(product.id)) {
    return removeFromWishlist(product.id)
  }
  return addToWishlist(product)
}

export function clearWishlist() {
  write([])
  return []
}

export function getWishlistCount() {
  return read().length
}
/**
 * Utilidades de lista de deseos.
 * Persistencia en localStorage y helpers de UI.
 */
