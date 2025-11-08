/**
 * Servicio de órdenes.
 * Coordina creación, pago y estados de órdenes de compra.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.dto.OrderItemRequest;
import co.edu.uniquindio.ProyectoFinalp3.enums.OrderStatus;
import co.edu.uniquindio.ProyectoFinalp3.models.Order;
import co.edu.uniquindio.ProyectoFinalp3.models.OrderItem;
import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.OrderRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.ProductRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(String username, List<OrderItemRequest> orderItemsRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderNumber(generateOrderNumber());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderItemsRequest) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.getProductId()));

            // Validar stock disponible
            Integer currentQty = product.getAvailableQuantity() == null ? 0 : product.getAvailableQuantity();
            int requested = Math.max(1, itemRequest.getQuantity());
            if (currentQty < requested) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = new OrderItem(itemRequest.getQuantity(), product);
            orderItem.setOrder(order); // Asocia el OrderItem con el Order
            order.getOrderItems().add(orderItem); // Añade el OrderItem a la lista

            // Decrementar stock
            product.setAvailableQuantity(currentQty - requested);
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Verificar si la transición de estado es válida
        if (!order.getStatus().canTransitionTo(status)) {
            throw new IllegalArgumentException("Invalid status transition from " + 
                order.getStatus() + " to " + status);
        }

        if (status == OrderStatus.CANCELLED) {
            // Elimina la orden si el estado es "CANCELLED"
            orderRepository.delete(order);
            return null; // o lanza una excepción si prefieres manejar el caso en el controlador
        } else {
            // Si no está cancelada, solo actualiza el estado
            order.setStatus(status);
            return orderRepository.save(order);
        }
    }

    public List<Order> getOrdersByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return orderRepository.findByUser(user);
    }
}
