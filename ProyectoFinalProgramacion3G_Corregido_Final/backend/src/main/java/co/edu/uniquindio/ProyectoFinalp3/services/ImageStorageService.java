/**
 * Servicio de almacenamiento de imágenes.
 * Maneja subida, obtención y borrado de imágenes.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "uploads/images/";

    private final Cloudinary cloudinary;

    public ImageStorageService() {
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");
        if (cloudinaryUrl != null && !cloudinaryUrl.isBlank()) {
            this.cloudinary = new Cloudinary(cloudinaryUrl);
            return;
        }

        String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
        String apiKey = System.getenv("CLOUDINARY_API_KEY");
        String apiSecret = System.getenv("CLOUDINARY_API_SECRET");
        if (cloudName != null && apiKey != null && apiSecret != null
                && !cloudName.isBlank() && !apiKey.isBlank() && !apiSecret.isBlank()) {
            this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
            ));
        } else {
            this.cloudinary = null;
        }
    }

    public String storeImage(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Si Cloudinary está configurado, subir a la nube
        if (cloudinary != null) {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folder
            ));
            Object secureUrl = result.get("secure_url");
            Object url = result.get("url");
            return secureUrl != null ? secureUrl.toString() : (url != null ? url.toString() : null);
        }

        // Fallback a almacenamiento local
        Path uploadPath = Paths.get(UPLOAD_DIR + folder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);
        return "/images/" + folder + "/" + filename;
    }

    public void deleteImage(String imagePath) throws IOException {
        if (imagePath == null || imagePath.isEmpty()) return;

        // Si Cloudinary está configurado y la imagen es remota, intentar borrar en Cloudinary
        if (cloudinary != null && imagePath.startsWith("http") && imagePath.contains("/upload/")) {
            String publicId = extractPublicId(imagePath);
            if (publicId != null && !publicId.isBlank()) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                return;
            }
        }

        // Fallback: borrar del disco local si es una ruta relativa
        if (!imagePath.startsWith("http")) {
            String relative = imagePath.replaceFirst("^/images/", "");
            Path filePath = Paths.get(UPLOAD_DIR + relative);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }
    }

    // Extraer public_id desde la URL de Cloudinary: .../upload/v123/<folder>/<name>.<ext>
    private String extractPublicId(String imageUrl) {
        try {
            int idx = imageUrl.indexOf("/upload/");
            if (idx == -1) return null;
            String after = imageUrl.substring(idx + "/upload/".length());
            // Quitar segmento de versión si existe (v12345/)
            if (after.startsWith("v")) {
                int slash = after.indexOf('/');
                if (slash != -1) after = after.substring(slash + 1);
            }
            int dot = after.lastIndexOf('.');
            if (dot != -1) after = after.substring(0, dot);
            return after;
        } catch (Exception e) {
            return null;
        }
    }
}
