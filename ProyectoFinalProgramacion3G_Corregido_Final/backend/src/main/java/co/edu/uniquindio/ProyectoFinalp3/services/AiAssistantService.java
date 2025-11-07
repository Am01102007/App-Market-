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
        // Construir contexto con catálogo activo/available
        List<Product> catalog = productService.getActiveProducts();
        String catalogSummary = catalog.stream()
                .limit(12)
                .map(p -> String.format("- %s (%.2f) [%s]", p.getName(), p.getPrice(), p.getCategory() != null ? p.getCategory().getName() : "sin categoría"))
                .collect(Collectors.joining("\n"));

        String systemPrompt = "Eres un asistente de compras para AppMarket. " +
                "Ayuda al usuario a: descubrir productos, comparar opciones, y decidir qué añadir al carrito. " +
                "Responde breve, clara y con pasos accionables. Si el usuario pide añadir al carrito, sugiere nombre del producto y cantidad. " +
                "Catálogo disponible (muestra parcial):\n" + catalogSummary;

        // Construir JSON para Groq Chat Completions (API compatible tipo OpenAI)
        ObjectNode root = objectMapper.createObjectNode();
        root.put("model", "llama-3.1-8b-instant");
        root.put("temperature", 0.3);
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
            throw new RuntimeException("Groq API error: status=" + response.statusCode() + " body=" + response.body());
        }
        JsonNode json = objectMapper.readTree(response.body());
        JsonNode content = json.path("choices").path(0).path("message").path("content");
        return content.isMissingNode() ? "No se pudo obtener respuesta del asistente." : content.asText();
    }
}

