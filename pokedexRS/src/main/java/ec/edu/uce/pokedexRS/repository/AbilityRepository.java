package ec.edu.uce.pokedexRS.repository;

import ec.edu.uce.pokedexRS.model.Ability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbilityRepository extends JpaRepository<Ability, Long> {
    Optional<Ability> findByName(String name);
}
