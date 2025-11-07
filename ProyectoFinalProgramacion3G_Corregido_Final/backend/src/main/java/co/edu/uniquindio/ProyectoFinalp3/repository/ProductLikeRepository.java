/**
 * Repositorio de reacciones a productos.
 * Administra persistencia y conteo de "me gusta".
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, UUID> {

    // Contar los "likes" de un producto específico
    long countByProductId(UUID productId);

    // Buscar si un usuario ya dio "like" a un producto específico
    Optional<ProductLike> findByProductIdAndUserId(UUID productId, UUID userId);
}
