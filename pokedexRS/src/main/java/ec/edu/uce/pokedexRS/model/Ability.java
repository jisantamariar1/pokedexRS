package ec.edu.uce.pokedexRS.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
public class Ability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean hidden;  // Indica si la habilidad es oculta
    private String description;
    @ManyToMany(mappedBy = "abilities", fetch = FetchType.LAZY)  // Se usa "mappedBy" para evitar la tabla extra
    private Set<Pokemon> pokemons;

    public Ability() {
    }
}
