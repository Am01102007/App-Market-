import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import Header from '../components/Header'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../lib/api'
import Toast from '../components/ui/Toast'
import { addToCart } from '../lib/cart'
import { fetchActiveProducts } from '../lib/products'

export default function Chat() {
  const [messages, setMessages] = useState([
    { id: 1, sender: 'Asistente', content: 'Hola 👋 ¿En qué puedo ayudarte a comprar hoy?' },
  ])
  const [newMessage, setNewMessage] = useState('')
  const [toast, setToast] = useState({ message: '', type: 'info' })
  const [catalog, setCatalog] = useState([])
  const [qtyMap, setQtyMap] = useState({})

  useEffect(() => {
    let mounted = true
    fetchActiveProducts()
      .then((data) => { if (mounted) setCatalog(Array.isArray(data) ? data : []) })
      .catch(() => { /* silencioso: sin catálogo no mostramos acciones */ })
    return () => { mounted = false }
  }, [])

  const normalize = (s) => (s||'')
    .toLowerCase()
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
    .replace(/[^a-z0-9\s]/g, ' ')
    .replace(/\s+/g, ' ') // colapsar espacios
    .trim()

  const scoreMatch = (textNorm, nameNorm) => {
    if (!textNorm || !nameNorm) return 0
    if (textNorm.includes(nameNorm)) return nameNorm.length / textNorm.length
    const tTokens = textNorm.split(' ')
    const nTokens = nameNorm.split(' ')
    const overlap = nTokens.filter(tok => tok.length > 2 && tTokens.includes(tok)).length
    return overlap / Math.max(1, nTokens.length)
  }

  const findSuggestedProduct = (text) => {
    const t = normalize(text)
    let best = null
    let bestScore = 0
    for (const p of catalog) {
      const nameNorm = normalize(p.name)
      const s = scoreMatch(t, nameNorm)
      if (s > bestScore) { best = p; bestScore = s }
    }
    return bestScore >= 0.4 ? best : null
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const content = newMessage.trim()
    if (!content) return

    const userMsg = { id: Date.now(), sender: 'Tú', content }
    setMessages((prev) => [...prev, userMsg])
    setNewMessage('')

    try {
      const { data } = await api.post('/assistant/chat', { message: content })
      if (data?.answer) {
        setMessages((prev) => [...prev, { id: Date.now() + 1, sender: 'Asistente', content: data.answer }])
      } else if (data?.error) {
        setMessages((prev) => [...prev, { id: Date.now() + 2, sender: 'Asistente', content: `Error: ${data.error}` }])
      }
    } catch (err) {
      setMessages((prev) => [...prev, { id: Date.now() + 3, sender: 'Asistente', content: 'Hubo un problema al contactar el asistente.' }])
    }
  }

  return (
    <div className="min-h-screen bg-neutral-100 flex flex-col">
      <Header />
      <main className="flex-grow max-w-4xl w-full mx-auto p-4 flex flex-col">
        <div className="bg-white border border-neutral-200 rounded-lg shadow-sm flex-grow flex flex-col">
          <div className="p-4 border-b border-neutral-200">
            <h1 className="text-xl font-semibold text-neutral-900">Chat</h1>
          </div>
          <div className="flex-grow p-4 space-y-4 overflow-y-auto">
            {messages.map((msg) => {
              const suggested = msg.sender !== 'Tú' ? findSuggestedProduct(msg.content) : null
              return (
                <div key={msg.id} className={msg.sender === 'Tú' ? 'flex justify-end' : 'flex justify-start'}>
                  <div className={msg.sender === 'Tú' ? 'bg-neutral-200 text-neutral-900 rounded-lg p-3 max-w-xs' : 'bg-primary-light text-primary-dark rounded-lg p-3 max-w-xs'}>
                    <p><strong>{msg.sender}:</strong> {msg.content}</p>
                    {suggested && (
                      <div className="mt-2">
                        <div className="border border-neutral-300 rounded-lg bg-white text-neutral-900 shadow-sm overflow-hidden">
                          <div className="flex gap-3 p-3 items-center">
                            {suggested?.imageUrl && (
                              <img src={suggested.imageUrl} alt={suggested.name} className="w-20 h-20 object-cover rounded-md border border-neutral-200" />
                            )}
                            <div className="flex-1 min-w-0">
                              <p className="text-sm font-semibold truncate">{suggested.name}</p>
                              {typeof suggested?.category === 'object' ? (
                                <p className="text-xs text-neutral-600 truncate">{suggested.category?.name}</p>
                              ) : (
                                <p className="text-xs text-neutral-600 truncate">{suggested?.category}</p>
                              )}
                              <p className="text-lg font-bold text-primary mt-1">${(Number(suggested.price))?.toFixed ? Number(suggested.price).toFixed(2) : suggested.price}</p>
                              <div className="mt-2 flex items-center gap-2">
                                <Input
                                  type="number"
                                  min={1}
                                  max={99}
                                  className="w-20"
                                  value={qtyMap[suggested.id] ?? 1}
                                  onChange={(e) => setQtyMap((prev) => ({ ...prev, [suggested.id]: Math.max(1, Number(e.target.value) || 1) }))}
                                />
                                <Button 
                                  variant="primary" 
                                  onClick={() => {
                                    try {
                                      const q = qtyMap[suggested.id] ?? 1
                                      addToCart(suggested, q)
                                      setToast({ message: `Añadido al carrito: ${suggested.name} ×${q}`, type: 'success' })
                                    } catch {
                                      setToast({ message: 'No se pudo añadir al carrito', type: 'error' })
                                    }
                                  }}
                                >Añadir al carrito</Button>
                                <Link to={`/product/${suggested.id}`} className="flex-shrink-0">
                                  <Button variant="outlineLight">Ver detalles</Button>
                                </Link>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              )
            })}
          </div>
          <div className="p-4 border-t border-neutral-200">
            <form className="flex items-center gap-3" onSubmit={handleSubmit}>
              <Input name="message" placeholder="Escribe tu consulta: producto, categoría o presupuesto" className="flex-grow" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
              <Button type="submit" variant="primary">Enviar</Button>
            </form>
          </div>
        </div>
        <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: 'info' })} />
      </main>
    </div>
  )
}
/**
 * Página de chat.
 * Muestra conversaciones y mensajes en tiempo real.
  */
