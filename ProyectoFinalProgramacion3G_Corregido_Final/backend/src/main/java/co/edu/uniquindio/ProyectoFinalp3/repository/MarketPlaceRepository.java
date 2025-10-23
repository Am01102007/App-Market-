package co.edu.uniquindio.ProyectoFinalp3.repository;

import co.edu.uniquindio.ProyectoFinalp3.models.MarketPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketPlaceRepository extends JpaRepository<MarketPlace, Long> {
    Optional<MarketPlace> findByNombre(String nombre);
}
