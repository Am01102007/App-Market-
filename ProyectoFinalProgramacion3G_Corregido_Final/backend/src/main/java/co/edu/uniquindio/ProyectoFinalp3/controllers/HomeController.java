package co.edu.uniquindio.ProyectoFinalp3.controllers;
// Controlador Home: endpoints básicos y verificación del servicio.

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador que expone información básica de la API.
 * Informa estado y rutas principales disponibles.
 */
@RestController
public class HomeController {

    /**
     * Punto de entrada informativo de la API.
     * Evita interceptar "/" para que el SPA gestione la ruta raíz.
     * 
     * @return datos de estado y rutas principales
     */
    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> apiRoot() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "AppMarket Backend API");
        response.put("version", "1.0.3");
        response.put("status", "running");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("authentication", "/api/auth");
        endpoints.put("users", "/api/users");
        endpoints.put("products", "/api/products");
        endpoints.put("orders", "/api/orders");
        endpoints.put("contacts", "/contacts");
        endpoints.put("payments", "/payments");
        
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de salud para verificar que la API está funcionando.
     * 
     * @return Estado de salud de la API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "API is healthy");
        return ResponseEntity.ok(response);
    }
}
