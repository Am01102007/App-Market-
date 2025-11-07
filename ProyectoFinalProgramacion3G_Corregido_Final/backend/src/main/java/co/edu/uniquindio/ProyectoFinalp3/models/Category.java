/**
 * Modelo de categoría.
 * Representa la clasificación de productos.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una categoría de productos en el marketplace.
 * 
 * Las categorías permiten organizar y clasificar los productos de manera
 * lógica, facilitando la búsqueda y navegación de los usuarios.
 * 
 * Características principales:
 * - Identificador único autogenerado
 * - Nombre único y obligatorio
 * - Descripción opcional
 * - URL de imagen opcional
 * - Relación con múltiples productos
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Identificador único de la categoría.
     * Se genera automáticamente usando estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la categoría.
     * Debe ser único en el sistema y no puede ser nulo.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Descripción detallada de la categoría.
     * Campo opcional que proporciona más información sobre la categoría.
     */
    @Column(length = 500)
    private String description;

    /**
     * URL de la imagen representativa de la categoría.
     * Campo opcional para mostrar una imagen asociada a la categoría.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Lista de productos que pertenecen a esta categoría.
     * Relación uno-a-muchos con la entidad Product.
     * Se ignora en la serialización JSON para evitar referencias circulares.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    /**
     * Constructor para crear una categoría con información básica.
     * 
     * @param name Nombre de la categoría
     * @param description Descripción de la categoría
     * @param imageUrl URL de la imagen de la categoría
     */
    public Category(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    /**
     * Constructor para crear una categoría solo con ID.
     * Útil para referencias rápidas.
     * 
     * @param id Identificador de la categoría
     */
    public Category(Long id) {
        this.id = id;
    }

    // Métodos explícitos para compatibilidad con compilación en entornos sin procesador de anotaciones
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
