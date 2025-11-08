import Header from '../components/Header'
import Button from '../components/ui/Button'
import Toast from '../components/ui/Toast'
import { useEffect, useState } from 'react'
import { getWishlist, removeFromWishlist, clearWishlist } from '../lib/wishlist'
import ProductCard from '../components/ProductCard'

export default function Wishlist() {
  const [items, setItems] = useState([])
  const [toast, setToast] = useState({ message: '', type: 'info' })

  useEffect(() => {
    setItems(getWishlist())
  }, [])

  const onRemove = (id) => {
    const next = removeFromWishlist(id)
    setItems(next)
    setToast({ message: 'Quitado de favoritos', type: 'success' })
  }

  const onClear = () => {
    clearWishlist()
    setItems([])
    setToast({ message: 'Lista de deseos vaciada', type: 'success' })
  }

  return (
    <div className="min-h-screen bg-neutral-100 text-neutral-900">
      <Header />

      <main className="max-w-6xl mx-auto p-8">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold">Tus favoritos</h1>
          {items.length > 0 && (
            <Button variant="ghost" onClick={onClear}>Vaciar favoritos</Button>
          )}
        </div>
        {items.length === 0 ? (
          <p className="text-neutral-600 mt-6">Aún no tienes productos en tu lista de deseos.</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
            {items.map(item => (
              <div key={item.id}>
                <ProductCard product={item} />
                <div className="mt-2">
                  <Button variant="ghost" onClick={() => onRemove(item.id)}>Quitar</Button>
                </div>
              </div>
            ))}
          </div>
        )}

        {toast.message && (
          <div className="fixed bottom-6 right-6">
            <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: 'info' })} />
          </div>
        )}
      </main>
    </div>
  )
}
/**
 * Página de lista de deseos.
 * Muestra y permite gestionar favoritos.
 */
