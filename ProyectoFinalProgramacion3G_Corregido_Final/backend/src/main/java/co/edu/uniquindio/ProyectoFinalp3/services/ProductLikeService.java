/**
 * Servicio de reacciones a productos.
 * Administra creación y conteo de "me gusta".
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import co.edu.uniquindio.ProyectoFinalp3.models.ProductLike;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.ProductLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductLikeService {

    @Autowired
    private ProductLikeRepository productLikeRepository;

    // Agregar un "like" a un producto. Si ya existe, no lo agrega de nuevo.
    public boolean addLikeToProduct(UUID productId, UUID userId) {
        Optional<ProductLike> existingLike = productLikeRepository.findByProductIdAndUserId(productId, userId);
        if (existingLike.isPresent()) {
            return false; // El usuario ya ha dado "like"
        } else {
            ProductLike like = new ProductLike(new Product(productId), new User(userId));
            productLikeRepository.save(like);
            return true; // Like agregado exitosamente
        }
    }

    // Contar el número de "likes" de un producto específico
    public long getLikesCountByProduct(UUID productId) {
        return productLikeRepository.countByProductId(productId);
    }
}
