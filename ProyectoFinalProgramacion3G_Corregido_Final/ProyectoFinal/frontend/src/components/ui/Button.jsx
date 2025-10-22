const base = 'inline-flex items-center justify-center rounded-xl font-semibold transition shadow-soft focus:outline-none focus-visible:ring-2 focus-visible:ring-neon focus-visible:ring-offset-2 focus-visible:ring-offset-navy disabled:opacity-60 disabled:cursor-not-allowed'
const variants = {
  primary: 'bg-neon text-navy hover:brightness-95',
  secondary: 'bg-white/10 text-white hover:bg-white/15',
  ghost: 'bg-transparent text-white hover:bg-white/10 border border-white/10',
}

export default function Button({ children, type = 'button', onClick, className = '', variant = 'primary', disabled = false }) {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${base} px-5 py-3 ${variants[variant]} ${className}`}
    >
      {children}
    </button>
  );
}