/*
  Componente BrandLogo
  Descripción: Muestra la marca "AppMarket" como logotipo simple y legible.
  Uso: Se utiliza en layouts y navegación para reforzar la identidad.
*/
export default function BrandLogo({ size = 'md', className = '' }) {
  const map = {
    sm: { box: 'h-6 w-6', text: 'text-xs' },
    md: { box: 'h-8 w-8', text: 'text-sm' },
    lg: { box: 'h-12 w-12', text: 'text-lg' },
    xl: { box: 'h-16 w-16', text: 'text-2xl' },
  }
  const s = map[size] || map.md
  return (
    <div className={`flex items-center gap-2 select-none ${className}`}>
      <div className={`${s.box} rounded-md bg-neon/20 border border-neon/40 flex items-center justify-center`}>
        <span className={`text-neon font-black ${s.text}`}>AM</span>
      </div>
      <span className="font-bold tracking-wide">AppMarket</span>
    </div>
  );
}
/**
 * Logotipo de la marca.
 * Muestra el logo en tamaños configurables.
 */
