package co.edu.uniquindio.ProyectoFinalp3.controllers;

import co.edu.uniquindio.ProyectoFinalp3.models.Order;
import co.edu.uniquindio.ProyectoFinalp3.dto.OrderItemRequest;
import co.edu.uniquindio.ProyectoFinalp3.services.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Endpoint para crear un pedido utilizando el username y una lista de productos
    @PostMapping("/{username}")
    public ResponseEntity<Order> createOrder(@PathVariable String username, @RequestBody List<OrderItemRequest> orderItemsRequest) {
        Order order = orderService.createOrder(username, orderItemsRequest);
        return ResponseEntity.ok(order);
    }

    // Endpoint para obtener un pedido por su ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar el estado de un pedido
    @PatchMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable UUID orderId, @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Endpoint para obtener todos los pedidos de un usuario específico utilizando su username
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Order>> getOrdersByUsername(@PathVariable String username) {
        List<Order> orders = orderService.getOrdersByUsername(username);
        return ResponseEntity.ok(orders);
    }
}
