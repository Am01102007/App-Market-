/**
 * Modelo de marketplace.
 * Representa el mercado y sus propiedades clave.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un marketplace en el sistema.
 * 
 * Un marketplace es una plataforma donde se agrupan y organizan
 * los productos disponibles para la venta. Actúa como contenedor
 * principal para todos los productos del sistema.
 * 
 * Características principales:
 * - Identificador único autogenerado
 * - Nombre descriptivo del marketplace
 * - Relación con múltiples productos
 * - Gestión automática de la lista de productos
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "market_place")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketPlace {

    /**
     * Identificador único del marketplace.
     * Se genera automáticamente usando estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del marketplace.
     * Campo obligatorio que identifica el marketplace.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Lista de productos asociados a este marketplace.
     * Relación uno a muchos con Product.
     * Se inicializa automáticamente para evitar NullPointerException.
     */
    @OneToMany(mappedBy = "marketPlace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> productos = new ArrayList<>();

    /**
     * Constructor específico para crear un marketplace con nombre.
     * 
     * @param nombre Nombre del marketplace
     */
    public MarketPlace(String nombre) {
        this.nombre = nombre;
        this.productos = new ArrayList<>();
    }

    // Compatibilidad sin Lombok: constructor por defecto y getters/setters explícitos
    // El constructor por defecto lo genera Lombok (@NoArgsConstructor)

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
