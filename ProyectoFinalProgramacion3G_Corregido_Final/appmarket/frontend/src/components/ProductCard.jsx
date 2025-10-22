import { Link } from 'react-router-dom';

export default function ProductCard({ product }) {
  const { id, name, imageUrl, category, price } = product;
  const displayPrice = typeof price === 'number' ? price : Number(price);

  // Extraer el nombre de la categoría si es un objeto
  const categoryName = typeof category === 'object' && category?.name ? category.name : category;

  return (
    <Link to={`/product/${id}`} className="block bg-white border border-neutral-200 rounded-lg shadow-sm hover:shadow-lg transition-shadow duration-300 overflow-hidden group">
      <div className="w-full h-48 bg-neutral-200/50 overflow-hidden">
        {imageUrl ? (
          <img 
            src={imageUrl} 
            alt={name} 
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            onError={(e) => {
              e.target.style.display = 'none';
              e.target.nextSibling.style.display = 'flex';
            }}
          />
        ) : null}
        <div 
          className="w-full h-full flex items-center justify-center text-neutral-500"
          style={{ display: imageUrl ? 'none' : 'flex' }}
        >
          Sin imagen
        </div>
      </div>
      <div className="p-4">
        <h3 className="text-lg font-semibold text-neutral-900 truncate">{name}</h3>
        <p className="text-sm text-neutral-600 capitalize">{categoryName}</p>
        <p className="text-xl font-bold text-primary mt-2">${displayPrice?.toFixed ? displayPrice.toFixed(2) : displayPrice}</p>
      </div>
    </Link>
  );
}
