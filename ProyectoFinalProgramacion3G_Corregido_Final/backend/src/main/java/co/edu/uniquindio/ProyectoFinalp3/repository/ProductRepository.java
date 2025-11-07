/**
 * Repositorio de productos.
 * Soporta consultas por estado, categoría y vendedor.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus;
import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    @Query("SELECT p FROM Product p WHERE p.user.username = :username")
    List<Product> findByUser_Username(@Param("username") String username);
    @Query("SELECT p FROM Product p WHERE p.category.name = :category")
    List<Product> findByCategory(@Param("category") String category);
    List<Product> findByStatus(ProductStatus status);
    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> findByCategory_Name(@Param("categoryName") String categoryName);
    
    // Métodos de búsqueda por texto
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findByNameOrDescriptionContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT p FROM Product p WHERE p.status = :status AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Product> findByStatusAndNameOrDescriptionContainingIgnoreCase(@Param("status") ProductStatus status, @Param("searchTerm") String searchTerm);
}
