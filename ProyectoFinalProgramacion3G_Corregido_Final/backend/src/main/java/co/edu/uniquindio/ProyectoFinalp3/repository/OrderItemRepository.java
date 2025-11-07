/**
 * Repositorio de items de orden.
 * Maneja persistencia y consultas de detalle de órdenes.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    // Encuentra todos los elementos de una orden específica
    List<OrderItem> findByOrderId(UUID orderId);
}
