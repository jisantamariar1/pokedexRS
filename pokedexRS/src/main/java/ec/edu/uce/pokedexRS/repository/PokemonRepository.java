package ec.edu.uce.pokedexRS.repository;

import ec.edu.uce.pokedexRS.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    Optional<Pokemon> findByName(String name);
    List<Pokemon> findAllByOrderByIdAsc();
    List<Pokemon> findBySpecies(Species species);
    List<Pokemon> findByHabitat(Habitat habitat);
    List<Pokemon> findByGeneration(Generation generation);
    List<Pokemon> findByAbilities(Ability ability);
}
