import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import Header from '../components/Header'

export default function Chat() {
  return (
    <div className="min-h-screen bg-neutral-100 flex flex-col">
      <Header />
      <main className="flex-grow max-w-4xl w-full mx-auto p-4 flex flex-col">
        <div className="bg-white border border-neutral-200 rounded-lg shadow-sm flex-grow flex flex-col">
          <div className="p-4 border-b border-neutral-200">
            <h1 className="text-xl font-semibold text-neutral-900">Chat</h1>
          </div>
          <div className="flex-grow p-4 space-y-4 overflow-y-auto">
            {/* Placeholder for chat messages */}
            <div className="flex justify-start">
              <div className="bg-primary-light text-primary-dark rounded-lg p-3 max-w-xs">
                <p>Hola! Estoy interesado en tu producto.</p>
              </div>
            </div>
            <div className="flex justify-end">
              <div className="bg-neutral-200 text-neutral-900 rounded-lg p-3 max-w-xs">
                <p>¡Hola! Claro, ¿en qué puedo ayudarte?</p>
              </div>
            </div>
          </div>
          <div className="p-4 border-t border-neutral-200">
            <form className="flex items-center gap-3">
              <Input name="message" placeholder="Escribe un mensaje..." className="flex-grow" />
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
