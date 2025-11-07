import { useNavigate, useParams } from 'react-router-dom'
import Button from '../components/ui/Button'
import Toast from '../components/ui/Toast'
import Skeleton from '../components/ui/Skeleton'
import { sampleProducts } from '../lib/sampleProducts'
import { addToCart } from '../lib/cart'
import { fetchProductById, updateProduct, deleteProduct } from '../lib/products'
import Header from '../components/Header'
import ConfirmModal from '../components/ui/ConfirmModal'
import { useEffect, useState } from 'react'

export default function ProductDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [product, setProduct] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState({ name: '', price: '', category: 'tecnologia', imageUrl: '', status: 'ACTIVE' })
  const [toast, setToast] = useState({ message: '', type: 'info' })
  const [showConfirmDelete, setShowConfirmDelete] = useState(false)
  const [deleting, setDeleting] = useState(false)

  useEffect(() => {
    let mounted = true
    setLoading(true)
    fetchProductById(id)
      .then((data) => {
        if (mounted) {
          setProduct(data)
          setError(null)
        }
      })
      .catch((err) => {
        console.error('Error cargando producto', err)
        const fallback = sampleProducts.find(p => String(p.id) === String(id)) || sampleProducts[0]
        setProduct(fallback)
        setError(null)
      })
      .finally(() => mounted && setLoading(false))
    return () => { mounted = false }
  }, [id])

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
                    <p className="text-neutral-500 mt-1">Estado: {({ ACTIVE: 'Activo', INACTIVE: 'Inactivo', SOLD: 'Vendido' }[product.status]) || product.status || '—'}</p>
                    <p className="mt-4 text-3xl font-semibold text-primary">${Number(product.price)?.toFixed ? Number(product.price).toFixed(2) : product.price}</p>
                  </div>

                  <div className="mt-6 flex flex-wrap gap-3 border-t border-neutral-200 pt-6">
                    <Button variant="primary" to={'/chat'}>Abrir chat</Button>
                    <Button variant="success" onClick={() => { try { addToCart(product, 1); setToast({ message: 'Añadido al carrito', type: 'success' }); } catch(e) { setToast({ message: 'No se pudo añadir al carrito', type: 'error' }); } }}>Agregar al carrito</Button>
                    <Button variant="secondary">Agregar a favoritos</Button>
                    <Button variant="ghost" onClick={startEdit}>Editar</Button>
                    <Button variant="danger" onClick={() => setShowConfirmDelete(true)}>Eliminar</Button>
                  </div>
                </>
              ) : (
                <div className="space-y-4">
                  <h2 className="text-xl font-semibold">Editando Producto</h2>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Nombre</label>
                    <input value={form.name} onChange={(e)=>setForm({ ...form, name: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50" />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Precio</label>
                    <input type="number" value={form.price} onChange={(e)=>setForm({ ...form, price: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50" />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-neutral-600">Imagen (URL)</label>
                    <input value={form.imageUrl} onChange={(e)=>setForm({ ...form, imageUrl: e.target.value })} className="mt-1 w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary/50" />
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
