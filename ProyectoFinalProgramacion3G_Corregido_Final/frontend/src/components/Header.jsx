import { Link, NavLink, useNavigate } from 'react-router-dom';
import BrandLogo from './BrandLogo';
import Button from './ui/Button';
import { clearToken, clearUsername } from '../lib/auth';

/**
 * Enlaces de navegación principal de la aplicación.
 * Define las rutas y textos para el menú de navegación.
 */
const navLinks = [
  { to: '/dashboard', text: 'Inicio' },
  { to: '/catalog', text: 'Catálogo' },
  { to: '/wishlist', text: 'Favoritos' },
  { to: '/publish', text: 'Publicar' },
  { to: '/chat', text: 'Chat' },
];

/**
 * Componente de encabezado principal de la aplicación.
 * Proporciona navegación entre páginas, logo de la marca y funcionalidad de logout.
 * Se mantiene fijo en la parte superior de la pantalla (sticky).
 * 
 * @component
 * @param {Object} props - Propiedades del componente
 * @param {boolean} [props.showBack=false] - Determina si mostrar el botón "Volver"
 * @returns {JSX.Element} Encabezado con navegación y controles de usuario
 */
export default function Header({ showBack = false }) {
  const navigate = useNavigate();

  /**
   * Maneja el proceso de cierre de sesión del usuario.
   * Limpia el token y nombre de usuario del localStorage y redirige al login.
   */
  const handleLogout = () => {
    clearToken();
    clearUsername();
    navigate('/login');
  };

  return (
    <header className="bg-white/80 backdrop-blur shadow-md sticky top-0 z-40">
      <nav className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Sección izquierda: Logo y navegación principal */}
          <div className="flex items-center">
            <Link to="/dashboard" className="flex-shrink-0">
              <BrandLogo className="h-8 w-auto" />
            </Link>
            {/* Menú de navegación - oculto en móviles */}
            <div className="hidden md:block">
              <div className="ml-10 flex items-baseline space-x-4">
                {navLinks.map((link) => (
                  <NavLink
                    key={link.to}
                    to={link.to}
                    className={({ isActive }) =>
                      `px-3 py-2 rounded-lg text-sm font-medium transition-all ${
                        isActive
                          ? 'bg-gradient-primary text-white shadow-soft'
                          : 'text-neutral-700 hover:bg-neutral-100 hover:text-neutral-900'
                      }`
                    }
                  >
                    {link.text}
                  </NavLink>
                ))}
                <NavLink
                  to="/cart"
                  className={({ isActive }) =>
                    `px-3 py-2 rounded-lg text-sm font-medium transition-all ${
                      isActive
                        ? 'bg-gradient-primary text-white shadow-soft'
                        : 'text-neutral-700 hover:bg-neutral-100 hover:text-neutral-900'
                    }`
                  }
                >
                  Carrito
                </NavLink>
              </div>
            </div>
          </div>
          {/* Sección derecha: Botones de acción */}
          <div className="hidden md:block">
            <div className="ml-4 flex items-center md:ml-6">
              {/* Botón "Volver" condicional */}
              {showBack && (
                <Button variant="ghost" onClick={() => navigate(-1)} className="mr-3">
                  Volver
                </Button>
              )}
              {/* Botón de logout */}
              <Button variant="secondary" onClick={handleLogout}>
                Salir
              </Button>
            </div>
          </div>
        </div>
      </nav>
    </header>
  );
}
/**
 * Encabezado de la aplicación.
 * Muestra navegación y acciones principales.
 */
