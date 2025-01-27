package ec.edu.uce.pokedexRS.repository;

import ec.edu.uce.pokedexRS.model.Evolution;
import ec.edu.uce.pokedexRS.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvolutionRepository extends JpaRepository<Evolution, Long> {
    Optional<Evolution> findByBasePokemonAndEvolvedPokemon(Pokemon basePokemon, Pokemon evolvedPokemon);
    // Método para obtener todas las evoluciones de un Pokémon base
    List<Evolution> findByBasePokemon(Pokemon basePokemon);
}
