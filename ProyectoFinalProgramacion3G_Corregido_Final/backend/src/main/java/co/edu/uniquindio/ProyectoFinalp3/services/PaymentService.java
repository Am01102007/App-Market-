/**
 * Servicio de pagos.
 * Procesa transacciones y actualiza estados de pago.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.models.Order;
import co.edu.uniquindio.ProyectoFinalp3.models.Payment;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.enums.OrderStatus;
import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentMethod;
import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentStatus;
import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentType;
import co.edu.uniquindio.ProyectoFinalp3.repository.OrderRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.PaymentRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Método para procesar un pago, se requiere el username del usuario y el orderId del pedido
    @Transactional
    public Payment processPayment(String username, UUID orderId, BigDecimal amount, PaymentType paymentType, PaymentMethod paymentMethod) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (amount.compareTo(order.getTotalAmount()) < 0) {
            throw new IllegalArgumentException("Amount is less than the order total amount.");
        }

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentDate(new Date());
        payment.setPaymentType(paymentType);
        payment.setPaymentMethod(paymentMethod);
        payment.setOrder(order);
        payment.setUser(user);
        payment.setStatus(PaymentStatus.COMPLETED);

        // Verificar si la transición de estado es válida antes de actualizar la orden
        if (order.getStatus().canTransitionTo(OrderStatus.CONFIRMED)) {
            order.setStatus(OrderStatus.CONFIRMED); // Actualizar el estado del pedido
            orderRepository.save(order); // Guardar el cambio en el pedido
        }

        return paymentRepository.save(payment);
    }

    // Método para obtener todos los pagos asociados a un usuario específico mediante su username
    public List<Payment> getPaymentsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return paymentRepository.findByUser(user);
    }
}
