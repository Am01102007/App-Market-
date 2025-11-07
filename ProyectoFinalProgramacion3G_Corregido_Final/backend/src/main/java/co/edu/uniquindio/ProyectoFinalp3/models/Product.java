/**
 * Modelo de producto.
 * Describe atributos, estado y relaciones con usuario y categoría.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus;

/**
 * Entidad que representa un producto en el marketplace.
 * Contiene toda la información relacionada con los productos que los usuarios
 * pueden publicar, comprar y gestionar en la plataforma.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * Identificador único del producto.
     * Se genera automáticamente como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    /**
     * Nombre del producto.
     * Campo obligatorio que identifica el producto.
     */
    private String name;
    
    /**
     * URL de la imagen del producto.
     * Almacena la ruta o URL donde se encuentra la imagen del producto.
     */
    private String imageUrl;
    
    /**
     * Descripción detallada del producto.
     * Proporciona información adicional sobre las características del producto.
     */
    private String description;
    
    /**
     * Precio del producto.
     * Utiliza BigDecimal para precisión en cálculos monetarios.
     */
    private BigDecimal price;

    /**
     * Cantidad disponible en stock para el producto.
     * Permite controlar el inventario y limitar compras.
     */
    @Column(name = "available_quantity")
    private Integer availableQuantity;

    /**
     * Categoría a la que pertenece el producto.
     * Relación muchos-a-uno con la entidad Category.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Estado actual del producto (ACTIVE, SOLD, INACTIVE).
     * Determina la disponibilidad del producto en el marketplace.
     */
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    /**
     * Usuario propietario del producto.
     * Relación muchos-a-uno con la entidad User.
     * Se excluye de la serialización JSON para evitar referencias circulares.
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    
    /**
     * Marketplace donde está publicado el producto.
     * Relación muchos-a-uno con la entidad MarketPlace.
     * Campo obligatorio (NOT NULL).
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "market_place_id", nullable = false)
    private MarketPlace marketPlace;

    /**
     * Lista de comentarios asociados al producto.
     * Relación uno-a-muchos con la entidad ProductComment.
     * Se excluye de la serialización JSON para evitar referencias circulares.
     */
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductComment> comments;

    /**
     * Lista de likes/me gusta del producto.
     * Relación uno-a-muchos con la entidad ProductLike.
     * Se excluye de la serialización JSON para evitar referencias circulares.
     */
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductLike> likes;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Product() {}
    
    /**
     * Constructor que inicializa solo el ID del producto.
     * Útil para referencias rápidas cuando solo se conoce el ID.
     * 
     * @param id Identificador único del producto
     */
    public Product(UUID id) {
        this.id = id;
    }

    /**
     * Constructor completo para crear un producto con todos los campos principales.
     * 
     * @param name Nombre del producto
     * @param imageUrl URL de la imagen del producto
     * @param category Categoría del producto
     * @param price Precio del producto
     * @param status Estado del producto
     * @param user Usuario propietario del producto
     * @param marketPlace Marketplace donde se publica el producto
     */
    public Product(String name, String imageUrl, Category category, BigDecimal price, ProductStatus status, User user, MarketPlace marketPlace) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
        this.status = status;
        this.user = user;
        this.marketPlace = marketPlace;
    }

    // Métodos getter y setter con documentación JavaDoc

    /**
     * Obtiene el ID único del producto.
     * @return UUID que identifica únicamente el producto
     */
    public UUID getId() { return id; }
    
    /**
     * Establece el ID único del producto.
     * @param id UUID que identifica únicamente el producto
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Obtiene el nombre del producto.
     * @return String con el nombre del producto
     */
    public String getName() { return name; }
    
    /**
     * Establece el nombre del producto.
     * @param name String con el nombre del producto
     */
    public void setName(String name) { this.name = name; }

    /**
     * Obtiene la URL de la imagen del producto.
     * @return String con la URL de la imagen
     */
    public String getImageUrl() { return imageUrl; }
    
    /**
     * Establece la URL de la imagen del producto.
     * @param imageUrl String con la URL de la imagen
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /**
     * Obtiene la descripción del producto.
     * @return String con la descripción detallada del producto
     */
    public String getDescription() { return description; }
    
    /**
     * Establece la descripción del producto.
     * @param description String con la descripción detallada del producto
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Obtiene la categoría del producto.
     * @return Category a la que pertenece el producto
     */
    public Category getCategory() { return category; }
    
    /**
     * Establece la categoría del producto.
     * @param category Category a la que pertenece el producto
     */
    public void setCategory(Category category) { this.category = category; }

    /**
     * Obtiene el precio del producto.
     * @return BigDecimal con el precio del producto
     */
    public BigDecimal getPrice() { return price; }
    
    /**
     * Establece el precio del producto.
     * @param price BigDecimal con el precio del producto
     */
    public void setPrice(BigDecimal price) { this.price = price; }

    /**
     * Obtiene la cantidad disponible del producto.
     * @return Integer con unidades disponibles
     */
    public Integer getAvailableQuantity() { return availableQuantity; }

    /**
     * Establece la cantidad disponible del producto.
     * @param availableQuantity Unidades disponibles (puede ser null en actualizaciones parciales)
     */
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    /**
     * Obtiene el estado actual del producto.
     * @return ProductStatus que indica el estado del producto
     */
    public ProductStatus getStatus() { return status; }
    
    /**
     * Establece el estado del producto.
     * @param status ProductStatus que indica el estado del producto
     */
    public void setStatus(ProductStatus status) { this.status = status; }

    /**
     * Obtiene el usuario propietario del producto.
     * @return User que es el propietario del producto
     */
    public User getUser() { return user; }
    
    /**
     * Establece el usuario propietario del producto.
     * @param user User que es el propietario del producto
     */
    public void setUser(User user) { this.user = user; }
    
    /**
     * Obtiene el marketplace donde está publicado el producto.
     * @return MarketPlace donde está publicado el producto
     */
    public MarketPlace getMarketPlace() { return marketPlace; }
    
    /**
     * Establece el marketplace donde se publica el producto.
     * @param marketPlace MarketPlace donde se publica el producto
     */
    public void setMarketPlace(MarketPlace marketPlace) { this.marketPlace = marketPlace; }

    /**
     * Obtiene la lista de comentarios del producto.
     * @return List<ProductComment> con todos los comentarios del producto
     */
    public List<ProductComment> getComments() { return comments; }
    
    /**
     * Establece la lista de comentarios del producto.
     * @param comments List<ProductComment> con los comentarios del producto
     */
    public void setComments(List<ProductComment> comments) { this.comments = comments; }

    /**
     * Obtiene la lista de likes del producto.
     * @return List<ProductLike> con todos los likes del producto
     */
    public List<ProductLike> getLikes() { return likes; }
    
    /**
     * Establece la lista de likes del producto.
     * @param likes List<ProductLike> con los likes del producto
     */
    public void setLikes(List<ProductLike> likes) { this.likes = likes; }
}
