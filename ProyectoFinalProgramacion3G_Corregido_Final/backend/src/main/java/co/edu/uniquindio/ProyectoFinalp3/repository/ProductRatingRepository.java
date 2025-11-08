package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, UUID> {

    @Query("SELECT AVG(r.stars) FROM ProductRating r WHERE r.product.id = :productId")
    Double getAverageByProductId(@Param("productId") UUID productId);

    @Query("SELECT COUNT(r) FROM ProductRating r WHERE r.product.id = :productId")
    Long getCountByProductId(@Param("productId") UUID productId);

    List<ProductRating> findByProduct_Id(UUID productId);

    boolean existsByProduct_IdAndUser_Id(UUID productId, UUID userId);

    // Buscar la calificación de un usuario específico por username
    java.util.Optional<ProductRating> findByProduct_IdAndUser_Username(UUID productId, String username);

    // Buscar la calificación por combinación productId + userId
    java.util.Optional<ProductRating> findByProduct_IdAndUser_Id(UUID productId, UUID userId);
}
