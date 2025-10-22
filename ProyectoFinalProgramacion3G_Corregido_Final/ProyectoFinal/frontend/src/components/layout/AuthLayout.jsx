/*
  AuthLayout
  Descripción: Layout coherente para pantallas de autenticación. Muestra logo,
  título y subtítulo, y renderiza contenido dentro de una tarjeta accesible.
*/
import BrandLogo from '../BrandLogo'

export default function AuthLayout({ title, subtitle, children }) {
  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f131a] via-navy to-[#1d2230] flex items-center justify-center p-4">
      <div className="w-full max-w-xl bg-navy/95 text-white rounded-2xl shadow-soft border border-white/10">
        <div className="px-8 pt-8 flex items-center gap-3">
          <BrandLogo />
          <div className="space-y-1">
            {title && <h1 className="text-2xl font-bold">{title}</h1>}
            {subtitle && <p className="text-white/70 text-sm">{subtitle}</p>}
          </div>
        </div>
        <div className="px-8 pb-8 pt-6">
          {children}
        </div>
      </div>
    </div>
  )
}