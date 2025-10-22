/*
  Componente Input
  Descripción: Campo de texto accesible con etiqueta asociada y soporte de
  mensaje de error. Recibe id para vincular label e input.
  Props: { id, label, type, placeholder, value, onChange, error, className }
*/
export default function Input({ id, label, type = 'text', placeholder, value, onChange, error, className='' }) {
  return (
    <div className="space-y-2">
      {label && <label htmlFor={id} className="text-slate-100 text-sm font-semibold">{label}</label>}
      <input
        id={id}
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        aria-invalid={!!error}
        aria-describedby={error ? `${id}-error` : undefined}
        className={`w-full px-4 py-3 rounded-xl bg-slate text-navy placeholder:opacity-70 focus:outline-none focus:ring-2 focus:ring-neon ${error ? 'ring-2 ring-red-500' : ''} ${className}`}
      />
      {error && <p id={`${id}-error`} className="text-red-400 text-xs">{error}</p>}
    </div>
  );
}