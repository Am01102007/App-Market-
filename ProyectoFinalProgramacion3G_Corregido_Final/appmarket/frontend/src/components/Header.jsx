import { Link, NavLink, useNavigate } from 'react-router-dom';
import BrandLogo from './BrandLogo';
import Button from './ui/Button';
import { clearToken, clearUsername } from '../lib/auth';

const navLinks = [
  { to: '/dashboard', text: 'Inicio' },
  { to: '/catalog', text: 'Catálogo' },
  { to: '/publish', text: 'Publicar' },
  { to: '/chat', text: 'Chat' },
];

export default function Header({ showBack = false }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    clearToken();
    clearUsername();
    navigate('/login');
  };

  return (
    <header className="bg-white shadow-md sticky top-0 z-40">
      <nav className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <Link to="/dashboard" className="flex-shrink-0">
              <BrandLogo className="h-8 w-auto" />
            </Link>
            <div className="hidden md:block">
              <div className="ml-10 flex items-baseline space-x-4">
                {navLinks.map((link) => (
                  <NavLink
                    key={link.to}
                    to={link.to}
                    className={({ isActive }) =>
                      `px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                        isActive
                          ? 'bg-primary text-white'
                          : 'text-neutral-600 hover:bg-neutral-100 hover:text-neutral-900'
                      }`
                    }
                  >
                    {link.text}
                  </NavLink>
                ))}
              </div>
            </div>
          </div>
          <div className="hidden md:block">
            <div className="ml-4 flex items-center md:ml-6">
              {showBack && (
                <Button variant="ghost" onClick={() => navigate(-1)} className="mr-3">
                  Volver
                </Button>
              )}
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
