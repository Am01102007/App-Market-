/**
 * Modal de confirmación reutilizable.
 * Muestra un diálogo con título, mensaje y acciones de confirmar/cancelar.
 */
export default function ConfirmModal({
  open,
  title = 'Confirmar acción',
  message = '¿Estás seguro que deseas continuar?',
  confirmText = 'Confirmar',
  cancelText = 'Cancelar',
  onConfirm,
  onCancel,
  loading = false,
}) {
  if (!open) return null
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-md border border-neutral-200">
        <div className="p-5 border-b border-neutral-200">
          <h2 className="text-xl font-semibold">{title}</h2>
        </div>
        <div className="p-5">
          <p className="text-neutral-700">{message}</p>
        </div>
        <div className="p-5 flex justify-end gap-3 border-t border-neutral-200">
          <button
            className="px-4 py-2 rounded-lg text-sm font-medium bg-neutral-100 text-neutral-800 hover:bg-neutral-200"
            onClick={onCancel}
            disabled={loading}
          >
            {cancelText}
          </button>
          <button
            className="px-4 py-2 rounded-lg text-sm font-semibold bg-danger text-white hover:brightness-95 disabled:opacity-50"
            onClick={onConfirm}
            disabled={loading}
          >
            {loading ? 'Procesando...' : confirmText}
          </button>
        </div>
      </div>
    </div>
  )
}

