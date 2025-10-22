import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';
import AuthLayout from '../components/layout/AuthLayout';
import api from '../lib/api';
import Toast from '../components/ui/Toast';

export default function Register(){
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState({ message: '', type: 'info' });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      const response = await api.post('/auth/register', {
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
      <p className="text-neutral-600 mt-4 text-center">
        ¿Ya tienes una cuenta?{' '}
        <Button to="/login" variant="ghost">Inicia sesión</Button>
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