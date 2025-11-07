/**
 * Modelo de pago.
 * Almacena método, estado, monto y referencias.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentMethod;
import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentStatus;
import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentType;

/**
 * Entidad que representa un pago realizado en el marketplace.
 * 
 * Un pago está asociado a una orden específica y contiene información
 * sobre el monto, método de pago, estado y fechas de transacción.
 * 
 * Características principales:
 * - Identificador único UUID
 * - Estado del pago (PENDING, COMPLETED, etc.)
 * - Monto y fecha de pago
 * - Método y tipo de pago
 * - Relación con orden y usuario
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "payments")
public class Payment {

    /**
     * Identificador único del pago.
     * Se genera automáticamente como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    /**
     * Estado actual del pago.
     * Utiliza enum para garantizar valores válidos.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    /**
     * Monto del pago.
     * Debe coincidir con el monto total de la orden.
     */
    @Column(name = "amount", nullable = false, precision = 13, scale = 2)
    private BigDecimal amount;
    
    /**
     * Fecha y hora en que se realizó el pago.
     */
    @Column(name = "payment_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    /**
     * Tipo de pago realizado.
     * Utiliza enum para garantizar valores válidos.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    /**
     * Método de pago utilizado.
     * Utiliza enum para garantizar valores válidos.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    /**
     * ID de transacción externa del proveedor de pagos.
     * Útil para rastrear la transacción en sistemas externos.
     */
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    /**
     * Orden asociada al pago.
     * Relación muchos-a-uno con la entidad Order.
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    /**
     * Usuario que realizó el pago.
     * Relación muchos-a-uno con la entidad User.
     * Se excluye de la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Payment() {
        this.status = PaymentStatus.PENDING; // Estado inicial por defecto
    }

    /**
     * Constructor para crear un pago con información básica.
     * 
     * @param amount Monto del pago
     * @param paymentDate Fecha del pago
     * @param paymentType Tipo de pago
     * @param paymentMethod Método de pago
     * @param order Orden asociada
     * @param user Usuario que paga
     */
    public Payment(BigDecimal amount, Date paymentDate, PaymentType paymentType,
            PaymentMethod paymentMethod, Order order, User user) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.user = user;
        this.status = PaymentStatus.PENDING;
    }

    // Getters y Setters con documentación

    /**
     * Obtiene el identificador único del pago.
     * @return UUID del pago
     */
    public UUID getId() {
        return id;
    }

    /**
     * Establece el identificador único del pago.
     * @param id UUID del pago
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Obtiene el estado actual del pago.
     * @return Estado del pago
     */
    public PaymentStatus getStatus() {
        return status;
    }

    /**
     * Establece el estado del pago.
     * @param status Nuevo estado del pago
     */
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    /**
     * Obtiene el monto del pago.
     * @return Monto del pago
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Establece el monto del pago.
     * @param amount Monto del pago
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Obtiene la fecha del pago.
     * @return Fecha del pago
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * Establece la fecha del pago.
     * @param paymentDate Fecha del pago
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Obtiene el tipo de pago.
     * @return Tipo de pago
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * Establece el tipo de pago.
     * @param paymentType Tipo de pago
     */
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * Obtiene el método de pago.
     * @return Método de pago
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Establece el método de pago.
     * @param paymentMethod Método de pago
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Obtiene el ID de transacción externa.
     * @return ID de transacción
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Establece el ID de transacción externa.
     * @param transactionId ID de transacción
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Obtiene la orden asociada al pago.
     * @return Orden asociada
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Establece la orden asociada al pago.
     * @param order Orden asociada
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Obtiene el usuario que realizó el pago.
     * @return Usuario que paga
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece el usuario que realizó el pago.
     * @param user Usuario que paga
     */
    public void setUser(User user) {
        this.user = user;
    }
}
