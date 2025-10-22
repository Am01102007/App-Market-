package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus;
import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    List<Product> findByUser_Username(String username);
    List<Product> findByCategory(String category);
    List<Product> findByStatus(ProductStatus status); 
}
