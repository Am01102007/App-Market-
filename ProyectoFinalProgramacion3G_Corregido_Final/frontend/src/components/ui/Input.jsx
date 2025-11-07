/*
  Componente Input
  Descripción: Campo de texto accesible con etiqueta asociada y soporte de
  mensaje de error. Recibe id para vincular label e input.
  Props: { id, label, type, placeholder, value, onChange, error, className }
*/
export default function Input({ id, label, type = 'text', placeholder, value, onChange, error, className='', ...rest }) {
  return (
    <div className="space-y-2">
      {label && <label htmlFor={id} className="text-neutral-700 text-sm font-semibold">{label}</label>}
      <input
        id={id}
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        aria-invalid={!!error}
        aria-describedby={error ? `${id}-error` : undefined}
        className={`w-full px-4 py-2.5 rounded-lg bg-white text-neutral-900 placeholder:text-neutral-500 border border-neutral-300 focus:outline-none focus:ring-2 focus:ring-primary-light focus:border-primary-light ${error ? 'ring-2 ring-danger' : ''} ${className}`}
        {...rest}
      />
      {error && <p id={`${id}-error`} className="text-danger text-xs">{error}</p>}
    </div>
  );
}
