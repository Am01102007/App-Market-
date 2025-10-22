/*
  Componente BrandLogo
  Descripción: Muestra la marca "AppMarket" como logotipo simple y legible.
  Uso: Se utiliza en layouts y navegación para reforzar la identidad.
*/
export default function BrandLogo() {
  return (
    <div className="flex items-center gap-2 select-none">
      <div className="h-8 w-8 rounded-md bg-neon/20 border border-neon/40 flex items-center justify-center">
        <span className="text-neon font-black text-sm">AM</span>
      </div>
      <span className="font-bold tracking-wide">AppMarket</span>
    </div>
  );
}