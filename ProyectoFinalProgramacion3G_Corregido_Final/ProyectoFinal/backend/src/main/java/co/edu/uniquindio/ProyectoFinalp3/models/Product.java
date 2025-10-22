package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String imageUrl;
    private String category;
    private BigDecimal price;

    
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "market_place_id")
    private MarketPlace marketPlace;

    @OneToMany(mappedBy = "product")
    private List<ProductComment> comments;

    @OneToMany(mappedBy = "product")
    private List<ProductLike> likes;

    // Constructor vacío
    public Product() {}
    
    public Product(UUID id) {
        this.id = id;
    }

    // Constructor con parámetros
    public Product(String name, String imageUrl, String category, BigDecimal price, ProductStatus status, User user, MarketPlace marketPlace) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
        this.status = status;
        this.user = user;
        this.marketPlace = marketPlace;
    }

    // Getter y Setter
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public MarketPlace getMarketPlace() { return marketPlace; }
    public void setMarketPlace(MarketPlace marketPlace) { this.marketPlace = marketPlace; }

    public List<ProductComment> getComments() { return comments; }
    public void setComments(List<ProductComment> comments) { this.comments = comments; }

    public List<ProductLike> getLikes() { return likes; }
    public void setLikes(List<ProductLike> likes) { this.likes = likes; }
}