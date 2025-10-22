import BrandLogo from '../components/BrandLogo';
import Button from '../components/ui/Button';
import { Link, useNavigate } from 'react-router-dom';
import { clearToken } from '../lib/auth';
import { clearUsername } from '../lib/auth';
import { useEffect, useState } from 'react';
import ProductCard from '../components/ProductCard';
import { fetchActiveProducts } from '../lib/products';
import { sampleProducts } from '../lib/sampleProducts';
import Header from '../components/Header';
import Input from '../components/ui/Input';

export default function Dashboard() {
  const navigate = useNavigate();
  const [featured, setFeatured] = useState([]);
  const [error, setError] = useState(null);

  const handleLogout = () => {
    clearToken();
    clearUsername();
    navigate('/login');
  };

  useEffect(() => {
    let mounted = true;
    fetchActiveProducts()
      .then((data) => { if (mounted) setFeatured(Array.isArray(data) ? data.slice(0,6) : []); })
      .catch((err) => { 
        console.error('Error cargando destacados', err); 
        setError('No se pudieron cargar los productos destacados. Mostrando datos de ejemplo.');
        if (mounted) setFeatured(sampleProducts.slice(0,6)); 
      });
    return () => { mounted = false };
  }, []);

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
        {error && <p className="text-danger bg-danger/10 p-3 rounded-md mb-6">{error}</p>}
        <section>
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