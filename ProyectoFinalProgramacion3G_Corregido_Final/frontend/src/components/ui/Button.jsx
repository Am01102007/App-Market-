/**
 * Componente de botón reutilizable.
 * Ofrece variantes visuales y accesibilidad.
 */
import { Link } from 'react-router-dom';

const base = 'inline-flex items-center justify-center rounded-lg font-semibold shadow-soft transition-all duration-300 ease-in-out transform hover:scale-105 focus:outline-none focus-visible:ring-2 focus-visible:ring-primary-light focus-visible:ring-offset-2 focus-visible:ring-offset-neutral-100 disabled:opacity-50 disabled:cursor-not-allowed';

const variants = {
  primary: 'bg-gradient-primary text-white',
  secondary: 'bg-secondary text-white',
  ghost: 'text-primary hover:bg-primary/10',
  success: 'bg-gradient-primary text-white',
  danger: 'bg-danger text-white',
};

export default function Button({ children, to, type = 'button', onClick, className = '', variant = 'primary', disabled = false }) {
  const classNames = `${base} px-5 py-2.5 text-sm tracking-wider ${variants[variant]} ${className}`;

  if (to) {
    return (
      <Link to={to} className={classNames}>
        {children}
      </Link>
    );
  }

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={classNames}
    >
      {children}
    </button>
  );
}
