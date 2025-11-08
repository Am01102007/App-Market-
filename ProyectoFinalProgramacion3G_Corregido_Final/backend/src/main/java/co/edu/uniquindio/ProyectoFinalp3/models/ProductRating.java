package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidad para almacenar calificaciones de productos (1-5 estrellas).
 */
@Entity
@Table(name = "product_ratings")
public class ProductRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    /** Producto calificado */
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Usuario que califica (opcional para permitir calificación anónima) */
    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    /** Número de estrellas (1..5) */
    @Column(nullable = false)
    private int stars;

    /** Fecha de creación */
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public ProductRating() {}

    public ProductRating(Product product, User user, int stars) {
        this.product = product;
        this.user = user;
        this.stars = stars;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

