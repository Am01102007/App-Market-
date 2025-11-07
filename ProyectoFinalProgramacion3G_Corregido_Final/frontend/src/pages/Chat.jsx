import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import Header from '../components/Header'
import { useState } from 'react'
import api from '../lib/api'

export default function Chat() {
  const [messages, setMessages] = useState([
    { id: 1, sender: 'Asistente', content: 'Hola 👋 ¿En qué puedo ayudarte a comprar hoy?' },
  ])
  const [newMessage, setNewMessage] = useState('')

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
            {messages.map((msg) => (
              <div key={msg.id} className={msg.sender === 'Tú' ? 'flex justify-end' : 'flex justify-start'}>
                <div className={msg.sender === 'Tú' ? 'bg-neutral-200 text-neutral-900 rounded-lg p-3 max-w-xs' : 'bg-primary-light text-primary-dark rounded-lg p-3 max-w-xs'}>
                  <p><strong>{msg.sender}:</strong> {msg.content}</p>
                </div>
              </div>
            ))}
          </div>
          <div className="p-4 border-t border-neutral-200">
            <form className="flex items-center gap-3" onSubmit={handleSubmit}>
              <Input name="message" placeholder="Escribe un mensaje..." className="flex-grow" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
              <Button type="submit" variant="primary">Enviar</Button>
            </form>
          </div>
        </div>
      </main>
    </div>
  )
}
/**
 * Página de chat.
 * Muestra conversaciones y mensajes en tiempo real.
 */
