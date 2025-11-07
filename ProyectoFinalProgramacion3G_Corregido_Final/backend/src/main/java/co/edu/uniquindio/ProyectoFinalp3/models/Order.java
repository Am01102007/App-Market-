/**
 * Modelo de orden.
 * Registra compra, items, estado y montos.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import co.edu.uniquindio.ProyectoFinalp3.enums.OrderStatus;

/**
 * Entidad que representa una orden de compra en el marketplace.
 * 
 * Una orden agrupa uno o más productos que un usuario desea comprar,
 * incluyendo información sobre el estado, monto total y items individuales.
 * 
 * Características principales:
 * - Identificador único UUID
 * - Estado de la orden (PENDING, CONFIRMED, etc.)
 * - Número de orden único
 * - Monto total calculado
 * - Relación con usuario comprador
 * - Lista de items de la orden
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * Identificador único de la orden.
     * Se genera automáticamente como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    /**
     * Estado actual de la orden.
     * Utiliza enum para garantizar valores válidos.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * Número de orden único para identificación externa.
     * Útil para referencias de usuario y seguimiento.
     */
    @Column(name = "order_number", unique = true)
    private String orderNumber;
    
    /**
     * Monto total de la orden.
     * Suma de todos los items incluidos en la orden.
     */
    @Column(name = "total_amount", nullable = false, precision = 13, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * Usuario que realizó la orden.
     * Relación muchos-a-uno con la entidad User.
     * Se excluye de la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * Lista de items que componen la orden.
     * Relación uno-a-muchos con la entidad OrderItem.
     * Se excluye de la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public Order() {}

    /**
     * Constructor para crear una orden con información básica.
     * 
     * @param orderNumber Número único de la orden
     * @param totalAmount Monto total de la orden
     * @param user Usuario que realiza la orden
     */
    public Order(String orderNumber, BigDecimal totalAmount, User user) {
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.user = user;
        this.status = OrderStatus.PENDING; // Estado inicial por defecto
        this.orderItems = new ArrayList<>();
    }

    // Getters y Setters con documentación

    /**
     * Obtiene el ID único de la orden.
     * @return UUID que identifica únicamente la orden
     */
    public UUID getId() { return id; }
    
    /**
     * Establece el ID de la orden.
     * @param id Identificador único de la orden
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Obtiene el estado actual de la orden.
     * @return Estado de la orden (PENDING, CONFIRMED, etc.)
     */
    public OrderStatus getStatus() { return status; }
    
    /**
     * Establece el estado de la orden.
     * @param status Nuevo estado de la orden
     */
    public void setStatus(OrderStatus status) { this.status = status; }

    /**
     * Obtiene el número de orden.
     * @return Número único de identificación de la orden
     */
    public String getOrderNumber() { return orderNumber; }
    
    /**
     * Establece el número de orden.
     * @param orderNumber Número único de la orden
     */
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    /**
     * Obtiene el monto total de la orden.
     * @return Monto total calculado de todos los items
     */
    public BigDecimal getTotalAmount() { return totalAmount; }
    
    /**
     * Establece el monto total de la orden.
     * @param totalAmount Monto total de la orden
     */
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    /**
     * Obtiene el usuario propietario de la orden.
     * @return Usuario que realizó la orden
     */
    public User getUser() { return user; }
    
    /**
     * Establece el usuario propietario de la orden.
     * @param user Usuario que realiza la orden
     */
    public void setUser(User user) { this.user = user; }

    /**
     * Obtiene la lista de items de la orden.
     * @return Lista de productos incluidos en la orden
     */
    public List<OrderItem> getOrderItems() { return orderItems; }
    
    /**
     * Establece la lista de items de la orden.
     * @param orderItems Lista de items de la orden
     */
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
