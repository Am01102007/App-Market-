/*
  Pantalla Dashboard
  Descripción: Vista principal tras autenticación. Contiene navbar con marca,
  navegación y paneles de contenido. Diseñada para ser clara y responsiva.
*/
import { useNavigate } from 'react-router-dom'
import BrandLogo from '../components/BrandLogo'
import Button from '../components/ui/Button'
import { clearToken } from '../lib/auth'

export default function Dashboard(){
  const navigate = useNavigate()

  const handleLogout = () => {
    clearToken()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f131a] via-navy to-[#1d2230] text-white">
      {/* Navbar */}
      <header className="bg-navy/90 border-b border-white/10 backdrop-blur sticky top-0 z-10">
        <div className="max-w-6xl mx-auto px-6 py-3 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <BrandLogo />
            <span className="font-semibold">AppMarket</span>
          </div>
          <nav className="hidden sm:flex items-center gap-6 text-sm text-white/80">
            <a href="#" className="hover:text-white">Dashboard</a>
            <a href="#" className="hover:text-white">Catálogo</a>
            <a href="#" className="hover:text-white">Chat</a>
          </nav>
          <Button variant="ghost" onClick={handleLogout}>Cerrar sesión</Button>
        </div>
      </header>

      {/* Contenido */}
      <main className="max-w-6xl mx-auto px-6 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <section className="md:col-span-2 bg-navy/90 border border-white/10 rounded-xl p-6 shadow-soft">
            <h2 className="text-xl font-semibold mb-2">Bienvenido</h2>
            <p className="text-white/70">Explora el catálogo, chatea con vendedores y gestiona tus publicaciones desde aquí.</p>
          </section>
          <aside className="bg-navy/90 border border-white/10 rounded-xl p-6 shadow-soft">
            <h3 className="font-semibold mb-3">Acciones rápidas</h3>
            <div className="space-y-3">
              <Button className="w-full">Ver catálogo</Button>
              <Button variant="secondary" className="w-full">Abrir chat</Button>
            </div>
          </aside>
        </div>
      </main>
    </div>
  )
}