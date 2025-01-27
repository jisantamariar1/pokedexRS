package ec.edu.uce.pokedexRS.service;

import ec.edu.uce.pokedexRS.model.*;
import ec.edu.uce.pokedexRS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private HabitatRepository habitatRepository;
    @Autowired
    private GenerationRepository generationRepository;
    @Autowired
    private AbilityRepository abilityRepository;
    @Autowired
    private  EvolutionRepository evolutionRepository;



    public List<Pokemon> getAllPokemons() {
        return pokemonRepository.findAllByOrderByIdAsc();
    }

    public Optional<Pokemon> getPokemonByName(String name) {
        return pokemonRepository.findByName(name);
    }

    public List<Pokemon> getPokemonsBySpecies(String speciesName) {
        Optional<Species> species = speciesRepository.findByName(speciesName);
        return species.map(pokemonRepository::findBySpecies).orElseGet(List::of);
    }

    public List<Pokemon> getPokemonsByHabitat(String habitatName) {
        Optional<Habitat> habitat = habitatRepository.findByName(habitatName);
        return habitat.map(pokemonRepository::findByHabitat).orElseGet(List::of);
    }

    public List<Pokemon> getPokemonsByGeneration(String generationName) {
        Optional<Generation> generation = generationRepository.findByName(generationName);
        return generation.map(pokemonRepository::findByGeneration).orElseGet(List::of);
    }

    public List<Pokemon> getPokemonsByAbility(String abilityName) {
        Optional<Ability> ability = abilityRepository.findByName(abilityName);
        return ability.map(pokemonRepository::findByAbilities).orElseGet(List::of);
    }

    public List<Species> getAllSpecies() {
        return speciesRepository.findAll();
    }

    public List<Habitat> getAllHabitats() {
        return habitatRepository.findAll();
    }

    public List<Generation> getAllGenerations() {
        return generationRepository.findAll();
    }

    public List<Ability> getAllAbilities() {
        return abilityRepository.findAll();
    }
    public String getAbilityDescription(String selectedAbility) {
        //busca la habilidad en la base de datos
        Optional<Ability> ability = abilityRepository.findByName(selectedAbility);
        // si la habilidad se encuentra busca su información
        return ability.map(Ability::getDescription).orElse("Descripción no definida");
    }

    public Optional<Pokemon> getPokemonById(Long id) {
        return pokemonRepository.findById(id);
    }

}