package ec.edu.uce.pokedexRS.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@Entity
@ToString
public class Pokemon {

    @Id
    private Long id;
    private String name;
    private Double height;
    private Double weight;
    private String description;
    private String spriteUrl;
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private int total;

    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL) //Esto le dice a Hibernate que cargue la colección types (tipos de Pokémon) inmediatamente cuando se cargue el objeto Pokemon
    @JoinTable(
            name = "pokemon_types",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<Type> types;
    @ManyToMany(fetch =FetchType.EAGER, cascade =CascadeType.ALL)
    @JoinTable(name = "pokemon_ability",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "ability_id"))
    private Set<Ability> abilities;  // Relación con las habilidades
    @OneToMany(mappedBy = "basePokemon")
    private Set<Evolution> evolutions; // Relación con la tabla Evolution
    @ManyToOne
    @JoinColumn(name = "generation_id")
    private Generation generation;  // Relación con la generación
    @ManyToOne
    @JoinColumn(name = "habitat_id")
    private Habitat habitat;
    @ManyToOne
    @JoinColumn(name = "species_id")
    private Species species;  // Relación con Species

    public Pokemon() {
    }
}
