package ec.edu.uce.pokedexRS.api;

import ec.edu.uce.pokedexRS.handler.*;
import ec.edu.uce.pokedexRS.model.*;
import ec.edu.uce.pokedexRS.repository.PokemonRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class PokemonInfo {
    @Autowired
    private AbilityProcessor abilityProcessor;
    @Autowired
    private GenerationProcessor generationProcessor;
    @Autowired
    private EvolutionProcessor evolutionProcessor;
    @Autowired
    private HabitatProcessor habitatProcessor;
    @Autowired
    private SpeciesProcessor speciesProcessor;
    @Autowired
    private PokemonApiClient pokemonApiClient;
    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private TypeProcessor typeProcessor;
    @Autowired
    private PokemonProcessor pokemonProcessor;
    private final ExecutorService executorService;

    public PokemonInfo() {
        //this.executorService = Executors.newFixedThreadPool(2); // Usamos 2 hilos
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Transactional
    public Pokemon fetchAndSavePokemon(JSONObject pokemonData) throws InterruptedException, ExecutionException {
        // Crear futuros para ejecutar las tareas en paralelo
        CompletableFuture<Pokemon> pokemonFuture = CompletableFuture.supplyAsync(() -> pokemonProcessor.processPokemon(pokemonData), executorService);
        CompletableFuture<Set<Type>> typesFuture = CompletableFuture.supplyAsync(() -> typeProcessor.processTypes(pokemonData.getJSONArray("types")), executorService);
        CompletableFuture<Set<Ability>> abilitiesFuture = CompletableFuture.supplyAsync(() -> abilityProcessor.processAbilities(pokemonData.getJSONArray("abilities")), executorService);
        CompletableFuture<Generation> generationFuture = CompletableFuture.supplyAsync(() -> generationProcessor.processGeneration(pokemonData.getJSONObject("species").getString("url")), executorService);
        CompletableFuture<Habitat> habitatFuture = CompletableFuture.supplyAsync(() -> habitatProcessor.processHabitat(pokemonData.getJSONObject("species").getString("url")), executorService);
        CompletableFuture<Species> speciesFuture = CompletableFuture.supplyAsync(() -> speciesProcessor.processSpecies(pokemonData.getJSONObject("species").getString("url")), executorService);
        // Esperamos a que ambas tareas se completen
        Pokemon pokemon = pokemonFuture.get(); // Espera el resultado de la tarea de Pokemon
        Set<Type> types = typesFuture.get(); // Espera el resultado de la tarea de Types
        Set<Ability> abilities = abilitiesFuture.get(); // Espera el resultado de la tarea de Habilidades
        Generation generation = generationFuture.get();
        Habitat habitat = habitatFuture.get(); // Espera el resultado del hábitat
        Species species = speciesFuture.get();
        // Ahora que tenemos ambos resultados, asignamos los tipos al Pokémon
        pokemon.setTypes(types);
        pokemon.setAbilities(abilities);
        pokemon.setGeneration(generation);
        pokemon.setHabitat(habitat);
        pokemon.setSpecies(species);
        return pokemon;
    }

    //Metodo para procesar las evoluciones a posteriori
    public void procesarEvoluciones(List<String> pokemonUrls) {
        for (String pokemonUrl : pokemonUrls) {
            JSONObject pokemonData = pokemonApiClient.getPokemonDetails(pokemonUrl);
            Optional<Pokemon> pokemon = pokemonRepository.findByName(pokemonData.getString("name"));
            evolutionProcessor.processEvolutions(pokemonData.getJSONObject("species").getString("url"), pokemon.get().getName());

        }

    }
}
