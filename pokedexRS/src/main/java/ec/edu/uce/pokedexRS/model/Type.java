package ec.edu.uce.pokedexRS.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    private String spriteUrl;

    @ManyToMany(mappedBy = "types", fetch = FetchType.LAZY)
    private Set<Pokemon> pokemons;
}
