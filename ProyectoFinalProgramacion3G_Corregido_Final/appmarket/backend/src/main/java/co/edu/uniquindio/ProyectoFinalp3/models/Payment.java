package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentMethod;
import co.edu.uniquindio.ProyectoFinalp3.enums.PaymentType;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "status") // Ensure the correct column name is here
    private String status;

    private BigDecimal amount;
    private Date paymentDate;

    @Enumerated(EnumType.STRING) // Esto asegura que se almacene como un string en la base de datos
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id") // Relación con el modelo User
    private User user;

    // Constructor vacío
    public Payment() {
    }

    // Constructor con parámetros
    public Payment(BigDecimal amount, Date paymentDate, PaymentType paymentType,
            PaymentMethod paymentMethod, Order order, User user) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.user = user;
    }

    // Getter y Setter
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}