/**
 * Servicio de almacenamiento de imágenes.
 * Maneja subida, obtención y borrado de imágenes.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "uploads/images/";
    private static final String PUBLIC_DIR = "frontend/public/images/";

    public String storeImage(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Crear directorio si no existe
        Path uploadPath = Paths.get(PUBLIC_DIR + folder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único para el archivo
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String filename = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(filename);
        
        // Guardar el archivo
        Files.copy(file.getInputStream(), filePath);
        
        // Retornar la ruta relativa para usar en la aplicación
        return "/images/" + folder + "/" + filename;
    }

    public void deleteImage(String imagePath) throws IOException {
        if (imagePath != null && !imagePath.isEmpty() && !imagePath.startsWith("http")) {
            Path filePath = Paths.get("frontend/public" + imagePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }
    }
}
