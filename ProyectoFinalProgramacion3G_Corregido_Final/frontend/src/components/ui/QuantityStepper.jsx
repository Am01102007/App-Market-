export default function QuantityStepper({ value = 1, min = 1, max = 99, onChange, className = '' }) {
  const val = Math.max(min, Math.min(max, Number(value) || min))

  const setVal = (next) => {
    const v = Math.max(min, Math.min(max, Number(next) || min))
    onChange && onChange(v)
  }

  return (
    <div className={`inline-flex items-center rounded-md border border-neutral-300 bg-white overflow-hidden ${className}`} aria-label="Selector de cantidad">
      <button type="button" className="px-3 py-2 text-neutral-700 hover:bg-neutral-100" onClick={() => setVal(val - 1)} aria-label="Restar">
        −
      </button>
      <input
        type="number"
        className="w-16 text-center outline-none py-2"
        min={min}
        max={max}
        value={val}
        onChange={(e) => setVal(e.target.value)}
      />
      <button type="button" className="px-3 py-2 text-neutral-700 hover:bg-neutral-100" onClick={() => setVal(val + 1)} aria-label="Sumar">
        +
      </button>
    </div>
  )
}
/**
 * Selector de cantidad con botones −/+, accesible y compacto.
 */
