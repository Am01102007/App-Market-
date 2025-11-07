import Header from '../components/Header'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import Toast from '../components/ui/Toast'
import ConfirmModal from '../components/ui/ConfirmModal'
import { useEffect, useState } from 'react'
import { getCart, updateQuantity, removeFromCart, clearCart, getCartTotals } from '../lib/cart'

export default function Cart() {
  const [items, setItems] = useState([])
  const [totals, setTotals] = useState({ subtotal: 0, totalItems: 0 })
  const [toast, setToast] = useState({ message: '', type: 'info' })
  const [confirmRemoveId, setConfirmRemoveId] = useState(null)
  const [confirmRemoveName, setConfirmRemoveName] = useState('')
  const [confirmClear, setConfirmClear] = useState(false)
  const [processing, setProcessing] = useState(false)

  const refresh = () => {
    const list = getCart()
    setItems(list)
    setTotals(getCartTotals())
  }

  useEffect(() => { refresh() }, [])

  const onQtyChange = (id, qty) => {
    updateQuantity(id, qty)
    refresh()
  }

  const onRemove = (id) => {
    const item = items.find(it => it.id === id)
    setConfirmRemoveId(id)
    setConfirmRemoveName(item?.name || '')
  }

  const onClear = () => {
    setConfirmClear(true)
  }

  const confirmRemove = () => {
    if (!confirmRemoveId) return
    try {
      setProcessing(true)
      setToast({ message: 'Eliminando del carrito...', type: 'info' })
      removeFromCart(confirmRemoveId)
      refresh()
      setToast({ message: 'Producto eliminado del carrito', type: 'success' })
    } catch (e) {
      setToast({ message: 'No se pudo eliminar del carrito', type: 'error' })
    } finally {
      setProcessing(false)
      setConfirmRemoveId(null)
      setConfirmRemoveName('')
    }
  }

  const confirmClearCart = () => {
    try {
      setProcessing(true)
      setToast({ message: 'Vaciando carrito...', type: 'info' })
      clearCart()
      refresh()
      setToast({ message: 'Carrito vaciado', type: 'success' })
    } catch (e) {
      setToast({ message: 'No se pudo vaciar el carrito', type: 'error' })
    } finally {
      setProcessing(false)
      setConfirmClear(false)
    }
  }

  return (
    <div className="min-h-screen bg-neutral-100 text-neutral-900">
      <Header />
      <main className="max-w-5xl mx-auto p-8">
        <h1 className="text-3xl font-bold mb-6">Carrito</h1>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="md:col-span-2 space-y-4">
            {items.length === 0 && (
              <div className="bg-white border border-neutral-200 rounded-lg p-6 shadow-soft">
                <p className="text-neutral-600">Tu carrito está vacío.</p>
              </div>
            )}
            {items.map((i) => (
              <div key={i.id} className="bg-white border border-neutral-200 rounded-lg p-4 shadow-soft flex items-center gap-4">
                <div className="w-20 h-20 bg-neutral-200/50 rounded-md overflow-hidden">
                  {i.imageUrl ? (
                    <img src={i.imageUrl} alt={i.name} className="w-full h-full object-cover" />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center text-neutral-500">Sin imagen</div>
                  )}
                </div>
                <div className="flex-grow">
                  <p className="font-semibold">{i.name}</p>
                  <p className="text-neutral-600">${Number(i.price).toFixed(2)}</p>
                </div>
                <div className="flex items-center gap-2">
                  <Input type="number" value={i.quantity} min={1} onChange={(e)=>onQtyChange(i.id, e.target.value)} className="w-20" />
                  <Button variant="ghost" onClick={()=>onRemove(i.id)}>Eliminar</Button>
                </div>
              </div>
            ))}
          </div>
          <div className="bg-white border border-neutral-200 rounded-lg p-6 shadow-soft">
            <h2 className="text-xl font-semibold mb-4">Resumen</h2>
            <p className="text-neutral-700">Ítems: {totals.totalItems}</p>
            <p className="text-neutral-700 mb-4">Subtotal: ${totals.subtotal.toFixed(2)}</p>
            <div className="flex flex-col gap-3">
              <Button variant="primary">Ir al pago</Button>
              <Button variant="ghost" onClick={onClear}>Vaciar carrito</Button>
            </div>
          </div>
        </div>
      </main>
      {toast.message && (
        <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: 'info' })} />
      )}

      <ConfirmModal
        open={!!confirmRemoveId}
        title="Eliminar del carrito"
        message={`¿Deseas eliminar "${confirmRemoveName}" del carrito?`}
        confirmText="Eliminar"
        cancelText="Cancelar"
        loading={processing}
        onConfirm={confirmRemove}
        onCancel={() => { setConfirmRemoveId(null); setConfirmRemoveName('') }}
      />

      <ConfirmModal
        open={confirmClear}
        title="Vaciar carrito"
        message="¿Deseas vaciar todo el carrito? Esta acción no se puede deshacer."
        confirmText="Vaciar"
        cancelText="Cancelar"
        loading={processing}
        onConfirm={confirmClearCart}
        onCancel={() => setConfirmClear(false)}
      />
    </div>
  )
}
