import { Link } from 'react-router-dom';
import Button from './ui/Button';
import Input from './ui/Input';
import QuantityStepper from './ui/QuantityStepper';
import RatingStars from './ui/RatingStars';
import Toast from './ui/Toast';
import ConfirmModal from './ui/ConfirmModal';
import { useEffect, useMemo, useState } from 'react';
import { addToCart, getCart, removeFromCart, getLastQty, setLastQty } from '../lib/cart';
import { isWishlisted, toggleWishlist } from '../lib/wishlist';
import { getRating, submitRating } from '../lib/ratings';

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
  const { id, name, imageUrl, category, price, status, description, rating, reviewsCount } = product;

  const [qty, setQty] = useState(1);
  const [toastMsg, setToastMsg] = useState('');
  const [toastType, setToastType] = useState('info');
  const [confirmRemove, setConfirmRemove] = useState(false);
  const [wish, setWish] = useState(false);
  const [localRating, setLocalRating] = useState({ avg: 0, count: 0 });

  const cartItems = useMemo(() => getCart(), []);
  const inCart = useMemo(() => cartItems.some(i => String(i.id) === String(id)), [cartItems, id]);

  const displayPrice = typeof price === 'number' ? price : Number(price);
  const categoryName = typeof category === 'object' && category?.name ? category.name : category;
  const statusLabel = ({ ACTIVE: 'Activo', AVAILABLE: 'Disponible', INACTIVE: 'Inactivo', SOLD: 'Vendido' }[status]) || null;
  const qualifiesFreeShipping = Number(displayPrice) >= 50; // umbral simple al estilo Amazon

  useEffect(() => {
    try {
      const initial = getLastQty(id)
      setQty(initial)
    } catch {}
  }, [id])

  useEffect(() => {
    setWish(isWishlisted(id))
    setLocalRating(getRating(id))
  }, [id])

  const onAdd = (e) => {
    e.preventDefault();
    const q = Math.max(1, Number(qty) || 1);
    setLastQty(id, q);
    addToCart(product, q);
    setToastType('success');
    setToastMsg(`Añadido al carrito: ${name} ×${q}`);
  };

  const onRemove = (e) => {
    e.preventDefault();
    setConfirmRemove(true);
  };

  const confirmRemoveAction = () => {
    removeFromCart(id);
    setConfirmRemove(false);
    setToastType('success');
    setToastMsg(`Eliminado del carrito: ${name}`);
  };

  const onToggleWish = (e) => {
    e.preventDefault();
    toggleWishlist(product);
    const next = !wish;
    setWish(next);
    setToastType('success');
    setToastMsg(next ? 'Añadido a favoritos' : 'Quitado de favoritos');
  }

  return (
    <div className="bg-white/90 backdrop-blur border border-neutral-200 rounded-xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden group">
      <Link to={`/product/${id}`} className="relative block w-full h-48 bg-neutral-200/50 overflow-hidden">
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
        <div className="absolute top-2 right-2 flex flex-col items-end gap-1">
          {statusLabel && (
            <span className="text-xs px-2 py-1 rounded-full bg-white/90 border border-neutral-200 text-neutral-700 shadow-soft">
              {statusLabel}
            </span>
          )}
          {qualifiesFreeShipping && (
            <span className="text-[11px] px-2 py-1 rounded-full bg-neon/20 border border-neon/40 text-neon font-semibold shadow-soft">
              Envío gratis
            </span>
          )}
        </div>
      </Link>

      <div className="p-4">
        <Link to={`/product/${id}`} className="block">
          <h3 className="text-lg font-semibold text-neutral-900 truncate">{name}</h3>
        </Link>
        <RatingStars 
          rating={localRating.avg || rating} 
          count={localRating.count || reviewsCount} 
          className="mt-1"
          onRate={(stars)=>{ const next = submitRating(id, stars); setLocalRating(next); setToastType('success'); setToastMsg(`Gracias por tu calificación: ${stars}★`); }} 
        />
        <p className="text-sm text-neutral-600 capitalize">{categoryName}</p>
        {description && (
          <p className="text-sm text-neutral-700 mt-2 line-clamp-2">{description}</p>
        )}
        <p className="text-2xl font-bold text-primary mt-2">
          ${displayPrice?.toFixed ? displayPrice.toFixed(2) : displayPrice}
        </p>

        <div className="mt-3 flex items-center gap-3">
          {!inCart ? (
            <>
              <QuantityStepper value={qty} min={1} max={99} onChange={(v) => { setQty(v); setLastQty(id, v); }} />
              <Button variant="primary" onClick={onAdd}>Añadir</Button>
            </>
          ) : (
            <Button variant="danger" onClick={onRemove}>Quitar del carrito</Button>
          )}
          <Button variant="ghost" to={`/product/${id}`}>Ver detalles</Button>
          <Button variant="ghost" onClick={onToggleWish} className="ml-auto">
            {wish ? '❤️ Favorito' : '🤍 Guardar'}
          </Button>
        </div>
     </div>

      <Toast message={toastMsg} type={toastType} onClose={() => setToastMsg('')} />
      <ConfirmModal
        open={confirmRemove}
        title="Eliminar del carrito"
        message={`¿Deseas quitar "${name}" del carrito?`}
        confirmText="Eliminar"
        cancelText="Cancelar"
        onConfirm={confirmRemoveAction}
        onCancel={() => setConfirmRemove(false)}
      />
    </div>
  );
}
/**
 * Tarjeta de producto.
 * Presenta información resumida y acciones rápidas.
 */
