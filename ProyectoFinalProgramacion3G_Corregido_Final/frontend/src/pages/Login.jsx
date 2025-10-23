import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';
import AuthLayout from '../components/layout/AuthLayout';
import { setToken, setUsername } from '../lib/auth';
import api from '../lib/api';
import Toast from '../components/ui/Toast';

/**
 * Componente de página de inicio de sesión.
 * Permite a los usuarios autenticarse en la aplicación mediante email y contraseña.
 * Maneja la validación de credenciales y redirige al dashboard tras un login exitoso.
 * 
 * @component
 * @returns {JSX.Element} Página de login con formulario de autenticación
 */
export default function Login(){
  // Estados para manejar los datos del formulario
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState({ message: '', type: 'info' });
  const navigate = useNavigate();

  /**
   * Maneja el envío del formulario de login.
   * Realiza la autenticación del usuario y gestiona la respuesta del servidor.
   * 
   * @param {Event} e - Evento del formulario
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      // Envía las credenciales al servidor
      const response = await api.post('/auth/login', {
        email,
        password
      });
      
      if (response.data.ok) {
        // Guarda el token y nombre de usuario en localStorage
        setToken(response.data.token);
        setUsername(response.data.nombre);
        setToast({ message: 'Inicio de sesión exitoso', type: 'success' });
        // Redirige al dashboard después de un breve delay
        setTimeout(() => navigate('/dashboard'), 800);
      } else {
        setToast({ message: response.data.msg || 'Error en el inicio de sesión', type: 'error' });
      }
    } catch (error) {
      console.error('Error en login:', error);
      
      // Manejo más específico de errores según el código de respuesta
      let errorMessage = 'Error de conexión. Intenta de nuevo.';
      
      if (error.response) {
        // El servidor respondió con un código de error
        switch (error.response.status) {
          case 401:
            errorMessage = 'Credenciales incorrectas. Verifica tu email y contraseña.';
            break;
          case 404:
            errorMessage = 'Usuario no encontrado. Verifica tu email.';
            break;
          case 500:
            errorMessage = 'Error interno del servidor. Intenta más tarde.';
            break;
          default:
            errorMessage = `Error del servidor (${error.response.status}). Intenta de nuevo.`;
        }
      } else if (error.request) {
        // La petición se hizo pero no hubo respuesta
        errorMessage = 'No se pudo conectar al servidor. Verifica tu conexión.';
      }
      
      setToast({ message: errorMessage, type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout title="Inicia sesión" subtitle="Bienvenido de nuevo a AppMarket">
      <form onSubmit={handleSubmit} className="space-y-4">
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
        <div className="flex items-center justify-between">
          <Button to="/forgot" variant="ghost">¿Olvidaste tu contraseña?</Button>
          <Button type="submit" variant="success" disabled={loading}>
            {loading ? 'Iniciando...' : 'Entrar'}
          </Button>
        </div>
      </form>
      <p className="text-neutral-600 mt-4 text-center">
        ¿No tienes cuenta?{' '}
        <Button to="/register" variant="ghost">Regístrate</Button>
      </p>
      
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