package ec.edu.uce.pokedexRS.repository;

import ec.edu.uce.pokedexRS.model.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HabitatRepository extends JpaRepository<Habitat, Long> {
    Optional<Habitat> findByName(String name);
}
