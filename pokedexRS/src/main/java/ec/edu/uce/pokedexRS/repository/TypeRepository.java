package ec.edu.uce.pokedexRS.repository;

import ec.edu.uce.pokedexRS.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String name);
}
