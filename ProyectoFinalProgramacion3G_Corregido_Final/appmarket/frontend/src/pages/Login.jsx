import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';
import AuthLayout from '../components/layout/AuthLayout';
import { setToken } from '../lib/auth';
import { setUsername } from '../lib/auth';

export default function Login(){
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setToken('mock');
    const uname = email ? (email.split('@')[0] || 'demo') : 'demo';
    setUsername(uname);
    navigate('/dashboard');
  };

  return (
    <AuthLayout title="Inicia sesión" subtitle="Bienvenido de nuevo a AppMarket">
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input label="Correo" type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="tu@correo.com" />
        <Input label="Contraseña" type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="••••••••" />
        <div className="flex items-center justify-between">
          <Button to="/forgot" variant="ghost">¿Olvidaste tu contraseña?</Button>
          <Button type="submit" variant="success">Entrar</Button>
        </div>
      </form>
      <p className="text-neutral-600 mt-4 text-center">
        ¿No tienes cuenta?{' '}
        <Button to="/register" variant="ghost">Regístrate</Button>
      </p>
    </AuthLayout>
  );
}