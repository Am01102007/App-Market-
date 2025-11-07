import { useState } from 'react';
import { Link } from 'react-router-dom';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';
import AuthLayout from '../components/layout/AuthLayout';

export default function ForgotPassword(){
  const [email, setEmail] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    alert('Te enviamos un enlace para resetear tu contraseña');
  };

  return (
    <AuthLayout title="Recupera tu acceso" subtitle="Te enviaremos un enlace de recuperación">
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input label="Correo" type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="tu@correo.com" />
        <div className="flex items-center justify-between">
          <Link to="/login" className="text-pastel-teal font-semibold hover:text-pastel-cyan">Volver a iniciar sesión</Link>
          <Button type="submit" variant="brand">Enviar enlace</Button>
        </div>
      </form>
    </AuthLayout>
  );
}
/**
 * Recuperación de contraseña.
 * Permite solicitar restablecimiento de acceso.
 */
