/**
 * Controlador de pagos.
 * Maneja procesos de pago, estados y notificaciones.
 */
package co.edu.uniquindio.ProyectoFinalp3.controllers;

import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentMethod;
import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentType;
import co.edu.uniquindio.ProyectoFinalp3.models.Payment;
import co.edu.uniquindio.ProyectoFinalp3.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Endpoint para consultar pagos específicos mediante el username del usuario
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Payment>> getPaymentsByUsername(@PathVariable String username) {
        List<Payment> payments = paymentService.getPaymentsByUsername(username);
        return ResponseEntity.ok(payments);
    }
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(
        @RequestParam String username,
        @RequestParam UUID orderId,
        @RequestParam BigDecimal amount,
        @RequestParam PaymentType paymentType,
        @RequestParam PaymentMethod paymentMethod) {
    Payment payment = paymentService.processPayment(username, orderId, amount, paymentType, paymentMethod);
    return ResponseEntity.ok(payment);
}
}
