package ec.edu.uce.pokedexRS.repository;

import ec.edu.uce.pokedexRS.model.Species;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpeciesRepository extends JpaRepository<Species, Long> {
    Optional<Species> findByName(String name);
}
