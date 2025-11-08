package co.edu.uniquindio.ProyectoFinalp3.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AiAssistantService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ProductService productService;

    @Value("${GROQ_API_KEY:}")
    private String groqApiKey;

    public AiAssistantService(ProductService productService) {
        this.productService = productService;
    }

    public String chat(String userMessage) throws Exception {
        // Construir contexto con catálogo activo/available desde la base real (Neon)
        List<Product> catalog = productService.getActiveProducts(ProductStatus.ACTIVE);

        // Si el catálogo está vacío, responder determinísticamente sin inventar datos
        if (catalog == null || catalog.isEmpty()) {
            return "No hay productos activos actualmente en el catálogo.";
        }

        String catalogSummary = catalog.stream()
                .limit(12)
                .map(p -> String.format("- %s (%.2f) [%s]", p.getName(), p.getPrice(), p.getCategory() != null ? p.getCategory().getName() : "sin categoría"))
                .collect(Collectors.joining("\n"));

        // Respuesta segura para consultas de disponibilidad/listado: nunca inventar
        String lower = userMessage == null ? "" : userMessage.toLowerCase(Locale.ROOT);
        boolean asksForList = lower.contains("disponible") || lower.contains("disponibles")
                || lower.contains("que tienes") || lower.contains("tienes")
                || lower.contains("catálogo") || lower.contains("catalogo")
                || lower.contains("lista") || lower.contains("productos");
        if (asksForList) {
            return "En AppMarket hay estos productos activos:\n" + catalogSummary +
                    "\n¿Cuál te interesa? Puedes pedirme: 'añadir <nombre> al carrito'.";
        }

        // Fallback local si no hay API key configurada
        if (groqApiKey == null || groqApiKey.isBlank()) {
            // Heurística: si pide "añadir" o "agregar", intenta sugerir producto coincidente
            if (lower.contains("añadir") || lower.contains("agregar") || lower.contains("carrito")) {
                Product match = catalog.stream()
                        .filter(p -> lower.contains(p.getName().toLowerCase(Locale.ROOT)))
                        .findFirst()
                        .orElse(catalog.stream().findFirst().orElse(null));
                if (match != null) {
                    return String.format("Puedes añadir '%s' (%.2f) a tu carrito. ¿Cantidad 1-3?", match.getName(), match.getPrice());
                }
            }
            // Respuesta por defecto basada en catálogo
            return "(Modo offline) Te ayudo a explorar el catálogo. Algunos destacados:\n" + catalogSummary +
                    "\nPide: 'recomienda portátiles' o 'añadir <producto> al carrito'.";
        }

        String systemPrompt = "Eres un asistente de compras para AppMarket. " +
                "Ayuda al usuario a: descubrir productos, comparar opciones, y decidir qué añadir al carrito. " +
                "Responde breve, clara y con pasos accionables. Si el usuario pide añadir al carrito, sugiere nombre del producto y cantidad. " +
                "Regla crítica: NO inventes productos ni precios. SOLO menciona ítems presentes en el catálogo listado. " +
                "Si un producto no aparece en ese catálogo, informa que no hay datos. " +
                "Catálogo disponible (muestra parcial, fuente DB):\n" + catalogSummary;

        // Construir JSON para Groq Chat Completions (API compatible tipo OpenAI)
        ObjectNode root = objectMapper.createObjectNode();
        root.put("model", "llama-3.1-8b-instant");
        // Temperatura baja para evitar respuestas creativas que inventen datos
        root.put("temperature", 0.0);
        ArrayNode messages = root.putArray("messages");
        ObjectNode sys = messages.addObject();
        sys.put("role", "system");
        sys.put("content", systemPrompt);
        ObjectNode usr = messages.addObject();
        usr.put("role", "user");
        usr.put("content", userMessage);

        String body = objectMapper.writeValueAsString(root);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Authorization", "Bearer " + groqApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            // Devolver un mensaje amable sin romper el flujo
            return "No pude contactar al asistente externo (" + response.statusCode() + "). Basado en el catálogo: \n" + catalogSummary;
        }
        JsonNode json = objectMapper.readTree(response.body());
        JsonNode content = json.path("choices").path(0).path("message").path("content");
        return content.isMissingNode() ? "No se pudo obtener respuesta del asistente." : content.asText();
    }
}
