import BrandLogo from '../components/BrandLogo';
import Button from '../components/ui/Button';
import { Link, useNavigate } from 'react-router-dom';
// import { clearToken } from '../lib/auth';
// import { clearUsername } from '../lib/auth';
import { useEffect, useState } from 'react';
import ProductCard from '../components/ProductCard';
import { fetchActiveProducts } from '../lib/products';
import { sampleProducts } from '../lib/sampleProducts';
import Header from '../components/Header';
import Input from '../components/ui/Input';

/**
 * Componente de página principal del dashboard.
 * Muestra la página de inicio con productos destacados, estadísticas del usuario
 * y funcionalidad de búsqueda. Incluye manejo de autenticación y navegación.
 * 
 * @component
 * @returns {JSX.Element} Página del dashboard con productos destacados y búsqueda
 */
export default function Dashboard() {
  const navigate = useNavigate();
  // Estados para productos destacados y manejo de errores
  const [allProducts, setAllProducts] = useState([]);
  const [featured, setFeatured] = useState([]);
  const [error, setError] = useState(null);
  const [sort, setSort] = useState('relevance');
  const [category, setCategory] = useState('all');
  const [priceMin, setPriceMin] = useState('');
  const [priceMax, setPriceMax] = useState('');

  /**
   * Función para cerrar sesión del usuario.
   * Limpia el token y nombre de usuario del almacenamiento local
   * y redirige a la página de login.
   */
  // const handleLogout = () => {
  //   clearToken();
  //   clearUsername();
  //   navigate('/login');
  // };

  // Efecto para cargar productos destacados al montar el componente
  useEffect(() => {
    let mounted = true;
    fetchActiveProducts()
      .then((data) => { 
        if (mounted) {
          const list = Array.isArray(data) ? data : [];
          setAllProducts(list);
        }
      })
      .catch((err) => { 
        console.error('Error cargando destacados', err); 
        setError('No se pudieron cargar los productos destacados. Mostrando datos de ejemplo.');
        if (mounted) {
          setAllProducts(sampleProducts);
        }
      });
    return () => { mounted = false };
  }, []);

  // Aplicar filtros y orden para mostrar productos destacados
  useEffect(() => {
    let list = [...allProducts];
    // Filtro por categoría
    if (category !== 'all') {
      list = list.filter(p => (p.category?.name || p.category || '').toLowerCase() === category.toLowerCase());
    }
    // Filtros de precio
    const min = priceMin ? Number(priceMin) : null;
    const max = priceMax ? Number(priceMax) : null;
    if (min != null) list = list.filter(p => Number(p.price) >= min);
    if (max != null) list = list.filter(p => Number(p.price) <= max);
    // Ordenamiento
    if (sort === 'price_asc') list = [...list].sort((a,b) => Number(a.price) - Number(b.price));
    else if (sort === 'price_desc') list = [...list].sort((a,b) => Number(b.price) - Number(a.price));
    else if (sort === 'name_asc') list = [...list].sort((a,b) => (a.name||'').localeCompare(b.name||''));

    // Limitar a destacados (primeros 6)
    setFeatured(list.slice(0,6));
  }, [allProducts, sort, category, priceMin, priceMax]);

  /**
   * Maneja el envío del formulario de búsqueda.
   * Redirige a la página de catálogo con el término de búsqueda como parámetro.
   * 
   * @param {Event} e - Evento del formulario
   */
  const onSearch = (e) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);
    const q = form.get('q');
    navigate(`/catalog?q=${encodeURIComponent(q || '')}`);
  };

  return (
    <div className="min-h-screen bg-neutral-100 text-neutral-900">
      <Header />

      <main className="max-w-7xl mx-auto p-8">
        {/* Mensaje de error si hay problemas cargando productos */}
        {error && <p className="text-danger bg-danger/10 p-3 rounded-md mb-6">{error}</p>}
        
        <section>
          {/* Sección de bienvenida y búsqueda */}
          <div className="bg-white border border-neutral-200 rounded-lg p-6 mb-6">
            <h1 className="text-3xl font-bold mb-3">Bienvenido</h1>
            <form onSubmit={onSearch} className="flex items-center gap-3">
              <Input
                name="q"
                placeholder="Buscar productos en todo el mercado"
                className="flex-grow"
              />
              <Button type="submit" variant="primary">Buscar</Button>
            </form>
          </div>
          
          <p className="text-neutral-600 mb-6">Resumen rápido de tu actividad reciente.</p>

          {/* Estadísticas del usuario */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="bg-white border border-neutral-200 rounded-lg p-5">
              <h3 className="font-semibold mb-2">Productos vistos</h3>
              <p className="text-neutral-600">Llevas 12 productos explorados esta semana.</p>
            </div>
            <div className="bg-white border border-neutral-200 rounded-lg p-5">
              <h3 className="font-semibold mb-2">Favoritos</h3>
              <p className="text-neutral-600">Has marcado 4 productos como favoritos.</p>
            </div>
          </div>

          {/* Filtros y ordenamiento */}
          <div className="mt-8 bg-white border border-neutral-200 rounded-lg p-4">
            <div className="flex flex-wrap items-end gap-4">
              <div>
                <span className="text-sm text-neutral-600">Categoría</span>
                <div className="flex flex-wrap items-center gap-2 mt-2">
                  {['all','tecnologia','hogar','moda'].map((cat) => (
                    <button 
                      key={cat}
                      onClick={() => setCategory(cat)}
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
              </div>
              <div className="flex items-end gap-3">
                <Input label="Precio mínimo" type="number" value={priceMin} onChange={(e)=>setPriceMin(e.target.value)} />
                <Input label="Precio máximo" type="number" value={priceMax} onChange={(e)=>setPriceMax(e.target.value)} />
                <Button variant="ghost" onClick={()=>{ setPriceMin(''); setPriceMax(''); }}>Limpiar</Button>
              </div>
              <div className="ml-auto">
                <span className="text-sm text-neutral-600">Ordenar</span>
                <select 
                  value={sort}
                  onChange={(e)=>setSort(e.target.value)}
                  className="ml-2 px-3 py-2 rounded-lg bg-white border border-neutral-300 text-neutral-900 focus:outline-none focus:ring-2 focus:ring-primary-light"
                >
                  <option value="relevance">Relevancia</option>
                  <option value="price_asc">Precio ascendente</option>
                  <option value="price_desc">Precio descendente</option>
                  <option value="name_asc">Nombre (A-Z)</option>
                </select>
              </div>
            </div>
          </div>

          {/* Sección de productos destacados */}
          <div className="mt-8">
            <h3 className="text-2xl font-bold mb-4">Destacados</h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {featured.map((p) => (
                <ProductCard key={p.id} product={p} />
              ))}
              {featured.length === 0 && (
                <p className="text-neutral-600">No hay productos activos.</p>
              )}
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}
/**
 * Panel principal.
 * Resumen de actividad y accesos rápidos.
 */
