import { useNavigate } from 'react-router-dom'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import { useState, useEffect } from 'react'
import Toast from '../components/ui/Toast'
import { createProduct, fetchCategories, createCategory } from '../lib/products'
import { getUsername } from '../lib/auth'
import Header from '../components/Header'

export default function Publish() {
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [price, setPrice] = useState('')
  const [category, setCategory] = useState('')
  const [imageUrl, setImageUrl] = useState('')
  const [imageFile, setImageFile] = useState(null)
  const [imageType, setImageType] = useState('url') // 'url' o 'file'
  const [description, setDescription] = useState('')
  const [saving, setSaving] = useState(false)
  const [toast, setToast] = useState({ message: '', type: 'info' })
  const [categories, setCategories] = useState([])
  const [newCategoryName, setNewCategoryName] = useState('')
  const [showNewCategoryInput, setShowNewCategoryInput] = useState(false)
  const [creatingCategory, setCreatingCategory] = useState(false)

  useEffect(() => {
    loadCategories()
  }, [])

  const loadCategories = async () => {
    try {
      const data = await fetchCategories()
      setCategories(data || [])
      if (data && data.length > 0) {
        setCategory(data[0].name)
      }
    } catch (err) {
      console.error('Error cargando categorías', err)
      setToast({ message: 'Error cargando categorías', type: 'error' })
    }
  }

  const handleCreateCategory = async () => {
    if (!newCategoryName.trim()) {
      setToast({ message: 'Ingresa un nombre para la categoría', type: 'error' })
      return
    }

    setCreatingCategory(true)
    try {
      const newCategory = {
        name: newCategoryName.trim(),
        description: `Categoría ${newCategoryName.trim()}`,
        imageUrl: ''
      }
      
      await createCategory(newCategory)
      await loadCategories() // Recargar categorías
      setCategory(newCategoryName.trim())
      setNewCategoryName('')
      setShowNewCategoryInput(false)
      setToast({ message: 'Categoría creada exitosamente', type: 'success' })
    } catch (err) {
      console.error('Error creando categoría', err)
      setToast({ message: 'Error al crear la categoría', type: 'error' })
    } finally {
      setCreatingCategory(false)
    }
  }

  const handleImageFileChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setImageFile(file)
      // Crear URL temporal para preview
      const tempUrl = URL.createObjectURL(file)
      setImageUrl(tempUrl)
    }
  }

  const onSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      const username = getUsername()
      
      let finalImageUrl = imageUrl
      
      // Si se seleccionó un archivo, por ahora usamos la URL temporal
      // En una implementación completa, aquí subirías el archivo al servidor
      if (imageType === 'file' && imageFile) {
        // TODO: Implementar subida de archivo al servidor
        finalImageUrl = imageUrl // Por ahora usa la URL temporal
      }

      const product = {
        name,
        price: Number(price),
        category: { name: category }, // Enviar como objeto con nombre
        imageUrl: finalImageUrl,
        description,
        status: 'ACTIVE',
      }
      
      const created = await createProduct(product, username)
      setToast({ message: 'Producto creado correctamente', type: 'success' })
      if (created?.id) {
        setTimeout(() => navigate(`/product/${created.id}`), 800)
      }
    } catch (err) {
      console.error('Error creando producto', err)
      setToast({ message: 'No se pudo crear el producto', type: 'error' })
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="min-h-screen bg-neutral-100 text-neutral-900">
      <Header />

      <main className="max-w-2xl mx-auto p-8">
        <div className="bg-white border border-neutral-200 rounded-lg shadow-sm p-8">
          <h1 className="text-3xl font-bold mb-6 text-center">Publicar un Nuevo Producto</h1>
          <form onSubmit={onSubmit} className="space-y-5">
            <Input 
              id="name" 
              label="Nombre del Producto" 
              placeholder="Ej: Laptop Gamer XYZ" 
              value={name} 
              onChange={(e) => setName(e.target.value)} 
              required
            />
            
            <Input 
              id="price" 
              label="Precio" 
              type="number" 
              step="0.01"
              placeholder="0.00" 
              value={price} 
              onChange={(e) => setPrice(e.target.value)} 
              required
            />

            {/* Sección de imagen mejorada */}
            <div className="space-y-3">
              <label className="text-sm font-medium text-neutral-700">Imagen del Producto</label>
              
              {/* Selector de tipo de imagen */}
              <div className="flex gap-4 mb-3">
                <label className="flex items-center gap-2">
                  <input
                    type="radio"
                    name="imageType"
                    value="url"
                    checked={imageType === 'url'}
                    onChange={(e) => setImageType(e.target.value)}
                    className="text-blue-500"
                  />
                  <span className="text-sm">URL de imagen</span>
                </label>
                <label className="flex items-center gap-2">
                  <input
                    type="radio"
                    name="imageType"
                    value="file"
                    checked={imageType === 'file'}
                    onChange={(e) => setImageType(e.target.value)}
                    className="text-blue-500"
                  />
                  <span className="text-sm">Subir archivo</span>
                </label>
              </div>

              {/* Input según el tipo seleccionado */}
              {imageType === 'url' ? (
                <Input 
                  id="imageUrl" 
                  placeholder="https://ejemplo.com/imagen.jpg" 
                  value={imageUrl} 
                  onChange={(e) => setImageUrl(e.target.value)} 
                />
              ) : (
                <div className="space-y-2">
                  <input
                    type="file"
                    id="imageFile"
                    accept="image/*"
                    onChange={handleImageFileChange}
                    className="w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  {imageFile && (
                    <p className="text-sm text-neutral-600">
                      Archivo seleccionado: {imageFile.name}
                    </p>
                  )}
                </div>
              )}

              {/* Preview de imagen */}
              {imageUrl && (
                <div className="mt-2">
                  <img 
                    src={imageUrl} 
                    alt="Preview" 
                    className="w-32 h-32 object-cover rounded-md border border-neutral-300"
                    onError={(e) => {
                      e.target.style.display = 'none'
                    }}
                  />
                </div>
              )}
            </div>

            {/* Sección de categoría mejorada */}
            <div className="space-y-3">
              <label className="text-sm font-medium text-neutral-700" htmlFor="category">Categoría</label>
              
              <select 
                id="category" 
                value={category} 
                onChange={(e) => setCategory(e.target.value)} 
                className="w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              >
                <option value="">Selecciona una categoría</option>
                {categories.map((cat) => (
                  <option key={cat.id} value={cat.name}>
                    {cat.name}
                  </option>
                ))}
              </select>

              {/* Opción para crear nueva categoría */}
              <div className="flex items-center gap-2">
                <button
                  type="button"
                  onClick={() => setShowNewCategoryInput(!showNewCategoryInput)}
                  className="text-sm text-blue-600 hover:text-blue-800 underline"
                >
                  {showNewCategoryInput ? 'Cancelar' : '+ Crear nueva categoría'}
                </button>
              </div>

              {/* Input para nueva categoría */}
              {showNewCategoryInput && (
                <div className="flex gap-2 mt-2">
                  <input
                    type="text"
                    placeholder="Nombre de la nueva categoría"
                    value={newCategoryName}
                    onChange={(e) => setNewCategoryName(e.target.value)}
                    className="flex-1 px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <Button
                    type="button"
                    variant="primary"
                    onClick={handleCreateCategory}
                    disabled={creatingCategory || !newCategoryName.trim()}
                  >
                    {creatingCategory ? 'Creando...' : 'Crear'}
                  </Button>
                </div>
              )}
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium text-neutral-700" htmlFor="description">Descripción</label>
              <textarea 
                id="description" 
                value={description} 
                onChange={(e) => setDescription(e.target.value)} 
                rows={4} 
                className="w-full px-3 py-2 rounded-md bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-blue-500" 
                placeholder="Describe tu producto en detalle"
                required
              />
            </div>

            <div className="flex gap-3 pt-2">
              <Button variant="success" type="submit" disabled={saving}>
                {saving ? 'Publicando...' : 'Publicar'}
              </Button>
              <Button variant="secondary" type="button" onClick={() => navigate(-1)}>
                Cancelar
              </Button>
            </div>
          </form>
        </div>

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
 * Publicación de producto.
 * Permite crear y enviar nuevos productos.
 */
