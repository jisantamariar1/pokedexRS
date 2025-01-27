package ec.edu.uce.pokedexRS.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"base_pokemon_id", "evolved_pokemon_id"}))
public class Evolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_pokemon_id") // Relación con el Pokémon base
    private Pokemon basePokemon;

    @ManyToOne
    @JoinColumn(name = "evolved_pokemon_id") // Relación con el Pokémon evolucionado
    private Pokemon evolvedPokemon;

    public Evolution() {
    }
}
