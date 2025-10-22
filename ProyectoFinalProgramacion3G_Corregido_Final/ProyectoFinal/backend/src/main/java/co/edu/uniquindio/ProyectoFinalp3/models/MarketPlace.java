package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "market_place")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "marketPlace", cascade = CascadeType.ALL)
    private List<Product> productos = new ArrayList<>();
}