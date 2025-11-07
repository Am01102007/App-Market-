import { Link, useSearchParams } from 'react-router-dom'
import { useEffect, useState } from 'react'
import BrandLogo from '../components/BrandLogo'
import Button from '../components/ui/Button'
import ProductCard from '../components/ProductCard'
import Skeleton from '../components/ui/Skeleton'
import { fetchActiveProducts, searchProducts } from '../lib/products'
import { sampleProducts } from '../lib/sampleProducts'
import Header from '../components/Header'
import Input from '../components/ui/Input'

/**
 * Componente de página de catálogo de productos.
 * Permite buscar, filtrar y ordenar productos con funcionalidad completa de navegación.
 * Incluye filtros por categoría, rango de precios, búsqueda por texto y ordenamiento.
 * 
 * @component
 * @returns {JSX.Element} Página de catálogo con productos filtrados y ordenados
 */
export default function Catalog() {
  const [searchParams, setSearchParams] = useSearchParams()
  // Estados para productos, carga y manejo de errores
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  // Extracción de parámetros de búsqueda de la URL
  const query = searchParams.get('q') || ''
  const sort = searchParams.get('sort') || 'relevance'
  const category = searchParams.get('category') || 'all'
  const priceMinParam = searchParams.get('price_min') || ''
  const priceMaxParam = searchParams.get('price_max') || ''

  // Efecto para cargar y filtrar productos basado en parámetros de búsqueda
  useEffect(() => {
    let mounted = true
    setLoading(true)
    
    // Si hay un término de búsqueda, usar el endpoint de búsqueda
    const fetchFunction = query ? () => searchProducts(query) : fetchActiveProducts
    
    fetchFunction()
      .then((data) => {
        if (mounted) {
          let list = Array.isArray(data) ? data : []
          
          // Si no hay término de búsqueda, aplicar filtros locales
          if (!query) {
            // Filtro por categoría
            if (category !== 'all') {
              list = list.filter(p => (p.category?.name || '').toLowerCase() === category.toLowerCase())
            }
          } else {
            // Si hay búsqueda, también filtrar por categoría si se especifica
            if (category !== 'all') {
              list = list.filter(p => (p.category?.name || '').toLowerCase() === category.toLowerCase())
            }
          }
          
          // Filtros por rango de precios
          const min = priceMinParam ? Number(priceMinParam) : null
          const max = priceMaxParam ? Number(priceMaxParam) : null
          if (min != null) {
            list = list.filter(p => Number(p.price) >= min)
          }
          if (max != null) {
            list = list.filter(p => Number(p.price) <= max)
          }
          
          // Ordenamiento de productos
          if (sort === 'price_asc') {
            list = [...list].sort((a,b) => Number(a.price) - Number(b.price))
          } else if (sort === 'price_desc') {
            list = [...list].sort((a,b) => Number(b.price) - Number(a.price))
          } else if (sort === 'name_asc') {
            list = [...list].sort((a,b) => (a.name||'').localeCompare(b.name||''))
          }
          
          setProducts(list)
          setError(null)
        }
      })
      .catch((err) => {
        console.error('Error cargando productos', err)
        // Fallback a datos de muestra si el backend no está disponible
        setProducts(sampleProducts)
        setError(null)
      })
      .finally(() => mounted && setLoading(false))
    return () => { mounted = false }
  }, [query, sort, category, priceMinParam, priceMaxParam])

  /**
   * Maneja cambios en el campo de búsqueda.
   * Actualiza los parámetros de URL con el nuevo término de búsqueda.
   * 
   * @param {Event} e - Evento del input de búsqueda
   */
  const onSearchChange = (e) => {
    const next = new URLSearchParams(searchParams)
    next.set('q', e.target.value)
    setSearchParams(next)
  }

  /**
   * Establece el criterio de ordenamiento.
   * Actualiza los parámetros de URL con el nuevo criterio de ordenamiento.
   * 
   * @param {string} value - Criterio de ordenamiento (relevance, price_asc, price_desc, name_asc)
   */
  const setSort = (value) => {
    const next = new URLSearchParams(searchParams)
    next.set('sort', value)
    setSearchParams(next)
  }

  /**
   * Establece el filtro de categoría.
   * Actualiza los parámetros de URL con la nueva categoría seleccionada.
   * 
   * @param {string} value - Categoría seleccionada ('all' para todas las categorías)
   */
  const setCategoryParam = (value) => {
    const next = new URLSearchParams(searchParams)
    if (value === 'all') next.delete('category')
    else next.set('category', value)
    setSearchParams(next)
  }

  /**
   * Aplica filtros de rango de precios.
   * Procesa el formulario de filtros de precio y actualiza los parámetros de URL.
   * 
   * @param {Event} e - Evento del formulario de filtros de precio
   */
  const applyPriceFilter = (e) => {
    e.preventDefault()
    const form = new FormData(e.currentTarget)
    const min = form.get('min')
    const max = form.get('max')
    const next = new URLSearchParams(searchParams)
    if (min) next.set('price_min', min); else next.delete('price_min')
    if (max) next.set('price_max', max); else next.delete('price_max')
    setSearchParams(next)
  }

  return (
    <div className="min-h-screen bg-neutral-100 text-neutral-900">
      <Header />

      <main className="max-w-7xl mx-auto p-8">
        {/* Encabezado con título y búsqueda */}
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold">Catálogo</h1>
          <div className="flex items-center gap-3">
            <Input
              name="q"
              value={query}
              onChange={onSearchChange}
              placeholder="Buscar por nombre o categoría"
              className="w-72"
            />
          </div>
        </div>

        {/* Panel de filtros */}
        <div className="bg-white border border-neutral-200 rounded-lg p-4 mb-6">
          {/* Filtros de categoría */}
          <div className="flex flex-wrap items-center gap-2 mb-4">
            {['all','tecnologia','hogar','moda'].map((cat) => (
              <button 
                key={cat} 
                onClick={() => setCategoryParam(cat)} 
                className={`px-3 py-1 rounded-full border ${
                  category===cat
                    ? 'bg-primary text-white border-primary'
                    : 'bg-white text-neutral-700 border-neutral-300 hover:bg-neutral-100'
                }`}
              >
                {cat === 'all' ? 'Todos' : cat[0].toUpperCase()+cat.slice(1)}
              </button>
            ))}
          </div>
          
          {/* Filtros de precio */}
          <form onSubmit={applyPriceFilter} className="flex items-end gap-3">
            <Input name="min" defaultValue={priceMinParam} type="number" label="Precio mínimo" />
            <Input name="max" defaultValue={priceMaxParam} type="number" label="Precio máximo" />
            <Button type="submit" variant="secondary">Aplicar</Button>
            <Button 
              type="button" 
              variant="ghost" 
              onClick={() => { 
                const next = new URLSearchParams(searchParams); 
                next.delete('price_min'); 
                next.delete('price_max'); 
                setSearchParams(next); 
              }}
            >
              Limpiar
            </Button>
          </form>
        </div>

        {/* Selector de ordenamiento */}
        <div className="flex items-center gap-3 mb-6">
          <span className="text-sm text-neutral-600">Ordenar:</span>
          <select 
            value={sort} 
            onChange={(e)=>setSort(e.target.value)} 
            className="px-3 py-2 rounded-lg bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary-light"
          >
            <option value="relevance">Relevancia</option>
            <option value="price_asc">Precio ascendente</option>
            <option value="price_desc">Precio descendente</option>
            <option value="name_asc">Nombre (A-Z)</option>
          </select>
        </div>

        {/* Estados de carga, error y contenido */}
        {loading && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {Array.from({ length: 8 }).map((_, i) => (
              <div key={i} className="bg-white border border-neutral-200 rounded-lg p-5">
                <Skeleton className="h-40 w-full mb-4" />
                <Skeleton className="h-4 w-2/3 mb-2" />
                <Skeleton className="h-3 w-1/3 mb-2" />
                <Skeleton className="h-4 w-24" />
              </div>
            ))}
          </div>
        )}
        
        {!loading && error && (<div className="text-danger">{error}</div>)}
        
        {!loading && !error && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {products.map((p) => (
              <ProductCard key={p.id} product={p} />
            ))}
            {products.length === 0 && (
              <p className="text-neutral-600">No hay productos que coincidan con los filtros.</p>
            )}
          </div>
        )}
      </main>
    </div>
  )
}
/**
 * Página de catálogo.
 * Lista productos y permite filtrado básico.
 */
