/**
 * Repositorio de comentarios de producto.
 * Gestiona almacenamiento y consultas por producto.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductCommentRepository extends JpaRepository<ProductComment, UUID> {

    // Obtener todos los comentarios de un producto específico
    List<ProductComment> findByProductId(UUID productId);

    // Obtener todos los comentarios realizados por un usuario específico
    List<ProductComment> findByUserId(UUID userId);
}
