package co.edu.uniquindio.ProyectoFinalp3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la página de inicio de la API.
 * Proporciona información básica sobre el estado y endpoints disponibles.
 */
@RestController
public class HomeController {

    /**
     * Endpoint raíz que proporciona información básica de la API.
     * 
     * @return Información sobre la API y sus endpoints principales
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "AppMarket Backend API");
        response.put("version", "1.0.0");
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