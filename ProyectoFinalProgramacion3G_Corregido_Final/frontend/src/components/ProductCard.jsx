import { Link } from 'react-router-dom';

/**
 * Componente de tarjeta de producto para mostrar información básica de un producto.
 * Renderiza una tarjeta clickeable que redirige a la página de detalles del producto.
 * Incluye imagen, nombre, categoría y precio del producto con efectos hover.
 * 
 * @component
 * @param {Object} props - Propiedades del componente
 * @param {Object} props.product - Objeto que contiene la información del producto
 * @param {string|number} props.product.id - ID único del producto
 * @param {string} props.product.name - Nombre del producto
 * @param {string} props.product.imageUrl - URL de la imagen del producto
 * @param {string|Object} props.product.category - Categoría del producto (string o objeto con propiedad name)
 * @param {number|string} props.product.price - Precio del producto
 * @returns {JSX.Element} Tarjeta de producto clickeable
 */
export default function ProductCard({ product }) {
  const { id, name, imageUrl, category, price, status } = product;
  
  // Convierte el precio a número para asegurar formato correcto
  const displayPrice = typeof price === 'number' ? price : Number(price);

  // Extrae el nombre de la categoría si es un objeto, sino usa el valor directo
  const categoryName = typeof category === 'object' && category?.name ? category.name : category;

  const statusLabel = ({ ACTIVE: 'Activo', INACTIVE: 'Inactivo', SOLD: 'Vendido' }[status]) || null;

  return (
    <Link to={`/product/${id}`} className="block bg-white/90 backdrop-blur border border-neutral-200 rounded-xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden group">
      {/* Contenedor de imagen con fallback */}
      <div className="relative w-full h-48 bg-neutral-200/50 overflow-hidden">
        {imageUrl ? (
          <img 
            src={imageUrl} 
            alt={name} 
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            onError={(e) => {
              // Maneja errores de carga de imagen mostrando placeholder
              e.target.style.display = 'none';
              e.target.nextSibling.style.display = 'flex';
            }}
          />
        ) : null}
        {/* Placeholder cuando no hay imagen o falla la carga */}
        <div 
          className="w-full h-full flex items-center justify-center text-neutral-500"
          style={{ display: imageUrl ? 'none' : 'flex' }}
        >
          Sin imagen
        </div>
        {statusLabel && (
          <span className="absolute top-2 right-2 text-xs px-2 py-1 rounded-full bg-white/90 border border-neutral-200 text-neutral-700 shadow-soft">
            {statusLabel}
          </span>
        )}
      </div>
      {/* Información del producto */}
      <div className="p-4">
        <h3 className="text-lg font-semibold text-neutral-900 truncate">{name}</h3>
        <p className="text-sm text-neutral-600 capitalize">{categoryName}</p>
        <p className="text-xl font-bold text-primary mt-2">
          ${displayPrice?.toFixed ? displayPrice.toFixed(2) : displayPrice}
        </p>
      </div>
    </Link>
  );
}
/**
 * Tarjeta de producto.
 * Presenta información resumida y acciones rápidas.
 */
