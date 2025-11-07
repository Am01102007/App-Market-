import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';
import AuthLayout from '../components/layout/AuthLayout';
import api from '../lib/api';
import Toast from '../components/ui/Toast';

/**
 * Componente de página de registro de usuarios.
 * Permite a los nuevos usuarios crear una cuenta en la aplicación.
 * Incluye validación de formulario, manejo de errores y redirección automática.
 * 
 * @component
 * @returns {JSX.Element} Página de registro con formulario y notificaciones
 */
export default function Register(){
  // Estados para los campos del formulario
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState({ message: '', type: 'info' });
  const navigate = useNavigate();

  /**
   * Maneja el envío del formulario de registro.
   * Realiza la petición al API para crear una nueva cuenta de usuario.
   * Muestra notificaciones de éxito o error y redirige al login si es exitoso.
   * 
   * @param {Event} e - Evento del formulario
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      await api.post('/auth/register', {
        name,
        email,
        password
      });
      
      setToast({ message: 'Registro exitoso. Ahora puedes iniciar sesión.', type: 'success' });
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (error) {
      const errorMessage = error.response?.data?.msg || 'Error al crear la cuenta. Inténtalo de nuevo.';
      setToast({ message: errorMessage, type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout title="Crea tu cuenta" subtitle="Únete a AppMarket y publica tus productos">
      {/* Formulario de registro */}
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input 
          label="Nombre" 
          type="text" 
          value={name} 
          onChange={e => setName(e.target.value)} 
          placeholder="Tu nombre" 
          required
        />
        <Input 
          label="Correo" 
          type="email" 
          value={email} 
          onChange={e => setEmail(e.target.value)} 
          placeholder="tu@correo.com" 
          required
        />
        <Input 
          label="Contraseña" 
          type="password" 
          value={password} 
          onChange={e => setPassword(e.target.value)} 
          placeholder="••••••••" 
          required
        />
        <Button type="submit" variant="success" className="w-full" disabled={loading}>
          {loading ? 'Creando cuenta...' : 'Crear cuenta'}
        </Button>
      </form>
      
      {/* Enlace para usuarios existentes */}
      <p className="text-neutral-600 mt-4 text-center">
        ¿Ya tienes una cuenta?{' '}
        <Button to="/login" variant="ghost">Inicia sesión</Button>
      </p>
      
      {/* Notificación toast */}
      {toast.message && (
        <div className="fixed bottom-6 right-6">
          <Toast 
            message={toast.message} 
            type={toast.type} 
            onClose={() => setToast({ message: '', type: 'info' })} 
          />
        </div>
      )}
    </AuthLayout>
  );
}
/**
 * Registro de usuario.
 * Crea cuentas nuevas con validaciones básicas.
 */
