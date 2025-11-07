/**
 * Repositorio de pagos.
 * Permite consultas por estado y orden asociada.
 */
package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentStatus;
import co.edu.uniquindio.ProyectoFinalp3.models.Payment;
import co.edu.uniquindio.ProyectoFinalp3.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // Encuentra todos los pagos de un usuario específico
    List<Payment> findByUser(User user);

    // Encuentra todos los pagos por estado
    List<Payment> findByStatus(PaymentStatus status);
}
