/**
 * Repositorio de órdenes.
 * Provee consultas por estado, usuario y fechas.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.enums.OrderStatus;
import co.edu.uniquindio.ProyectoFinalp3.models.Order;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    // Encuentra todas las órdenes de un usuario específico
    List<Order> findByUser(User user);

    // Método adicional si necesitas buscar órdenes por estado
    List<Order> findByUserAndStatus(User user, OrderStatus status);
}
