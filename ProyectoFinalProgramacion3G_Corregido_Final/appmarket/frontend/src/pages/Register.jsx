import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';
import AuthLayout from '../components/layout/AuthLayout';

export default function Register(){
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    navigate('/login');
  };

  return (
    <AuthLayout title="Crea tu cuenta" subtitle="Únete a AppMarket y publica tus productos">
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input label="Nombre" type="text" value={name} onChange={e => setName(e.target.value)} placeholder="Tu nombre" />
        <Input label="Correo" type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="tu@correo.com" />
        <Input label="Contraseña" type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="••••••••" />
        <Button type="submit" variant="success" className="w-full">Crear cuenta</Button>
      </form>
      <p className="text-neutral-600 mt-4 text-center">
        ¿Ya tienes una cuenta?{' '}
        <Button to="/login" variant="ghost">Inicia sesión</Button>
      </p>
    </AuthLayout>
  );
}