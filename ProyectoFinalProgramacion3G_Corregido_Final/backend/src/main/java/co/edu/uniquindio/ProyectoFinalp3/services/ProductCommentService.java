/**
 * Servicio de comentarios de productos.
 * Gestiona creación, listado y moderación básica.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import co.edu.uniquindio.ProyectoFinalp3.models.ProductComment;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.ProductCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductCommentService {

    @Autowired
    private ProductCommentRepository productCommentRepository;

    // Agregar un comentario a un producto
    public ProductComment addCommentToProduct(UUID productId, UUID userId, String commentText) {
        ProductComment comment = new ProductComment(commentText, new Product(productId), new User(userId));
        return productCommentRepository.save(comment);
    }

    // Obtener todos los comentarios de un producto específico
    public List<ProductComment> getCommentsByProduct(UUID productId) {
        return productCommentRepository.findByProductId(productId);
    }
}
