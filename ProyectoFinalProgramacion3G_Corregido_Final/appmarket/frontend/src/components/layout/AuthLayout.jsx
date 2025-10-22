/*
  AuthLayout
  Descripción: Layout coherente para pantallas de autenticación. Muestra logo,
  título y subtítulo, y renderiza contenido dentro de una tarjeta accesible.
*/
import BrandLogo from '../BrandLogo'

export default function AuthLayout({ title, subtitle, children }) {
  return (
    <div className="min-h-screen bg-neutral-100 flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-white text-neutral-900 rounded-lg shadow-md border border-neutral-200 overflow-hidden">
        <div className="px-8 py-10">
          <div className="flex flex-col items-center text-center mb-6">
            <BrandLogo size="lg" />
            <div className="space-y-1 mt-4">
              {title && <h1 className="text-2xl font-bold">{title}</h1>}
              {subtitle && <p className="text-neutral-600 text-sm">{subtitle}</p>}
            </div>
          </div>
          {children}
        </div>
      </div>
    </div>
  )
}