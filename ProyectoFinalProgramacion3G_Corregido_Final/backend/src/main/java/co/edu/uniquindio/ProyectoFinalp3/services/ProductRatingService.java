package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import co.edu.uniquindio.ProyectoFinalp3.models.ProductRating;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.ProductRatingRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.ProductRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductRatingService {

    @Autowired
    private ProductRatingRepository ratingRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getSummary(UUID productId) {
        Double avg = ratingRepository.getAverageByProductId(productId);
        Long count = ratingRepository.getCountByProductId(productId);
        Map<String, Object> res = new HashMap<>();
        res.put("average", avg == null ? 0.0 : avg);
        res.put("count", count == null ? 0L : count);
        return res;
    }

    public Map<String, Object> submit(UUID productId, UUID userId, int stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5");
        }
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado");
        }

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        // Upsert por userId: si existe, actualizar; si no, crear
        if (user != null) {
            Optional<ProductRating> existing = ratingRepository.findByProduct_IdAndUser_Id(productId, user.getId());
            if (existing.isPresent()) {
                ProductRating r = existing.get();
                r.setStars(stars);
                ratingRepository.save(r);
            } else {
                ProductRating rating = new ProductRating(productOpt.get(), user, stars);
                ratingRepository.save(rating);
            }
        } else {
            // Calificación anónima (sin usuario) siempre crea nueva entrada
            ProductRating rating = new ProductRating(productOpt.get(), null, stars);
            ratingRepository.save(rating);
        }
        return getSummary(productId);
    }

    /**
     * Upsert por username: busca al usuario por nombre y crea/actualiza su calificación.
     */
    public Map<String, Object> submitByUsername(UUID productId, String username, int stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        if (username == null || username.isBlank()) {
            // si no hay username, tratamos como calificación anónima
            ProductRating rating = new ProductRating(product, null, stars);
            ratingRepository.save(rating);
            return getSummary(productId);
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));

        Optional<ProductRating> existing = ratingRepository.findByProduct_IdAndUser_Username(productId, username);
        if (existing.isPresent()) {
            ProductRating r = existing.get();
            r.setStars(stars);
            ratingRepository.save(r);
        } else {
            ProductRating rating = new ProductRating(product, user, stars);
            ratingRepository.save(rating);
        }
        return getSummary(productId);
    }

    /**
     * Obtiene la calificación del usuario para un producto. Devuelve { stars } o { stars: 0 } si no hay calificación.
     */
    public Map<String, Object> getMyRating(UUID productId, String username) {
        Map<String, Object> res = new HashMap<>();
        if (username == null || username.isBlank()) {
            res.put("stars", 0);
            return res;
        }
        Optional<ProductRating> existing = ratingRepository.findByProduct_IdAndUser_Username(productId, username);
        res.put("stars", existing.map(ProductRating::getStars).orElse(0));
        return res;
    }
}
