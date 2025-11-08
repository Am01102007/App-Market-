import { useNavigate, useParams, useLocation } from 'react-router-dom'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import QuantityStepper from '../components/ui/QuantityStepper'
import RatingStars from '../components/ui/RatingStars'
import Toast from '../components/ui/Toast'
import Skeleton from '../components/ui/Skeleton'
// Eliminamos datos de muestra; el detalle debe venir solo del backend
import { addToCart, getLastQty, setLastQty } from '../lib/cart'
import { fetchProductById, updateProduct, deleteProduct, fetchProductsByCategory, fetchProductAvailability, fetchProductRatingSummary, submitProductRating, fetchMyProductRating } from '../lib/products'
import Header from '../components/Header'
import ConfirmModal from '../components/ui/ConfirmModal'
import { useEffect, useState } from 'react'
import { isWishlisted, toggleWishlist } from '../lib/wishlist'
import RelatedCarousel from '../components/RelatedCarousel'
import { getUsername } from '../lib/auth'
import { getRating as getLocalRating, submitRating as submitLocalRating } from '../lib/ratings'

export default function ProductDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const location = useLocation()
  const fromChat = location?.state?.from === 'chat'
  const [product, setProduct] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState({ name: '', price: '', category: 'tecnologia', imageUrl: '', status: 'ACTIVE' })
  const [toast, setToast] = useState({ message: '', type: 'info' })
  const [showConfirmDelete, setShowConfirmDelete] = useState(false)
  const [deleting, setDeleting] = useState(false)
  const [qty, setQty] = useState(1)
  const [wish, setWish] = useState(false)
  const [related, setRelated] = useState([])
  const [ratingSummary, setRatingSummary] = useState({ average: 0, count: 0 })
  const [availability, setAvailability] = useState({ availableQuantity: 0, available: false, status: 'INACTIVE' })

  useEffect(() => {
    let mounted = true
    setLoading(true)
    fetchProductById(id)
      .then((data) => {
        if (mounted) {
          setProduct(data)
          setError(null)
          setWish(isWishlisted(id))
          // Calificaciones: intentar backend, fallback local
          fetchProductRatingSummary(id)
            .then((summary) => setRatingSummary(summary))
            .catch(() => {
              const lr = getLocalRating(id)
              setRatingSummary({ average: lr.avg || 0, count: lr.count || 0 })
            })

          // Disponibilidad: unidades y bandera disponible
          fetchProductAvailability(id)
            .then((data) => setAvailability(data))
            .catch(() => setAvailability({ availableQuantity: Number(data?.availableQuantity) || 0, available: (data?.availableQuantity || 0) > 0, status: data?.status || (data?.availableQuantity > 0 ? 'AVAILABLE' : 'INACTIVE') }))
        }
      })
      .catch((err) => {
        console.error('Error cargando producto', err)
        setProduct(null)
        setError('No se pudo cargar el producto. Intenta nuevamente.')
      })
      .finally(() => mounted && setLoading(false))
    return () => { mounted = false }
  }, [id])

  useEffect(() => {
    try {
      const initial = getLastQty(id)
      setQty(initial)
    } catch {}
  }, [id])

  // Ajustar cantidad al límite de stock cuando cambia disponibilidad
  useEffect(() => {
    const maxQty = Math.max(1, availability?.availableQuantity || 1)
    setQty((prev) => Math.min(Math.max(1, Number(prev) || 1), maxQty))
  }, [availability])

  useEffect(() => {
    if (!product) return
    const categoryName = typeof product.category === 'object' && product.category?.name ? product.category.name : product.category
    fetchProductsByCategory(categoryName)
      .then((data) => {
        const rel = (data || []).filter(p => String(p.id) !== String(id)).slice(0, 12)
        setRelated(rel)
      })
      .catch(() => {
        setRelated([])
      })
  }, [product, id])

  const startEdit = () => {
    if (!product) return
    setForm({
      name: product.name || '',
      price: product.price || '',
      category: product.category || 'tecnologia',
      imageUrl: product.imageUrl || '',
      status: product.status || 'ACTIVE',
    })
    setEditing(true)
  }

  const cancelEdit = () => {
    setEditing(false)
  }

  const saveEdit = async () => {
    try {
      const updated = await updateProduct(id, {
        ...form,
        price: Number(form.price)
      })
      setProduct(updated)
      setEditing(false)
      setToast({ message: 'Producto actualizado', type: 'success' })
    } catch (err) {
      console.error('Error actualizando producto', err)
      setToast({ message: 'No se pudo actualizar', type: 'error' })
    }
  }

  const removeProduct = async () => {
    try {
      setDeleting(true)
      setToast({ message: 'Eliminando producto...', type: 'info' })
      await deleteProduct(id)
      setToast({ message: 'Producto eliminado', type: 'success' })
      setShowConfirmDelete(false)
      setTimeout(() => navigate('/catalog'), 800)
    } catch (err) {
      console.error('Error eliminando producto', err)
      setToast({ message: 'No se pudo eliminar', type: 'error' })
    } finally {
      setDeleting(false)
    }
  }

  return (
    <div className="min-h-screen bg-neutral-100 text-neutral-900">
      <Header showBack />

      <main className="max-w-5xl mx-auto p-8">
        {fromChat && (
          <div className="mb-4">
            <div className="flex gap-2">
              <Button variant="outlineLight" onClick={() => navigate('/chat')}>Regresar al chat</Button>
              <Button
                variant="outlineLight"
                onClick={() => {
                  if (!window.confirm('¿Seguro que deseas reiniciar la conversación?')) return
                  navigate('/chat', { state: { resetConversation: true } })
                }}
              >
                Reiniciar conversación
              </Button>
            </div>
          </div>
        )}
        {loading && (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="bg-white border border-neutral-200 rounded-xl p-5 shadow-sm">
              <Skeleton className="h-80 w-full rounded-lg" />
            </div>
            <div className="bg-white border border-neutral-200 rounded-xl p-6 shadow-sm flex flex-col">
              <div className="space-y-4 flex-grow">
                <Skeleton className="h-4 w-1/4" />
                <Skeleton className="h-8 w-3/4" />
                <Skeleton className="h-4 w-1/3" />
                <Skeleton className="h-8 w-1/4 mt-4" />
              </div>
              <div className="flex flex-wrap gap-3 pt-6 border-t border-neutral-200 mt-6">
                <Skeleton className="h-10 w-28" />
                <Skeleton className="h-10 w-40" />
              </div>
            </div>
          </div>
        )}
        {error && (<div className="text-danger text-center py-10">{error}</div>)}
        {!loading && !error && product && (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="bg-white border border-neutral-200 rounded-xl p-5 shadow-sm">
              <div className="h-80 w-full rounded-lg bg-neutral-200/50 border border-neutral-200 overflow-hidden">
                {product.imageUrl ? (
                  <img 
                    src={product.imageUrl} 
                    alt={product.name} 
                    className="w-full h-full object-cover"
                    onError={(e) => {
                      e.target.style.display = 'none';
                      e.target.nextSibling.style.display = 'flex';
                    }}
                  />
                ) : null}
                <div 
                  className="w-full h-full flex items-center justify-center text-neutral-500"
                  style={{ display: product.imageUrl ? 'none' : 'flex' }}
                >
                  Sin imagen
                </div>
              </div>
            </div>

            <div className="bg-white border border-neutral-200 rounded-xl p-6 shadow-sm flex flex-col">
              {!editing ? (
                <>
                  <div className="flex-grow">
                    <p className="text-sm text-neutral-500 uppercase tracking-wider">{typeof product.category === 'object' && product.category?.name ? product.category.name : product.category || '—'}</p>
                    <h1 className="text-3xl font-bold mt-1">{product.name}</h1>
                    <RatingStars 
                      rating={ratingSummary.average || product.rating} 
                      count={ratingSummary.count || product.reviewsCount} 
                      className="mt-1" 
                      onRate={(stars) => {
                        const username = getUsername()
                        submitProductRating(id, stars, username)
                          .then((summary) => { setRatingSummary(summary); setToast({ message: `¡Gracias! Calificaste con ${stars}★`, type: 'success' }) })
                          .catch(() => {
                            const next = submitLocalRating(id, stars)
                            setRatingSummary({ average: next.avg, count: next.count })
                            setToast({ message: `Guardado localmente: ${stars}★`, type: 'info' })
                          })
                      }}
                    />
                    <p className="text-neutral-500 mt-1">Estado: {({ ACTIVE: 'Activo', INACTIVE: 'Inactivo', SOLD: 'Vendido' }[product.status]) || product.status || '—'}</p>
                    <div className="mt-4">
                      <p className="text-3xl font-semibold text-primary">${Number(product.price)?.toFixed ? Number(product.price).toFixed(2) : product.price}</p>
                      {Number(product.price) >= 50 && (
                        <p className="text-sm text-neon font-semibold mt-1">Envío gratis en este producto</p>
                      )}
                    </div>
                    <p className="text-sm text-neutral-700 mt-3">{product.description || 'Sin descripción.'}</p>
                  </div>

                  <div className="mt-6 border-t border-neutral-200 pt-6">
                    <div className="p-4 rounded-lg bg-neutral-50 border border-neutral-200">
                      <p className={availability?.available ? 'text-success font-semibold' : 'text-danger font-semibold'}>
                        {availability?.available ? 'En stock' : 'Agotado'}
                      </p>
                      {availability?.available && (
                        <p className="text-xs text-neutral-600">Unidades disponibles: {availability.availableQuantity}</p>
                      )}
                      <div className="mt-3 flex flex-wrap items-center gap-3">
                        <QuantityStepper
                          value={qty}
                          min={1}
                          max={Math.max(1, availability?.availableQuantity || 1)}
                          onChange={(v) => { setQty(v); setLastQty(id, v) }}
                        />
                        <Button disabled={!availability?.available} onClick={() => {
                          const raw = Math.max(1, Number(qty) || 1)
                          const limit = availability?.availableQuantity || 0
                          if (limit <= 0) { setToast({ message: 'Sin unidades disponibles', type: 'error' }); return }
                          const q = Math.min(raw, limit)
                          if (q < raw) { setToast({ message: 'Cantidad ajustada al stock disponible', type: 'info' }) }
                          setLastQty(id, q)
                          try { addToCart(product, q); setToast({ message: `Añadido al carrito: ${product.name} ×${q}`, type: 'success' }); } catch(e) { setToast({ message: 'No se pudo añadir al carrito', type: 'error' }); }
                        }}>Añadir al carrito</Button>
                        <Button variant="secondary">Comprar ahora</Button>
                        <Button variant="ghost" onClick={() => { toggleWishlist(product); const next = !wish; setWish(next); setToast({ message: next ? 'Añadido a favoritos' : 'Quitado de favoritos', type: 'success' }) }}>
                          {wish ? '❤️ Favorito' : '🤍 Guardar'}
                        </Button>
                      </div>
                      <p className="mt-3 text-xs text-neutral-600">Entregado por AppMarket. Devoluciones gratis dentro de 30 días.</p>
                    </div>

                    <div className="mt-6 flex flex-wrap gap-3">
                      <Button variant="primary" to={'/chat'}>Abrir chat</Button>
                      <Button variant="ghost" onClick={startEdit}>Editar</Button>
                      <Button variant="danger" onClick={() => setShowConfirmDelete(true)}>Eliminar</Button>
                    </div>
                  </div>
                </>
              ) : (
                <div className="space-y-4">
                  <h2 className="text-xl font-semibold">Editando Producto</h2>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Nombre</label>
                    <input placeholder="Ej. Laptop gamer 15 pulgadas" value={form.name} onChange={(e)=>setForm({ ...form, name: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50" />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Precio</label>
                    <input placeholder="Ej. 1499.99" type="number" value={form.price} onChange={(e)=>setForm({ ...form, price: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50" />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Imagen (URL)</label>
                    <input placeholder="URL completa de la imagen (https://...)" value={form.imageUrl} onChange={(e)=>setForm({ ...form, imageUrl: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50" />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Categoría</label>
                    <select value={form.category} onChange={(e)=>setForm({ ...form, category: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50">
                      <option value="tecnologia">Tecnología</option>
                      <option value="hogar">Hogar</option>
                      <option value="moda">Moda</option>
                    </select>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Estado</label>
                    <select value={form.status} onChange={(e)=>setForm({ ...form, status: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50">
                      <option value="ACTIVE">Activo</option>
                      <option value="INACTIVE">Inactivo</option>
                    </select>
                  </div>

                  <div className="mt-6 flex gap-3 pt-2">
                    <Button variant="success" onClick={saveEdit}>Guardar</Button>
                    <Button variant="ghost" onClick={cancelEdit}>Cancelar</Button>
                  </div>
                </div>
              )}
            </div>
          </div>
        )}

        {toast.message && (
          <div className="fixed bottom-6 right-6">
            <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: 'info' })} />
          </div>
        )}

        {/* Carrusel de relacionados */}
        {!loading && related?.length > 0 && (
          <RelatedCarousel products={related} />
        )}

        <ConfirmModal
          open={showConfirmDelete}
          title="Eliminar producto"
          message={`¿Deseas eliminar "${product?.name || ''}"? Esta acción no se puede deshacer.`}
          confirmText="Eliminar"
          cancelText="Cancelar"
          loading={deleting}
          onConfirm={removeProduct}
          onCancel={() => setShowConfirmDelete(false)}
        />
      </main>
    </div>
  )
}
/**
 * Detalle de producto.
 * Muestra información completa, comentarios y acciones.
 */
