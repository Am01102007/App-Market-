/**
 * Controlador proxy de imágenes.
 * Sirve imágenes externas aplicando reglas de seguridad y caché.
 */
package co.edu.uniquindio.ProyectoFinalp3.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Controlador proxy para manejar imágenes externas.
 * Permite obtener imágenes de URLs externas evitando problemas de CORS.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@RestController
@RequestMapping("/api/proxy")
@CrossOrigin(origins = "*")
public class ImageProxyController {

    /**
     * Obtiene una imagen desde una URL externa y la devuelve como proxy.
     * Esto evita problemas de CORS al cargar imágenes desde dominios externos.
     * 
     * @param url La URL de la imagen a obtener
     * @return Los bytes de la imagen con headers apropiados
     */
    @GetMapping("/image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
        try {
            // Validar que la URL sea válida
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            
            // Configurar headers para evitar bloqueos
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Pragma", "no-cache");
            
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            // Verificar el código de respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return ResponseEntity.status(responseCode).build();
            }
            
            // Leer la imagen
            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                
                byte[] imageBytes = outputStream.toByteArray();
                
                if (imageBytes.length == 0) {
                    return ResponseEntity.notFound().build();
                }
                
                // Determinar el tipo de contenido
                String contentType = connection.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    contentType = "image/jpeg"; // Default
                }
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(contentType));
                headers.setCacheControl("public, max-age=3600"); // Cache por 1 hora
                headers.add("Access-Control-Allow-Origin", "*");
                
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            System.err.println("Error proxying image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
