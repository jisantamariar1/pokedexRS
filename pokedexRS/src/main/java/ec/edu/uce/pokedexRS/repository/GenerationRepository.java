package ec.edu.uce.pokedexRS.repository;

import ec.edu.uce.pokedexRS.model.Generation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenerationRepository extends JpaRepository<Generation, Long> {
    Optional<Generation> findByName(String name);
}
