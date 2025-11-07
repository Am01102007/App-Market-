const KEY = 'cart'

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

export function getCart() {
  return read()
}

export function addToCart(product, quantity = 1) {
  const items = read()
  const idx = items.findIndex(i => String(i.id) === String(product.id))
  if (idx >= 0) {
    items[idx].quantity += quantity
  } else {
    items.push({ id: product.id, name: product.name, price: Number(product.price), imageUrl: product.imageUrl, quantity })
  }
  write(items)
  return items
}

export function updateQuantity(productId, quantity) {
  const items = read()
  const idx = items.findIndex(i => String(i.id) === String(productId))
  if (idx >= 0) {
    items[idx].quantity = Math.max(1, Number(quantity) || 1)
    write(items)
  }
  return items
}

export function removeFromCart(productId) {
  const items = read().filter(i => String(i.id) !== String(productId))
  write(items)
  return items
}

export function clearCart() {
  write([])
  return []
}

export function getCartTotals() {
  const items = read()
  const subtotal = items.reduce((sum, i) => sum + Number(i.price) * Number(i.quantity), 0)
  const totalItems = items.reduce((sum, i) => sum + Number(i.quantity), 0)
  return { subtotal, totalItems }
}

