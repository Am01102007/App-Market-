import ProductCard from './ProductCard'

export default function RelatedCarousel({ products = [], title = 'Productos relacionados' }) {
  if (!products?.length) return null
  return (
    <section className="mt-10">
      <h2 className="text-xl font-semibold mb-3">{title}</h2>
      <div className="relative">
        <div className="flex gap-4 overflow-x-auto pb-2">
          {products.map(p => (
            <div key={p.id} className="min-w-[16rem] max-w-[18rem]">
              <ProductCard product={p} />
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
/**
 * Carrusel simple con desplazamiento horizontal.
 * Reutiliza ProductCard para consistencia visual.
 */
